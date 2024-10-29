package com.example.design.leetcode.数组.买卖股票的最佳时机;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * 示例 1：
 * 输入：[7,1,5,3,6,4]
 * 输出：5
 * 解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
 *      注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
 * 示例 2：
 * 输入：prices = [7,6,4,3,1]
 * 输出：0
 * 解释：在这种情况下, 没有交易完成, 所以最大利润为 0。
 */
public class Solution {
    public static void main(String[] args) {
        int[] prices = {3,2,6,5,0,3};
        //int[] prices = {2,4,1};
        int i = maxProfit2(prices);
        System.out.println(i);
    }

    /**
     * 自己写，思路：迭代法、如果有利润的话，肯定是要判断后一天比前一天价格高的，然后比较 当前天和之前最小的天数之间的差 和之前利润的最大值重新作为利润，
     * 如果后一天价格比前一天价格低，那么把后一天价格作为起始的前一天价格
     * @param prices
     * @return
     */
    public static int maxProfit(int[] prices) {
        //int money = prices[0];
        int pre = prices[0];
        int profit=0;
        for(int i=1;i<prices.length;i++){
            if(prices[i]-pre>0){
                profit=Math.max(profit,prices[i]-pre);
                //money = Math.max(pre,money);
            }else{
                pre=prices[i];
                //profit=0;
            }
        }
        return profit;
    }

    /**
     * 自己写,思路：先找到最小值，然后找到它后面的最大值。因为最大利润其实与最小值和最大值的位置无关，只要是最小值后面的数比它大，或者说后面有最大值，那么就会有利润。
     * 这种思路不对。例子：[11,2,7,1,4]
     * @param prices
     * @return
     */
    public static int maxProfit2(int[] prices){
        List<Integer> collect = Arrays.stream(prices).boxed().collect(Collectors.toList());
        while(true){
            Integer min = collect.stream().min(Comparator.comparingInt(Integer::intValue)).get();
            Integer max =collect.stream().max(Comparator.comparingInt(Integer::intValue)).get();
            int i = collect.indexOf(min);
            int j = collect.indexOf(max);
            if(i!=collect.size()-1){
                List<Integer> subList = collect.subList(i+1,collect.size());
                Integer rangeMax =subList.stream().max(Comparator.comparingInt(Integer::intValue)).get();
                int range = rangeMax-min;
                if(j!=0){
                    List<Integer> subListTwo = collect.subList(0,j);
                    Integer rangeMin =subListTwo.stream().min(Comparator.comparingInt(Integer::intValue)).get();
                    int rangeTwo = max-rangeMin;

                    return rangeTwo>range?rangeTwo:range;
                }
                return range;

            }else if(i==0){
                return 0 ;
            }
            else{
                collect = collect.subList(0,i);
            }
        }


    }

}
