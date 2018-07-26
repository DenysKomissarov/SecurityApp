package com.security.service;

import com.security.data.audit.dao.FileRepository;
import com.security.data.audit.dao.UserRepository;
import com.security.data.audit.model.User;
import com.security.data.audit.model.UserFile;
import com.security.dto.LoginDTO;
import com.security.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final Path rootLocation = Paths.get("images");
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;

    @Override
    public void store(MultipartFile file, String name, String email, String password){
        try {

            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));

            User user = userRepository.findByEmail(email).orElseThrow(() ->
                    new UserNotFoundException("User not found!", "User not found with email " + email));
            UserFile userFile = new UserFile();
            userFile.setFileName(rootLocation.resolve(file.getOriginalFilename()).toString());
            userFile.setOwner(user);
            fileRepository.save(userFile);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource loadFile(String filename) throws FileNotFoundException {

        String path = rootLocation.resolve(filename).toString() ;
        InputStream inputStream = new FileInputStream(path);
        return new InputStreamResource(inputStream);
    }

    @Override
    public List<String> getFiles(LoginDTO loginDTO) {
       User user = userRepository.findByEmail(loginDTO.getUsernameOrEmail()).orElseThrow(() ->
               new UserNotFoundException("User not found!", "User not found with email " + loginDTO.getUsernameOrEmail()));
       List<String> filePath = user.getFiles().stream()
               .map(UserFile::getFileName)
               .collect(Collectors.toCollection(ArrayList::new));
        return filePath;

    }
}