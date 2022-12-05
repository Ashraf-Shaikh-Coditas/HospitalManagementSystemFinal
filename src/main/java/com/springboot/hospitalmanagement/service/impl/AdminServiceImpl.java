package com.springboot.hospitalmanagement.service.impl;

import com.springboot.hospitalmanagement.entity.Doctor;
import com.springboot.hospitalmanagement.entity.DoctorRequest;
import com.springboot.hospitalmanagement.entity.Nurse;
import com.springboot.hospitalmanagement.entity.NurseRequest;
import com.springboot.hospitalmanagement.exception.UserNotFoundException;
import com.springboot.hospitalmanagement.payload.Request.AcceptDoctorRequest;
import com.springboot.hospitalmanagement.payload.Request.AcceptNurseRequest;
import com.springboot.hospitalmanagement.repository.DoctorRepository;
import com.springboot.hospitalmanagement.repository.DoctorRequestRepository;
import com.springboot.hospitalmanagement.repository.NurseRepository;
import com.springboot.hospitalmanagement.repository.NurseRequestRepository;
import com.springboot.hospitalmanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorRequestRepository doctorRequestRepository;

    @Autowired
    private NurseRequestRepository nurseRequestRepository;

    @Override
    public String assignNurseToDoctor(long doctorId, long nurseId) {

        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", nurseId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", doctorId));

        if (nurse.isAllocated()) {
            return "Nurse already allocated";
        }

        Set<Nurse> nursesSet = doctor.getNurses();
        nursesSet.add(nurse);

        doctorRepository.save(doctor);
        nurse.setAllocated(true);
        nurse.setDoctor(doctor);
        nurseRepository.save(nurse);

        return "Nurse assigned Successfully";

    }

    @Override
    public String assignDoctorToNurse(long nurseId, long doctorId) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", nurseId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", doctorId));

        if (nurse.isAllocated()) {
            return "Nurse already allocated";
        }

        nurse.setDoctor(doctor);
        nurse.setAllocated(true);
        nurseRepository.save(nurse);

        return "Doctor assigned to nurse successfully";

    }

    @Override
    public String acceptDoctorRequest(AcceptDoctorRequest acceptDoctorRequest) {
        DoctorRequest doctorRequest = doctorRequestRepository.findById(acceptDoctorRequest.getRequestId())
                .orElseThrow(() -> new UserNotFoundException("Request", "Id", acceptDoctorRequest.getRequestId()));

        doctorRequest.setRequestRejectOrAcceptReason(acceptDoctorRequest.getAcceptOrDeclineReason());

        Doctor doctor = doctorRequest.getDoctor();
        Nurse oldNurse = nurseRepository.findById(doctorRequest.getOldNurseId())
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", doctorRequest.getOldNurseId()));

        Nurse newNurse = nurseRepository.findById(doctorRequest.getNewNurseId())
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", doctorRequest.getNewNurseId()));


        if (newNurse.isAllocated()) {
            return "Nurse already allocated";
        }

        Set<Nurse> nurses = doctor.getNurses();
        nurses.remove(oldNurse);
        oldNurse.setAllocated(false);
        oldNurse.setDoctor(null);
        nurses.add(newNurse);
        newNurse.setAllocated(true);
        newNurse.setDoctor(doctor);
        doctorRepository.save(doctor);
        nurseRepository.save(oldNurse);
        nurseRepository.save(newNurse);

        doctorRequest.setRequestStatus("APPROVED");
        doctorRequestRepository.save(doctorRequest);

        return "Nurse Replaced Successfully";
    }

    @Override
    public String declineDoctorRequest(AcceptDoctorRequest acceptDoctorRequest) {
        DoctorRequest doctorRequest = doctorRequestRepository.findById(acceptDoctorRequest.getRequestId())
                .orElseThrow(() -> new UserNotFoundException("Request", "Id", acceptDoctorRequest.getRequestId()));

        doctorRequest.setRequestRejectOrAcceptReason(acceptDoctorRequest.getAcceptOrDeclineReason());
        doctorRequest.setRequestStatus("DECLINED");
        doctorRequestRepository.save(doctorRequest);
        return "Request Rejected";

    }

    @Override
    public void acceptNurseRequest(AcceptNurseRequest acceptNurseRequest) {
        NurseRequest nurseRequest = nurseRequestRepository.findById(acceptNurseRequest.getRequestId())
                .orElseThrow(() -> new UserNotFoundException("Request", "Id", acceptNurseRequest.getRequestId()));
        nurseRequest.setRequestRejectOrAcceptReason(acceptNurseRequest.getAcceptOrDeclineReason());

        Nurse nurse = nurseRequest.getNurse();
        Doctor oldDoctor = doctorRepository.findById(nurseRequest.getOldDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", nurseRequest.getOldDoctorId()));

        Doctor newDoctor = doctorRepository.findById(nurseRequest.getNewDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", nurseRequest.getOldDoctorId()));


        nurse.setDoctor(null);
        nurse.setDoctor(newDoctor);
        oldDoctor.getNurses().remove(nurse);
        newDoctor.getNurses().add(nurse);

        nurseRepository.save(nurse);
        doctorRepository.save(oldDoctor);
        doctorRepository.save(newDoctor);


        nurseRequest.setRequestStatus("APPROVED");
        nurseRequestRepository.save(nurseRequest);
    }

    @Override
    public void declineNurseRequest(AcceptNurseRequest acceptNurseRequest) {
        NurseRequest nurseRequest = nurseRequestRepository.findById(acceptNurseRequest.getRequestId())
                .orElseThrow(() -> new UserNotFoundException("Request", "Id", acceptNurseRequest.getRequestId()));

        nurseRequest.setRequestRejectOrAcceptReason(acceptNurseRequest.getAcceptOrDeclineReason());
        nurseRequest.setRequestStatus("DECLINED");
        nurseRequestRepository.save(nurseRequest);
    }
}
