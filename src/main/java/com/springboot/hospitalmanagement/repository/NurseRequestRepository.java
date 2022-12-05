package com.springboot.hospitalmanagement.repository;

import com.springboot.hospitalmanagement.entity.DoctorRequest;
import com.springboot.hospitalmanagement.entity.NurseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NurseRequestRepository extends JpaRepository<NurseRequest, Long> {
    List<NurseRequest> findByRequestStatusLike(String requestStatus);

    List<NurseRequest> findByNurse_NurseId(Long nurseId);

    List<NurseRequest> findByNurse_NurseIdAndRequestStatus(Long nurseId, String requestStatus);


}
