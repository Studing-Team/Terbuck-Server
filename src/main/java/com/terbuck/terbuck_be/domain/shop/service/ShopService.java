package com.terbuck.terbuck_be.domain.shop.service;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.MapShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.ShopListResponse;
import com.terbuck.terbuck_be.domain.shop.dto.ShopResponse;
import com.terbuck.terbuck_be.domain.shop.entity.Benefit;
import com.terbuck.terbuck_be.domain.shop.entity.Location;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import com.terbuck.terbuck_be.domain.shop.repository.JpaShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final JpaShopRepository repository;

    public ShopListResponse<HomeShopDto> getHomeShop(University university, List<ShopCategory> categoryList, Location location) {
        ShopListResponse<HomeShopDto> homeShopListResponse = new ShopListResponse<>();

        // shop과 Benefit 패치 조인 조회
        List<Shop> shopsByUniv = repository.findAllByUnivAndCategoryAndLocation(university, categoryList, location);
        // Benefit 리스트의 각 Detail들 조회
        List<Long> benefitIds = shopsByUniv.stream()
                .flatMap(s -> s.getBenefitList().stream())
                .map(Benefit::getId)
                .collect(Collectors.toList());
        repository.findAllWithDetails(benefitIds);

        for (Shop shop : shopsByUniv) {
            HomeShopDto homeShopDto = HomeShopDto.of(shop);
            homeShopListResponse.getList().add(homeShopDto);
        }

        return homeShopListResponse;
    }

    public ShopListResponse<MapShopDto> getMapShop(University university, List<ShopCategory> categoryList, Location location) {
        ShopListResponse<MapShopDto> shopListResponse = new ShopListResponse<>();

        List<Shop> shopsByUniv = repository.findAllByUnivAndCategoryAndLocation(university, categoryList, location);
        for (Shop shop : shopsByUniv) {
            MapShopDto mapShopDto = MapShopDto.of(shop);
            shopListResponse.getList().add(mapShopDto);
        }

        return shopListResponse;
    }

    public ShopResponse getShop(Long shopId) {
        Shop shop = repository.findById(shopId);
        return ShopResponse.of(shop);
    }
}
