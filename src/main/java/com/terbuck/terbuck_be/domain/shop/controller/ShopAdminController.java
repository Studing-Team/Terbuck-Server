package com.terbuck.terbuck_be.domain.shop.controller;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.image.dto.UpdateShopRequest;
import com.terbuck.terbuck_be.domain.image.service.S3ImageService;
import com.terbuck.terbuck_be.domain.shop.service.CsvShopImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopAdminController {

    private final CsvShopImporter csvShopImporter;
    private final S3ImageService s3ImageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file, @RequestParam("university") University university) {
        log.info("upload");
        try {
            csvShopImporter.importFromCsv(file, university);
            return ResponseEntity.ok("업로드 및 저장 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("업로드 실패: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image")
    public ResponseEntity<?> updateShopImageList(@RequestBody UpdateShopRequest updateShopRequest) {
        try {
            s3ImageService.updateAllShopImagesByUniversity(updateShopRequest.getUniversity());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            throw new RuntimeException("오류 발생 : " + e.getMessage());
        }
    }
}
