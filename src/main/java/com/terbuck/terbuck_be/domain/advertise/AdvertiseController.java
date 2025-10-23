package com.terbuck.terbuck_be.domain.advertise;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.advertise.dto.BannerRequest;
import com.terbuck.terbuck_be.domain.advertise.dto.BannerResponse;
import com.terbuck.terbuck_be.domain.advertise.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ad/banners")
@RequiredArgsConstructor
public class AdvertiseController {

    private final BannerService bannerService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SuccessStatusResponse<BannerResponse> createBanner(@RequestBody BannerRequest request) {
        return SuccessStatusResponse.of(SuccessMessage.BANNER_CREATE_SUCCESS, bannerService.createBanner(request));
    }

    @GetMapping
    public SuccessStatusResponse<List<BannerResponse>> getBanners() {
        return SuccessStatusResponse.of(SuccessMessage.BANNER_GET_SUCCESS, bannerService.getBanners());
    }

    @GetMapping("/{bannerId}")
    public SuccessStatusResponse<BannerResponse> getBanner(@PathVariable Long bannerId) {
        return SuccessStatusResponse.of(SuccessMessage.BANNER_GET_SUCCESS, bannerService.getBanner(bannerId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bannerId}")
    public SuccessStatusResponse<BannerResponse> updateBanner(@PathVariable Long bannerId, @RequestBody BannerRequest request) {
        return SuccessStatusResponse.of(SuccessMessage.BANNER_UPDATE_SUCCESS, bannerService.updateBanner(bannerId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bannerId}")
    public SuccessStatusResponse<?> deleteBanner(@PathVariable Long bannerId) {
        bannerService.deleteBanner(bannerId);
        return SuccessStatusResponse.of(SuccessMessage.BANNER_DELETE_SUCCESS);
    }
}
