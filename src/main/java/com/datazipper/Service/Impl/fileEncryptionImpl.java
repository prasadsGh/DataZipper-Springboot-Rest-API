package com.datazipper.Service.Impl;

import com.datazipper.Entity.ByteNode;
import com.datazipper.Service.fileEncryption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import java.io.*;
import com.datazipper.Utils.MinPriorityQueue;

@Service
public class fileEncryptionImpl implements fileEncryption {

    private static StringBuilder sb = new StringBuilder();
    public static Map<Byte, String> huffmap = new HashMap<>();

    public byte[] createZip(byte[] bytes) throws IOException {
        MinPriorityQueue<ByteNode> nodes = getByteNodes(bytes);
        ByteNode root = createHuffmanTree(nodes);
        Map<Byte, String> huffmanCodes = getHuffCodes(root);
        byte[] huffmanCodeBytes = zipBytesWithCodes(bytes, huffmanCodes);
        return huffmanCodeBytes;
    }

    private static MinPriorityQueue<ByteNode> getByteNodes(byte[] bytes) {
        MinPriorityQueue<ByteNode> nodes = new MinPriorityQueue<ByteNode>();
        Map<Byte, Integer> tempMap = new HashMap<>();
        for (byte b : bytes) {
            Integer value = tempMap.get(b);
            if (value == null)
                tempMap.put(b, 1);
            else
                tempMap.put(b, value + 1);
        }
        for (Map.Entry<Byte, Integer> entry : tempMap.entrySet())
            nodes.add(new ByteNode(entry.getKey(), entry.getValue()));
        return nodes;
    }

    private static ByteNode createHuffmanTree(MinPriorityQueue<ByteNode> nodes) {
        while (nodes.len() > 1) {
            ByteNode left = nodes.poll();
            ByteNode right = nodes.poll();
            ByteNode parent = new ByteNode(null, left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            nodes.add(parent);
        }
        return nodes.poll();
    }

    private static Map<Byte, String> getHuffCodes(ByteNode root) {
        if (root == null) return null;
        getHuffCodes(root.left, "0", sb);
        getHuffCodes(root.right, "1", sb);
        return huffmap;
    }

    private static void getHuffCodes(ByteNode node, String code, StringBuilder sb1) {
        StringBuilder sb2 = new StringBuilder(sb1);
        sb2.append(code);
        if (node != null) {
            if (node.data == null) {
                getHuffCodes(node.left, "0", sb2);
                getHuffCodes(node.right, "1", sb2);
            } else
                huffmap.put(node.data, sb2.toString());
        }
    }

    private static String convertbyteInBit(boolean flag, byte b) {
        int byte0 = b;
        if (flag) byte0 |= 256;
        String str0 = Integer.toBinaryString(byte0);
        if (flag || byte0 < 0)
            return str0.substring(str0.length() - 8);
        else return str0;
    }

    private static byte[] zipBytesWithCodes(byte[] bytes, Map<Byte, String> huffCodes) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte b : bytes)
            strBuilder.append(huffCodes.get(b));

        int length = (strBuilder.length() + 7) / 8;
        byte[] huffCodeBytes = new byte[length];
        int idx = 0;
        for (int i = 0; i < strBuilder.length(); i += 8) {
            String strByte;
            if (i + 8 > strBuilder.length())
                strByte = strBuilder.substring(i);
            else strByte = strBuilder.substring(i, i + 8);
            huffCodeBytes[idx] = (byte) Integer.parseInt(strByte, 2);
            idx++;
        }
        return huffCodeBytes;
    }

}


