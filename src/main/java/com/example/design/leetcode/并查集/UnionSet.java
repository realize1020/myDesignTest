package com.example.design.leetcode.并查集;



import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public  class UnionSet<V> {
    public static class Node<V> {
        V value;

        public Node(V v) {
            value = v;
        }
    }
    public HashMap<V, Node<V>> nodes; // 将原始值映射到对应的节点
    public HashMap<Node<V>, Node<V>> parents; // 每个节点指向其父节点
    public HashMap<Node<V>, Integer> sizeMap; // 每个代表节点对应的集合大小

    // 构造函数，初始化并查集
    public UnionSet(List<V> values) {
        nodes = new HashMap<>();
        parents = new HashMap<>();
        sizeMap = new HashMap<>();
        for (V cur : values) {
            Node<V> node = new Node<>(cur);
            nodes.put(cur, node);
            parents.put(node, node); // 初始时每个节点是自己的父节点
            sizeMap.put(node, 1); // 初始时每个集合只有一个元素
        }
    }

    // 查找节点 cur 的根节点，并进行路径压缩
    public Node<V> findFather(Node<V> cur) {
        Stack<Node<V>> path = new Stack<>();
        while (cur != parents.get(cur)) { // 如果当前节点不是自己的父节点，则继续向上查找
            path.push(cur);
            cur = parents.get(cur);
        }
        // 路径压缩：将路径上的所有节点直接连接到根节点
        while (!path.isEmpty()) {
            parents.put(path.pop(), cur);
        }
        return cur;
    }

    // 检查两个元素是否属于同一个集合
    public boolean isSameSet(V a, V b) {
        if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
            return false;
        }
        return findFather(nodes.get(a)) == findFather(nodes.get(b));
    }

    // 合并两个元素所在的集合
    public void union(V a, V b) {
        if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
            return;
        }
        Node<V> aHead = findFather(nodes.get(a));
        Node<V> bHead = findFather(nodes.get(b));
        if (aHead != bHead) { // 只有当两个元素不属于同一个集合时才进行合并
            int aSetSize = sizeMap.get(aHead);
            int bSetSize = sizeMap.get(bHead);
            Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
            Node<V> small = big == aHead ? bHead : aHead;
            parents.put(small, big); // 将较小的集合连接到较大的集合
            sizeMap.put(big, aSetSize + bSetSize); // 更新较大集合的大小
            sizeMap.remove(small); // 移除较小集合的大小记录
        }
    }

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5);
        UnionSet<Integer> uf = new UnionSet<>(values);
        //System.out.println(uf.isSameSet(1, 2));

        uf.union(1, 2);

        uf.union(2,3);

        System.out.println(uf.isSameSet(1, 3));
    }
}