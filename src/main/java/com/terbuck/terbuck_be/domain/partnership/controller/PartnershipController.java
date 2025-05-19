package com.terbuck.terbuck_be.domain.partnership.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.partnership.dto.HomePartnershipDto;
import com.terbuck.terbuck_be.domain.partnership.dto.PartnershipListResponse;
import com.terbuck.terbuck_be.domain.partnership.dto.PartnershipResponse;
import com.terbuck.terbuck_be.domain.partnership.service.PartnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partnership")
public class PartnershipController {

    private final PartnershipService partnershipService;


    @GetMapping("/home")
    public ResponseEntity<SuccessStatusResponse<PartnershipListResponse<HomePartnershipDto>>> getHomeShop(
            @RequestParam University university) {
        PartnershipListResponse<HomePartnershipDto> homePartnershipList = partnershipService.getHomePartnership(university);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.HOME_PARTNERSHIP_GET_SUCCESS, homePartnershipList));
    }

    @GetMapping("/home_new")
    public ResponseEntity<SuccessStatusResponse<PartnershipListResponse<HomePartnershipDto>>> getNewHomeShop(
            @RequestParam University university) {
        PartnershipListResponse<HomePartnershipDto> homePartnershipList = partnershipService.getNewHomePartnership(university);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.HOME_NEW_PARTNERSHIP_GET_SUCCESS, homePartnershipList));
    }

    @GetMapping("/{partnership_id}")
    public ResponseEntity<SuccessStatusResponse<PartnershipResponse>> getPartnership(@PathVariable(name = "partnership_id") Long partnershipId) {
        PartnershipResponse partnershipResponse = partnershipService.getShop(partnershipId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.ID_PARTNERSHIP_GET_SUCCESS, partnershipResponse));
    }


}
