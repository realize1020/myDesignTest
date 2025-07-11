package com.example.design.leetcode.数组.在无序数组中找到第K小的数;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * 在无序数组中找到第K小的数
 */
public class Solution {
    public static class MaxHeapComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }

    }

    /**
     * 方式一、利用大根堆，时间复杂度O(N*logK)
     * 先把前k个数放入大根堆，然后遍历后面的数，如果当前数比堆顶小，则替换堆顶，然后返回堆顶即可
     */
    public static int minKth1(int[] arr, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(new MaxHeapComparator());
        for (int i = 0; i < k; i++) {
            maxHeap.add(arr[i]);
        }
        for (int i = k; i < arr.length; i++) {
            if (arr[i] < maxHeap.peek()) {
                maxHeap.poll();
                maxHeap.add(arr[i]);
            }
        }
        return maxHeap.peek();
    }

    /**
     * 方式二、改写快排,时间复杂度O(N),最坏时间复杂度O(N^2)
     * @param arr
     * @param k
     */
    public static int minKth2(int[] arr, int k){
        if(arr.length==0||k<1||k>arr.length){
            return -1;
        }
        return quickSort2(arr,0,arr.length-1,k-1);
    }


    static int quickSort2(int a[],int low,int high,int k)
    {
        if (low == high) {
            return a[low];
        }                                 //递归出口
        //Partition()是划分操作，将表a[low...high]划分为满足上述条件的两个子表
            int pivotpos = Partition(a,low,high);//划分
            if(pivotpos==k){
                return a[pivotpos];
            }else if(pivotpos>k){
                return quickSort2(a,low,pivotpos-1,k);
            }else{
                return quickSort2(a,pivotpos+1,high,k);
            }
    }
    static int Partition(int a[],int low,int high)
    {
        int pivot = a[low];                            //当前表中第一个元素设为枢轴质，对表进行划分
        while(low<high)                                     //循环跳出条件
        {
            while(low<high && a[high]>=pivot)
                --high;
            a[low] = a[high];                               //将比枢轴值小的元素移动到左端
            while(low<high && a[low]<=pivot)
                ++low;
            a[high] = a[low];                               //将比枢轴值大的元素移动到右端
        }
        a[low] = pivot;                                     //枢轴元素存放到最终位置
        return low;                                         //返回存放枢轴的最终位置
    }

    /**
     * 方式三、bfprt算法，时间复杂度O(N)，主要是为了改善快排那种方式选择pivot时，选择的可能会有些问题，bfprt算法可以解决这个问题
     * 快排选择pivot时可能出现很差的情况，导致k一直再pivot的一侧
     *
     * BFPRT算法是一种选择算法，旨在优化快速排序中的分区过程。通过选取五分位数的中位数作为基准值，减少最坏情况的发生。
     * 算法步骤包括分组、寻找中位数、递归寻找中位数的中位数，最终确定基准值并划分数组。相比于快速排序，BFPRT算法能更均匀地划分数据，提高效率。
     * 时间复杂度分析表明其最坏情况下为O(n)，优于快速排序的最坏情况。
     * @param arr
     * @param k
     * @return
     */
    public static int minKth3(int[] arr, int k) {
        if(arr.length==0||k<1||k>arr.length){
            return -1;
        }
        return bfprt(arr,0,arr.length-1,k-1);
    }



    public static int bfprt(int[] arr,int start,int end,int k){
        if (start == end) {
            return arr[start]; // 单个索引，直接返回
        }
        int pivot= getCenter(arr, start,end);
        int result = partition2(arr,start,end,pivot);
        if(result == k){
            return arr[result];
        }else if(result > k){
            return bfprt(arr, start,result-1,k);
        }else{
            return bfprt(arr, result+1, end,k);
        }
    }

    private static int partition2(int[] a, int low, int high, int pivotIndex) {
        int pivotValue = a[pivotIndex];
        swap(a, low, pivotIndex); // 将主元交换到 low 位置
        while (low < high) {
            while (low < high && a[high] >= pivotValue)
                --high;
            a[low] = a[high];
            while (low < high && a[low] <= pivotValue)
                ++low;
            a[high] = a[low];
        }
        a[low] = pivotValue; // 主元归位
        return low;
    }


    /**
     * 返回的的是中位数数组组成的中位数(是索引)
     * @param arr
     * @param start
     * @param end
     * @return
     */
    private static int getCenter(int[] arr, int start, int end ) {
        int n = end-start+1;
        int groups = n % 5 == 0 ? n / 5 : n / 5 + 1;
        int [] center = new int[groups];
        int j=0;
        int preIndex=start;
        for(int i=start;i<=end;i++){
            if(i!=start&&(i-start+1)%5==0){
                center[j++]=getCenterIndex(arr,preIndex,i);
                preIndex=i;
            }else if(i==end){
                center[j++]=getCenterIndex(arr,preIndex,i);
            }
        }
        return bfprt(center,0,center.length-1,center.length/2);
    }

    /**
     * 排序并找到中位数
     * @param
     * @return
     */
    private static int getCenterIndex(int[] arr, int start, int end) {
        Arrays.sort(arr, start, end + 1);
        return start + (end - start+1) / 2;
    }

    private static int  getstart(int[] temp) {
        int start=0;
        for(int i=0;i<temp.length;i++){
            if(temp[i]!=-1){
                start = i;
                break;
            }
        }
        return start;
    }

    public static void main(String[] args) {
        int[] arr = {1,2,4,5,3,8,7,6,9,11,10,12,13,14,15};
        //int[] arr = {7,6,5,4,3,2,1};
//        System.out.println(minKth1(arr, 3));
        //System.out.println(minKth2(arr, 14));
        //System.out.println(minKth3(arr, 5));
        //System.out.println(minKth4(arr, 5));
       // System.out.println(minKth5(arr, 5));

        int []arr2 ={1,2,3,4,5};
        partition3(arr2,0,arr2.length-1,3);
    }



    // 利用bfprt算法，时间复杂度O(N)
    public static int minKth4(int[] array, int k) {
        int[] arr = copyArray(array);
        return bfprt2(arr, 0, arr.length - 1, k - 1);
    }

    // arr[L..R]  如果排序的话，位于index位置的数，是什么，返回
    public static int bfprt2(int[] arr, int L, int R, int index) {
        if (L == R) {
            return arr[L];
        }
        // L...R  每五个数一组
        // 每一个小组内部排好序
        // 小组的中位数组成新数组
        // 这个新数组的中位数返回
        int pivot = medianOfMedians(arr, L, R);
        int[] range = partition(arr, L, R, pivot);
        if (index >= range[0] && index <= range[1]) {
            return arr[index];
        } else if (index < range[0]) {
            return bfprt2(arr, L, range[0] - 1, index);
        } else {
            return bfprt2(arr, range[1] + 1, R, index);
        }
    }

    // arr[L...R]  五个数一组
    // 每个小组内部排序
    // 每个小组中位数领出来，组成marr
    // marr中的中位数，返回
    public static int medianOfMedians(int[] arr, int L, int R) {
        int size = R - L + 1;
        int offset = size % 5 == 0 ? 0 : 1;
        int[] mArr = new int[size / 5 + offset];
        for (int team = 0; team < mArr.length; team++) {
            int teamFirst = L + team * 5;
            // L ... L + 4
            // L +5 ... L +9
            // L +10....L+14
            mArr[team] = getMedian(arr, teamFirst, Math.min(R, teamFirst + 4));
        }
        // marr中，找到中位数
        // marr(0, marr.len - 1,  mArr.length / 2 )
        return bfprt2(mArr, 0, mArr.length - 1, mArr.length / 2);
    }

    public static int getMedian(int[] arr, int L, int R) {
        insertionSort(arr, L, R);
        return arr[(L + R) / 2];
    }

    public static void insertionSort(int[] arr, int L, int R) {
        for (int i = L + 1; i <= R; i++) {
            for (int j = i - 1; j >= L && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }


    public static int[] partition(int[] arr, int L, int R, int pivot) {
        int less = L - 1;
        int more = R + 1;
        int cur = L;
        while (cur < more) {
            if (arr[cur] < pivot) {
                swap(arr, ++less, cur++);
            } else if (arr[cur] > pivot) {
                swap(arr, cur, --more);
            } else {
                cur++;
            }
        }
        return new int[] { less + 1, more - 1 };
    }

    public static void swap(int[] arr, int i1, int i2) {
        int tmp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = tmp;
    }

    public static int[] copyArray(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i != ans.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }


    //**************************************第三种写法**************************************

        public static int minKth5(int[] arr, int k) {
            if (arr.length == 0 || k < 1 || k > arr.length) {
                return -1;
            }
            return bfprt3(arr, 0, arr.length - 1, k - 1);
        }

        private static int bfprt3(int[] arr, int start, int end, int k) {
            if (start == end) {
                return arr[start];
            }
            int pivotValue = getMedianOfMedians(arr, start, end);
            int pivotIndex = partition3(arr, start, end, pivotValue);
            if (pivotIndex == k) {
                return arr[pivotIndex];
            } else if (pivotIndex > k) {
                return bfprt3(arr, start, pivotIndex - 1, k);
            } else {
                return bfprt3(arr, pivotIndex + 1, end, k);
            }
        }

        private static int getMedianOfMedians(int[] arr, int start, int end) {
            int n = end - start + 1;
            int numGroups = (n + 4) / 5; // 计算分组数，向上取整
            int[] medians = new int[numGroups];

            for (int i = 0; i < numGroups; i++) {
                int groupStart = start + i * 5;
                int groupEnd = Math.min(groupStart + 4, end);
                // 对当前组进行排序并获取中位数
                Arrays.sort(arr, groupStart, groupEnd + 1); // +1因为sort的toIndex是exclusive
                int medianIndex = groupStart + (groupEnd - groupStart) / 2;
                medians[i] = arr[medianIndex];
            }
            // 递归找中位数的中位数
            return bfprt3(medians, 0, medians.length - 1, medians.length / 2);
        }

    /**
     * 第k小的数
     * @param arr
     * @param start
     * @param end
     * @param pivotValue
     * @return
     */
        private static int partition3(int[] arr, int start, int end, int pivotValue) {
            // 找到pivotValue的索引作为基准
            int pivotIndex = start;
            for (; pivotIndex <= end; pivotIndex++) {
                if (arr[pivotIndex] == pivotValue) {
                    break;
                }
            }
            swap3(arr, pivotIndex, end); // 将基准值放到末尾
            int i = start;
            //用户认为在分区方法之前已经对数组进行了排序，并且pivotValue是排序后的中位数，因此pivotValue左边应该都是小于它的，右边都是大于它的，直接返回pivotIndex就可以，所以觉得没有必要再进行下面分区的循环操作。这可能存在误解
            //但需要注意的是，这只是局部排序，整个数组并没有完全排序。因此，主元（pivotValue）虽然是这些中位数的中位数，但整个数组中可能有比它大或小的元素分布在不同的位置，
            // 因此需要分区步骤来重新排列整个数组，使得主元的位置正确，左边的元素都小于或等于它，右边的元素都大于或等于它。分组排序 ≠ 全局排序
            for (int j = start; j < end; j++) {
                if (arr[j] <= pivotValue) {
                    swap3(arr, i, j);
                    i++;
                }
            }
            swap3(arr, i, end); // 将基准值放到正确位置
            return i;
        }


        private static void swap3(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
}

