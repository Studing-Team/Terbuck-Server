package com.terbuck.terbuck_be.domain.advertise.service;

import com.terbuck.terbuck_be.domain.advertise.dto.BannerRequest;
import com.terbuck.terbuck_be.domain.advertise.dto.BannerResponse;
import com.terbuck.terbuck_be.domain.advertise.entity.Banner;
import com.terbuck.terbuck_be.domain.advertise.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository bannerRepository;

    @Transactional
    public BannerResponse createBanner(BannerRequest request) {
        Banner banner = Banner.builder()
                .title(request.getTitle())
                .imageURL(request.getImageURL())
                .link(request.getLink())
                .build();
        bannerRepository.save(banner);
        return BannerResponse.from(banner);
    }

    public List<BannerResponse> getBanners() {
        return bannerRepository.findAll().stream()
                .map(BannerResponse::from)
                .collect(Collectors.toList());
    }

    public BannerResponse getBanner(Long bannerId) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배너를 찾을 수 없습니다."));
        return BannerResponse.from(banner);
    }

    @Transactional
    public BannerResponse updateBanner(Long bannerId, BannerRequest request) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배너를 찾을 수 없습니다."));
        banner.update(request.getTitle(), request.getImageURL(), request.getLink());
        return BannerResponse.from(banner);
    }

    @Transactional
    public void deleteBanner(Long bannerId) {
        bannerRepository.deleteById(bannerId);
    }
}
