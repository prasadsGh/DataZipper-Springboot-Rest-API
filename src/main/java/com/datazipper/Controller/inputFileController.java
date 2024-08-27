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
    public ResponseEntity<byte[]> inputFile(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] tempResponse = null;
        if (file.isEmpty()) {
            return new ResponseEntity<>(tempResponse, HttpStatus.BAD_REQUEST);
        }

        try {

            InputStream inStream = file.getInputStream();
            int content;
            StringBuilder fileContent = new StringBuilder();
            while ((content = inStream.read()) != -1) {
                fileContent.append((char) content);
            }

            byte[] b = new byte[inStream.available()];
            inStream.read(b);


//            OutputStream outStream = new FileOutputStream(dst);
//            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
            byte[] huffmanBytes = fileEncryptionService.createZip(b);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  // For binary file
            headers.setContentDispositionFormData("attachment", "compressedFile.bin");
            // Return the file as a downloadable response

            inStream.close();
            return new ResponseEntity<>(huffmanBytes, headers, HttpStatus.OK);

            //return new ResponseEntity<>("File got uploaded successfully", HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(tempResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/DecryptFile")
    public ResponseEntity<byte[]> DecryptFile(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] tempResponse = null;
        if (file.isEmpty()) {
            return new ResponseEntity<>(tempResponse, HttpStatus.BAD_REQUEST);
        }

        try {

            InputStream inStream = file.getInputStream();
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);

            byte[] huffmanBytes = (byte[]) objectInStream.readObject();
            Map<Byte, String> huffmanCodes = (Map<Byte, String>) objectInStream.readObject();

            byte[] bytes = fileDecryptionService.decomp(huffmanCodes, huffmanBytes);
//            OutputStream outStream = new FileOutputStream(dst);
//            outStream.write(bytes);
//            inStream.close();
//            objectInStream.close();
//            outStream.close();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
            // Get the serialized data as a byte array
            byte[] serializedData = outStream.toByteArray();

            // Set headers for the response
            objectOutStream.close();
            outStream.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  // For binary file
            headers.setContentDispositionFormData("attachment", "compressedFile.bin");  // Name of the file to download

            // Return the file as a downloadable response
            return new ResponseEntity<>(serializedData, headers, HttpStatus.OK);



        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(tempResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

