package com.example.design.leetcode.递归迭代.牛吃草问题;
// 草一共有n的重量，两只牛轮流吃草，A牛先吃，B牛后吃
// 每只牛在自己的回合，吃草的重量必须是4的幂，1、4、16、64....
// 谁在自己的回合正好把草吃完谁赢，根据输入的n，返回谁赢
//假设牛和羊都绝顶聪明，都想赢，都会做出理性的决定
public class EatGrass {

    public static String win(int n){
       String cow =  f("A",n,0);
       return cow;
    }

    /**
     * 这种逻辑有问题，因为每一次递归的时候，不一定都是4的幂，所以这个逻辑有问题
     * @param cow
     * @param n
     * @param count
     * @return
     */
    public static String f(String cow,int n,int count){
        if(n==1){
            return cow;
        }
        if(n < 4){
            return "null";
        }
        String currentCow = "null";
        count++;
        if(cow.equals("A")){
            currentCow = f("B",n-4*count,count);
        }else{
            currentCow =f("A",n-4*count,count);
        }
        return currentCow.equals("null")?"null":currentCow;
    }

//    public static void main(String[] args) {
//        System.out.println(win(16));
//    }
// "A"  "B"
public static String win1(int n) {
    return f(n, "A");
}

    // rest : 还剩多少草
    // cur  : 当前选手的名字
    // 返回  : 还剩rest份草，当前选手是cur，按照题目说的，返回最终谁赢
    public static String f(int rest, String cur) {
        String enemy = cur.equals("A") ? "B" : "A";
        if (rest < 5) {
            return (rest == 0 || rest == 2) ? enemy : cur;
        }
        // rest >= 5
        // rest == 100
        // cur :
        // 1) 1 ->99,enemy ....
        // 2) 4 ->96,enemy ....
        // 3) 16 -> 84,enemy ....
        // 4) 64 -> 36,enemy ...
        // 没有cur赢的分支，enemy赢
        //递归判断逻辑：
        //对于每种可能的取草数量，递归判断对手在剩余草量下的结果
        //如果存在任何一种选择能让当前玩家最终获胜，就返回当前玩家胜利
        //只有当所有选择都导致对手获胜时，才返回对手胜利
        int pick = 1;
        while (pick <= rest) {// 从4^0=1开始
            if (f(rest - pick, enemy).equals(cur)) {// 遍历所有不超过剩余草量的4的幂次
                return cur;// 找到让当前玩家获胜的策略
            }
            pick *= 4;
        }
        return enemy; // 所有选择都对对手有利时，对手获胜
    }

    public static String win2(int n) {
        if (n % 5 == 0 || n % 5 == 2) {
            return "B";
        } else {
            return "A";
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 50; i++) {
            System.out.println(i + " : " + win1(i));
        }
    }
}
