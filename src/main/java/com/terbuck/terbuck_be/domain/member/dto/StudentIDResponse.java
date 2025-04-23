package com.terbuck.terbuck_be.domain.member.dto;

import lombok.Data;

@Data
public class StudentIDResponse {

    private String name;
    private String studentNumber;
    private String imageURL;

    public StudentIDResponse(String name, String studentNumber, String imageURL) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.imageURL = imageURL;
    }
}
