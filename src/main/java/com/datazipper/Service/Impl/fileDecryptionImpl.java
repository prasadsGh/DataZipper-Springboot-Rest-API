package com.datazipper.Service.Impl;

import com.datazipper.Service.fileDecryption;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class fileDecryptionImpl implements fileDecryption {


    @Override
    public byte[] decomp(Map<Byte, String> huffmanCodes, byte[] huffmanBytes) {
        StringBuilder sb1 = new StringBuilder();
        for (int i=0; i<huffmanBytes.length; i++) {
            byte b = huffmanBytes[i];
            boolean flag = (i == huffmanBytes.length - 1);
            sb1.append(convertbyteInBit(!flag, b));
        }
        Map<String, Byte> map = new HashMap<>();
        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        java.util.List<Byte> list = new java.util.ArrayList<>();
        for (int i = 0; i < sb1.length();) {
            int count = 1;
            boolean flag = true;
            Byte b = null;
            while (flag) {
                String key = sb1.substring(i, i + count);
                b = map.get(key);
                if (b == null) count++;
                else flag = false;
            }
            list.add(b);
            i += count;
        }
        byte b[] = new byte[list.size()];
        for (int i = 0; i < b.length; i++)
            b[i] = list.get(i);
        return b;
    }
    private static String convertbyteInBit(boolean flag, byte b) {
        int byte0 = b;
        if (flag) byte0 |= 256;
        String str0 = Integer.toBinaryString(byte0);
        if (flag || byte0 < 0)
            return str0.substring(str0.length() - 8);
        else return str0;
    }
}
