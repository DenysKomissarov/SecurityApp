package com.security.app.controller;

import com.security.app.dto.CreateUserDTO;
import com.security.app.exception.AppException;
import com.security.app.model.User;
import com.security.app.repository.UserRepository;
import com.security.app.responce.ApiResponse;
import com.security.app.security.CurrentUser;
import com.security.app.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
    public ResponseEntity<?> editUser(@CurrentUser UserPrincipal userPrincipal,
                                      @Valid @RequestBody CreateUserDTO editUserDTO) { // FIXME You need to avoid work inside controllers (With out reason)

        List<String> authorities = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority
        ).collect(Collectors.toList());

        User user = userRepository.findByUsernameOrEmail(editUserDTO.getEmail(), editUserDTO.getEmail())
                .orElseThrow(() -> new AppException("User not found."));

        if (authorities.contains("ROLE_ADMIN")){

            userRepository.save(updateUser(user, editUserDTO));
            return new ResponseEntity<>(new ApiResponse(true, "User update successfully"), HttpStatus.CREATED);
        }


        if (editUserDTO.getEmail().equals(userPrincipal.getEmail())){

            userRepository.save(updateUser(user, editUserDTO));
            return new ResponseEntity<>(new ApiResponse(true, "User update successfully"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new ApiResponse(false, "you can only edit your data"), HttpStatus.BAD_REQUEST);

    }


    private User updateUser(User user, CreateUserDTO editUserDTO){

        user.setUsername(editUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(editUserDTO.getPassword()));
        user.setName(editUserDTO.getName());
        user.setRoles(editUserDTO.getRoles());

        return user;
    }

}
