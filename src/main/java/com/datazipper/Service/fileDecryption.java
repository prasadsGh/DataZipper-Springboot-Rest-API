package com.datazipper.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public interface fileDecryption {

          byte[] decomp(Map<Byte, String> huffmanCodes, byte[] huffmanBytes) throws IOException;

}
