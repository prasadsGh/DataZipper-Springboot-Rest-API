package com.datazipper.Service.fileEncryptionServiceImpl;

import com.datazipper.Entity.encryptionReturnEntity;
import com.datazipper.Entity.minHeapNode;
import com.datazipper.Entity.treeNode;
import com.datazipper.Service.fileEncryptionService;
import org.springframework.stereotype.Service;

import java.io.File;
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
    public List<treeNode> encryptedFileCreation(Map<Character,Integer> map) {
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
            levelOrderKey.add(node);
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
        String fileName="output.txt";
        //File file = new File(fileName);
        encryptionReturnEntity entity = new encryptionReturnEntity(levelOrderKey);
        return levelOrderKey;
    }
}
