package com.springboot.hospitalmanagement.controller;

import com.springboot.hospitalmanagement.payload.ForgotPasswordDto;
import com.springboot.hospitalmanagement.payload.ResetPasswordDto;
import com.springboot.hospitalmanagement.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/credentials")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;


    @CrossOrigin
    @PostMapping("/forgotPassword")
    public ResponseEntity<HashMap<String, String>> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        int status = passwordService.forgotPassword(forgotPasswordDto);
        HashMap<String, String> map = new HashMap<>();
        if (status > 0) {
            map.put("Msg : Password Changed Successfully", "Error : null");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("Msg : null", "Error : User Not Exists with given username");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);

        }
    }

    @CrossOrigin
    @PostMapping("/resetPassword")
    public ResponseEntity<HashMap<String, String>> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        String status = passwordService.resetPassword(resetPasswordDto);
        HashMap<String, String> map = new HashMap<>();
        if (status.equals("Password changed successfully")) {

            map.put("Msg : Password Changed Successfully", "Error : null");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("Msg : null", "Error : User Not Exists with given username");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);

        }

    }
}
