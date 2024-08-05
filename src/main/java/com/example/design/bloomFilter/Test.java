package com.example.design.bloomFilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;


public class Test {
    public static void main(String[] args) {
        //hutoolBloomFilter();
        guavaBloomFilter();


        //System.out.println(bloomFilter.mightContain(1)); // 输出 true
        //System.out.println(bloomFilter.mightContain(4)); // 输出可能是 false，但有可能误报为 true

        // 注意：Guava的BloomFilter没有直接提供清除所有元素的方法，通常创建后就不再改变其大小和内容

    }

    private static void guavaBloomFilter() {
        // 预估元素数量
        long expectedInsertions = 10000L;
        // 期望的误报率
        double fpp = 0.01;

        // 创建布隆过滤器实例
        BloomFilter<Integer> bloomFilter = BloomFilter.create(
                Funnels.integerFunnel(),
                expectedInsertions,
                fpp
        );

        // 添加元素
        for(int i=0; i<10000;i++){
            bloomFilter.put(i);
        }


        int j=0;
        // 检查元素是否存在
        for(int i=0; i<10500;i++){
            boolean result=bloomFilter.mightContain(i);
            if(!result){
                j++;
            }
        }
        System.out.println("误判数："+j);
    }

    private static void hutoolBloomFilter() {
        BitMapBloomFilter filter = BloomFilterUtil.createBitMap(5000);
        System.out.println("布隆过滤器添加数据中......");
        for(int i=0;i<5000;i++){
            filter.add(String.valueOf(i));
        }

        int j=0;

        System.out.println("布隆过滤器添加数据完毕");
        for(int i=0;i<5000;i++){
            boolean contains = filter.contains(String.valueOf(i));
            if(!contains){
                j++;
            }
        }

        System.out.println("误判数："+j);

    }
}
