package com.terbuck.terbuck_be.domain.partnership;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.partnership.dto.HomePartnershipDto;
import com.terbuck.terbuck_be.domain.partnership.dto.PartnershipListResponse;
import com.terbuck.terbuck_be.domain.partnership.service.CsvPartnershipImporter;
import com.terbuck.terbuck_be.domain.partnership.service.PartnershipService;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.ShopListResponse;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partnership")
public class PartnershipController {

    private final PartnershipService partnershipService;
    private final CsvPartnershipImporter csvPartnershipImporter;

    @GetMapping("/home")
    public ResponseEntity<SuccessStatusResponse<PartnershipListResponse<HomePartnershipDto>>> getHomeShop(
            @RequestParam University university) {
        PartnershipListResponse<HomePartnershipDto> homePartnershipList = partnershipService.getHomePartnership(university);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.HOME_PARTNERSHIP_GET_SUCCESS, homePartnershipList));
    }

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
}
