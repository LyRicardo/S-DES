package src;

import java.util.ArrayList;
import java.util.Arrays;



public class SDES {
    private static final int BLOCK_LENGTH = 8; //分组长度
    private static final int KEY_LENGTH = 10; //密钥长度

    public static final int [] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    public static final int [] P8 = {6, 3, 7, 4, 8, 5, 10, 9}; //P-BOX

    public static final int [] LeftShift1 = {2, 3, 4, 5, 1};
    public static final int [] LeftShift2 = {3, 4, 5, 1, 2}; //循环左移1~2位

    private static final int [] IP = {2, 6, 3, 1, 4, 8, 5, 7}; //初始置换盒
    private static final int [] IP_1 = {4, 1, 3, 5, 7, 2, 8, 6}; //最终置换盒
    private static final int [] EP_BOX = {4, 1, 2, 3, 2, 3, 4, 1}; //EP置换

    private static final int [][] S_BOX1 = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 0, 2}};
    private static final int [][] S_BOX2 = {{0, 1, 2, 3}, {2, 3, 1, 0}, {3, 0, 1, 2}, {2, 1, 0, 3}}; //S-BOX1和2

    private static final int [] SP_BOX = {2, 4, 3, 1}; //直接置换

    //根据P_BOX对密钥进行置换,也可以用于初始置换和最终置换
    public int[] P_BOX_replacement(int[] key, int [] P_BOX) {

        int[] result = new int[P_BOX.length];
        for (int i = 0; i < P_BOX.length; i++) {
            result[i] = key[P_BOX[i]-1];
        }
        return result;
    }  //遍历P_BOX并赋值给result

    public int[] LeftShift(int[] key,int[] LeftShift) {
        int shift_num = LeftShift[0]-1;
        int[] result = new int[key.length];
        for (int i = 0; i < key.length/2; i++) {
            result[i] = key[(i+shift_num)%(key.length/2)];
        }
        for (int i = key.length/2; i < key.length; i++) {
            if (i+shift_num < key.length) result[i] = key[(i+shift_num)];
            else result[i] = key[i+shift_num-key.length/2];
        }
        return result;
    } //循环左移操作

    public int [] expand_replacement(int [] half_key){
        int [] result = new int [8];
        for (int i = 0; i < 8; i++) {
            result[i] = half_key[EP_BOX[i]-1];
        }
        return result;
    } //对加密的一半进行EP扩展置换

    public int [] round_key(int [] expand_key,int [] key_i){
        int [] result = new int[expand_key.length];
        for (int i = 0; i < expand_key.length; i++) {
            result[i] = expand_key[i] ^ key_i[i];
        }
        return result;
    } //将EP扩展置换结果和key_i按位异或

    public int [] S_P_BOX(int [] half_key){
        int [] result = new int[4];
        int x1 = half_key[0]*2+half_key[3];
        int y1 = half_key[1]*2+half_key[2];
        int x2 = half_key[4]*2+half_key[7];
        int y2 = half_key[5]*2+half_key[6]; //每一半的首尾和中间两位分别相加
        result[0] = S_BOX1[x1][y1]/2;
        result[1] = S_BOX1[x1][y1]%2;
        result[2] = S_BOX2[x2][y2]/2;
        result[3] = S_BOX2[x2][y2]%2;
        return result;
    } //经过S_P_BOX输出然后直接置换得到轮密钥

    public int [] encrypt(int [] key,int [] plaintext){
        int [] ip = P_BOX_replacement(plaintext,IP);
        //对主密钥进行P10置换后左移一位并P8置换得到k1
        int [] k1 = P_BOX_replacement(LeftShift(P_BOX_replacement(key,P10),SDES.LeftShift1),P8);
        //在k1基础上左移两位并P8置换得到k2
        int [] k2 = P_BOX_replacement(LeftShift(LeftShift(P_BOX_replacement(key,P10),SDES.LeftShift1),SDES.LeftShift2),P8);
        int [] left_ip = Arrays.copyOfRange(ip,0,4);
        int [] right_ip = Arrays.copyOfRange(ip,4,8);
        int [] f_k1 = P_BOX_replacement(S_P_BOX(round_key(expand_replacement(right_ip),k1)),SP_BOX);//求出k1对应的轮密钥
        left_ip = round_key(left_ip,f_k1);
        int [] f_k2 = P_BOX_replacement(S_P_BOX(round_key(expand_replacement(left_ip),k2)),SP_BOX);//求出k2对应的轮密钥
        right_ip = round_key(right_ip,f_k2);
        int [] result = new int[8];
        System.arraycopy(right_ip,0,result,0,4);
        System.arraycopy(left_ip,0,result,4,4);
        result = P_BOX_replacement(result,IP_1);
        return result;
    }

    //若输入密文为字符串，则将字符串中每个字符转化为8位ASCII码，并对每个8位ASCII码分别加密。
    public String encrypt(String key,String plaintext){
        ArrayList<int []> plaintext_list =new ArrayList<>();
        for(int i = 0;i < plaintext.length();i++){
            int plaintext_a = plaintext.charAt(i);
            String binary = Integer.toBinaryString(plaintext_a);
            StringBuilder word = new StringBuilder(binary);
            while (word.length() < 8) {
                word.insert(0, '0');
            }
            int [] plaintext_i =  new int[8];
            for (int j = 0; j < 8; j++) {
                plaintext_i[j] = word.charAt(j) - '0';
            }
            plaintext_list.add(plaintext_i);
        }
        ArrayList<int []> ciphertext_list =new ArrayList<>();
        char[] chars = key.toCharArray();
        int length = chars.length;
        int[] keys = new int[length];
        for (int i = 0; i < length; i++) {
            keys[i] = chars[i] - '0';
        }
        for (int[] ints : plaintext_list) {
            ciphertext_list.add(encrypt(keys, ints));
        }
        StringBuilder result = new StringBuilder();
        for (int[] arr : ciphertext_list) {
            int num = 0;
            for (int i = 0; i < 8; i++) {
                num = num * 2 + arr[i];
            }
            result.append((char) num);
        }
        return result.toString();
    }

    //标准格式的解密，原理同标准格式的加密
    public int [] decrypt(int [] key,int [] ciphertext){
        int [] ip = P_BOX_replacement(ciphertext,IP);
        int [] k1 = P_BOX_replacement(LeftShift(P_BOX_replacement(key,P10),SDES.LeftShift1),P8);
        int [] k2 = P_BOX_replacement(LeftShift(LeftShift(P_BOX_replacement(key,P10),SDES.LeftShift1),SDES.LeftShift2),P8);
        int [] left_ip = Arrays.copyOfRange(ip,0,4);
        int [] right_ip = Arrays.copyOfRange(ip,4,8);
        int [] f_k1 = P_BOX_replacement(S_P_BOX(round_key(expand_replacement(right_ip),k2)),SP_BOX);
        left_ip = round_key(left_ip,f_k1);
        int [] f_k2 = P_BOX_replacement(S_P_BOX(round_key(expand_replacement(left_ip),k1)),SP_BOX);
        right_ip = round_key(right_ip,f_k2);
        int [] result = new int[8];
        System.arraycopy(right_ip,0,result,0,4);
        System.arraycopy(left_ip,0,result,4,4);
        result = P_BOX_replacement(result,IP_1);
        return result;
    }

    public String decrypt(String key,String ciphertext){
        ArrayList<int []> ciphertext_list = new ArrayList<>();
        for(int i = 0; i < ciphertext.length(); i++) {
            int ciphertext_a = ciphertext.charAt(i);
            String binary = Integer.toBinaryString(ciphertext_a);
            StringBuilder word = new StringBuilder(binary);
            while (word.length() < 8) {
                word.insert(0, '0');
            }
            int [] plaintext_i =  new int[8];
            for (int j = 0; j < 8; j++) {
                plaintext_i[j] = word.charAt(j) - '0';
            }
            ciphertext_list.add(plaintext_i);
        }
        char[] chars = key.toCharArray();
        int length = chars.length;
        int[] keys = new int[length];
        for (int i = 0; i < length; i++) {
            keys[i] = chars[i] - '0';
        }
        ArrayList<int []> plaintext_list = new ArrayList<>();
        for (int[] ints : ciphertext_list) {
            plaintext_list.add(decrypt(keys, ints));
        }
        StringBuilder result = new StringBuilder();
        for (int[] arr : plaintext_list) {
            int num = 0;
            for (int i = 0; i < 8; i++) {
                num = num * 2 + arr[i];
            }
            result.append((char) num);
        }
        return result.toString();
    }
}