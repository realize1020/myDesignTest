package com.example.design.leetcode.并查集;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyUnionSet<V> {

    public static class Node<V>{
        V value;
        public Node(V value){
            this.value=value;
        }
    }

    public HashMap<Node<V>,Node<V>> parents =new HashMap<>();
    public HashMap<V,Node<V>> nodes =new HashMap<>();
    public HashMap<Node<V>,Integer> sizeMap =new HashMap<>();

    public MyUnionSet(List<V> values){
        for(V value: values){
            Node<V> node =new Node<>(value);
            nodes.put(value,node);
            parents.put(node,node);
            sizeMap.put(node,1);
        }
    }

    public void union(V a,V b){
        if(!nodes.containsKey(a)||!nodes.containsKey(b)){
            return;
        }
        Node<V> nodeA = findFather(a);
        Node<V> nodeB = findFather(b);
        if(nodeA!=nodeB){
            int aSize = sizeMap.get(nodeA);
            int bSize = sizeMap.get(nodeB);
            if(aSize>=bSize){
                parents.put(nodeB,nodeA);// 将较小的集合连接到较大的集合
                sizeMap.put(nodeA,aSize+bSize);
                sizeMap.remove(nodeB);
            }else{
                parents.put(nodeA,nodeB);
                sizeMap.put(nodeB,aSize+bSize);
                sizeMap.remove(nodeA);
            }
        }
    }

    private Node<V> findFather(V a) {
        Node<V> node = nodes.get(a);
        while(parents.get(node)!=node){
            Node<V> parentNode = parents.get(node);
            parents.put(node,findFather(parentNode.value));
            node = parentNode;
        }
        return node;
    }


    public boolean isSameSet(V a, V b) {
        if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
            return false;
        }
        return findFather(a) == findFather(b);
    }

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5);
        MyUnionSet<Integer> uf = new MyUnionSet<>(values);

        System.out.println(uf.isSameSet(1,2));

        uf.union(1,2);
        uf.union(2,3);
        uf.union(3,4);

        System.out.println(uf.isSameSet(4,2));
    }
}
