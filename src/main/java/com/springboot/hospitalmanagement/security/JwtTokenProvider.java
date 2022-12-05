package com.springboot.hospitalmanagement.security;

import com.springboot.hospitalmanagement.entity.Doctor;
import com.springboot.hospitalmanagement.entity.Nurse;
import com.springboot.hospitalmanagement.entity.Role;
import com.springboot.hospitalmanagement.entity.User;
import com.springboot.hospitalmanagement.exception.JWTException;
import com.springboot.hospitalmanagement.repository.DoctorRepository;
import com.springboot.hospitalmanagement.repository.NurseRepository;
import com.springboot.hospitalmanagement.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    // In these class we are fetching credentials from app.prop file.

    @Value("${app.jwt-secret}")
    private String jwtSecretKey;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private NurseRepository nurseRepository;

    // generate token method

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();

        return token;
    }

    // get username from token

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getRoleFromUsername(String username) {
        User user = userRepository.findByUsername(username).get();
        for (Role role : user.getRoles()) {
            return role.getRoleName();

        }
        return null;
    }

    public Long getIdFromUsername(String username) {
        String role = getRoleFromUsername(username);
        if (role.equals("ROLE_DOCTOR")) {
            Doctor doctor = doctorRepository.findByEmail(username).get();
            return doctor.getDoctorId();
        } else if (role.trim().equals("ROLE_NURSE")) {
            Nurse nurse = nurseRepository.findByEmail(username).get();
            return nurse.getNurseId();
        } else
            return 0L;
//        Doctor doctor = doctorRepository.findByEmail(username).get();
//        Nurse nurse = nurseRepository.findByEmail(username).get();
//        if(doctor.getDoctorId()>0) {
//            return doctor.getDoctorId();
//        } else if (nurse.getNurseId()>0) {
//            return nurse.getNurseId();
//        } else
//            return 0L; // return 0 if user is admin


    }

    // Validate jwt token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException exception) {
            throw new JWTException(HttpStatus.BAD_REQUEST, "Invalid Jwt Signature");
        } catch (MalformedJwtException exception) {
            throw new JWTException(HttpStatus.BAD_REQUEST, "Invalid Jwt token");
        } catch (ExpiredJwtException exception) {
            throw new JWTException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
        } catch (UnsupportedJwtException exception) {
            throw new JWTException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException exception) {
            throw new JWTException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }

    }
}
