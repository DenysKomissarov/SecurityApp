package com.security.app.controller;

import com.security.app.dto.CreateUserDTO;
import com.security.app.responce.ApiResponse;
import com.security.app.security.UserPrincipal;
import com.security.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse> editUser(@Valid @RequestBody CreateUserDTO editUserDTO,
                                                Authentication authentication) {

        return userService.editUser((UserPrincipal) authentication.getPrincipal(), editUserDTO);
    }
}
