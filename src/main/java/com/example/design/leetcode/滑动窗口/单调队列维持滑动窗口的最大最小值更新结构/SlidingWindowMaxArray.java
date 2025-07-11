package com.example.design.leetcode.滑动窗口.单调队列维持滑动窗口的最大最小值更新结构;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 窗口内最大值或最小值更新结构的实现
 * 假设一个固定大小为W的窗口，依次划过arr，
 * 返回每一次滑出状况的最大值
 * 例如，arr = [4,3,5,4,3,3,6,7], W = 3
 * 返回：[5,5,5,4,6,7]
 */
public class SlidingWindowMaxArray {

    /**
     * 时间复杂度：O(n*w)n是数组长度，w是窗口大小，因为对于每个窗口，都需要遍历窗口内的所有元素来找出最大值。
     * 空间复杂度:O(n-w+1)
     * @param arr
     * @param w
     * @return
     */
    public static int[] getMaxWindow(int[] arr, int w){
        if(arr.length<w){
            return null;
        }
        int[] result=new int[arr.length-w+1];
        for(int i=0;i<=arr.length-w;i++){
            int sum = 0;
            for(int j=i;j<arr.length;j++){
                sum = Math.max(arr[j],sum);
                if(j==i+w-1){
                    result[j-w+1]=sum;
                    break;
                }

            }
        }
        return result;
    }



    /**
     * 简单改进
     * @param arr
     * @param w
     * @return
     */
    public static int[] getMaxWindow2(int[] arr, int w){
        // 如果数组长度小于窗口大小，返回 null
        if (arr.length < w) {
            return null;
        }
        // 结果数组的长度为 arr.length - w + 1
        int[] result = new int[arr.length - w + 1];
        // 外层循环遍历数组，直到窗口无法完全包含在数组中
        for (int i = 0; i <= arr.length - w; i++) {
            int max = arr[i];
            // 内层循环遍历当前窗口内的元素，找出最大值
            for (int j = i + 1; j < i + w; j++) {
                max = Math.max(arr[j], max);
            }
            // 将当前窗口的最大值存入结果数组
            result[i] = max;
        }
        return result;
    }

    /**
     * 时间复杂度：O(n)n是数组长度，每个元素进队和出队一次
     * 空间复杂度:O(w)
     * @param arr
     * @param w
     * @return
     */
    public static int[] getMaxWindow3(int[] arr, int w){

        if(arr.length<w){
            return null;
        }

        LinkedList<Integer> qmax = new LinkedList<Integer>();
        int[] result=new int[arr.length - w + 1];
        for(int i=0;i<arr.length;i++){
            while(!qmax.isEmpty() && arr[qmax.peekLast()]<arr[i]){
                qmax.pollLast();
            }
            qmax.addLast(i);
            if(qmax.peekFirst()==i-w){
                qmax.pollFirst();
            }
            if(i-w+1>=0){//窗口里有w个值了
                result[i-w+1] = arr[qmax.peekFirst()];
            }
        }

        return result;
    }


    /**
     * 左程云：滑动窗口内的最大最小值更新结构(单调队列)
     * @param arr
     * @param w
     * @return
     */
    public static int[] getMaxWindow4(int[] arr, int w) {
        // 输入校验：如果数组为空、窗口大小小于 1 或者数组长度小于窗口大小，直接返回 null
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        // 用于存储窗口最大值的更新结构，存储的是数组元素的下标
        LinkedList<Integer> qmax = new LinkedList<Integer>();
        // 结果数组，长度为 arr.length - w + 1，即窗口的数量
        int[] res = new int[arr.length - w + 1];
        // 结果数组的索引
        int index = 0;
        // 遍历数组，R 表示窗口的右边界
        for (int R = 0; R < arr.length; R++) {
            // 当队列不为空且队列尾部元素对应的值小于等于当前元素时，移除队列尾部元素
            while (!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[R]) {
                qmax.pollLast();
            }
            // 将当前元素的下标加入队列尾部
            qmax.addLast(R);
            // 如果队列头部元素的下标等于 R - w，说明该元素已经不在当前窗口内，移除队列头部元素
            if (qmax.peekFirst() == R - w) {
                qmax.pollFirst();
            }
            // 当窗口的右边界大于等于 w - 1 时，说明窗口已经形成，记录当前窗口的最大值
            if (R >= w - 1) {
                res[index++] = arr[qmax.peekFirst()];
            }
        }
        // 返回结果数组
        return res;
    }

    public static void main(String[] args) {
        int [] arr = {4,3,5,4,3,3,6,7};
        int[] maxWindow = getMaxWindow3(arr, 3);
        System.out.println(Arrays.toString(maxWindow));

    }

}
