package com.example.jobportal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "C://Users//Acer//IdeaProjects//JobPortal//src//main//resources//static//uploadImg/";

    public void saveFile(MultipartFile file, String fileName) throws IOException {
        String filePath = UPLOAD_DIR + fileName;
        File dest = new File(filePath);
        file.transferTo(dest);
    }
}
