package com.springboot.hospitalmanagement.payload.Request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class AcceptNurseRequest {
    @NotEmpty
    private Long requestId;
    @NotEmpty
    @NotBlank
    private String acceptOrDeclineReason;
}
