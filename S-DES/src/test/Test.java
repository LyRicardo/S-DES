package test;

import src.SDES;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        SDES sdes = new SDES();
        int [] key = {1,0,1,0,0,0,0,0,1,0};
        int [] plaintext = {0,1,0,0,0,0,1,1};
        int  [] ciphertext = sdes.encrypt(key,plaintext);
        System.out.println(Arrays.toString(ciphertext));
        int [] decrypted = sdes.decrypt(key,ciphertext);
        System.out.println(Arrays.toString(decrypted));

    }
}
