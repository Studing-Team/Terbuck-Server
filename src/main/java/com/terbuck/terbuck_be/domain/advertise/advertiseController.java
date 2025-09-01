package com.terbuck.terbuck_be.domain.advertise;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.advertise.dto.BannerResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ad")
public class advertiseController {

    @GetMapping("/banner/{number}")
    public SuccessStatusResponse<BannerResponse> getBanner(@PathVariable Integer number) {
        String bannerImageUrl = "https://terbuck-bucket.s3.ap-northeast-2.amazonaws.com/ad/banner" + number + ".png";
        return SuccessStatusResponse.of(SuccessMessage.BANNER_REQUEST_SUCCESS, BannerResponse.of(bannerImageUrl));
    }
}