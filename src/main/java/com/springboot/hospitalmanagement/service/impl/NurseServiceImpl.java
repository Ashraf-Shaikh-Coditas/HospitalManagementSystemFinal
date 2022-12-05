package com.springboot.hospitalmanagement.service.impl;

import com.springboot.hospitalmanagement.entity.*;
import com.springboot.hospitalmanagement.exception.UserNotFoundException;
import com.springboot.hospitalmanagement.payload.DoctorDto;
import com.springboot.hospitalmanagement.payload.NurseDto;
import com.springboot.hospitalmanagement.payload.Request.ReplaceDoctorDtoRequest;
import com.springboot.hospitalmanagement.payload.Response.ReplaceDoctorDtoResponse;
import com.springboot.hospitalmanagement.repository.*;
import com.springboot.hospitalmanagement.service.DoctorService;
import com.springboot.hospitalmanagement.service.NurseService;
import com.springboot.hospitalmanagement.util.PasswordEncoderProvider;
import com.springboot.hospitalmanagement.util.RandomPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NurseServiceImpl implements NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NurseRequestRepository nurseRequestRepository;

    @Override
    public NurseDto addNurse(NurseDto nurseDto) {
        String password = RandomPasswordGenerator.passwordGenerator();
        nurseDto.setPassword(password);
        Nurse nurse = mapToEntity(nurseDto);
        nurse.setAllocated(false);
        Nurse addedNurse = nurseRepository.save(nurse);
        NurseDto newNurseDto = mapToDto(addedNurse);
        newNurseDto.setPassword(password);
        newNurseDto.setAllocated(false);
        saveUserCredentials(newNurseDto);
        return newNurseDto;
    }

    @Override
    public NurseDto updateNurse(NurseDto nurseDto, Long nurseId) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", nurseId));

        nurse.setNurseName(nurseDto.getNurseName());
        nurseRepository.save(nurse);
        return mapToDto(nurse);

    }

    @Override
    public void deleteNurse(Long nurseId) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", nurseId));
        Doctor doctor = nurse.getDoctor();

        nurse.setDeleted(true);
        nurse.setDoctor(null);
        nurse.setAllocated(false);
        nurseRepository.save(nurse);
        doctor.getNurses().remove(nurse);
        doctorRepository.save(doctor);

    }

    @Override
    public List<NurseDto> findAllocatedNurses() {
        List<Nurse> nurses = nurseRepository.findByIsAllocatedTrueAndIsDeletedFalse();

        return nurses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<NurseDto> findUnAllocatedNurses() {
        List<Nurse> nurses = nurseRepository.findByIsAllocatedFalseAndIsDeletedFalse();

        return nurses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public NurseDto getNurseById(Long nurseId) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", nurseId));


        return mapToDto(nurse);
    }


    @Override
    public List<NurseDto> getAllNurses() {
        List<Nurse> nurses = nurseRepository.findByIsDeletedFalse();
        return nurses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ReplaceDoctorDtoResponse replaceDoctor(ReplaceDoctorDtoRequest replaceDoctorDtoRequest) {
        Nurse nurse = nurseRepository.findById(replaceDoctorDtoRequest.getNurseId())
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", replaceDoctorDtoRequest.getNurseId()));
        Doctor oldDoctor = doctorRepository.findById(replaceDoctorDtoRequest.getOldDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", replaceDoctorDtoRequest.getOldDoctorId()));

        Doctor newDoctor = doctorRepository.findById(replaceDoctorDtoRequest.getNewDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", replaceDoctorDtoRequest.getNewDoctorId()));


        NurseRequest nurseRequest = new NurseRequest();
        nurseRequest.setNurse(nurse);
        nurseRequest.setOldDoctorId(replaceDoctorDtoRequest.getOldDoctorId());
        nurseRequest.setNewDoctorId(replaceDoctorDtoRequest.getNewDoctorId());
        nurseRequest.setReplaceReason(replaceDoctorDtoRequest.getReplaceReason());
        NurseRequest newNurseRequest = nurseRequestRepository.save(nurseRequest);

        ReplaceDoctorDtoResponse replaceDoctorDtoResponse = new ReplaceDoctorDtoResponse();
        replaceDoctorDtoResponse.setRequestId(newNurseRequest.getRequestId());
        replaceDoctorDtoResponse.setNurseName(nurse.getNurseName());
        replaceDoctorDtoResponse.setOldDoctorName(oldDoctor.getDoctorName());
        replaceDoctorDtoResponse.setNewDoctorName(newDoctor.getDoctorName());
        replaceDoctorDtoResponse.setReplaceReason(replaceDoctorDtoRequest.getReplaceReason());
        replaceDoctorDtoResponse.setStatus("PENDING");
        return replaceDoctorDtoResponse;

    }

    @Override
    public List<ReplaceDoctorDtoResponse> getNurseHistory() {
        List<NurseRequest> nurseRequests = nurseRequestRepository.findAll();
        return nurseRequests.stream().map(this::mapRequestToResponse).collect(Collectors.toList());

    }

    @Override
    public List<ReplaceDoctorDtoResponse> getNurseRequests() {
        List<NurseRequest> nurseRequests = nurseRequestRepository.findByRequestStatusLike("PENDING");
        return nurseRequests.stream().map(this::mapRequestToResponse).collect(Collectors.toList());
    }


    @Override
    public Nurse mapToEntity(NurseDto nurseDto) {
        Nurse nurse = new Nurse();
        nurse.setNurseName(nurseDto.getNurseName());
        nurse.setEmail(nurseDto.getEmail());
        if (nurseDto.getDoctorDto() == null) {
            return nurse;
        }
        nurse.setDoctor(doctorService.mapToEntity(nurseDto.getDoctorDto()));
        return nurse;
    }

    @Override
    public NurseDto mapToDto(Nurse nurse) {
        System.out.println(nurse.getDoctor());
        NurseDto nurseDto = new NurseDto();
        nurseDto.setNurseId(nurse.getNurseId());
        nurseDto.setNurseName(nurse.getNurseName());
        nurseDto.setEmail(nurse.getEmail().trim());
        nurseDto.setAllocated(nurse.isAllocated());
        if (nurse.getDoctor() == null) {
            return nurseDto;
        }
        nurseDto.setDoctorDto(doctorService.mapToDto(nurse.getDoctor()));
        return nurseDto;

    }

    private void saveUserCredentials(NurseDto nurseDto) {
        User user = new User();
        user.setUsername(nurseDto.getEmail().trim());
        user.setPassword(PasswordEncoderProvider.passwordEncoding(nurseDto.getPassword().trim()));
        Role role = roleRepository.findByRoleName("ROLE_NURSE").get();
        user.setRoles(Collections.singleton(role));
        userRepository.save(user);
    }

    private ReplaceDoctorDtoResponse mapRequestToResponse(NurseRequest nurseRequest) {
        Nurse nurse = nurseRequest.getNurse();
        Doctor oldDoctor = doctorRepository.findById(nurseRequest.getOldDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", nurseRequest.getOldDoctorId()));
        Doctor newDoctor = doctorRepository.findById(nurseRequest.getNewDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", nurseRequest.getNewDoctorId()));


        ReplaceDoctorDtoResponse replaceDoctorDtoResponse = new ReplaceDoctorDtoResponse();
        replaceDoctorDtoResponse.setRequestId(nurseRequest.getRequestId());
        replaceDoctorDtoResponse.setNurseName(nurse.getNurseName());
        replaceDoctorDtoResponse.setOldDoctorName(oldDoctor.getDoctorName());
        replaceDoctorDtoResponse.setNewDoctorName(newDoctor.getDoctorName());
        replaceDoctorDtoResponse.setStatus(nurseRequest.getRequestStatus());
        replaceDoctorDtoResponse.setReplaceReason(nurseRequest.getReplaceReason());
        replaceDoctorDtoResponse.setAcceptOrDeclineReason(nurseRequest.getRequestRejectOrAcceptReason());
        return replaceDoctorDtoResponse;
    }

    @Override
    public DoctorDto getDoctorByNurseId(Long nurseId) {
        return doctorService.mapToDto(nurseRepository.findByNurseId(nurseId)
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", nurseId)));

    }

    @Override
    public List<ReplaceDoctorDtoResponse> getHistoryByNurseId(long nurseId) {
        List<NurseRequest> nurseRequests = nurseRequestRepository.findByNurse_NurseId(nurseId);
        return nurseRequests.stream().map(this::mapRequestToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ReplaceDoctorDtoResponse> getRequestsByNurseId(long nurseId) {
        List<NurseRequest> nurseRequests = nurseRequestRepository
                .findByNurse_NurseIdAndRequestStatus(nurseId, "PENDING");
        return nurseRequests.stream().map(this::mapRequestToResponse).collect(Collectors.toList());
    }

    @Override
    public void unAllocateNurse(Long nurseId) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", nurseId));

        Doctor doctor = nurse.getDoctor();
        nurse.setDoctor(null);
        nurse.setAllocated(false);
        nurseRepository.save(nurse);
        doctor.getNurses().remove(nurse);
        doctorRepository.save(doctor);

    }

    @Override
    public DoctorDto fetchDoctorByNurseId(long nurseId) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", nurseId));

        Doctor doctor = nurse.getDoctor();
        return doctorService.mapToDto(doctor);
    }
}
