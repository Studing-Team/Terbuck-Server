package com.terbuck.terbuck_be.domain.shop.repository;

import com.terbuck.terbuck_be.domain.shop.entity.Shop;

import java.util.List;

public interface ShopRepository {

    List<Shop> findAll();

    Shop findById(Long id);
}
