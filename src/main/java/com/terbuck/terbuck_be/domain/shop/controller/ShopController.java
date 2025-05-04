package com.terbuck.terbuck_be.domain.shop.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.MapShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.ShopListResponse;
import com.terbuck.terbuck_be.domain.shop.dto.ShopResponse;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import com.terbuck.terbuck_be.domain.shop.service.CsvShopImporter;
import com.terbuck.terbuck_be.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;
    private final CsvShopImporter csvShopImporter;

    @GetMapping("/home")
    public ResponseEntity<SuccessStatusResponse<ShopListResponse<HomeShopDto>>> getHomeShop(
            @RequestParam University university,
            @RequestParam(name = "category", required = false) List<ShopCategory> categoryList) {
        ShopListResponse<HomeShopDto> homeShopListResponse = shopService.getHomeShop(university, categoryList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.HOME_SHOP_GET_SUCCESS, homeShopListResponse));
    }

    @GetMapping("/map")
    public ResponseEntity<SuccessStatusResponse<ShopListResponse<MapShopDto>>> getMapShop(
            @RequestParam University university,
            @RequestParam(name = "category", required = false) List<ShopCategory> categoryList) {
        ShopListResponse<MapShopDto> shopListResponse = shopService.getMapShop(university, categoryList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.MAP_SHOP_GET_SUCCESS, shopListResponse));
    }

    @GetMapping("/{shop_id}")
    public ResponseEntity<SuccessStatusResponse<ShopResponse>> getShop(@PathVariable(name = "shop_id") Long shopId) {
        ShopResponse shopResponse = shopService.getShop(shopId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.ID_SHOP_GET_SUCCESS, shopResponse));
    }

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
}
