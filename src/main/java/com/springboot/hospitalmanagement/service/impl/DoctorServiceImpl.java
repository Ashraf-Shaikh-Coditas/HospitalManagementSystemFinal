package com.springboot.hospitalmanagement.service.impl;

import com.springboot.hospitalmanagement.entity.*;
import com.springboot.hospitalmanagement.exception.UserNotFoundException;
import com.springboot.hospitalmanagement.payload.DoctorDto;
import com.springboot.hospitalmanagement.payload.NurseDto;
import com.springboot.hospitalmanagement.payload.Request.ReplaceNurseDtoRequest;
import com.springboot.hospitalmanagement.payload.Response.ReplaceNurseDtoResponse;
import com.springboot.hospitalmanagement.repository.*;
import com.springboot.hospitalmanagement.service.DoctorService;
import com.springboot.hospitalmanagement.service.NurseService;
import com.springboot.hospitalmanagement.util.PasswordEncoderProvider;
import com.springboot.hospitalmanagement.util.RandomPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DoctorRequestRepository doctorRequestRepository;


    @Override
    public DoctorDto addDoctor(DoctorDto doctorDto) {
        doctorDto.setPassword(RandomPasswordGenerator.passwordGenerator());
        Doctor doctor = mapToEntity(doctorDto);
        Doctor newDoctor = doctorRepository.save(doctor);
        saveUserCredentials(doctorDto);
        DoctorDto newDoctorDto = mapToDto(newDoctor);
        newDoctorDto.setPassword(doctorDto.getPassword());
        return newDoctorDto;
    }

    @Override
    public DoctorDto getDoctorById(long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", doctorId));

        return mapToDto(doctor);
    }

    public Doctor mapToEntity(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setDoctorName(doctorDto.getDoctorName());
        doctor.setEmail(doctorDto.getEmail().trim());
        doctor.setSpecialization(doctorDto.getSpecialization());
        Set<NurseDto> nurseSet = doctorDto.getNurses();
        Set<Nurse> nurses = new HashSet<>();
        if (nurseSet.size() > 0) {
            nurses = doctorDto.getNurses().stream().map(nurseDto -> nurseService.mapToEntity(nurseDto)).collect(Collectors.toSet());
            doctor.setNurses(nurses);
        }
        doctor.setNurses(nurses);
        return doctor;
    }

    public DoctorDto mapToDto(Doctor doctor) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setDoctorId(doctor.getDoctorId());
        doctorDto.setDoctorName(doctor.getDoctorName());
        doctorDto.setEmail(doctor.getEmail());

        Set<NurseDto> nurseDtos = new HashSet<>();
        nurseDtos = doctor.getNurses().stream().map(nurse ->
                new NurseDto(nurse.getNurseId(),
                        nurse.getNurseName(),
                        nurse.getEmail(),
                        nurse.isAllocated())
        ).collect(Collectors.toSet());

        doctorDto.setNurses(nurseDtos);


        doctorDto.setSpecialization(doctor.getSpecialization());
        return doctorDto;
    }

    @Override
    public DoctorDto updateDoctor(DoctorDto doctorDto, Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", doctorId));

        doctor.setDoctorName(doctorDto.getDoctorName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctorRepository.save(doctor);
        return mapToDto(doctor);

    }

    @Override
    public ReplaceNurseDtoResponse replaceNurse(ReplaceNurseDtoRequest replaceNurseDto) {
        Doctor doctor = doctorRepository.findById(replaceNurseDto.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", replaceNurseDto.getDoctorId()));
        Nurse oldNurse = nurseRepository.findById(replaceNurseDto.getOldNurseId())
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", replaceNurseDto.getOldNurseId()));

        Nurse newNurse = nurseRepository.findById(replaceNurseDto.getNewNurseId())
                .orElseThrow(() -> new UserNotFoundException("Nurse", "Id", replaceNurseDto.getNewNurseId()));


        if (newNurse.isAllocated()) {
            return null;
        }

        DoctorRequest doctorRequest = new DoctorRequest();
        doctorRequest.setDoctor(doctor);
        doctorRequest.setNewNurseId(replaceNurseDto.getNewNurseId());
        doctorRequest.setOldNurseId(replaceNurseDto.getOldNurseId());
        doctorRequest.setReplaceReason(replaceNurseDto.getReplaceReason());
        DoctorRequest newDoctorRequest = doctorRequestRepository.save(doctorRequest);

        ReplaceNurseDtoResponse replaceNurseDtoResponse = new ReplaceNurseDtoResponse();
        replaceNurseDtoResponse.setRequestId(newDoctorRequest.getRequestId());
        replaceNurseDtoResponse.setDoctorName(doctor.getDoctorName());
        replaceNurseDtoResponse.setOldNurseName(oldNurse.getNurseName());
        replaceNurseDtoResponse.setNewNurseName(newNurse.getNurseName());
        replaceNurseDtoResponse.setReplaceReason(replaceNurseDto.getReplaceReason());
        replaceNurseDtoResponse.setStatus("PENDING");
        return replaceNurseDtoResponse;
    }

    @Override
    public List<ReplaceNurseDtoResponse> getDoctorHistory() {
        List<DoctorRequest> doctorRequests = doctorRequestRepository.findAll();
        return doctorRequests.stream().map(this::mapRequestToResponse).collect(Collectors.toList());

    }

    @Override
    public List<ReplaceNurseDtoResponse> getDoctorRequests() {
        List<DoctorRequest> doctorRequests = doctorRequestRepository.findByRequestStatusLike("PENDING");
        return doctorRequests.stream().map(this::mapRequestToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", doctorId));
        doctor.setDeleted(true);

        Set<Nurse> nurses = doctor.getNurses();
        for (Nurse nurse : nurses) {
            nurse.setDoctor(null);
            nurse.setAllocated(false);
            nurseRepository.save(nurse);
        }

        doctor.setNurses(null);
        doctorRepository.save(doctor);

    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findByIsDeletedFalse();
        return doctors.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Set<NurseDto> getAllNurseAssignedToDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor", "Id", doctorId));

        Set<NurseDto> nurseDtos = new HashSet<>();
        nurseDtos = doctor.getNurses().stream().map(nurse ->
                new NurseDto(nurse.getNurseId(),
                        nurse.getNurseName(),
                        nurse.getEmail(),
                        nurse.isAllocated())
        ).collect(Collectors.toSet());
        return nurseDtos;
    }

    private void saveUserCredentials(DoctorDto doctorDto) {
        User user = new User();
        user.setUsername(doctorDto.getEmail().trim());
        user.setPassword(PasswordEncoderProvider.passwordEncoding(doctorDto.getPassword().trim()));
        Role role = roleRepository.findByRoleName("ROLE_DOCTOR").get();
        user.setRoles(Collections.singleton(role));
        userRepository.save(user);
    }

    private ReplaceNurseDtoResponse mapRequestToResponse(DoctorRequest doctorRequest) {
        Doctor doctor = doctorRequest.getDoctor();
        Nurse oldNurse = nurseRepository.findById(doctorRequest.getOldNurseId()).get();
        Nurse newNurse = nurseRepository.findById(doctorRequest.getNewNurseId()).get();

        ReplaceNurseDtoResponse replaceNurseDtoResponse = new ReplaceNurseDtoResponse();
        replaceNurseDtoResponse.setRequestId(doctorRequest.getRequestId());
        replaceNurseDtoResponse.setDoctorName(doctor.getDoctorName());
        replaceNurseDtoResponse.setOldNurseName(oldNurse.getNurseName());
        replaceNurseDtoResponse.setNewNurseName(newNurse.getNurseName());
        replaceNurseDtoResponse.setStatus(doctorRequest.getRequestStatus());
        replaceNurseDtoResponse.setReplaceReason(doctorRequest.getReplaceReason());
        replaceNurseDtoResponse.setAcceptOrDeclineReason(doctorRequest.getRequestRejectOrAcceptReason());
        return replaceNurseDtoResponse;
    }

    @Override
    public List<ReplaceNurseDtoResponse> getHistoryByDoctorId(long doctorId) {
        List<DoctorRequest> doctorRequests = doctorRequestRepository.findByDoctor_DoctorId(doctorId);
        return doctorRequests.stream().map(this::mapRequestToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ReplaceNurseDtoResponse> getRequestsBydoctorId(long doctorId) {
        List<DoctorRequest> doctorRequests = doctorRequestRepository
                .findByDoctor_DoctorIdAndRequestStatus(doctorId, "PENDING");
        return doctorRequests.stream().map(this::mapRequestToResponse).collect(Collectors.toList());
    }
}
