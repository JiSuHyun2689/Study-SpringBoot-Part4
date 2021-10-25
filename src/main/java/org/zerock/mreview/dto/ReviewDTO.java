package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    // review num
    private Long reviewnum;

    // movie mno
    private Long mno;

    // member id
    private Long mid;

    // member nickname
    private String nickname;

    // member email
    private String email;

    private int grade;

    private String text;

    private LocalDateTime regDate, modDate;

}
