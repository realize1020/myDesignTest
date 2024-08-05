package com.example.design.leetcode.二叉树;

import javax.swing.*;
import java.awt.*;

public class BinaryTreePanel extends JPanel {

    private TreeNode root;
    private int gap = 50; // 节点间水平间隙
    private int vGap = 70; // 垂直间隙

    public BinaryTreePanel(TreeNode root) {
        this.root = root;
        setPreferredSize(new Dimension(600, 600)); // 初始大小，根据实际调整
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 调用示例
        drawTree(g, root, getWidth() / 2, getHeight() / 2, getWidth() / 6, getHeight() / 10);
    }

    public void drawTree(Graphics g, TreeNode node, int x, int y, int hGap, int vGap) {
        if (node == null) return;

        // 绘制当前节点（这里仅为示意，具体实现可能包括绘制节点值等）
        g.drawOval(x - hGap / 2, y - vGap / 2, hGap, vGap);


        // 在矩形中间绘制节点的值
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(node.val + "");
        int textHeight = fm.getHeight();
        int textX = x - textWidth / 2;
        int textY = y + vGap / 4; // 调整文本的垂直位置以居中
        g.drawString(node.val + "", textX, textY);

        // 传递给左子节点，更新x坐标减去水平间隙，y坐标增加垂直间隙
        if (node.left != null) {
            g.drawLine(x, y + vGap / 2, x - hGap, y + vGap);
            drawTree(g, node.left, x - hGap, y + vGap, hGap / 2, vGap);
        }

        // 传递给右子节点，更新x坐标加上水平间隙，y坐标增加垂直间隙
        if (node.right != null) {
            g.drawLine(x, y + vGap / 2, x + hGap, y + vGap);
            drawTree(g, node.right, x + hGap, y + vGap, hGap / 2, vGap);
        }
    }
}

