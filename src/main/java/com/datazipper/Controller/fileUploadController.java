package com.datazipper.Controller;


import com.datazipper.Service.fileDecryption;
import com.datazipper.Service.fileEncryption;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/file")
public class fileUploadController {
    private final fileEncryption fileEncryptionService;
    private final fileDecryption fileDecryptionService;

    public fileUploadController(fileEncryption fileEncryptionService,
                               fileDecryption fileDecryptionService) {
        this.fileEncryptionService = fileEncryptionService;
        this.fileDecryptionService = fileDecryptionService;
    }

    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        byte[] b= new byte[inputStream.available()];
        inputStream.read(b);
        byte[] huffmanBytes = fileEncryptionService.createZip(b);
        File newFile = new File("output.txt");
        OutputStream outStream = new FileOutputStream(newFile);
        ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
        objectOutStream.writeObject(huffmanBytes);
        objectOutStream.writeObject(huffmap);
        inputStream.close();
        objectOutStream.close();
        outStream.close();


        return new ResponseEntity<>(newFile,HttpStatus.OK);


    }
    @PostMapping("/download")
    public ResponseEntity<File> downloadFile(@RequestParam("file")MultipartFile file) throws IOException, ClassNotFoundException {
        InputStream inputStream = file.getInputStream();
        byte[] b= new byte[inputStream.available()];
        ObjectInputStream objectInStream = new ObjectInputStream(inputStream);
        byte[] huffmanBytes = (byte[]) objectInStream.readObject();
        Map<Byte, String> huffmanCodes =
                (Map<Byte, String>) objectInStream.readObject();

        byte[] bytes = fileDecryptionService.decomp(huffmanCodes, huffmanBytes);
        File newFile = new File("decrypted.txt");
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(bytes);
            System.out.println("File written successfully using FileOutputStream");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>(newFile,HttpStatus.OK);


    }

}
