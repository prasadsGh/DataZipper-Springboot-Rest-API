package com.datazipper.Entity;

import jakarta.persistence.Entity;

public class treeNode {
    public Character character;
    public Integer frequency;
    public treeNode left;
    public treeNode right;

    public treeNode(){
        this.character = null;
        this.right = null;
        this.left = null;
        this.frequency = null;
    }
    public treeNode(Character character, treeNode right, treeNode left, Integer frequency) {
        this.character = character;
        this.right = right;
        this.left = left;
        this.frequency = frequency;
    }
}
