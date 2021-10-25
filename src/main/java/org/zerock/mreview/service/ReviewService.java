package org.zerock.mreview.service;

import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewService{

    // 해당 영화의 모든 리뷰 가져옴
    List<ReviewDTO> getListOfMovie(Long mno);

    // 영화 리뷰 추가
    Long regsiter(ReviewDTO movieReviewDTO);

    // 특정한 영화 리뷰 수정
    void modify(ReviewDTO movieReviewDTO);

    // 영화 리뷰 삭제
    void remove(Long reviewNum);



    default Review dtoToEntity(ReviewDTO movieReviewDTO){

        Review movieReview = Review.builder().reviewnum(movieReviewDTO.getReviewnum()).movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().mid(movieReviewDTO.getMid()).build()).grade(movieReviewDTO.getGrade()).text(movieReviewDTO.getText()).build();
        return movieReview;
    }

    default ReviewDTO entityToDto(Review moviewReview){

        ReviewDTO moviewReviewDTO = ReviewDTO.builder().reviewnum(moviewReview.getReviewnum()).mno(moviewReview.getMovie().getMno())
                .mid(moviewReview.getMember().getMid()).nickname(moviewReview.getMember().getNickname()).email(moviewReview.getMember().getEmail())
                .grade(moviewReview.getGrade()).text(moviewReview.getText()).regDate(moviewReview.getRegDate()).modDate(moviewReview.getModDate()).build();

        return moviewReviewDTO;
    }

}
