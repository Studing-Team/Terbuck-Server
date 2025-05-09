package com.terbuck.terbuck_be.domain.partnership.dto;

import com.terbuck.terbuck_be.domain.partnership.entity.Institution;
import com.terbuck.terbuck_be.domain.partnership.entity.PartnerCategory;
import com.terbuck.terbuck_be.domain.partnership.entity.Partnership;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomePartnershipDto {

    private Long id;

    private String name;

    private Institution institution;

    private PartnerCategory category;

    public static HomePartnershipDto of(Partnership partnership) {
        return new HomePartnershipDto(
                partnership.getId(),
                partnership.getName(),
                partnership.getInstitution(),
                partnership.getCategory());
    }
}
