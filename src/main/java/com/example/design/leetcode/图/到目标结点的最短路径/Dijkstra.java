package com.example.design.leetcode.图.到目标结点的最短路径;

import com.example.design.leetcode.图.Edge;
import com.example.design.leetcode.图.Graph;
import com.example.design.leetcode.图.GraphDrawingFrame;
import com.example.design.leetcode.图.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Dijkstra {
    /**
     * 使用 Dijkstra 算法计算从起点出发到所有其他节点的最短路径。
     *
     * @param from 起点节点
     * @return 从起点到所有节点的最短距离映射
     */
    public static HashMap<Node, Integer> dijkstra1(Node from) {
        // 存储从起点到每个节点的最小距离，初始时只有起点的距离为0
        HashMap<Node, Integer> distanceMap = new HashMap<>();
        distanceMap.put(from, 0);
        // 已经确定最短路径的节点集合，避免重复处理
        HashSet<Node> selectedNodes = new HashSet<>();
        // from 0
        Node minNode = getMinDistanceAndUnselectedNode(distanceMap, selectedNodes);
        while (minNode != null) { // 获取并移除当前距离最小且未处理的节点
            int distance = distanceMap.get(minNode);
            for (Edge edge : minNode.edges) { // 遍历当前节点的所有邻接边
                Node toNode = edge.to;
                if (!distanceMap.containsKey(toNode)) {
                    distanceMap.put(toNode, distance + edge.weight);
                } else {
                    distanceMap.put(edge.to,
                            Math.min(distanceMap.get(toNode), distance + edge.weight));
                }
            }
            selectedNodes.add(minNode);
            minNode = getMinDistanceAndUnselectedNode(distanceMap, selectedNodes);
        }
        return distanceMap;
    }
    /**
     * 辅助方法：查找当前距离最小且未处理的节点（原版实现，使用优先队列优化后不再需要）
     *
     * @param distanceMap 存储从起点到各节点的最短距离
     * @param touchedNodes 已处理的节点集合
     * @return 当前距离最小且未处理的节点，如果不存在则返回null
     */
    public static Node getMinDistanceAndUnselectedNode(HashMap<Node, Integer> distanceMap, HashSet<Node> touchedNodes) {
        Node minNode = null;
        int minDistance = Integer.MAX_VALUE;
        for (Map.Entry<Node, Integer> entry : distanceMap.entrySet()) {
            Node node = entry.getKey();
            int distance = entry.getValue();
            if (!touchedNodes.contains(node) && distance < minDistance) {// 如果该节点已经被处理过，则跳过
                minNode = node;
                minDistance = distance;
            }
        }
        return minNode;
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

        HashMap<Node, Integer> nodeIntegerHashMap = dijkstra1(node1);
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
