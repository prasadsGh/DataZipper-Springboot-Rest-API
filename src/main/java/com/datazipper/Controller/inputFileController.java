package com.datazipper.Controller;


import com.datazipper.Entity.treeNode;
import com.datazipper.Service.fileEncryptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.datazipper.Entity.encryptionReturnEntity;

@RestController
@RequestMapping("/api")
public class inputFileController {


    private fileEncryptionService encryption;

    public inputFileController(fileEncryptionService encryption) {
        this.encryption = encryption;
    }

    @PostMapping("/inputFileUpload")
    public ResponseEntity<encryptionReturnEntity> inputFile(@RequestParam("file") MultipartFile file) throws IOException {
        encryptionReturnEntity errorEntity = new encryptionReturnEntity(null,null);

        if (file.isEmpty()) {
            return new ResponseEntity<encryptionReturnEntity>(errorEntity, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            Map<Character, Integer> frequencyMap = new HashMap<>();
            frequencyMap = encryption.mappingChars(content);
            encryptionReturnEntity entity = encryption.encryptedFileCreation(frequencyMap,content);
            return new ResponseEntity<encryptionReturnEntity>(entity, HttpStatus.CREATED);
        } catch (IOException exception) {

            return new ResponseEntity<>(errorEntity, HttpStatus.BAD_REQUEST);

        }
    }
}
