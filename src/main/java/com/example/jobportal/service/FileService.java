package com.example.jobportal.service;

import com.example.jobportal.model.userDocs;
import com.example.jobportal.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private static final String UPLOAD_DIR = "C://Users//Acer//IdeaProjects//JobPortal//src//main//resources//static//uploadImg/";

    public void saveFile(MultipartFile file, String fileName) throws IOException {
        String filePath = UPLOAD_DIR + fileName;
        File dest = new File(filePath);
        file.transferTo(dest);
    }

    public List<userDocs> findAll() {
        return fileRepository.findAll();
    }

    public Page<userDocs> findUserWithPagination(int offSet, int pageSize){
        //offset=next element

        Page<userDocs> user = fileRepository.findAll(PageRequest.of(offSet, pageSize));
        return user;
    }


}
