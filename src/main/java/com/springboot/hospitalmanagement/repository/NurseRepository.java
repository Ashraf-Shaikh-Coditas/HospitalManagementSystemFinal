package com.springboot.hospitalmanagement.repository;

import com.springboot.hospitalmanagement.entity.Doctor;
import com.springboot.hospitalmanagement.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
    Optional<Nurse> findByEmail(String email);

    List<Nurse> findByIsAllocatedFalseAndIsDeletedFalse();

    List<Nurse> findByIsAllocatedTrueAndIsDeletedFalse();

    List<Nurse> findByIsDeletedFalse();

    List<Nurse> findByDoctor_DoctorId(Long doctorId);

    Optional<Doctor> findByNurseId(Long nurseId);


}
