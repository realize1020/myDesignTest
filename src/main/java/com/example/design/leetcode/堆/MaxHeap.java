package com.example.design.leetcode.堆;

public class MaxHeap {
    private int[] heap;
    private int limit;
    private int heapSize;

    public MaxHeap(int limit) {
        heap = new int[limit];
        this.limit = limit;
        heapSize = 0;
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }

    public boolean isFull() {
        return heapSize == limit;
    }

    public void push(int value) {
        if (heapSize == limit) {
            throw new RuntimeException("heap is full");
        }
        heap[heapSize] = value;
        // value  heapSize
        heapInsert(heap, heapSize++);
    }

    // 用户此时，让你返回最大值，并且在大根堆中，把最大值删掉
    // 剩下的数，依然保持大根堆组织
    public int pop() {
        int ans = heap[0];
        swap(heap, 0, --heapSize);
        heapify(heap, 0, heapSize);
        return ans;
    }

    private void heapInsert(int[] arr, int index) {
        // arr[index]
        // arr[index] 不比 arr[index父]大了 ， 停
        // index = 0;
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    // 从index位置，往下看，不断的下沉，
    // 停：我的孩子都不再比我大；已经没孩子了
    private void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1;
        while (left < heapSize) {
            // 左右两个孩子中，谁大，谁把自己的下标给largest
            // 右  ->  1) 有右孩子   && 2）右孩子的值比左孩子大才行
            // 否则，左
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);
            index = largest;
            left = index * 2 + 1;
        }
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    /**
     * 暴力实现大根堆
     */
    public static class RightMaxHeap {
        private int[] arr;
        private final int limit;
        private int size;

        public RightMaxHeap(int limit) {
            arr = new int[limit];
            this.limit = limit;
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == limit;
        }

        public void push(int value) {
            if (size == limit) {
                throw new RuntimeException("heap is full");
            }
            arr[size++] = value;
        }

        /**
         * pop 操作应该总是移除并返回堆顶元素（即数组的第一个元素），然后调整堆以保持其性质。
         * 而您的 pop 方法通过遍历整个数组找到最大值，并将其与最后一个元素交换后移除。
         * 这种方式虽然也能找到并移除最大值，但它的时间复杂度是 O(n)，而不是标准堆操作的 O(log n)。
         * @return
         */
        public int pop() {
            int maxIndex = 0;
            for (int i = 1; i < size; i++) {
                if (arr[i] > arr[maxIndex]) {
                    maxIndex = i;
                }
            }
            int ans = arr[maxIndex];
            arr[maxIndex] = arr[--size];
            return ans;
        }

    }

    public static void main(String[] args) {
        MaxHeap heap =new MaxHeap(5);
        heap.push(3);
        heap.push(1);
        heap.push(4);
        heap.push(2);
        heap.push(5);

        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());


    }


}
