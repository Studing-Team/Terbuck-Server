package com.terbuck.terbuck_be.domain.partnership.controller;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.image.dto.UpdateShopRequest;
import com.terbuck.terbuck_be.domain.image.service.S3ImageService;
import com.terbuck.terbuck_be.domain.partnership.dto.UpdateImageRequest;
import com.terbuck.terbuck_be.domain.partnership.service.CsvPartnershipImporter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partnership")
public class PartnershipAdminController {

    private final CsvPartnershipImporter csvPartnershipImporter;
    private final S3ImageService s3ImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file, @RequestParam("university") University university) {
        try {
            csvPartnershipImporter.importFromCsv(file, university);
            return ResponseEntity.ok("업로드 및 저장 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("업로드 실패: " + e.getMessage());
        }
    }

    @PostMapping("/image")
    public ResponseEntity<?> updatePartnershipImageList(@RequestBody UpdateImageRequest updateImageRequest) {
        try {
            s3ImageService.updateAllPartnershipImagesByUniversity(updateImageRequest.getUniversity());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            throw new RuntimeException("오류 발생 : " + e.getMessage());
        }
    }

}
