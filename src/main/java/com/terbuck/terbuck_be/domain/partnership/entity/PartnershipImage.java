package com.terbuck.terbuck_be.domain.partnership.entity;

import com.terbuck.terbuck_be.common.entity.Image;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartnershipImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partnership_id")
    private Partnership partnership;

    public PartnershipImage(String imageURL) {
        super(imageURL);
    }

    public void changePartnership(Partnership partnership) {
        this.partnership = partnership;
        partnership.getImageList().add(this);
    }
}
