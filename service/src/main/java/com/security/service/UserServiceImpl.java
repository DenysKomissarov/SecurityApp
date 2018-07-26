package com.security.service;


import com.security.data.audit.dao.UserRepository;
import com.security.data.audit.model.User;
import com.security.data.audit.model.UserFile;
import com.security.dto.CreateUserDTO;
import com.security.dto.LoginDTO;
import com.security.dto.SignUpDTO;
import com.security.exception.BadRequestException;
import com.security.exception.UserNotFoundException;
import com.security.responce.ApiResponse;
import com.security.responce.JwtAuthenticationResponse;
import com.security.security.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JWTService jwtService;

    @Autowired
    public UserServiceImpl( UserRepository userRepository,
                           PasswordEncoder passwordEncoder, JWTService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public ResponseEntity<ApiResponse> editUser(CreateUserDTO editUserDTO){

        User user = getUser(editUserDTO.getEmail());
        userRepository.save(updateUser(user, editUserDTO));
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "User update successfully"), HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> deleteUser(String email) {
        if(!userRepository.existsByEmail(email)) {
            throw  new UserNotFoundException("User not found!", "there is no user with email! " + email);
        }
        userRepository.deleteUserByEmail(email);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "User removed successfully"), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createUser(CreateUserDTO createUserDTO) {
        if(userRepository.existsByUsername(createUserDTO.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }
        if(userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new BadRequestException ("Email Address already in use!");
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

        User user = getUser( loginDTO.getUsernameOrEmail());

        if (user.getRoles().contains("ROLE_ADMIN") && passwordEncoder.matches( loginDTO.getPassword(), user.getPassword())){
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwtService.generateToken(user)));
        }
        if (passwordEncoder.matches( loginDTO.getPassword(), user.getPassword()) ){
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwtService.generateToken(user)));
        }

        throw new BadRequestException("password wrong");
    }

    @Override
    public ResponseEntity<ApiResponse> registerUser(SignUpDTO signUpDTO) {
        if(userRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new BadRequestException( "Username is already taken!");
        }

        if(userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new BadRequestException("Email Address already in use!");
        }

        User user = new User(signUpDTO.getName(), signUpDTO.getUsername(),
                signUpDTO.getEmail(), signUpDTO.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

//        user.setRoles(userRepository.findAll().size() > 0 ? Collections.singleton(RoleName.ROLE_USER.name()) : Collections.singleton(RoleName.ROLE_ADMIN.name()));
        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "User registered successfully"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> addFile(MultipartFile file, String name, String email, String password) {

        UserFile userFile = new UserFile();
        userFile.setFileName(file.getName());

        User user = getUser(email);
        List<UserFile> userFiles = user.getFiles();
        userFiles.add(userFile);
        user.setFiles(userFiles);
        userRepository.save(user);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "File added successfully"), HttpStatus.CREATED);
    }

    private User getUser(String email){

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
