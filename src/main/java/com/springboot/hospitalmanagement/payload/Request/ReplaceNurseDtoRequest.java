package com.springboot.hospitalmanagement.payload.Request;

import lombok.Data;

@Data
public class ReplaceNurseDtoRequest {
    private Long doctorId;
    private Long oldNurseId;
    private Long newNurseId;
    private String replaceReason;
}
