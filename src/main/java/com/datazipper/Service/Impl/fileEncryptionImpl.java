package com.datazipper.Service.Impl;

import com.datazipper.Entity.ByteNode;
import com.datazipper.Service.fileEncryption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.*;

@Service
public class fileEncryptionImpl implements fileEncryption {

    private static StringBuilder sb = new StringBuilder();
     public static Map<Byte, String> huffmap = new HashMap<>();

    public byte[] createZip(byte[] bytes) throws IOException {
        PriorityQueue<ByteNode> minHeap = new PriorityQueue<>();

        // putting all the nodes in to minHeap

        Map<Byte,Integer> map = new HashMap<>();
        for(byte b: bytes){
            Integer value = map.get(b);
            if(value==null){
                map.put(b,1);
            }
            else{
                map.put(b,value+1);
            }
        }

        for(Map.Entry<Byte,Integer> entry: map.entrySet()){
            minHeap.add(new ByteNode(entry.getKey(), entry.getValue()));
        }

        //--------------------------------------------
        ByteNode root = createHuffmanTree(minHeap);
        Map<Byte, String> huffmanCodes = getHuffCodes(root);

        byte[] huffmanCodeBytes = zipBytesWithCodes(bytes, huffmanCodes);
        //-----------------------------------------------------------------------
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);

        objectOutStream.writeObject(huffmanCodeBytes);
        objectOutStream.writeObject(huffmap);



        // Get the serialized data as a byte array
        byte[] serializedData = outStream.toByteArray();

        // Set headers for the response

        objectOutStream.close();
        outStream.close();
        // Name of the file to download


        //-----------------------------------------------------------------------
        return serializedData;
    }

    public ByteNode createHuffmanTree(PriorityQueue<ByteNode> minHeap){
        while(minHeap.size()>1){
            ByteNode lowerByteNode = minHeap.poll();

            ByteNode secondLowerByteNode = minHeap.poll();
            ByteNode parentByteNode = new ByteNode((byte)'\0',lowerByteNode.frequency + secondLowerByteNode.frequency);
            minHeap.add(parentByteNode);
        }

        ByteNode rootNode = minHeap.peek();
        return rootNode;
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
    private static byte[] zipBytesWithCodes(byte[] byteArray, Map<Byte, String> huffmanCodes) throws IOException {
        StringBuilder encodedData = new StringBuilder();

        for (byte b : byteArray) {
            encodedData.append(huffmanCodes.get(b));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        while (encodedData.length() % 8 != 0) {
//            encodedData.append("0");  // Padding with zeros
//        }
        int length = ((encodedData.length())/8);
        byte[] huffCodeBytes = new byte[length];
        int idx=0;
        for (int i = 0; i < encodedData.length(); i += 8) {
            String byteString = encodedData.substring(i, Math.min(i + 8, encodedData.length()));
            if(byteString.contains("null")){
                continue;
            }

            huffCodeBytes[idx]= (byte) Integer.parseInt(byteString, 2);
            idx++;

        }

        return huffCodeBytes;
    }
}
