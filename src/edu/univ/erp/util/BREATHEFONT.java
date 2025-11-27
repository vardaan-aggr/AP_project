package edu.univ.erp.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class BREATHEFONT {
    public static Font fontGen() {
        Font breatheFont;
        try {
            File fontFile = new File("C:\\Users\\DELL\\Desktop\\SEM 3\\AP\\project\\AP_project\\lib\\Compacta Std Bold.otf"); 
            breatheFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            breatheFont = new Font("Arial", Font.BOLD, 32); 
        }
        return breatheFont;
    }
    public static Font gFontGen() {
        Font gf;
        try {
            File fontFile = new File("C:\\Users\\DELL\\Desktop\\SEM 3\\AP\\project\\AP_project\\lib\\GoogleSansFlex-VariableFont_GRAD,ROND,opsz,slnt,wdth,wght.ttf"); 
            gf = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            gf = new Font("Arial", Font.BOLD, 32); 
        }
        return gf;
    }
}
