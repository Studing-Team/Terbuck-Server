package com.terbuck.terbuck_be.domain.shop.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.MapShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.ShopListResponse;
import com.terbuck.terbuck_be.domain.shop.dto.ShopResponse;
import com.terbuck.terbuck_be.domain.shop.entity.HomeCategory;
import com.terbuck.terbuck_be.domain.shop.entity.Location;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import com.terbuck.terbuck_be.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/home")
    public ResponseEntity<SuccessStatusResponse<ShopListResponse<HomeShopDto>>> getHomeShop(
            @RequestParam University university,
            @RequestParam(name = "category") HomeCategory homeCategory,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {

        Location location = null;
        if (latitude != null && longitude != null) {
            location = new Location(latitude, longitude);
        }

        List<ShopCategory> categoryList = new ArrayList<>();
        if (homeCategory == HomeCategory.먹고가기) {
            categoryList.add(ShopCategory.음식점);
            categoryList.add(ShopCategory.카페);
            categoryList.add(ShopCategory.주점);
        }

        if ( homeCategory == HomeCategory.이용하기) {
            categoryList.add(ShopCategory.운동);
            categoryList.add(ShopCategory.문화);
            categoryList.add(ShopCategory.병원);
            categoryList.add(ShopCategory.스터디);
        }

        ShopListResponse<HomeShopDto> homeShopListResponse = shopService.getHomeShop(university, categoryList, location);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.HOME_SHOP_GET_SUCCESS, homeShopListResponse));
    }

    @GetMapping("/map")
    public ResponseEntity<SuccessStatusResponse<ShopListResponse<MapShopDto>>> getMapShop(
            @RequestParam University university,
            @RequestParam(name = "category", required = false) List<ShopCategory> categoryList,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        Location location = null;
        if (latitude != null && longitude != null) {
            location = new Location(latitude, longitude);
        }

        ShopListResponse<MapShopDto> shopListResponse = shopService.getMapShop(university, categoryList, location);

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
}
