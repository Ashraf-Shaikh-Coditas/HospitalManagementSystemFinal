package com.springboot.hospitalmanagement.payload;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.hospitalmanagement.entity.Doctor;
import com.springboot.hospitalmanagement.util.RandomPasswordGenerator;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NurseDto {
    private Long nurseId;
    @NotEmpty
    private String nurseName;
    @NotEmpty
    @Email
    private String email;

    private boolean isAllocated = false;

    //    @JsonBackReference
    private DoctorDto doctorDto;

    private String password;


    public String show() {
        return "Nurse Id : " + nurseId +
                "\nNurse Name : " + nurseName +
                "\nNurse Email : " + email +
                "\nYOUR PASSWORD IS :" + password;
    }

    public NurseDto(Long nurseId, String nurseName, String email, boolean isAllocated) {
        this.nurseId = nurseId;
        this.nurseName = nurseName;
        this.email = email;
        this.isAllocated = isAllocated;
    }


}
