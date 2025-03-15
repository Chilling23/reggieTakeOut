package com.hwq.reggie.controller;

import com.hwq.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * File Upload and Download Controller
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * File Upload
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        // The file is a temporary file and needs to be saved to a specified location.
        // Otherwise, it will be deleted after the request is completed.
        log.info(file.toString());

        // Original file name
        String originalFilename = file.getOriginalFilename(); // e.g., abc.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // e.g., .jpg

        // Use UUID to generate a new file name to prevent overwriting due to duplicate names
        String fileName = UUID.randomUUID().toString() + suffix; // e.g., dfsdfdfd.jpg

        // Create a directory object
        File dir = new File(basePath);
        // Check if the directory exists
        if(!dir.exists()){
            // If the directory does not exist, create it
            dir.mkdirs();
        }

        try {
            // Save the temporary file to the specified location
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    /**
     * File Download
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            // Input stream to read the file content
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // Output stream to write the file back to the browser
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            // Close resources
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}