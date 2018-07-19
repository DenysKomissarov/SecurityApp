package com.security.app.controller;

import com.security.app.dto.CreateUserDTO;
import com.security.app.responce.ApiResponse;
import com.security.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationConditionsImpl.mayEditByUser(#editUserDTO, #authentication)")
    public ResponseEntity<ApiResponse> update(@Valid @RequestBody CreateUserDTO editUserDTO,
                                              Authentication authentication){
        return userService.editUser(editUserDTO);
    }
}

