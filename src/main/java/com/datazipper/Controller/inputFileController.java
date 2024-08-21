package com.datazipper.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class inputFileController {

    @PostMapping("/inputFileUpload")
    public ResponseEntity<String> inputFile(@RequestParam("file") MultipartFile file) throws IOException {

        if(file.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please dont upload blank file");
        }
        try{
            String content =    new String(file.getBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.ok("File uploaded + content --> "+content);
        }
        catch(IOException exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while reading the file");

        }
    }
}
