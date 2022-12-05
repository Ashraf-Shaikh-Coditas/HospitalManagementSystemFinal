package com.springboot.hospitalmanagement.payload.Request;

import lombok.Data;

@Data
public class ReplaceDoctorDtoRequest {
    private Long nurseId;
    private Long oldDoctorId;
    private Long newDoctorId;
    private String replaceReason;
}
