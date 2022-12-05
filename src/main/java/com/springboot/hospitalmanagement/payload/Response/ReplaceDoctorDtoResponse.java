package com.springboot.hospitalmanagement.payload.Response;

import lombok.Data;

@Data
public class ReplaceDoctorDtoResponse {
    private Long requestId;
    private String nurseName;
    private String oldDoctorName;
    private String newDoctorName;
    private String replaceReason;
    private String acceptOrDeclineReason;
    private String status;
}
