package com.datazipper.Controller;


import com.datazipper.Payload.PathDto;
import com.datazipper.Service.fileDecryption;
import com.datazipper.Service.fileEncryption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<String> inputFile(@RequestBody PathDto pathDto) throws IOException {


        //project folder path
        String destFolder= System.getProperty("user.dir");
        destFolder = destFolder + File.separator + "compressedFile";
        try{
            String pathOfFile = pathDto.getFilePath();
            FileInputStream inStream = new FileInputStream(pathOfFile);
            byte[] b = new byte[inStream.available()];
            inStream.read(b);
            byte[] huffmanBytes = fileEncryptionService.createZip(b);

            OutputStream outStream = new FileOutputStream(destFolder);
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
            objectOutStream.writeObject(huffmanBytes);
            objectOutStream.writeObject(huffmap);
            inStream.close();
            objectOutStream.close();
            outStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>("File encypted Successfully at "+ destFolder, HttpStatus.OK);

    }
    @PostMapping("/DecryptFile")
    public ResponseEntity<String> DecryptFile(@RequestBody PathDto pathDto) throws IOException {


        String destFolder= System.getProperty("user.dir");
        destFolder = destFolder + File.separator + "decryptedFile.txt";
        String srcFolder = pathDto.getFilePath();
        try{
            FileInputStream inStream = new FileInputStream(srcFolder);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);
            byte[] huffmanBytes = (byte[]) objectInStream.readObject();
            Map<Byte, String> huffmanCodes =
                    (Map<Byte, String>) objectInStream.readObject();


            byte[] bytes = fileDecryptionService.decomp(huffmanCodes, huffmanBytes);
            OutputStream outStream = new FileOutputStream(destFolder);
            outStream.write(bytes);
            inStream.close();
            objectInStream.close();
            outStream.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("File decrypted Successfully at "+ destFolder, HttpStatus.OK);
    }

}

