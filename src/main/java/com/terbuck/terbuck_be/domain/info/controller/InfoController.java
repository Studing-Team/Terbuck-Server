package com.terbuck.terbuck_be.domain.info.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.info.dto.AppVersionResponse;
import com.terbuck.terbuck_be.domain.info.dto.UpdateCheckResponse;
import com.terbuck.terbuck_be.domain.info.entity.AppVersion;
import com.terbuck.terbuck_be.domain.info.service.AppVersionService;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static com.terbuck.terbuck_be.common.dto.SuccessMessage.SUCCESS_CHECK_APP_UPDATE;
import static com.terbuck.terbuck_be.common.dto.SuccessMessage.SUCCESS_GET_APP_VERSION;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {

    private final AppVersionService appVersionService;

    @GetMapping("/universities")
    public ResponseEntity<SuccessStatusResponse<List<String>>> getUniversities() {
        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.INFO_UNIVERSITIES_GET_SUCCESS, List.of("성신여자대학교", "광운대학교", "서울과학기술대학교", "삼육대학교")));
    }

    @GetMapping("/shop_categories")
    public ResponseEntity<SuccessStatusResponse<List<ShopCategory>>> getCategories() {

        List<ShopCategory> shopCategoryList = Arrays.asList(ShopCategory.values());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.INFO_SHOP_CATEGORIES_GET_SUCCESS, shopCategoryList));
    }

    @GetMapping("/version")
    public SuccessStatusResponse<AppVersionResponse> getAppVersion(@RequestParam("os") AppVersion.OS os) {
        return SuccessStatusResponse.of(SUCCESS_GET_APP_VERSION, appVersionService.getAppVersion(os));
    }

    @GetMapping("/update-check")
    public SuccessStatusResponse<UpdateCheckResponse> checkUpdate(
            @RequestParam("os") AppVersion.OS os,
            @RequestParam("currentVersion") String currentVersion
    ) {
        return SuccessStatusResponse.of(SUCCESS_CHECK_APP_UPDATE, appVersionService.checkUpdate(os, currentVersion));
    }
}
