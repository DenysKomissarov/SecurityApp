package com.security.app.controller;

import com.security.app.dto.CreateUserDTO;
import com.security.app.responce.ApiResponse;
import com.security.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class AdminController {

    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam String email) {

        return userService.deleteUser(email);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {

        return userService.createUser(createUserDTO);

    }


}
