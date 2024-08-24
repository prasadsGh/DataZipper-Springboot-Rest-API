package com.datazipper.Service.fileEncryptionServiceImpl;

import com.datazipper.Entity.encryptionReturnEntity;
import com.datazipper.Entity.minHeapNode;
import com.datazipper.Entity.treeNode;
import com.datazipper.Service.fileEncryptionService;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

@Service
public class fileEncryptionImplementation implements fileEncryptionService {

    public Map<Character,Integer> mappingChars(String data){
        Map<Character,Integer> frequencyOfCharacter = new HashMap<>();

        for(char character:data.toCharArray()){
            frequencyOfCharacter.put(character,frequencyOfCharacter.getOrDefault(character,0)+1);
        }

        return frequencyOfCharacter;
    }

    @Override
    public encryptionReturnEntity encryptedFileCreation(Map<Character,Integer> map,String data) {

        PriorityQueue<minHeapNode> minHeap = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);
        for(Map.Entry<Character,Integer> entry: map.entrySet()){
               // System.out.println(entry.getValue()+" "+ entry.getKey());
            treeNode node = new treeNode();
            node.character= entry.getKey();
            node.frequency = entry.getValue();
            node.left=null;
            node.right=null;
            minHeap.add(new minHeapNode(entry.getValue(), entry.getKey(),node));
        }

        while(minHeap.size()!=1){
            minHeapNode nodeMininum = minHeap.peek();
            minHeap.poll();
            treeNode leftNode= nodeMininum.nodeValue;
            minHeapNode nodeSecondMinimum = minHeap.peek();
            minHeap.poll();
            treeNode rightNode = nodeSecondMinimum.nodeValue;
            int total = nodeMininum.frequency+ nodeSecondMinimum.frequency;

            treeNode parentNode = new treeNode();
            parentNode.frequency=total;
            parentNode.character='$';
            parentNode.left=leftNode;
            parentNode.right=rightNode;

            minHeapNode parentMinHeapNode = new minHeapNode(total,'$',parentNode);
            minHeap.add(parentMinHeapNode);
        }
        treeNode rootNode=minHeap.peek().nodeValue;

        List<treeNode> levelOrderKey = new ArrayList<>();
        Queue<treeNode> queue = new LinkedList<>() ;
        queue.add(rootNode);
        while(queue.size()!=0){
            treeNode node = queue.peek();
            //levelOrderKey.add(node);
            if(node==null){
                queue.poll();
            }
            else{
                treeNode leftNode = node.left;
                treeNode rightNode = node.right;
                queue.add(leftNode);
                queue.add(rightNode);
                queue.poll();
            }

        }
        levelOrderKey.add(rootNode);
        Map<Character,String>byteCodeMap= new HashMap<>();
        Map<Character,String>byteCodeMapFinal= new HashMap<>();
        String byteCodeString="";
        byteCodeMapFinal = byteCodeCharacter(rootNode,byteCodeMap,byteCodeString);
//        for (Map.Entry<Character, String> entry : byteCodeMapFinal.entrySet()) {
//            Character key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println("Key: " + key + ", Value: " + value);
//        }

         String encryptedText="";
         for(Character character:data.toCharArray()){
             String value = byteCodeMapFinal.get(character);
             encryptedText+=value;
         }

         // writing to file
        File file = new File("output.txt");

        try {
            // Create the file if it doesn't exist
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            // Write data to the file using FileWriter and BufferedWriter
            FileWriter fw = new FileWriter(file); // FileWriter is used to write into the file
            BufferedWriter bw = new BufferedWriter(fw); // BufferedWriter for efficient writing

            bw.write(encryptedText);

            // Close the BufferedWriter
            bw.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        encryptionReturnEntity entity = new encryptionReturnEntity(levelOrderKey,file);

        return entity;
    }

    @Override
    public Map<Character,String> byteCodeCharacter(treeNode node,Map<Character,String>map,String byteCode) {
        if(node.character!='$'){
            System.out.println(node.character+" = "+byteCode);
            map.put(node.character,byteCode);
            byteCode="";
            return map;
        }


//        System.out.println(node.character+" = "+byteCode);
//        System.out.println(node.character+" = "+node.frequency);
//        System.out.println(node.character+" = left = "+node.left.character);
//        System.out.println(node.character+" = right = "+ node.right.character);
        if(node.left!=null){

            byteCodeCharacter(node.left,map, byteCode+'0');
        }
        if(node.right!=null){

            byteCodeCharacter(node.right,map,byteCode+'1');
        }
        System.out.println("inside the mapp");
        return map;
    }
}
