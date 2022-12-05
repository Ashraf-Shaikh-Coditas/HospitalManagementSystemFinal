package com.springboot.hospitalmanagement.repository;

import com.springboot.hospitalmanagement.entity.DoctorRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRequestRepository extends JpaRepository<DoctorRequest, Long> {
    List<DoctorRequest> findByRequestStatusLike(String requestStatus);

    List<DoctorRequest> findByDoctor_DoctorId(Long doctorId);

    List<DoctorRequest> findByDoctor_DoctorIdAndRequestStatus(Long doctorId, String requestStatus);


}
