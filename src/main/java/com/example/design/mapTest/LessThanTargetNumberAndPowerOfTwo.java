package com.example.design.mapTest;

public class LessThanTargetNumberAndPowerOfTwo {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    public static void main(String[] args) {
//        int x=5;
//        int index=Integer.SIZE-Integer.numberOfLeadingZeros(x-1);
//        System.out.println(index);
//        int nextPowerOfTwo = 1 <<index;
//        System.out.println(nextPowerOfTwo);
//        int prePowerOfTwo=nextPowerOfTwo;
//        while(x<prePowerOfTwo){
//            prePowerOfTwo/=2;
//        }
//        System.out.println(prePowerOfTwo);

//        long start = System.currentTimeMillis();
//        for(int i=2;i<1000000;i++){
//            //LeadingZeros(i);
//            //calByHashMap(i);
//            System.out.println(Integer.highestOneBit(i));
//        }
        //System.out.println((System.currentTimeMillis()-start)+"ms");

        //System.out.println(Integer.numberOfLeadingZeros(8));

        System.out.println(Integer.highestOneBit(17));

        System.out.println(tableSizeFor(17));


    }

    private static void LeadingZeros(int x){
        int nextPowerOfTwo = 1 <<Integer.SIZE-Integer.numberOfLeadingZeros(x-1);
        System.out.println(nextPowerOfTwo);
    }


    private static void calByHashMap(int x){
        int nextPowerOfTwo =tableSizeFor(x);
        System.out.println(nextPowerOfTwo);
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

}
