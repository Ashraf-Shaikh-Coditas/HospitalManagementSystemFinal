package com.springboot.hospitalmanagement.payload.Response;

import lombok.Data;

@Data
public class ReplaceNurseDtoResponse {
    private Long requestId;
    private String doctorName;
    private String oldNurseName;
    private String newNurseName;
    private String replaceReason;
    private String acceptOrDeclineReason;
    private String status;
}
