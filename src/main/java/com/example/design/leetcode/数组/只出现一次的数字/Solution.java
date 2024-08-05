package com.example.design.leetcode.数组.只出现一次的数字;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * 给你一个 非空 整数数组 nums ，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 * 你必须设计并实现线性时间复杂度的算法来解决此问题，且该算法只使用常量额外空间。
 * 示例 1 ：
 * 输入：nums = [2,2,1]
 * 输出：1
 * 示例 2 ：
 *
 * 输入：nums = [4,1,2,1,2]
 * 输出：4
 * 示例 3 ：
 *
 * 输入：nums = [1]
 * 输出：1
 */
public class Solution {
    /**
     * 使用hashSet结构
     * @param nums
     * @return
     */
    public static int singleNumber(int[] nums) {
        HashSet<Integer> set =new HashSet<>();
        for(int i=0;i<nums.length;i++){
            if(set.contains(nums[i])){
                set.remove(nums[i]);
            }else{
                set.add(nums[i]);
            }
        }
        int num=0;
        Iterator<Integer> iterator = set.iterator();
        while(iterator.hasNext()){
            num = iterator.next();
        }
        return num;
    }

    /**
     * 使用hashMap
     * @param nums
     * @return
     */
    public static int singleNumber2(int[] nums) {
        HashMap<Integer,String> map =new HashMap<>();
        for(int i=0;i<nums.length;i++){
            if(map.containsKey(nums[i])){
                map.remove(nums[i]);
            }else{
                map.put(nums[i],nums[i]+"");
            }
        }
        int num=0;
        for(Map.Entry<Integer,String> m: map.entrySet()){
            num = m.getKey();
        }
        return num;

    }

    public static int singleNumber3(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
        }
        return single;
    }



    public static void main(String[] args) {
        int[] arr = {4,1,2,1,2};
        System.out.println(singleNumber3(arr));
    }
}
