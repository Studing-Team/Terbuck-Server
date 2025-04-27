package com.terbuck.terbuck_be.domain.shop.repository;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;

import java.util.List;

public interface ShopRepository {

    void save(Shop shop);

    List<Shop> findAll();

    List<Shop> findAllByUniv(University university);

    List<Shop> findAllByUnivAndCategory(University university, List<ShopCategory> categoryList);

    Shop findById(Long id);

    Shop findByUnivAndName(University university, String name);
}
