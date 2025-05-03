package com.terbuck.terbuck_be.domain.image.controller;

import com.terbuck.terbuck_be.domain.image.dto.UpdateShopRequest;
import com.terbuck.terbuck_be.domain.image.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final S3ImageService s3ImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadStudentIDImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = s3ImageService.uploadStudentIDImage(file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이미지 업로드 실패: " + e.getMessage());
        }
    }

    @PostMapping("/shop_update")
    public ResponseEntity<?> updateShopImageList(@RequestBody UpdateShopRequest updateShopRequest) {
        try {
            s3ImageService.updateAllShopImagesByUniversity(updateShopRequest.getUniversity());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            throw new RuntimeException("오류 발생 : " + e.getMessage());
        }
    }
}

