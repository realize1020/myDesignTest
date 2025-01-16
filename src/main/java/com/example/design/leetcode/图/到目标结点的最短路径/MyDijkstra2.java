package com.example.design.leetcode.图.到目标结点的最短路径;

import com.example.design.leetcode.图.Edge;
import com.example.design.leetcode.图.Graph;
import com.example.design.leetcode.图.GraphDrawingFrame;
import com.example.design.leetcode.图.Node;

import java.util.*;

public class MyDijkstra2 {
    public static class MyHeap{
        private int size;
        private HashMap<Node,Integer> indexMap;
        private HashMap<Node,Integer> distanceMap;
        private Node[] heap;
        private int distance;

        public MyHeap(int size){
            this.size=0;
            indexMap =new HashMap<>();
            distanceMap =new HashMap<>();
            heap = new Node[size];
        }


        public void addOrUpdateHeap(Node node,int index) {
            if(!indexMap.containsKey(node)){//新增
                heap[size]=node;
                indexMap.put(node,size);
                distanceMap.put(node,index);
                insertHeap(heap,size++);
            }else if(indexMap.containsKey(node)&&indexMap.get(node) != -1){
                Integer distance = distanceMap.get(node);
                distanceMap.put(node,Math.min(distance,index));
                heapify(index,indexMap.get(node));
            }
    }

        private void heapify(int index, int size) {
            int left = index * 2 + 1;
            while(left<size){
                int smallest = left+1<size && heap[left].value > heap[left+1].value ? left+1:left;
                smallest = heap[index].value < heap[smallest].value ? index:smallest;
                if(smallest==index){
                    break;
                }
                swap(index,smallest);
                index=smallest;
                left=index * 2 + 1;
            }
        }

        private void insertHeap(Node[] heap, int index) {
            while(distanceMap.get(heap[index]) < distanceMap.get(heap[(index-1)/2])){
                swap(index,(index-1)/2);
                index=(index-1)/2;
            }
        }

        private void swap(int i, int j) {
            indexMap.put(heap[i],j);
            indexMap.put(heap[j],i);
            Node temp=heap[i];
            heap[i]= heap[j];
            heap[j]=temp;

        }

        public Node pop() {
            Node node = heap[0];
            distance=distanceMap.get(heap[0]);
            swap(0,size-1);
            indexMap.put(heap[size-1],-1);
            distanceMap.remove(heap[size-1]);
            heap[size-1]=null;
            heapify(0,--size);
            return node;
        }
    }

    public static HashMap<Node,Integer> dijstra(Node from , int nodeSize){
        MyHeap heap = new MyHeap(nodeSize);
        heap.addOrUpdateHeap(from,0);
        HashMap<Node,Integer> result =new HashMap<>();
        while(heap.size!=0){
            Node node = heap.pop();
            result.put(node,heap.distance);
            for(Edge edge : node.edges){
                Node next = edge.to;
                heap.addOrUpdateHeap(next,heap.distance+edge.weight);
            }
            result.entrySet().stream().forEach(s-> System.out.printf(s.getKey().value+":"+s.getValue()+"、"));
            System.out.println();
        }
        return result;
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

        HashMap<Node,Integer> nodeIntegerHashMap = dijstra(node1,5);
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


    /**使用优先级队列(小根堆)优化
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

        // 使用优先队列（最小堆）来高效地获取当前距离最小且未处理的节点
        PriorityQueue<NodeDistancePair> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(pair -> pair.distance));
        priorityQueue.add(new NodeDistancePair(from, 0));

        while (!priorityQueue.isEmpty()) {
            // 获取并移除当前距离最小且未处理的节点
            NodeDistancePair currentPair = priorityQueue.poll();
            Node currentNode = currentPair.node;

            // 如果该节点已经被处理过，则跳过
            if (selectedNodes.contains(currentNode)) {
                continue;
            }

            // 标记该节点为已处理
            selectedNodes.add(currentNode);

            // 遍历当前节点的所有邻接边
            for (Edge edge : currentNode.edges) {
                Node toNode = edge.to;
                int newDistance = distanceMap.get(currentNode) + edge.weight;

                // 如果找到了更短的路径到达toNode，则更新距离并加入优先队列
                if (!distanceMap.containsKey(toNode) || newDistance < distanceMap.get(toNode)) {
                    distanceMap.put(toNode, newDistance);
                    priorityQueue.add(new NodeDistancePair(toNode, newDistance));
                }
            }
        }

        return distanceMap;
    }

    /**
     * 辅助类，用于存储节点及其对应的当前距离。
     */
    private static class NodeDistancePair {
        Node node;
        int distance;

        NodeDistancePair(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

}
