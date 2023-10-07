package page;

import src.SDES;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class Des extends JFrame implements ActionListener {

    JButton decode;
    JTextField tfName, tfKey;
    JCheckBox encryptionToggle;


    public Des() {
        getContentPane().setBackground(Color.GRAY.darker());
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("page/imgs/background.jpg"));
        JLabel image = new JLabel(i1);
        image.setBounds(270, -200, 900, 800);
        add(image);

        JLabel heading = new JLabel("S-DES");
        heading.setBounds(300, 510, 300, 45);
        heading.setFont(new Font("Mongolian Baiti", Font.BOLD, 48));
        heading.setForeground(Color.BLACK);
        add(heading);

        JLabel name = new JLabel("Enter Your Plain Text");
        name.setBounds(600, 420, 300, 20);
        name.setFont(new Font("Mongolian Baiti", Font.BOLD, 22));
        name.setForeground(Color.BLACK);
        add(name);

        tfName = new JTextField();
        tfName.setBounds(600, 460, 300, 25);
        tfName.setFont(new Font("Times New Roaman", Font.BOLD, 20));
        tfName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_SPACE) {
                    e.consume();
                }
                if (c < 0 || c > 127) {
                    e.consume();
                }
            }
        });
        add(tfName);

        JLabel key = new JLabel("Enter Your Key");
        key.setBounds(600, 520, 300, 20);
        key.setFont(new Font("Mongolian Baiti", Font.BOLD, 22));
        key.setForeground(Color.BLACK);
        add(key);

        tfKey = new JTextField();
        tfKey.setBounds(600, 560, 300, 25);
        tfKey.setFont(new Font("Times New Roaman", Font.BOLD, 20));
        tfKey.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_SPACE) {
                    e.consume();
                }
                if (c < 0 || c > 127) {
                    e.consume();
                }
            }
        });
        add(tfKey);

        decode = new JButton("Encode");
        decode.setBounds(680, 670, 140, 50);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 16); // 设置字体大小
        decode.setFont(buttonFont); // 应用字体
        decode.setBackground(Color.white.darker());
        decode.setForeground(Color.BLACK);
        decode.addActionListener(this);
        add(decode);

        encryptionToggle = new JCheckBox("Encryption Mode");
        encryptionToggle.setBounds(680, 600, 140, 50);
        Font ToggleFont = new Font("Times New Roman", Font.BOLD, 14);
        encryptionToggle.setFont(ToggleFont);
        encryptionToggle.setSelected(true); // 默认为加密模式
        encryptionToggle.setBackground(Color.white.darker());
        encryptionToggle.setOpaque(true);
        encryptionToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (encryptionToggle.isSelected()) {
                    name.setText("Enter Your Plain Text");
                    decode.setText("Encode");
                } else {
                    name.setText("Enter Your Cipher Text");
                    decode.setText("Decode");
                }
            }
        });
        add(encryptionToggle);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public boolean isStr(String str){
        if (str.length()!=8) return false;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i)!='1'&&str.charAt(i)!='0') return false;
        }
        return true;
    }
    public static int[] toIntArray(String str) {
        char[] chars = str.toCharArray();
        int length = chars.length;
        int[] ints = new int[length];
        for (int i = 0; i < length; i++) {
            ints[i] = chars[i] - '0';
        }
        return ints;
    }

    public void actionPerformed(ActionEvent e) {
        String str1 = this.tfName.getText().trim();
        String str2 = this.tfKey.getText().trim();
        if (str1.isEmpty() || str2.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The input field cannot be empty!", "Warning!", JOptionPane.WARNING_MESSAGE);
        } else {
            if (encryptionToggle.isSelected()) {
                // 调用加密算法

                SDES sdes = new SDES();
                String result;
                if (!isStr(str1)) result = sdes.encrypt(str2, str1);
                else result = Arrays.toString(sdes.encrypt(toIntArray(str2),toIntArray(str1)));

                new ResultDialog(this, "Encode Result", result).setVisible(true);
            } else {
                SDES sdes = new SDES();
                String result;
                if (!isStr(str1)) result = sdes.decrypt(str2, str1);
                else result = Arrays.toString(sdes.decrypt(toIntArray(str2),toIntArray(str1)));
                new ResultDialog(this, "Decode Result", result).setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        Des l = new Des();
    }
}