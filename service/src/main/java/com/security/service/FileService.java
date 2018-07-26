package com.security.service;

import com.security.dto.LoginDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileService {

    void store(MultipartFile file, String name, String email, String password);
    Resource loadFile(String filename) throws FileNotFoundException;
    List<String> getFiles(LoginDTO loginDTO);

}

