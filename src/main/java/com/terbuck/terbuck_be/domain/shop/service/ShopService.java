package com.terbuck.terbuck_be.domain.shop.service;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.MapShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.ShopListResponse;
import com.terbuck.terbuck_be.domain.shop.dto.ShopResponse;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import com.terbuck.terbuck_be.domain.shop.repository.JpaShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final JpaShopRepository repository;

    public ShopListResponse<HomeShopDto> getHomeShop(University university) {
        ShopListResponse<HomeShopDto> homeShopListResponse = new ShopListResponse<>();

        List<Shop> shopsByUniv = repository.findAllByUniv(university);
        for (Shop shop : shopsByUniv) {
            HomeShopDto homeShopDto = HomeShopDto.of(shop);
            homeShopListResponse.getList().add(homeShopDto);
        }

        return homeShopListResponse;
    }

    public ShopListResponse<MapShopDto> getMapShop(University university) {
        ShopListResponse<MapShopDto> shopListResponse = new ShopListResponse<>();

        List<Shop> shopsByUniv = repository.findAllByUniv(university);
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
