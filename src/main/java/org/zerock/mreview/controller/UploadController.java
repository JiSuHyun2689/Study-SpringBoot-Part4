package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    // application.properties의 변수
    @Value("${org.zerock.upload.path}")
    private String uploadPath;


    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        // 파일 업로드 결과 반환할 객체
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles){

            // image 파일만 업로드 가능 -> 걸러주기
            if(uploadFile.getContentType().startsWith("image") == false){
                log.warn("This file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 실제 파일 이름. IE/Edge는 전체 경로가 들어므로 parsing.
            String originalName = uploadFile.getOriginalFilename();

            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("File Name : " + fileName);

            // 날짜 폴더 생성
            String folerPath = makeFoler();

            // UUID 생성
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 "_" 이용해서 구분
            String saveName = uploadPath + File.separator + folerPath + File.separator + uuid + "_" + fileName;

            System.out.println("Save File name : " + saveName);

            Path savePath = Paths.get(saveName);

            try{
                uploadFile.transferTo(savePath); // 실제 이미지 저장
                resultDTOList.add(new UploadResultDTO(fileName, uuid, folerPath));
            }catch (IOException e){
                e.printStackTrace();
            }
        } // end for
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    // 날짜 폴더 생성
    private String makeFoler() {

        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);

        // 폴더 생성
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false)
            uploadPathFolder.mkdirs();

        return folderPath;
    }

    // 업로드 된 이미지 출력하기 위한 메서드
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName){

        ResponseEntity<byte[]> result = null;

        try{
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");

            log.info("FileName : " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);

            log.info("File : " + file);

            HttpHeaders header = new HttpHeaders();

            // MIME TYPE
            header.add("Content-Type", Files.probeContentType(file.toPath()));

            // File Data
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }
}
