package com.example.design.leetcode.图;

import javax.swing.*;
import java.awt.*;

public class GraphDrawingFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public GraphDrawingFrame(Graph graph) {
        setTitle("Graph Drawing Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new GraphPanel(graph));
        pack();
        setLocationRelativeTo(null); // Center the window
    }

    private static class GraphPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private Graph graph;

        public GraphPanel(Graph graph) {
            this.graph = graph;
            setPreferredSize(new Dimension(800, 600)); // 设置面板大小
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制边
            for (Edge edge : graph.edges) {
                Node from = edge.from;
                Node to = edge.to;
                int x1 = from.getX(), y1 = from.getY();
                int x2 = to.getX(), y2 = to.getY();
                g2d.drawLine(x1 + 10, y1 + 10, x2 + 10, y2 + 10); // 调整以使线段中心对齐节点
            }

            // 绘制节点
            for (Node node : graph.nodes.values()) {
                int x = node.getX() - 5; // 调整以使圆心在指定位置
                int y = node.getY() - 5;
                g2d.setColor(Color.BLUE);
                g2d.fillOval(x, y, 10, 10); // 绘制圆形表示节点
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(node.value), x + 15, y + 10); // 在节点旁边绘制标签
            }
        }
    }

    // 测试用例：创建并初始化一个图结构
    public static void main(String[] args) {
        // 创建图实例
        Graph graph = new Graph();

        // 创建5个节点，假设它们的值为1到5
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);

        // 手动设置节点的位置（x, y坐标）
        node1.setX(100); node1.setY(100);
        node2.setX(200); node2.setY(100);
        node3.setX(150); node3.setY(200);
        node4.setX(300); node4.setY(200);
        node5.setX(250); node5.setY(300);

        // 将节点添加到图中
        graph.nodes.put(node1.value, node1);
        graph.nodes.put(node2.value, node2);
        graph.nodes.put(node3.value, node3);
        graph.nodes.put(node4.value, node4);
        graph.nodes.put(node5.value, node5);

        // 定义边并更新节点的入度、出度、邻接节点和关联边
        addDirectedEdge(graph, node1, node2, 6); // A -> B
        addDirectedEdge(graph, node1, node3, 2); // A -> C
        addDirectedEdge(graph, node2, node4, 5); // B -> D
        addDirectedEdge(graph, node3, node4, 1); // C -> D
        addDirectedEdge(graph, node3, node5, 7); // C -> E
        addDirectedEdge(graph, node4, node5, 3); // D -> E

        // 创建并显示此应用程序的窗口
        SwingUtilities.invokeLater(() -> {
            GraphDrawingFrame frame = new GraphDrawingFrame(graph);
            frame.setVisible(true);
        });
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
