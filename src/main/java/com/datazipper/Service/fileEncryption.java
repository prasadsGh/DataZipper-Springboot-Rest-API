package com.datazipper.Service;

import com.datazipper.Entity.ByteNode;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public interface fileEncryption {
     byte[] createZip(byte[] bytes) throws IOException;

}
