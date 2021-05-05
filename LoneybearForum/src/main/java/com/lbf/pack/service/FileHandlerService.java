package com.lbf.pack.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public interface FileHandlerService {
    public Boolean SaveMultiPartFileToLocalPath(MultipartFile multipartFile, String path) throws IOException;

    public Boolean OverwriteMultiPartFile(MultipartFile multipartFile, String path) throws IOException;
}
