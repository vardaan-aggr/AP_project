package edu.univ.erp.util;

import java.awt.GraphicsEnvironment;

public class oblivion {
    public static void main(String[] args) {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String f : fonts) {
            System.out.println(f);
        }
    }
}