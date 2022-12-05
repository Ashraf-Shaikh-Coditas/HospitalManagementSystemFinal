package com.springboot.hospitalmanagement.controller;

import com.springboot.hospitalmanagement.payload.ForgotPasswordDto;
import com.springboot.hospitalmanagement.payload.JWTAuthResponse;
import com.springboot.hospitalmanagement.payload.LoginDto;
import com.springboot.hospitalmanagement.repository.RoleRepository;
import com.springboot.hospitalmanagement.repository.UserRepository;
import com.springboot.hospitalmanagement.security.JwtTokenProvider;
import com.springboot.hospitalmanagement.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordService passwordService;


    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> autheticateUser(@RequestBody LoginDto loginDto) {
        System.out.println(loginDto.getUsernameOrEmail());
        System.out.println(loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token,
                tokenProvider.getRoleFromUsername(tokenProvider.getUsernameFromJWT(token)
                ), tokenProvider.getIdFromUsername(tokenProvider.getUsernameFromJWT(token))));
    }


}
