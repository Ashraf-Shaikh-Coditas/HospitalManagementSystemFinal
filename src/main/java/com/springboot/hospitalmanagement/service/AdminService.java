package com.springboot.hospitalmanagement.service;

import com.springboot.hospitalmanagement.payload.Request.AcceptDoctorRequest;
import com.springboot.hospitalmanagement.payload.Request.AcceptNurseRequest;

public interface AdminService {
    String assignNurseToDoctor(long doctorId, long newNurseId);

    String assignDoctorToNurse(long nurseId, long doctorId);

    String acceptDoctorRequest(AcceptDoctorRequest acceptDoctorRequest);

    String declineDoctorRequest(AcceptDoctorRequest acceptDoctorRequest);

    void acceptNurseRequest(AcceptNurseRequest acceptNurseRequest);

    void declineNurseRequest(AcceptNurseRequest acceptNurseRequest);
}
