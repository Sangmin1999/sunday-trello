package com.sparta.sunday.domain.common.validator;

import com.sparta.sunday.domain.common.exception.InvalidRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Component
public class FileValidator {

    public void fileSizeValidator(MultipartFile file, Long size) {
        if(file.getSize() > size) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다." + file.getSize());
        }
    }

    public  void fileTypeValidator(MultipartFile file, List<String> acceptedTypes) {

        String fileName = file.getOriginalFilename();

        if (fileName == null || !acceptedTypes.contains(fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase())) {
            throw new IllegalArgumentException("파일 형식이 올바르지 않습니다." + file.getContentType());
        }
    }
}
