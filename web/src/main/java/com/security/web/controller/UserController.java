package com.security.web.controller;

import com.security.dto.CreateUserDTO;
import com.security.responce.ApiResponse;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

