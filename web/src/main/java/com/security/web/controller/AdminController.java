package com.security.web.controller;

import com.security.dto.CreateUserDTO;
import com.security.responce.ApiResponse;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class AdminController {

    private UserService userService;
    @Autowired
    public AdminController(UserService userService){
        this.userService = userService;
    }

    @DeleteMapping("/delete")
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
