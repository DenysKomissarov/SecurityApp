package com.security.web.controller;

import com.security.dto.LoginDTO;
import com.security.dto.SignUpDTO;
import com.security.responce.ApiResponse;
import com.security.responce.JwtAuthenticationResponse;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;
    @Autowired
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) throws UnsupportedEncodingException {

        return userService.authenticateUser(loginDTO);

    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        return  userService.registerUser(signUpDTO);
    }
}
