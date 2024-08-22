package com.datazipper.Service.Impl;

import com.datazipper.Service.fileEncryption;

import java.util.HashMap;
import java.util.Map;

public class fileEncryptionImplementation implements fileEncryption {

    @Override
    public Map<Character,Integer> mappingChars(String data){
        Map<Character,Integer> frequencyOfCharacter = new HashMap<>();

        for(char character:data.toCharArray()){
            frequencyOfCharacter.put(character,frequencyOfCharacter.getOrDefault(character,0)+1);
        }

        return frequencyOfCharacter;
    }
}
