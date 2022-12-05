package com.springboot.hospitalmanagement.payload;

import lombok.Data;

@Data
public class AssignNurseDto {
    private Long doctorId;
    private Long nurseId;
}
