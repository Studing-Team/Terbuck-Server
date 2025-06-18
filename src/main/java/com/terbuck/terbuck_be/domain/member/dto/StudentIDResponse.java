package com.terbuck.terbuck_be.domain.member.dto;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.entity.StudentID;
import lombok.Data;

@Data
public class StudentIDResponse {

    private String name;
    private University university;
    private Boolean isRegistered;
    private String studentNumber;
    private String imageURL;

    public static StudentIDResponse of(Member member) {
        StudentIDResponse studentIDResponse = new StudentIDResponse();
        studentIDResponse.setName(member.getName());
        studentIDResponse.setUniversity(member.getUniversity());

        if (member.getStudentID().getIsRegistered()) {
            studentIDResponse.setIsRegistered(true);
            StudentID studentID = member.getStudentID();
            studentIDResponse.setStudentNumber(studentID.getStudentNumber());
            studentIDResponse.setImageURL(studentID.getIdCardImage());
        }else{
            studentIDResponse.setIsRegistered(false);
            studentIDResponse.setStudentNumber(null);
            studentIDResponse.setImageURL(null);
        }

        return studentIDResponse;
    }
}
