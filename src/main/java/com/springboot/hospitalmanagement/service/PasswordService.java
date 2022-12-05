package com.springboot.hospitalmanagement.service;

import com.springboot.hospitalmanagement.payload.ForgotPasswordDto;
import com.springboot.hospitalmanagement.payload.ResetPasswordDto;

public interface PasswordService {
    int forgotPassword(ForgotPasswordDto forgotPasswordDto);

    String resetPassword(ResetPasswordDto resetPasswordDto);
}
