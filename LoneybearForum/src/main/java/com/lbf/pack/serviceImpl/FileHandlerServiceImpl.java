package com.lbf.pack.serviceImpl;

import com.lbf.pack.service.FileHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileHandlerServiceImpl implements FileHandlerService {
    @Override
    public Boolean SaveMultiPartFileToLocalPath(MultipartFile multipartFile, String path) throws IOException {
        if(!multipartFile.isEmpty()){
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            FileOutputStream write = new FileOutputStream(path+"/"+multipartFile.getOriginalFilename());
//        File save = new File("./src/main/resources/static/images/Users"+"/"+name3);
            byte[] bytes = multipartFile.getBytes();

            write.write(bytes);
            write.close();
            return  true;
        }
            else return false;
    }

    @Override
    public Boolean OverwriteMultiPartFile(MultipartFile multipartFile, String path) throws IOException {
        if(!multipartFile.isEmpty()){
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            FileOutputStream write = new FileOutputStream(path);
//        File save = new File("./src/main/resources/static/images/Users"+"/"+name3);
            byte[] bytes = multipartFile.getBytes();

            write.write(bytes);
            write.close();
            return  true;
        }
        else return false;
    }
}
