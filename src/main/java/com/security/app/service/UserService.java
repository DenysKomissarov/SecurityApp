package com.security.app.service;

import com.security.app.dto.CreateUserDTO;
import com.security.app.dto.LoginDTO;
import com.security.app.dto.SignUpDTO;
import com.security.app.responce.ApiResponse;
import com.security.app.responce.JwtAuthenticationResponse;
import com.security.app.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.io.UnsupportedEncodingException;

public interface UserService {

    ResponseEntity<ApiResponse> editUser(CreateUserDTO editUserDTO);

    ResponseEntity<ApiResponse> deleteUser(String email);

    ResponseEntity<ApiResponse> createUser(CreateUserDTO createUserDTO);

    ResponseEntity<JwtAuthenticationResponse> authenticateUser(LoginDTO loginDTO) throws UnsupportedEncodingException;

    ResponseEntity<ApiResponse> registerUser(SignUpDTO signUpDTO);

}
