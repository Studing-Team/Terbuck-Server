package com.terbuck.terbuck_be.domain.partnership.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartnershipListResponse <T> {

    private List<T> list = new ArrayList<>();
}
