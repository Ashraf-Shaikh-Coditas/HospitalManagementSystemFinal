package com.springboot.hospitalmanagement.service;

import com.springboot.hospitalmanagement.entity.Doctor;
import com.springboot.hospitalmanagement.entity.Nurse;
import com.springboot.hospitalmanagement.payload.DoctorDto;
import com.springboot.hospitalmanagement.payload.NurseDto;
import com.springboot.hospitalmanagement.payload.Request.ReplaceDoctorDtoRequest;
import com.springboot.hospitalmanagement.payload.Response.ReplaceDoctorDtoResponse;
import com.springboot.hospitalmanagement.payload.Response.ReplaceNurseDtoResponse;

import java.util.List;

public interface NurseService {
    NurseDto addNurse(NurseDto nurseDto);

    NurseDto updateNurse(NurseDto nurseDto, Long nurseId);

    void deleteNurse(Long nurseId);

    List<NurseDto> findAllocatedNurses();

    List<NurseDto> findUnAllocatedNurses();

    NurseDto getNurseById(Long nurseId);

    List<NurseDto> getAllNurses();

    ReplaceDoctorDtoResponse replaceDoctor(ReplaceDoctorDtoRequest replaceDoctorDtoRequest);

    List<ReplaceDoctorDtoResponse> getNurseHistory();

    List<ReplaceDoctorDtoResponse> getNurseRequests();

    Nurse mapToEntity(NurseDto nurseDto);

    NurseDto mapToDto(Nurse nurse);

    DoctorDto getDoctorByNurseId(Long nurseId);

    List<ReplaceDoctorDtoResponse> getHistoryByNurseId(long nurseId);

    List<ReplaceDoctorDtoResponse> getRequestsByNurseId(long doctorId);

    void unAllocateNurse(Long nurseId);

    DoctorDto fetchDoctorByNurseId(long nurseId);
}
