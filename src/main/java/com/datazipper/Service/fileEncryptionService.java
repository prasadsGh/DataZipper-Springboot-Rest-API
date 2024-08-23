package com.datazipper.Service;

import com.datazipper.Entity.encryptionReturnEntity;
import com.datazipper.Entity.treeNode;

import java.util.List;
import java.util.Map;

public interface fileEncryptionService {
    Map<Character,Integer> mappingChars(String data);
    List<treeNode> encryptedFileCreation(Map<Character,Integer> map);
}