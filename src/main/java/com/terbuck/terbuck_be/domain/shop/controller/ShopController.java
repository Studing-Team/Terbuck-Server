package com.terbuck.terbuck_be.domain.shop.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.MapShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.ShopListResponse;
import com.terbuck.terbuck_be.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;


    /**
     * TODO : @param Category 추가 List<String>을 통해서 여러 카테고리 보내면 모두 포함하도록
     */
    @GetMapping("/home")
    public ResponseEntity<SuccessStatusResponse<ShopListResponse<HomeShopDto>>> getHomeShop(@RequestParam University university) {
        ShopListResponse<HomeShopDto> homeShopListResponse = shopService.getHomeShop(university);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.HOME_SHOP_GET_SUCCESS, homeShopListResponse));
    }

    @GetMapping("/map")
    public ResponseEntity<SuccessStatusResponse<ShopListResponse<MapShopDto>>> getMapShop(@RequestParam University university) {
        ShopListResponse<MapShopDto> shopListResponse = shopService.getMapShop(university);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.MAP_SHOP_GET_SUCCESS, shopListResponse));
    }
}
