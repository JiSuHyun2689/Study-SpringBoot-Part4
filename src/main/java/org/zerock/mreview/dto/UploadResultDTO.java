package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {
    
    // 파일 업로드 결과 반환하기 위한 DTO 객체
    private String fileName;
    private String uuid;
    private String folderPath;

    // 나중에 전체 경로가 필요한 경우를 대비해 제공
    public String getImageURL(){
        try{
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

}
