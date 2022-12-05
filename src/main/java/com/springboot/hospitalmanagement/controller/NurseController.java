package com.springboot.hospitalmanagement.controller;

import com.springboot.hospitalmanagement.payload.DoctorDto;
import com.springboot.hospitalmanagement.payload.NurseDto;
import com.springboot.hospitalmanagement.payload.Request.ReplaceDoctorDtoRequest;
import com.springboot.hospitalmanagement.payload.Response.ReplaceDoctorDtoResponse;
import com.springboot.hospitalmanagement.payload.Response.ReplaceNurseDtoResponse;
import com.springboot.hospitalmanagement.service.DoctorService;
import com.springboot.hospitalmanagement.service.NurseService;
import com.springboot.hospitalmanagement.service.impl.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/hms/")

public class NurseController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private EmailSenderService emailSenderService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addNurse")
    public ResponseEntity<HashMap<NurseDto, String>> addNurse(@RequestBody NurseDto nurseDto) {
        HashMap<NurseDto, String> map = new HashMap<>();
        NurseDto newNurseDto = nurseService.addNurse(nurseDto);
        if (newNurseDto == null) {
            map.put(null, "Error : Error occured while adding Nurse.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        map.put(newNurseDto, "Error : null");
        emailSenderService.sendSimpleEmail(nurseDto.getEmail(), newNurseDto.show(), "NURSE " +
                "VERIFICATION MAIL.");
        return new ResponseEntity<>(map, HttpStatus.CREATED);

    }


    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin
    @PutMapping("/updateNurse/{nurseId}")
    public ResponseEntity<HashMap<NurseDto, String>> updateDoctor(@RequestBody NurseDto nurseDto,
                                                                  @PathVariable long nurseId) {
        HashMap<NurseDto, String> map = new HashMap<>();
        NurseDto newNurseDto = nurseService.updateNurse(nurseDto, nurseId);
        if (newNurseDto == null) {
            map.put(null, "Error : Error occured while updating Nurse.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        map.put(newNurseDto, "Error : null");
        return new ResponseEntity<>(map, HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin
    @DeleteMapping("/deleteNurse/{nurseId}")
    public ResponseEntity<HashMap<String, String>> deleteDoctor(@PathVariable Long nurseId) {
        nurseService.deleteNurse(nurseId);
        HashMap<String, String> map = new HashMap<>();
        map.put("Nurse deleted successfully", "Error : null");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/allocatedNurses")
    public ResponseEntity<List<NurseDto>> getAllocatedNurses() {
        return new ResponseEntity<>(nurseService.findAllocatedNurses(), HttpStatus.OK);
    }

    @GetMapping("/unallocatedNurses")
    public ResponseEntity<List<NurseDto>> getUnAllocatedNurses() {
        return new ResponseEntity<>(nurseService.findUnAllocatedNurses(), HttpStatus.OK);
    }

    @GetMapping("/allNurses")
    public ResponseEntity<List<NurseDto>> getAllNurses() {
        return new ResponseEntity<>(nurseService.getAllNurses(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('NURSE')")
    @PostMapping("/replaceDoctor")
    public ResponseEntity<ReplaceDoctorDtoResponse> replaceDoctor(@RequestBody ReplaceDoctorDtoRequest replaceDoctorDtoRequest) {
        return new ResponseEntity<>(nurseService.replaceDoctor(replaceDoctorDtoRequest), HttpStatus.OK);
    }

    @GetMapping("/getNurseHistory")
    public ResponseEntity<List<ReplaceDoctorDtoResponse>> getNurseHistory() {
        return new ResponseEntity<>(nurseService.getNurseHistory(), HttpStatus.OK);
    }

    @GetMapping("/getNurseRequest")
    public ResponseEntity<List<ReplaceDoctorDtoResponse>> getDoctorRequests() {
        return new ResponseEntity<>(nurseService.getNurseRequests(), HttpStatus.OK);
    }

    @GetMapping("/getNurse/{nurseId}")
    public ResponseEntity<NurseDto> getNurseById(@PathVariable long nurseId) {
        return new ResponseEntity<>(nurseService.getNurseById(nurseId), HttpStatus.OK);
    }

    @GetMapping("/getDoctorByNurse/{nurseId}")
    public ResponseEntity<DoctorDto> getDoctorByNurseById(@PathVariable long nurseId) {
        return new ResponseEntity<>(nurseService.getDoctorByNurseId(nurseId), HttpStatus.OK);
    }

    @GetMapping("/getNurseHistory/{nurseId}")
    public ResponseEntity<List<ReplaceDoctorDtoResponse>> getNurseHistory(@PathVariable long nurseId) {
        return new ResponseEntity<>(nurseService.getHistoryByNurseId(nurseId), HttpStatus.OK);
    }

    @GetMapping("/getNurseRequest/{nurseId}")
    public ResponseEntity<List<ReplaceDoctorDtoResponse>> getNurseRequests(@PathVariable long nurseId) {
        return new ResponseEntity<>(nurseService.getRequestsByNurseId(nurseId), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/unallocateNurse/{nurseId}")
    public ResponseEntity<String> getUnAllocatedNurses(@PathVariable long nurseId) {
        nurseService.unAllocateNurse(nurseId);
        return new ResponseEntity<>("Nurse unAllocated Successfully", HttpStatus.OK);
    }

    @GetMapping("/getDoctorByNurseId/{nurseId}")
    public ResponseEntity<DoctorDto> fetchDoctorByNurseId(@PathVariable long nurseId) {
        return new ResponseEntity<>(nurseService.fetchDoctorByNurseId(nurseId), HttpStatus.OK);
    }


}
