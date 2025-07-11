package com.example.design.leetcode.二叉树.线段树;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Solution {
    public static class SegmentTree {
        // arr[]为原序列的信息，从1开始使用（0位置不用）
        // sum[]模拟线段树维护区间和
        // lazy[]为累加和懒惰标记（延迟标记）
        // change[]为更新操作的值
        // update[]为更新操作的懒惰标记
        private int MAXN;
        private int[] arr;
        private int[] sum;
        private int[] lazy;
        private int[] change;
        private boolean[] update;

        // 构造函数：初始化线段树
        public SegmentTree(int[] origin) {
            MAXN = origin.length + 1;
            arr = new int[MAXN]; // arr[0] 不用，从1开始使用
            for (int i = 1; i < MAXN; i++) {
                arr[i] = origin[i - 1]; // 将原始数组复制到arr[1~n]
            }
            // 线段树数组大小为4倍，确保足够存储所有节点
            sum = new int[MAXN << 2];   // 存储区间和
            lazy = new int[MAXN << 2];  // 存储未下传的累加标记
            change = new int[MAXN << 2]; // 存储未下传的更新值
            update = new boolean[MAXN << 2]; // 标记该节点是否有待执行的更新操作
        }

        // 将左右子节点的和合并到当前节点
        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1]; // rt<<1 是左子节点，rt<<1|1 是右子节点
        }

        // 将当前节点的懒标记下传到左右子节点
        // ln表示左子树元素个数，rn表示右子树元素个数
        private void pushDown(int rt, int ln, int rn) {
            // 优先处理更新操作（因为更新会覆盖之前的累加）
            if (update[rt]) {
                update[rt << 1] = true;
                update[rt << 1 | 1] = true;
                change[rt << 1] = change[rt];
                change[rt << 1 | 1] = change[rt];
                lazy[rt << 1] = 0; // 清空子节点的lazy标记（被更新覆盖）
                lazy[rt << 1 | 1] = 0;
                sum[rt << 1] = change[rt] * ln; // 更新左子节点的和
                sum[rt << 1 | 1] = change[rt] * rn; // 更新右子节点的和
                update[rt] = false; // 清除当前节点的更新标记
            }
            // 处理累加标记
            if (lazy[rt] != 0) {
                lazy[rt << 1] += lazy[rt]; // 传递累加标记到左子节点
                sum[rt << 1] += lazy[rt] * ln; // 更新左子节点的和
                lazy[rt << 1 | 1] += lazy[rt]; // 传递累加标记到右子节点
                sum[rt << 1 | 1] += lazy[rt] * rn; // 更新右子节点的和
                lazy[rt] = 0; // 清除当前节点的累加标记
            }
        }

        // 构建线段树：初始化sum数组
        // 在arr[l~r]范围上构建线段树，rt为当前节点在线段树数组中的下标
        public void build(int l, int r, int rt) {
            if (l == r) { // 叶子节点
                sum[rt] = arr[l];
                return;
            }
            int mid = (l + r) >> 1; // 二分中点
            build(l, mid, rt << 1); // 递归构建左子树
            build(mid + 1, r, rt << 1 | 1); // 递归构建右子树
            pushUp(rt); // 合并左右子树的和
        }

        // 区间更新：将L~R范围内所有值更新为C
        // l~r为当前节点管辖范围，rt为当前节点下标
        public void update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) { // 当前区间完全被覆盖
                update[rt] = true;
                change[rt] = C;
                sum[rt] = C * (r - l + 1); // 直接更新当前节点的和
                lazy[rt] = 0; // 清空累加标记（被更新覆盖）
                return;
            }
            // 任务无法懒更新，需要下传标记
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid); // 下传标记
            if (L <= mid) update(L, R, C, l, mid, rt << 1); // 处理左子树
            if (R > mid) update(L, R, C, mid + 1, r, rt << 1 | 1); // 处理右子树
            pushUp(rt); // 合并左右子树的和
        }

        // 区间增加：将L~R范围内所有值增加C
        // l~r为当前节点管辖范围，rt为当前节点下标
        public void add(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) { // 当前区间完全被覆盖
                sum[rt] += C * (r - l + 1); // 更新当前节点的和
                lazy[rt] += C; // 记录累加标记
                return;
            }
            // 任务无法懒更新，需要下传标记
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid); // 下传标记
            if (L <= mid) add(L, R, C, l, mid, rt << 1); // 处理左子树
            if (R > mid) add(L, R, C, mid + 1, r, rt << 1 | 1); // 处理右子树
            pushUp(rt); // 合并左右子树的和
        }

        // 区间查询：查询L~R范围内的累加和
        // l~r为当前节点管辖范围，rt为当前节点下标
        public long query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) { // 当前区间完全被覆盖
                return sum[rt]; // 直接返回当前节点的和
            }
            // 任务需要分解到子节点
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid); // 下传标记
            long ans = 0;
            if (L <= mid) ans += query(L, R, l, mid, rt << 1); // 查询左子树
            if (R > mid) ans += query(L, R, mid + 1, r, rt << 1 | 1); // 查询右子树
            return ans;
        }
    }

    public static void main(String[] args) {
        int[] origin = { 2, 1, 1, 2, 3, 4, 5 };
        SegmentTree seg = new SegmentTree(origin);
        int S = 1; // 整个区间的开始位置，规定从1开始，不从0开始 -> 固定
        int N = origin.length; // 整个区间的结束位置，规定能到N，不是N-1 -> 固定
        int root = 1; // 整棵树的头节点位置，规定是1，不是0 -> 固定
        int L = 2; // 操作区间的开始位置 -> 可变
        int R = 5; // 操作区间的结束位置 -> 可变
        int C = 4; // 要加的数字或者要更新的数字 -> 可变
        // 区间生成，必须在[S,N]整个范围上build
        seg.build(S, N, root);
        // 区间修改，可以改变L、R和C的值，其他值不可改变
        seg.add(L, R, C, S, N, root);
        // 区间更新，可以改变L、R和C的值，其他值不可改变
//        seg.update(L, R, C, S, N, root);
//        // 区间查询，可以改变L和R的值，其他值不可改变
        long sum = seg.query(L, R, S, N, root);// 返回数组中[L,R]区间的累加和,L和R不是下标
        System.out.println(sum);

    }


    class MySegmentTree{
        private int[] arr;
        private int[] sum;
        private boolean[] update;
        private int[] change;
        private int[] lazy;

        public MySegmentTree(int[] original){
            int n = original.length+1;

            for(int i=1;i<n;i++){
                arr[i]=original[i-1];
            }

            int [] arr =new int[n];
            int[] sum =new int [n*4];
            lazy = new int[n*4];  // 存储未下传的累加标记
            change = new int[n*4]; // 存储未下传的更新值
            update = new boolean[n*4]; // 标记该节点是否有待执行的更新操作
        }

        private void build(int l,int r,int rt){
            if(l==r){
                sum[rt] = arr[rt];
                return;
            }
            int mid = (l+r)/2;
            build(l,mid,rt*2);
            build(mid+1,r,rt*2+1);

            mergeLeftAndRight(rt);// 合并左右子树的和
        }

        private void mergeLeftAndRight(int rt) {
            sum[rt] = sum[rt*2]+sum[rt*2+1];
        }

        private void add(int l,int r,int L,int R,int value,int rt){
            if(l<=L&&r>=R){
                sum[rt]+=value*(r-l+1);
                lazy[rt]+=value;
                return;
            }
            int mid = (l+r)/2;
            down(rt,mid-l+1,r-mid);
        }

        /**
         *
         * @param rt
         * @param ln 左子树节点个数
         * @param rn  右子树节点个数
         */
        private void down(int rt, int ln, int rn) {
            // 优先处理更新操作（因为更新会覆盖之前的累加）
            if(update[rt]){
                update[2*rt]=true;
                update[2*rt+1]=true;
                change[2*rt] = change[rt];
                change[2*rt+1] = change[rt];
                lazy[2*rt] = 0; // 清空子节点的lazy标记（被更新覆盖）
                lazy[2*rt+1] = 0;
                sum[2*rt] = change[rt] * ln; // 更新左子节点的和
                sum[2*rt+1] = change[rt] * rn; // 更新右子节点的和
                update[rt] = false; // 清除当前节点的更新标记
            }

            if(lazy[rt]!=0){
                lazy[rt*2]=lazy[rt];
                lazy[rt*2+1]=lazy[rt];
                sum[2*rt]+=lazy[rt]*ln;
                sum[2*rt+1]+=lazy[rt]*rn;
                lazy[rt] = 0; // 清除当前节点的累加标记
            }
        }
    }
}
