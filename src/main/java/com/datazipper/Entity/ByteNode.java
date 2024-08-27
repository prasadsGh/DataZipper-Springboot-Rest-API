package com.datazipper.Entity;

public class ByteNode implements Comparable<ByteNode>{
    public Byte data;
    public int frequency;
    public ByteNode left;
    public ByteNode right;
    public ByteNode(Byte data, int weight)
    {
        this.data=data;
        this.frequency=weight;
    }
    public int compareTo(ByteNode o)
    {
        return this.frequency - o.frequency;
    }
}
