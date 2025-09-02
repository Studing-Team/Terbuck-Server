package com.terbuck.terbuck_be.domain.info.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.university.dto.UniversityResponse;
import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.university.service.UniversityService;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {

    private final UniversityService universityService;

    @GetMapping("/universities")
    public ResponseEntity<SuccessStatusResponse<List<String>>> getUniversities() {
        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.INFO_UNIVERSITIES_GET_SUCCESS, List.of("성신여자대학교", "광운대학교", "서울과학기술대학교", "삼육대학교")));
    }

    @GetMapping("/shop_categories")
    public ResponseEntity<SuccessStatusResponse<List<ShopCategory>>> getCategories() {

        List<ShopCategory> shopCategoryList = Arrays.asList(ShopCategory.values());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.INFO_SHOP_CATEGORIES_GET_SUCCESS, shopCategoryList));
    }
}
