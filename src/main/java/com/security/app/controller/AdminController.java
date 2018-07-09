package com.security.app.controller;

import com.security.app.dto.CreateUserDTO;
import com.security.app.model.User;
import com.security.app.repository.UserRepository;
import com.security.app.responce.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
//    @Autowired
//    RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteUser(@RequestParam String email) {
        if(!userRepository.existsByEmail(email)) {
            return new ResponseEntity(new ApiResponse(false, "there is no user with that name!"),
                    HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteUserByEmail(email);
        return new ResponseEntity<>(new ApiResponse(true, "User removed successfully"), HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        if(userRepository.existsByUsername(createUserDTO.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(createUserDTO.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(createUserDTO.getName(), createUserDTO.getUsername(),
                createUserDTO.getEmail(), createUserDTO.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        Set<String> roles = new HashSet<>();
//        for (RoleName role : createUserDTO.getRoles()) {
//            roles.add(roleRepository.findByName(role).get().toString());
//        }
        user.setRoles(createUserDTO.getRoles());
        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.CREATED);

    }


}
