package page;

import javax.swing.*;
import java.awt.*;

class ResultDialog extends JDialog {
    public ResultDialog(JFrame parent, String title, String result) {
        setTitle(title);
        setSize(435, 300);  // 设置窗口大小
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(null);  // 使用null布局
        setResizable(false);          // 设置窗口大小不可变
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);  // 设置背景颜色为白色

        //ImageIcon加载图片
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("page/imgs/result.jpg"));
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(0, 0, 435, 202);  // 设置图片的位置和大小
        add(imageLabel);

        JLabel outputLabel = new JLabel(title);
        outputLabel.setBounds(50, 205, 335, 30);  // 设置标签的位置
        outputLabel.setHorizontalAlignment(JLabel.CENTER);
        outputLabel.setFont(new Font("Times New Roaman",Font.BOLD,17));
        add(outputLabel);

        JLabel resultLabel = new JLabel(result);
        resultLabel.setBounds(50, 225, 335, 30);  // 设置标签的位置
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setFont(new Font("Times New Roaman",Font.BOLD,17));
        add(resultLabel);

        setVisible(true);
    }
}