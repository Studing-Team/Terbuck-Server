package com.terbuck.terbuck_be.domain.info.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/info")
public class InfoController {

    @GetMapping("/universities")
    public ResponseEntity<SuccessStatusResponse<List<String>>> getUniversities() {

        List<String> univList = Arrays.stream(University.values())
                .map(Enum::name)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.INFO_UNIVERSITIES_GET_SUCCESS, univList));
    }

    @GetMapping("/shop_categories")
    public ResponseEntity<SuccessStatusResponse<List<String>>> getCategories() {

        List<String> shopCategoryList = Arrays.stream(ShopCategory.values())
                .map(Enum::name)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.INFO_SHOP_CATEGORIES_GET_SUCCESS, shopCategoryList));
    }
}
