package com.terbuck.terbuck_be.domain.shop.service;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopDto;
import com.terbuck.terbuck_be.domain.shop.dto.HomeShopResponse;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import com.terbuck.terbuck_be.domain.shop.repository.JpaShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {

    private final JpaShopRepository repository;

    @Transactional
    public HomeShopResponse getHomeShop(University university) {
        HomeShopResponse homeShopResponse = new HomeShopResponse();

        List<Shop> shopsByUniv = repository.findAllByUniv(university);
        for (Shop shop : shopsByUniv) {
            HomeShopDto homeShopDto = HomeShopDto.of(shop);
            homeShopResponse.getList().add(homeShopDto);
        }

        return homeShopResponse;
    }
}
