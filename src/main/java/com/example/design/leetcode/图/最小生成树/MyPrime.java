package com.example.design.leetcode.图.最小生成树;

import com.example.design.leetcode.图.Edge;
import com.example.design.leetcode.图.Graph;
import com.example.design.leetcode.图.GraphDrawingFrame;
import com.example.design.leetcode.图.Node;

import java.util.*;

public class MyPrime {
    public static class  EdgeComparator implements Comparator<Edge>{

        @Override
        public int compare(Edge edge1, Edge edge2) {
            return edge1.weight-edge2.weight;
        }
    }

    public static class ResultEdgeComparator implements Comparator<Edge>{

        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.weight-o2.weight;
        }
    }

    public static Set<Edge> prim(Graph graph){
        if(graph==null){
            return null;
        }
        PriorityQueue<Edge> queue =new PriorityQueue<>(new EdgeComparator());
        HashSet<Node> used =new HashSet<>();
        HashSet<Edge> result =new HashSet<>();
        //TreeSet<Edge> result=new TreeSet<>(ResultEdgeComparator);
        Node node = graph.nodes.get(1);
        if(!used.contains(node)){
            used.add(node);
            for(Edge edge : node.edges){
                queue.add(edge);
            }
        }
        while(!queue.isEmpty()){
            Edge edge = queue.poll();
            Node nextNode = edge.to;
            if(!used.contains(nextNode)){
                used.add(nextNode);
                result.add(edge);
                for (Edge nextEdge : nextNode.edges) {
                    queue.add(nextEdge);
                }
            }
        }

        return result;

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

        Set<Edge> edges = prim(graph);
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
