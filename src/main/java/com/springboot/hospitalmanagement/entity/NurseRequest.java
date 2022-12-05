package com.springboot.hospitalmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity

public class NurseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    private Long oldDoctorId;
    private Long newDoctorId;
    private String requestStatus = "PENDING";

    private String replaceReason;

    private String requestRejectOrAcceptReason;
    @JsonIgnore
    @ManyToOne
    private Nurse nurse;


}
