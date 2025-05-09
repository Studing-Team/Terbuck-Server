package com.terbuck.terbuck_be.domain.partnership.dto;

import com.terbuck.terbuck_be.common.entity.Image;
import com.terbuck.terbuck_be.domain.partnership.entity.Institution;
import com.terbuck.terbuck_be.domain.partnership.entity.Partnership;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PartnershipResponse {

    private String name;

    private Institution institution;

    private List<String> imageList;

    private String detail;

    private String snsLink;

    public static PartnershipResponse of(Partnership partnership) {
        PartnershipResponse partnershipResponse = new PartnershipResponse(
                partnership.getName(),
                partnership.getInstitution(),
                new ArrayList<>(),
                partnership.getDetail(),
                partnership.getSnsLink());

        for (Image image : partnership.getImageList()) {
            partnershipResponse.imageList.add(image.getImageURL());
        }

        return partnershipResponse;
    }
}
