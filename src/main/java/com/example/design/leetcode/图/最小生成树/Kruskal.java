package com.example.design.leetcode.图.最小生成树;

import com.example.design.leetcode.图.Edge;
import com.example.design.leetcode.图.Graph;
import com.example.design.leetcode.图.GraphDrawingFrame;
import com.example.design.leetcode.图.Node;

import java.util.*;

public class Kruskal {
    // 并查集（Union-Find Set）
    public static class UnionFind {
        // key: 某一个节点，value: 该节点直接指向的父节点
        private HashMap<Node, Node> fatherMap;
        // key: 某一个集合的代表节点, value: 该集合中的节点数量
        private HashMap<Node, Integer> sizeMap;

        // 构造函数初始化两个哈希表
        public UnionFind() {
            fatherMap = new HashMap<>();
            sizeMap = new HashMap<>();
        }

        /**
         * 初始化所有节点为独立的集合
         * @param nodes 节点集合
         */
        public void makeSets(Collection<Node> nodes) {
            fatherMap.clear(); // 清空之前的映射关系
            sizeMap.clear();   // 清空之前的大小信息

            for (Node node : nodes) {
                fatherMap.put(node, node); // 每个节点初始时指向自己
                sizeMap.put(node, 1);      // 每个节点初始时属于自己的集合，大小为1
            }
        }

        /**
         * 查找给定节点所属集合的根节点，并进行路径压缩优化
         * @param n 给定节点
         * @return 根节点
         */
        private Node findFather(Node n) {
            Stack<Node> path = new Stack<>(); // 用于存储查找路径上的节点
            while (n != fatherMap.get(n)) {   // 如果当前节点不是根节点，则继续向上查找
                path.add(n);
                n = fatherMap.get(n);         // 更新 n 为它的父节点
            }
            // 将路径上的所有节点直接连接到根节点，以加速未来查找
            while (!path.isEmpty()) {
                fatherMap.put(path.pop(), n);
            }
            return n; // 返回根节点
        }

        /**
         * 判断两个节点是否属于同一个集合
         * @param a 第一个节点
         * @param b 第二个节点
         * @return 如果两个节点属于同一集合返回 true，否则返回 false
         */
        public boolean isSameSet(Node a, Node b) {
            return findFather(a) == findFather(b); // 只需比较它们的根节点是否相同
        }

        /**
         * 合并两个节点所在的集合
         * @param a 第一个节点
         * @param b 第二个节点
         */
        public void union(Node a, Node b) {
            if (a == null || b == null) {
                return; // 如果任一节点为空，则无法合并
            }
            Node aDai = findFather(a); // 找到 a 的根节点
            Node bDai = findFather(b); // 找到 b 的根节点
            if (aDai != bDai) {        // 如果 a 和 b 不在同一集合中
                int aSetSize = sizeMap.get(aDai); // 获取 a 所在集合的大小
                int bSetSize = sizeMap.get(bDai); // 获取 b 所在集合的大小

                // 总是将较小的集合合并到较大的集合上，以保持树的平衡
                if (aSetSize <= bSetSize) {
                    fatherMap.put(aDai, bDai); // 将 a 的根节点指向 b 的根节点
                    sizeMap.put(bDai, aSetSize + bSetSize); // 更新 b 集合的大小
                    sizeMap.remove(aDai);                  // 移除旧的 a 集合大小
                } else {
                    fatherMap.put(bDai, aDai); // 将 b 的根节点指向 a 的根节点
                    sizeMap.put(aDai, aSetSize + bSetSize); // 更新 a 集合的大小
                    sizeMap.remove(bDai);                  // 移除旧的 b 集合大小
                }
            }
        }
    }

    /**
     * 边的比较器，用于优先队列中的排序
     */
    public static class EdgeComparator implements Comparator<Edge> {

        @Override
        public int compare(Edge o1, Edge o2) {
            // 使用 Integer.compare 避免整数溢出问题
            return Integer.compare(o1.weight, o2.weight);
        }
    }

    /**
     * 使用 Kruskal 算法计算图的最小生成树（MST）
     * @param graph 输入的图对象
     * @return 最小生成树中的边集合
     */
    public static Set<Edge> kruskalMST(Graph graph) {
        UnionFind unionFind = new UnionFind();
        unionFind.makeSets(graph.nodes.values()); // 初始化并查集，每个节点自成一个集合

        // 创建优先队列（最小堆），按照边的权重升序排列
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new EdgeComparator());

        // 将所有边加入优先队列
        for (Edge edge : graph.edges) {
            priorityQueue.add(edge);
        }

        Set<Edge> result = new HashSet<>(); // 存储最小生成树中的边

        // 当优先队列不为空时，持续取出权重最小的边
        while (!priorityQueue.isEmpty()) {
            Edge edge = priorityQueue.poll(); // 取出权重最小的边

            // 如果这条边连接的两个节点不在同一个集合中（即不会形成环）
            if (!unionFind.isSameSet(edge.from, edge.to)) {
                result.add(edge); // 将这条边加入结果集中
                unionFind.union(edge.from, edge.to); // 合并这两个节点所在的集合
            }
        }

        return result; // 返回包含最小生成树的所有边的集合
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

//        // 创建5个节点，假设它们的值为1到5
//        Node node1 = new Node(1);
//        Node node2 = new Node(2);
//        Node node3 = new Node(3);
//        Node node4 = new Node(4);
//        Node node5 = new Node(5);
//
//        // 将节点添加到图中
//        graph.nodes.put(node1.value, node1);
//        graph.nodes.put(node2.value, node2);
//        graph.nodes.put(node3.value, node3);
//        graph.nodes.put(node4.value, node4);
//        graph.nodes.put(node5.value, node5);

        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);
        Node node6 = new Node(6);


        graph.nodes.put(node1.value, node1);
        graph.nodes.put(node2.value, node2);
        graph.nodes.put(node3.value, node3);
        graph.nodes.put(node4.value, node4);
        graph.nodes.put(node5.value, node5);
        graph.nodes.put(node6.value, node6);





        // 手动设置节点的位置（x, y坐标）
//        node1.setX(100); node1.setY(100);
//        node2.setX(200); node2.setY(100);
//        node3.setX(150); node3.setY(200);
//        node4.setX(300); node4.setY(200);
//        node5.setX(250); node5.setY(300);

        node1.setX(150); node1.setY(350);
        node2.setX(0); node2.setY(100);
        node3.setX(150); node3.setY(150);
        node4.setX(200); node4.setY(200);
        node5.setX(0); node5.setY(0);
        node6.setX(200); node6.setY(0);

        // 定义边并更新节点的入度、出度、邻接节点和关联边
//        addDirectedEdge(graph, node1, node2, 6); // A -> B
//        addDirectedEdge(graph, node1, node3, 2); // A -> C
//        addDirectedEdge(graph, node2, node4, 5); // B -> D
//        addDirectedEdge(graph, node3, node4, 1); // C -> D
//        addDirectedEdge(graph, node3, node5, 7); // C -> E
//        addDirectedEdge(graph, node4, node5, 3); // D -> E

        addDirectedEdge(graph, node1, node2, 6); // A -> B
        addDirectedEdge(graph, node1, node3, 1); // A -> C
        addDirectedEdge(graph, node1, node4, 5); // A -> D
        addDirectedEdge(graph, node2, node1, 6); // B -> A
        addDirectedEdge(graph, node2, node3, 5); // B -> C
        addDirectedEdge(graph, node2, node5, 3); // B -> E
        addDirectedEdge(graph, node3, node4, 5); // C -> D
        addDirectedEdge(graph, node3, node5, 6); // C -> E
        addDirectedEdge(graph, node3, node6, 4); // C -> F
        addDirectedEdge(graph, node3, node1, 1); // C -> A
        addDirectedEdge(graph, node3, node2, 5); // C -> B
        addDirectedEdge(graph, node4, node6, 2); // D -> F
        addDirectedEdge(graph, node4, node3, 5); // D -> C
        addDirectedEdge(graph, node4, node1, 5); // D -> A
        addDirectedEdge(graph, node5, node2, 3); // E -> B
        addDirectedEdge(graph, node5, node3, 6); // E -> C
        addDirectedEdge(graph, node5, node6, 6); // E -> F
        addDirectedEdge(graph, node6, node5, 6); // F -> E
        addDirectedEdge(graph, node6, node4, 2); // F -> D
        addDirectedEdge(graph, node6, node3, 4); // F -> C

        GraphDrawingFrame frame = new GraphDrawingFrame(graph);
        frame.setVisible(true);

        Set<Edge> edges = kruskalMST(graph);
        edges.stream().forEach(e->System.out.println(e.from.value+"->"+e.to.value));

    }

    private static void addDirectedEdge(Graph graph, Node from, Node to, int weight) {
        Edge edge = new Edge(weight, from, to);
        from.out++;
        to.in++;
        from.nexts.add(to);
        from.edges.add(edge);
        graph.edges.add(edge);

    }
}
