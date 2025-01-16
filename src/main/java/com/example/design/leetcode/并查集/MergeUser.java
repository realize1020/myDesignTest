package com.example.design.leetcode.并查集;

import cn.hutool.core.lang.hash.Hash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MergeUser {
    public static class UnionCollection<V>{

        HashMap<V,V> parentMap =new HashMap<>();
        HashMap<V,Integer> sizeMap =new HashMap<>();

        public UnionCollection(Collection<V> collection){
            for(V v : collection){
                parentMap.put(v,v);//初始的父节点就是自己
                sizeMap.put(v,1);//初始都是1个
            }

        }

        public boolean findSameCollection(V v1,V v2){
            return findFather(v1)==findFather(v2);
        }

        private V findFather(V v) {
            while(v!=parentMap.get(v)){
               V parent =  parentMap.get(v);
               v=findFather(parent);
            }
            return v;
        }

        public void union(V a ,V b){
            V aNode = findFather(a);
            V bNode =findFather(b);
            Integer aSize = sizeMap.get(aNode);
            Integer bSize = sizeMap.get(bNode);
            V large = aSize>=bSize ? aNode:bNode;
            V low = large==aNode ? bNode:bNode;
            parentMap.put(low,large);
            sizeMap.put(large,aSize+bSize);
            sizeMap.remove(low);
        }
    }

    public static class Student{
        int chinese;//中文分数
        int english;
        String name;

        public Student(int chinese, int english, String name) {
            this.chinese = chinese;
            this.english = english;
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Student student1 =new Student(90,80,"mike");
        Student student2 =new Student(90,30,"lili");
        Student student3 =new Student(10,90,"kobe");

        Student execellent =new Student(90,90,"execellent");
        ArrayList<Student> students =new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(execellent);
        //只要两门分数中有一门分数90分以上就是优秀，就把他们合并到优秀的集合中
        UnionCollection<Student> unionCollection =new UnionCollection<>(students);

        for(Student student: students){
            if(student.chinese>=90||student.english>=90){
                unionCollection.union(execellent,student);
            }
        }

        System.out.println(unionCollection.findSameCollection(student1,student3));
        System.out.println("优秀的学生有几个："+unionCollection.sizeMap.size());
    }
}
