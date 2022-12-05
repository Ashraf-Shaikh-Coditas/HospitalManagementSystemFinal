package com.springboot.hospitalmanagement.service;

import com.springboot.hospitalmanagement.entity.Doctor;
import com.springboot.hospitalmanagement.payload.DoctorDto;
import com.springboot.hospitalmanagement.payload.NurseDto;
import com.springboot.hospitalmanagement.payload.Request.ReplaceNurseDtoRequest;
import com.springboot.hospitalmanagement.payload.Response.ReplaceNurseDtoResponse;

import java.util.List;
import java.util.Set;

public interface DoctorService {

    DoctorDto addDoctor(DoctorDto doctorDto);

    DoctorDto getDoctorById(long doctorId);

    Doctor mapToEntity(DoctorDto doctorDto);

    DoctorDto mapToDto(Doctor doctor);

    DoctorDto updateDoctor(DoctorDto doctorDto, Long doctorId);

    ReplaceNurseDtoResponse replaceNurse(ReplaceNurseDtoRequest replaceNurseDto);

    List<ReplaceNurseDtoResponse> getDoctorHistory();

    List<ReplaceNurseDtoResponse> getDoctorRequests();

    void deleteDoctor(Long doctorId);

    List<DoctorDto> getAllDoctors();

    Set<NurseDto> getAllNurseAssignedToDoctor(Long doctorId);

    List<ReplaceNurseDtoResponse> getHistoryByDoctorId(long doctorId);

    List<ReplaceNurseDtoResponse> getRequestsBydoctorId(long doctorId);
}
