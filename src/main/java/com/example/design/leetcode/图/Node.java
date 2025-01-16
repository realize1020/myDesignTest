package com.example.design.leetcode.图;

import java.util.ArrayList;

/**
 * 点
 */
public class Node {
    public int value;
    public int in; //入度
    public int out;//出度
    public ArrayList<Node> nexts;
    public ArrayList<Edge> edges;

    private int x, y;

    public Node(int value) {
        this.value = value;
        in = 0;
        out = 0;
        nexts = new ArrayList<>();
        edges = new ArrayList<>();

        x = 0;
        y = 0;
    }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }
}
