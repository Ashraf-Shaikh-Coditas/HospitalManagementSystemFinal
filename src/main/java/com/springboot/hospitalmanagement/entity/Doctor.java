package com.springboot.hospitalmanagement.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "doctors", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "doctor_name")
    private String doctorName;

    private String email;

    private String specialization;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "doctor")
    private Set<Nurse> nurses = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    private List<DoctorRequest> doctorRequests;
}
