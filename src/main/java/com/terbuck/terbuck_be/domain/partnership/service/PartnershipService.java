package com.terbuck.terbuck_be.domain.partnership.service;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.partnership.dto.HomePartnershipDto;
import com.terbuck.terbuck_be.domain.partnership.dto.PartnershipListResponse;
import com.terbuck.terbuck_be.domain.partnership.dto.PartnershipResponse;
import com.terbuck.terbuck_be.domain.partnership.entity.Partnership;
import com.terbuck.terbuck_be.domain.partnership.repository.JpaPartnershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnershipService {

    private final JpaPartnershipRepository partnershipRepository;

    @Transactional
    public PartnershipListResponse<HomePartnershipDto> getHomePartnership(University university) {
        PartnershipListResponse<HomePartnershipDto> homePartnershipListResponse = new PartnershipListResponse<>();

        List<Partnership> partnershipsByUniv = partnershipRepository.findAllByUniv(university);
        for (Partnership partnership : partnershipsByUniv) {
            HomePartnershipDto homePartnershipDto = HomePartnershipDto.of(partnership);
            homePartnershipListResponse.getList().add(homePartnershipDto);
        }

        return homePartnershipListResponse;
    }

    @Transactional
    public PartnershipListResponse<HomePartnershipDto> getNewHomePartnership(University university) {
        PartnershipListResponse<HomePartnershipDto> homePartnershipListResponse = new PartnershipListResponse<>();

        List<Partnership> partnershipsByUniv = partnershipRepository.findAllNewByUnivAndTime(university, LocalDateTime.now());
        for (Partnership partnership : partnershipsByUniv) {
            HomePartnershipDto homePartnershipDto = HomePartnershipDto.of(partnership);
            homePartnershipListResponse.getList().add(homePartnershipDto);
        }

        return homePartnershipListResponse;
    }

    @Transactional
    public PartnershipResponse getShop(Long partnershipId) {
        Partnership partnership = partnershipRepository.findById(partnershipId);
        return PartnershipResponse.of(partnership);
    }
}
