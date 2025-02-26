package com.example.design.test;

import com.example.design.Aggregation.Circle;
import com.example.design.内部类.OuterClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Test {
    /**
     * 2.找不同
     * 给定两个字符串 s 和 t ，它们只包含小写字母。
     * 字符串 t 由字符串 s 随机重排，然后在随机位置添加一个字母。
     * 请找出在 t 中被添加的字母。
     *
     * 示例 1：
     * 输入：s = "abcd", t = "abcde"
     * 输出："e"
     * 解释：'e' 是那个被添加的字母。
     * 示例 2：
     * 输入：s = "", t = "y"
     * 输出："y"
     *
     * 提示：
     * 0 <= s.length <= 1000
     * t.length == s.length + 1
     * s 和 t 只包含小写字母
     * @param args
     */
    private  String time;

    public static void main(String[] args) {
//        int [] arr=new int[]{0,1,2,3,4,5,6,7,8,9};
//        bubbleSort(arr);
//        for(int i=0;i<arr.length;i++){
//            System.out.println(arr[i]);
//        }




//        String s = "abcd";
//        String t = "abcde";


//        String s = "";
//        String t =


        String s = "abc";
        String t = "abcc";

//        String s = "aabb";
//        String t = "aabbb";

//        Character one = findOne(s, t);
//        System.out.println(one);

        //findOne2(s, t);
        List<Circle> circles =new ArrayList<>();
        circles.add(new Circle());
        circles.add(new Circle());
        circles.add(new Circle());
        circles.add(new Circle());
        for(Circle circle: circles){
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0" + (circles.indexOf(circle) + 1));
        }

    }

    public void getSub(){
        String value=Inner.value;
    }


    public static Character findOne(String s,String t){

        char[] sChar = s.toCharArray();
        char[] tChar = t.toCharArray();
        List<Character> sList=new ArrayList<Character>();
        List<Character> tList=new ArrayList<Character>();


        for(int i=0;i<sChar.length;i++){
            sList.add(sChar[i]);
        }
        for(int i=0;i<tChar.length;i++){
            tList.add(tChar[i]);
        }

        List<Character> diffList = tList.stream().filter(indexOne -> sList.stream().noneMatch(indexTwo -> indexOne.equals(indexTwo))).collect(Collectors.toList());



        if(diffList!=null&&diffList.size()==1){
            return diffList.get(0);
        }else {

            int flag=0;
            for(int i=0;i<tList.size();i++){
                for(int j=i+1;j<tList.size();j++){
                    if(tList.get(i)==tList.get(j)){
                        flag=j;
                        break;
                    }
                }
            }
            return tList.get(flag);

        }





    }



    public static void findOne2(String s,String t){
        HashMap<Integer,Character> map =new HashMap<Integer,Character>();
        char[] sChar = s.toCharArray();
        char[] tChar = t.toCharArray();
        int [] index=new int[]{0};

        for(int i=0;i<tChar.length;i++){
            map.put(i,tChar[i]);
        }

        for(int i=0;i<sChar.length;i++){
            if(!map.containsValue(sChar[i])){
                System.out.println(tChar[i]);

            }else{
                index[i]=1;
            }
        }
        if(Arrays.stream(index).allMatch(i->i==1)){
            System.out.println(tChar[tChar.length-1]);
        }
        if(Arrays.stream(index).anyMatch(i->i==1)){
            for(int i=0;i<index.length;i++){
                if(index[i]==0){
                    System.out.println(tChar[i]);
                }
            }

        }



    }





    public static void bubbleSort(int [] arr){
        for(int i=0;i<arr.length;i++){
            for(int j=i+1;j<arr.length;j++){
                if(arr[i]<arr[j]){
                    int temp =0;
                    temp=arr[i];
                    arr[i]=arr[j];
                    arr[j]=temp;
                }
            }
        }
    }


  class Inner{
        private  final static String value="1";



        String  getValue(){
            if(time.equals("111")){

            }
            return value;
        }
    }

}
