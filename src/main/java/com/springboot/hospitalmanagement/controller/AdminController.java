package com.springboot.hospitalmanagement.controller;

import com.springboot.hospitalmanagement.payload.AssignDoctorDto;
import com.springboot.hospitalmanagement.payload.AssignNurseDto;
import com.springboot.hospitalmanagement.payload.Request.AcceptDoctorRequest;
import com.springboot.hospitalmanagement.payload.Request.AcceptNurseRequest;
import com.springboot.hospitalmanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignNurseToDoctor")
    public ResponseEntity<Map<String, String>> assignNurseToDoctor(@RequestBody AssignNurseDto assignNurseDto) {
        String status = adminService.assignNurseToDoctor(assignNurseDto.getDoctorId(),
                assignNurseDto.getNurseId());
        HashMap<String, String> map = new HashMap<>();
        if (status.equals("Nurse already allocated")) {
            map.put("Data : null ", "Error : " + status);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        map.put("Data : " + status, "Error : null ");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignDoctorToNurse")
    public ResponseEntity<Map<String, String>> assignDoctorToNurse(@RequestBody AssignDoctorDto assignDoctorDto) {
        String status = adminService.assignDoctorToNurse(assignDoctorDto.getNurseId(),
                assignDoctorDto.getDoctorId());
        HashMap<String, String> map = new HashMap<>();
        if (status.equals("Nurse already allocated")) {
            map.put("Data : null ", "Error : " + status);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        map.put("Data : " + status, "Error : null ");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/acceptDoctorRequest")
    public ResponseEntity<Map<String, String>> acceptDoctorRequest(@RequestBody AcceptDoctorRequest acceptDoctorRequest) {
        String status = adminService.acceptDoctorRequest(acceptDoctorRequest);
        HashMap<String, String> map = new HashMap<>();
        if (status.equals("Nurse already allocated")) {
            map.put("Data : null ", "Error : " + status);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        map.put("Data : " + status, "Error : null ");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/declineDoctorRequest")
    public ResponseEntity<Map<String, String>> declineDoctorRequest(@RequestBody AcceptDoctorRequest acceptDoctorRequest) {
        adminService.declineDoctorRequest(acceptDoctorRequest);
        String status = "Request Declined for request id : " + acceptDoctorRequest.getRequestId();
        HashMap<String, String> map = new HashMap<>();
        map.put("Data : " + status, "Error : null ");
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/acceptNurseRequest")
    public ResponseEntity<Map<String, String>> acceptNurseRequest(@RequestBody AcceptNurseRequest acceptNurseRequest) {
        adminService.acceptNurseRequest(acceptNurseRequest);
        String status = "Request Accepted for request id : " + acceptNurseRequest.getRequestId();
        HashMap<String, String> map = new HashMap<>();
        map.put("Data : " + status, "Error : null ");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/declineNurseRequest")
    public ResponseEntity<Map<String, String>> declineNurseRequest(@RequestBody AcceptNurseRequest acceptNurseRequest) {
        adminService.declineNurseRequest(acceptNurseRequest);
        String status = "Request Declined for request id : " + acceptNurseRequest.getRequestId();
        HashMap<String, String> map = new HashMap<>();
        map.put("Data : " + status, "Error : null ");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
