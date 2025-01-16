package com.example.design.leetcode.图.到目标结点的最短路径;

import com.example.design.leetcode.图.Edge;
import com.example.design.leetcode.图.Graph;
import com.example.design.leetcode.图.GraphDrawingFrame;
import com.example.design.leetcode.图.Node;

import java.util.HashMap;

public class Dijkstra2 {
    /**
     * 节点堆类，用于维护一个优先队列，其中存储的是从源节点出发到达各节点的当前最短距离。
     */
    public static class NodeHeap {
        private Node[] nodes; // 实际的堆结构数组
        // key: 某一个节点，value: 该节点在堆中的位置索引
        private HashMap<Node, Integer> heapIndexMap;
        // key: 某一个节点，value: 从源节点出发到该节点的目前最小距离
        private HashMap<Node, Integer> distanceMap;
        private int size; // 堆中实际存储的节点数量

        /**
         * 构造函数初始化堆结构及相关映射表
         * @param size 堆的最大容量
         */
        public NodeHeap(int size) {
            this.nodes = new Node[size];
            this.heapIndexMap = new HashMap<>();
            this.distanceMap = new HashMap<>();
            this.size = 0;
        }

        /**
         * 判断堆是否为空
         * @return 如果堆为空返回 true，否则返回 false
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * 添加或更新节点的距离信息，并调整堆以维持最小堆属性
         * @param node 目标节点
         * @param distance 到达目标节点的新距离
         */
        public void addOrUpdateOrIgnore(Node node, int distance) {
            if (inHeap(node)) { // 如果节点已经在堆中，则更新其距离并调整堆
                distanceMap.put(node, Math.min(distanceMap.get(node), distance));
                insertHeapify(node, heapIndexMap.get(node)); // 上浮操作
            } else if (!isEntered(node)) { // 如果节点尚未进入过堆，则添加它
                nodes[size] = node;
                heapIndexMap.put(node, size);
                distanceMap.put(node, distance);
                insertHeapify(node, size++); // 上浮操作并增加堆大小
            }
        }

        /**
         * 弹出堆顶元素（即当前最短距离的节点），并调整堆以维持最小堆属性
         * @return 包含弹出节点及其最短距离的对象
         */
        public NodeRecord pop() {
            NodeRecord nodeRecord = new NodeRecord(nodes[0], distanceMap.get(nodes[0])); // 获取并记录堆顶元素
            swap(0, size - 1); // 交换堆顶与最后一个元素的位置
            heapIndexMap.put(nodes[size - 1], -1); // 标记原堆顶元素已不在堆中
            distanceMap.remove(nodes[size - 1]); // 移除该节点的距离信息
            nodes[size - 1] = null; // 清理堆顶元素（Java 中无需显式析构）
            heapify(0, --size); // 下沉操作以恢复最小堆属性
            return nodeRecord;
        }

        /**
         * 上浮操作：将指定索引处的节点上移到正确位置，以维持最小堆属性
         * @param node 当前节点
         * @param index 当前节点在堆中的索引
         */
        private void insertHeapify(Node node, int index) {
            while (distanceMap.get(nodes[index]) < distanceMap.get(nodes[(index - 1) / 2])) { // 比较并上浮
                swap(index, (index - 1) / 2); // 交换当前节点与其父节点
                index = (index - 1) / 2; // 更新当前节点索引为父节点索引
            }
        }

        /**
         * 下沉操作：将指定索引处的节点下移到正确位置，以维持最小堆属性
         * @param index 当前节点在堆中的索引
         * @param size 堆的实际大小
         */
        private void heapify(int index, int size) {
            int left = index * 2 + 1; // 左子节点索引
            while (left < size) { // 存在左子节点时继续循环
                int smallest = left + 1 < size && distanceMap.get(nodes[left + 1]) < distanceMap.get(nodes[left])
                        ? left + 1 : left; // 找到左右子节点中较小的那个
                smallest = distanceMap.get(nodes[smallest]) < distanceMap.get(nodes[index])
                        ? smallest : index; // 确定需要下沉的目标位置
                if (smallest == index) { // 如果不需要下沉则退出循环
                    break;
                }
                swap(smallest, index); // 交换当前节点与目标位置的节点
                index = smallest; // 更新当前节点索引为目标位置索引
                left = index * 2 + 1; // 更新左子节点索引
            }
        }

        /**
         * 判断节点是否曾经进入过堆
         * @param node 目标节点
         * @return 如果节点曾经进入过堆返回 true，否则返回 false
         */
        private boolean isEntered(Node node) {
            return heapIndexMap.containsKey(node);
        }

        /**
         * 判断节点是否当前存在于堆中
         * @param node 目标节点
         * @return 如果节点当前存在于堆中返回 true，否则返回 false
         */
        private boolean inHeap(Node node) {
            return isEntered(node) && heapIndexMap.get(node) != -1;
        }

        /**
         * 交换堆中两个节点的位置，并更新它们在索引映射表中的值
         * @param index1 第一个节点的索引
         * @param index2 第二个节点的索引
         */
        private void swap(int index1, int index2) {
            heapIndexMap.put(nodes[index1], index2); // 更新第一个节点的索引
            heapIndexMap.put(nodes[index2], index1); // 更新第二个节点的索引
            Node tmp = nodes[index1]; // 临时变量用于交换
            nodes[index1] = nodes[index2];
            nodes[index2] = tmp;
        }
    }

    /**
     * 改进后的 Dijkstra 算法，使用最小堆优化查找过程。
     * 从 head 出发，计算到达每个可达节点的最短路径，并返回结果。
     *
     * @param head 起始节点
     * @param size 最大堆容量
     * @return 包含从起始节点到所有可达节点的最短路径长度的哈希表
     */
    public static HashMap<Node, Integer> dijkstra2(Node head, int size) {
        NodeHeap nodeHeap = new NodeHeap(size); // 初始化最小堆
        nodeHeap.addOrUpdateOrIgnore(head, 0); // 将起始节点加入堆中，距离为 0
        HashMap<Node, Integer> result = new HashMap<>(); // 存储最终的最短路径结果

        while (!nodeHeap.isEmpty()) { // 当堆不为空时持续处理
            NodeRecord record = nodeHeap.pop(); // 弹出堆顶元素（当前最短路径的节点）
            Node cur = record.node;
            int distance = record.distance;
            for (Edge edge : cur.edges) {
                nodeHeap.addOrUpdateOrIgnore(edge.to, edge.weight + distance);
            }
            result.put(cur, distance);
            result.entrySet().stream().forEach(s-> System.out.printf(s.getKey().value+":"+s.getValue()+"、"));
            System.out.println();
        }
        return result;
    }

    public static class NodeRecord {
        public Node node;
        public int distance;

        public NodeRecord(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        // 创建5个节点，假设它们的值为1到5
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);

        // 将节点添加到图中
        graph.nodes.put(node1.value, node1);
        graph.nodes.put(node2.value, node2);
        graph.nodes.put(node3.value, node3);
        graph.nodes.put(node4.value, node4);
        graph.nodes.put(node5.value, node5);

        // 手动设置节点的位置（x, y坐标）
        node1.setX(100); node1.setY(100);
        node2.setX(200); node2.setY(100);
        node3.setX(150); node3.setY(200);
        node4.setX(300); node4.setY(200);
        node5.setX(250); node5.setY(300);

        // 定义边并更新节点的入度、出度、邻接节点和关联边
        addDirectedEdge(graph, node1, node2, 6); // A -> B
        addDirectedEdge(graph, node1, node3, 2); // A -> C
        addDirectedEdge(graph, node2, node4, 5); // B -> D
        addDirectedEdge(graph, node3, node4, 1); // C -> D
        addDirectedEdge(graph, node3, node5, 7); // C -> E
        addDirectedEdge(graph, node4, node5, 3); // D -> E

        GraphDrawingFrame frame = new GraphDrawingFrame(graph);
        frame.setVisible(true);

        HashMap<Node, Integer> nodeIntegerHashMap = dijkstra2(node1,5);
        nodeIntegerHashMap.entrySet().stream().forEach(s-> System.out.println(s.getKey().value+":"+s.getValue()));




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
