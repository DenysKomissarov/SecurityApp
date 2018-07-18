package com.security.app.service;

import com.security.app.controller.advice.PermisionException;
import com.security.app.controller.advice.UserNotFoundException;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

//    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JWTService jwtService;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, JWTService jwtService){
//        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public ResponseEntity<ApiResponse> editUserByAdmin(CreateUserDTO editUserDTO){

        User user = getUser(editUserDTO.getEmail(), editUserDTO.getEmail());
        userRepository.save(updateUser(user, editUserDTO));
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "User update successfully"), HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse> editByUser(CreateUserDTO editUserDTO, Authentication authentication){

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = getUser(editUserDTO.getEmail(), editUserDTO.getEmail());

        if (editUserDTO.getEmail().equals(userPrincipal.getUser().getEmail())){

            userRepository.save(updateUser(user, editUserDTO));
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "User update successfully"), HttpStatus.CREATED);
        }

        throw  new PermisionException("You don't have permission!", "you can only edit your data");
//        return new ResponseEntity<>(new ApiResponse(HttpStatus.FORBIDDEN, "you can only edit your data"), HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> deleteUser(String email) {
        if(!userRepository.existsByEmail(email)) {
            throw  new UserNotFoundException("User not found!", "there is no user with that email! " + email);
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "there is no user with that name!"),
//                    HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteUserByEmail(email);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "User removed successfully"), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createUser(CreateUserDTO createUserDTO) {
        if(userRepository.existsByUsername(createUserDTO.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(createUserDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(createUserDTO.getName(), createUserDTO.getUsername(),
                createUserDTO.getEmail(), createUserDTO.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(createUserDTO.getRoles());
        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "User created successfully"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(LoginDTO loginDTO) throws UnsupportedEncodingException {

        User user = getUser(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail());

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
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpDTO.getName(), signUpDTO.getUsername(),
                signUpDTO.getEmail(), signUpDTO.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoles(userRepository.findAll().size() > 0 ? Collections.singleton(RoleName.ROLE_USER.name()) : Collections.singleton(RoleName.ROLE_ADMIN.name()));
        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "User registered successfully"), HttpStatus.CREATED);
    }

    private User getUser(String username, String email){

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found!", "User not found with email " + email));
        return user;
    }

    private User updateUser(User user, CreateUserDTO editUserDTO){

        user.setUsername(editUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(editUserDTO.getPassword()));
        user.setName(editUserDTO.getName());
        user.setRoles(editUserDTO.getRoles());

        return user;
    }
}
