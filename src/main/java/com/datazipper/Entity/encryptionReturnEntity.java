package com.datazipper.Entity;

import jakarta.persistence.Entity;

import java.io.File;
import java.util.List;



public class encryptionReturnEntity {
    File file;
    List<treeNode> decryptionKey;

    public encryptionReturnEntity( List<treeNode> decryptionKey,File file) {
        this.file = file;
        this.decryptionKey = decryptionKey;
    }
}
