package com.terbuck.terbuck_be.domain.shop.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopResponse;
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

    @GetMapping("/home")
    public ResponseEntity<SuccessStatusResponse<HomeShopResponse>> getHomeShop(@RequestParam University university) {
        HomeShopResponse homeShopResponse = shopService.getHomeShop(university);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.HOME_SHOP_GET_SUCCESS, homeShopResponse));
    }
}
