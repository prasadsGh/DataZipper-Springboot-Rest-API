package com.datazipper.Controller;


import com.datazipper.Service.fileDecryption;
import com.datazipper.Service.fileEncryption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;


import static com.datazipper.Service.Impl.fileEncryptionImpl.huffmap;


@RestController
@RequestMapping("/api")
public class inputFileController {

    private final fileEncryption fileEncryptionService;
    private final fileDecryption fileDecryptionService;

    public inputFileController(fileEncryption fileEncryptionService,
                               fileDecryption fileDecryptionService) {
        this.fileEncryptionService = fileEncryptionService;
        this.fileDecryptionService = fileDecryptionService;
    }

    @PostMapping("/inputFileUpload")
    public ResponseEntity<File> inputFile(@RequestParam("file") MultipartFile file) throws IOException {
        File tempFile = new File("temp.txt");
        if (file.isEmpty()) {
            System.out.println("inside file empty");
            return new ResponseEntity<>(tempFile, HttpStatus.BAD_REQUEST);
        }

        try {
            InputStream inStream = file.getInputStream();
            byte[] b = new byte[inStream.available()];
            inStream.read(b);
            byte[] huffmanBytes = fileEncryptionService.createZip(b);


            System.out.println("huffmanBytes -->"+huffmanBytes);
            File newFile = new File("output.txt");

            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                // Write the byte array to the file
                fos.write(huffmanBytes);
                System.out.println("Data written to file successfully.");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }


            inStream.close();
            return new ResponseEntity<>(newFile, HttpStatus.OK);

            //return new ResponseEntity<>("File got uploaded successfully", HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOexception");
            return new ResponseEntity<>(tempFile, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/DecryptFile")
    public ResponseEntity<File> DecryptFile(@RequestParam("file") MultipartFile file) throws IOException {
        File tempFile = new File("temp.txt");
        if (file.isEmpty()) {
            return new ResponseEntity<>(tempFile, HttpStatus.BAD_REQUEST);
        }

        try {

            InputStream inStream = file.getInputStream();
            ObjectInputStream objectInStream = new  ObjectInputStream(inStream);
            byte[] huffmanBytes = (byte[]) objectInStream.readObject();
            Map<Byte, String> huffmanCodes =
                    (Map<Byte, String>) objectInStream.readObject();
            File newFile = new File("decryptedFile.txt");
            byte[] bytes = fileDecryptionService.decomp(huffmanCodes, huffmanBytes);
            OutputStream outStream = new FileOutputStream(newFile);
            outStream.write(bytes);
            inStream.close();
            objectInStream.close();
            outStream.close();

            inStream.close();
            return new ResponseEntity<>(newFile, HttpStatus.OK);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(tempFile, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

