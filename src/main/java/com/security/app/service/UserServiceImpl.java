package com.security.app.service;

import com.security.app.dto.CreateUserDTO;
import com.security.app.dto.LoginDTO;
import com.security.app.dto.SignUpDTO;
import com.security.app.model.RoleName;
import com.security.app.model.User;
import com.security.app.repository.UserRepository;
import com.security.app.responce.ApiResponse;
import com.security.app.responce.JwtAuthenticationResponse;
import com.security.app.security.UserPrincipal;
import com.security.app.security.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTService jwtService;

    public ResponseEntity<ApiResponse> editUser(UserPrincipal userPrincipal,
                                                CreateUserDTO editUserDTO){
        List<String> authorities = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority
        ).collect(Collectors.toList());

        User user = userRepository.findByUsernameOrEmail(editUserDTO.getEmail(), editUserDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("there is no user with this data"));

        if (authorities.contains("ROLE_ADMIN")){

            userRepository.save(updateUser(user, editUserDTO));
            return new ResponseEntity<>(new ApiResponse(true, "User update successfully"), HttpStatus.CREATED);
        }


        if (editUserDTO.getEmail().equals(userPrincipal.getUser().getEmail())){

            userRepository.save(updateUser(user, editUserDTO));
            return new ResponseEntity<>(new ApiResponse(true, "User update successfully"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new ApiResponse(false, "you can only edit your data"), HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> deleteUser(String email) {
        if(!userRepository.existsByEmail(email)) {
            return new ResponseEntity<>(new ApiResponse(false, "there is no user with that name!"),
                    HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteUserByEmail(email);
        return new ResponseEntity<>(new ApiResponse(true, "User removed successfully"), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createUser(CreateUserDTO createUserDTO) {
        if(userRepository.existsByUsername(createUserDTO.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(createUserDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(createUserDTO.getName(), createUserDTO.getUsername(),
                createUserDTO.getEmail(), createUserDTO.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(createUserDTO.getRoles());
        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(LoginDTO loginDTO) throws UnsupportedEncodingException {

        User user = userRepository.findByEmail(loginDTO.getUsernameOrEmail()).orElseThrow(() -> new IllegalArgumentException("there is no user with that name"));

        if (user.getRoles().contains("ROLE_ADMIN") && passwordEncoder.matches( loginDTO.getPassword(), user.getPassword())){
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwtService.generateToken(user)));

        }
        if (passwordEncoder.matches( loginDTO.getPassword(), user.getPassword()) ){
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwtService.generateToken(user)));
        }

        throw new IllegalArgumentException("password wrong");
    }

    @Override
    public ResponseEntity<ApiResponse> registerUser(SignUpDTO signUpDTO) {
        if(userRepository.existsByUsername(signUpDTO.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpDTO.getName(), signUpDTO.getUsername(),
                signUpDTO.getEmail(), signUpDTO.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoles(userRepository.findAll().size() > 0 ? Collections.singleton(RoleName.ROLE_USER.name()) : Collections.singleton(RoleName.ROLE_ADMIN.name()));
        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.CREATED);
    }


    private User updateUser(User user, CreateUserDTO editUserDTO){

        user.setUsername(editUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(editUserDTO.getPassword()));
        user.setName(editUserDTO.getName());
        user.setRoles(editUserDTO.getRoles());

        return user;
    }
}
