package com.sparta.sunday.domain.common.validator;

import org.springframework.stereotype.Component;

@Component
public class ImgUrlValidator {
    public boolean isValidImageUrl(String imgUrl) {

        String regex = ".*\\.(jpeg|jpg|png|gif|bmp)$";

        if(!imgUrl.matches(regex)) {
            throw new IllegalArgumentException("이미지 url이 올바르지 않습니다.");
        }

        return true;
    }
}
