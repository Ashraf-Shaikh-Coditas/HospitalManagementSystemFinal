package com.springboot.hospitalmanagement.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DoctorDtoResponse {
    private Long doctorId;
    @NotEmpty

    private String doctorName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String specialization;

    private String password;

    //    @JsonManagedReference
    private Set<NurseDto> nurses = new HashSet<>();


    public String show() {
        return "Doctor Id : " + doctorId +
                "\nDoctor Name : " + doctorName +
                "\nDoctor Email : " + email +
                "\nSpecialization : " + specialization +
                "\n" +
                "YOUR PASSWORD IS :" + password;
    }
}

