package com.datazipper.Entity;


public class minHeapNode {
    public int frequency;
    public Character character;
    public treeNode nodeValue;
    public minHeapNode(int frequency, Character character, treeNode nodeValue) {
        this.frequency = frequency;
        this.character = character;
        this.nodeValue=nodeValue;
    }
}
