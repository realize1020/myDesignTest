package com.example.design.leetcode.堆;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class MaxHeap3 {
    public static class MaxHead<T>{
        private ArrayList<T> heap;
        private HashMap<T, Integer> indexMap;
        private int heapSize;
        private Comparator<? super T> comparator;

        public MaxHead(Comparator<? super T> comparator) {
            this.comparator = comparator;
            heap=new ArrayList<>();
            indexMap=new HashMap<>();
            heapSize=0;
        }

        public boolean isEmpty() {
            return heapSize == 0;
        }

        public int size() {
            return heapSize;
        }

        public boolean contains(T key) {
            return indexMap.containsKey(key);
        }

        public void push(T value){
            heap.add(value);
            indexMap.put(value,heapSize);
            heapInsert(heapSize++);
        }

        /**
         * 上浮操作
         * @param index
         */
        private void heapInsert(int index) {
            while(comparator.compare(heap.get(index),heap.get((index-1)/2)) >0 ){
                swap(index,(index-1)/2);
                index=(index-1)/2;
            }
        }

        private void swap(int index, int i) {
            T t1 = heap.get(index);
            T t2 = heap.get(i);
            heap.set(i,t1);
            heap.set(index,t2);
            indexMap.put(t1,i);
            indexMap.put(t2,index);
        }

        public T pop() {
            T t = heap.get(0);
            //直接把最后一个元素拿来填补根节点,堆大小减一
            int end = heapSize - 1;
            swap(0, end);
            heap.remove(end);
            indexMap.remove(t);
            heapify(0, --heapSize);
            return t;
        }

        /**
         * 下沉操作
         * @param index
         * @param heapSize
         */
        private void heapify(int index, int heapSize) {

            while(index<heapSize){
                int left = index * 2+1;
                int right = index * 2+2;
                T largest = null;
                int largetIndex = -1;
                if(left<heapSize&&right<heapSize){
                    if(comparator.compare(heap.get(left),heap.get(right)) > 0){
                        largest=heap.get(left);
                        largetIndex=left;
                    }else{
                        largest=heap.get(right);
                        largetIndex=right;

                    }

                    if(comparator.compare(heap.get(index),largest)<0){
                        swap(index,largetIndex);
                    }
                }else if(left<heapSize){
                    if(comparator.compare(heap.get(index),heap.get(left))<0) {
                        swap(index,left);
                        largetIndex=left;
                    }

                }
                if(largetIndex==-1){
                    break;
                }else{
                    index=largetIndex;
                }
            }

        }
        /**
         * 优化下沉操作
         * @param index 要调整的节点索引
         * @param heapSize 堆的有效大小
         */
        private void heapify2(int index, int heapSize) {
            while (true) {
                int left = index * 2 + 1;
                int right = index * 2 + 2;
                int largest = index;

                if (left < heapSize && comparator.compare(heap.get(left), heap.get(largest)) > 0) {
                    largest = left;
                }
                if (right < heapSize && comparator.compare(heap.get(right), heap.get(largest)) > 0) {
                    largest = right;
                }

                if (largest != index) {
                    swap(index, largest);
                    index = largest;
                } else {
                    break;
                }
            }
        }


        private void heapify3(int index, int heapSize) {
            int left = index * 2 + 1;
            while (left < heapSize) {
                int largest = left + 1 < heapSize && (comparator.compare(heap.get(left + 1), heap.get(left)) < 0)
                        ? left + 1
                        : left;
                largest = comparator.compare(heap.get(largest), heap.get(index)) < 0 ? largest : index;
                if (largest == index) {
                    break;
                }
                swap(largest, index);
                index = largest;
                left = index * 2 + 1;
            }
        }

        public void resign(T value) {
            int valueIndex = indexMap.get(value);
            heapInsert(valueIndex);
            heapify(valueIndex, heapSize);
        }

        public static class Student {
            public int classNo;
            public int age;
            public int id;

            public Student(int c, int a, int i) {
                classNo = c;
                age = a;
                id = i;
            }

        }

        public static class StudentComparator implements Comparator<Student> {

            @Override
            public int compare(Student o1, Student o2) {
                return o1.age - o2.age;
            }

        }
        public static void main(String[] args) {
            MaxHead<Integer> heap =new MaxHead<>(Integer::compareTo);
            heap.push(1);
            heap.push(2);
            heap.push(3);
            heap.push(4);
            heap.push(5);


            System.out.println(heap.pop());
            System.out.println(heap.pop());
            System.out.println(heap.pop());
            System.out.println(heap.pop());
            System.out.println(heap.pop());

            StudentComparator studentComparator =new StudentComparator();
            Student s1=new Student(1,1,1);
            Student s2=new Student(2,2,2);
            Student s3=new Student(3,3,3);
            Student s4=new Student(4,4,4);

            MaxHead<Student> heap2 =new MaxHead<>(studentComparator);
            heap2.push(s1);
            heap2.push(s2);
            heap2.push(s3);
            heap2.push(s4);

            System.out.println("**********************************************");

            s2.age=5;
            heap2.resign(s2);
            System.out.println(heap2.pop().age);
            System.out.println(heap2.pop().age);
            System.out.println(heap2.pop().age);
            System.out.println(heap2.pop().age);
        }
    }
}
