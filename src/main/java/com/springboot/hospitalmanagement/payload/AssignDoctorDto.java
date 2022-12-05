package com.springboot.hospitalmanagement.payload;

import lombok.Data;

@Data
public class AssignDoctorDto {
    private Long nurseId;
    private Long doctorId;
}
