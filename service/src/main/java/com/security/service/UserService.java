package com.security.service;



import com.security.dto.CreateUserDTO;
import com.security.dto.LoginDTO;
import com.security.dto.SignUpDTO;
import com.security.responce.ApiResponse;
import com.security.responce.JwtAuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

public interface UserService {

    ResponseEntity<ApiResponse> editUser(CreateUserDTO editUserDTO);

    ResponseEntity<ApiResponse> deleteUser(String email);

    ResponseEntity<ApiResponse> createUser(CreateUserDTO createUserDTO);

    ResponseEntity<JwtAuthenticationResponse> authenticateUser(LoginDTO loginDTO) throws UnsupportedEncodingException;

    ResponseEntity<ApiResponse> registerUser(SignUpDTO signUpDTO);

    ResponseEntity<ApiResponse> addFile(MultipartFile file, String name, String email, String password );

}
