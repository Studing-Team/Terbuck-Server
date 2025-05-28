package com.terbuck.terbuck_be.domain.shop.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.MapShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.ShopListResponse;
import com.terbuck.terbuck_be.domain.shop.dto.ShopResponse;
import com.terbuck.terbuck_be.domain.shop.entity.Location;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import com.terbuck.terbuck_be.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(name = "category", required = false) List<ShopCategory> categoryList,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {

        Location location = null;
        if (latitude != null && longitude != null) {
            location = new Location(latitude, longitude);
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
