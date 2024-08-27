package com.datazipper.Service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface fileDecryption {

    byte[] decomp(Map<Byte,String>huffmanCodes, byte[] huffmanBytes);
}
