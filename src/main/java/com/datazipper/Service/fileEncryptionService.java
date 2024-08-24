package com.datazipper.Service;

import com.datazipper.Entity.encryptionReturnEntity;
import com.datazipper.Entity.treeNode;

import java.util.List;
import java.util.Map;

public interface fileEncryptionService {
    Map<Character,Integer> mappingChars(String data);
    encryptionReturnEntity encryptedFileCreation(Map<Character,Integer> map,String data);
    Map<Character,String> byteCodeCharacter(treeNode node,Map<Character,String> map, String byteCode);
}
