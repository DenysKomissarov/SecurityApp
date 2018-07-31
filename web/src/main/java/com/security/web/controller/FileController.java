package com.security.web.controller;

import com.security.dto.LoginDTO;
import com.security.dto.MultipartDto;
import com.security.service.FileService;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {


    private FileService fileService;

    @Autowired
    public FileController(FileService fileService, ServletContext servletContext, UserService userService){
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadMultipartFile(MultipartDto multipartDto ) {

        fileService.store(multipartDto);

        return new ResponseEntity<>("File uploaded successfully! " + multipartDto.getUploadfile().getOriginalFilename(), HttpStatus.OK);
    }


    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) throws FileNotFoundException {

        Resource file = fileService.loadFile(filename);
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        respHeaders.setContentDispositionFormData("attachment", filename);


        return new ResponseEntity<>(file, respHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/show", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> showFile(@RequestParam String filename) throws FileNotFoundException {

        Resource file = fileService.loadFile(filename);
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @PostMapping("/get/all")
    public ResponseEntity<List<String>> getAll(@RequestBody LoginDTO loginDTO) {

        List<String> files = fileService.getFiles(loginDTO);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

}
