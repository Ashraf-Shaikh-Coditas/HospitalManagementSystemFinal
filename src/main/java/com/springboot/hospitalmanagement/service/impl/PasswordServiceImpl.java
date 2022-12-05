package com.springboot.hospitalmanagement.service.impl;

import com.springboot.hospitalmanagement.entity.User;
import com.springboot.hospitalmanagement.payload.ForgotPasswordDto;
import com.springboot.hospitalmanagement.payload.ResetPasswordDto;
import com.springboot.hospitalmanagement.repository.UserRepository;
import com.springboot.hospitalmanagement.service.PasswordService;
import com.springboot.hospitalmanagement.util.PasswordEncoderProvider;
import com.springboot.hospitalmanagement.util.RandomPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    @Override
    public int forgotPassword(ForgotPasswordDto forgotPasswordDto) {

        String username = forgotPasswordDto.getEmail();

        System.out.println(username);

        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username).get();

            String newPassword = RandomPasswordGenerator.passwordGenerator();
            user.setPassword(PasswordEncoderProvider.passwordEncoding(newPassword.trim()));
            userRepository.save(user);

            emailSenderService.sendSimpleEmail(user.getUsername(),
                    "Hi user " + user.getUsername() +
                            "\nYour new password is : " + newPassword, "PASSWORD CHANGED SUCCESSFULLY");
            return 1;
        } else {
            System.out.println("User not exists.");
            return 0;
        }

    }

    @Override
    public String resetPassword(ResetPasswordDto resetPasswordDto) {

        String username = resetPasswordDto.getUsername();
        String oldPassWord = resetPasswordDto.getOldPassword();
        String newPassWord = resetPasswordDto.getNewPassword();


        if (userRepository.existsByUsername(username)) {

            User user = userRepository.findByUsername(username).get();


            if (new BCryptPasswordEncoder().matches(oldPassWord, user.getPassword())) {

                user.setPassword(PasswordEncoderProvider.passwordEncoding(newPassWord));
                userRepository.save(user);
                return "Password changed successfully";
            }
        }
        return "Password Not Changed";
    }
}
