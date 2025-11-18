package edu.univ.erp.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class BREATHEFONT {
    public static Font fontGen() {
        Font breatheFont;
        try {
            File fontFile = new File("AP_project/lib/Compacta Std Bold.otf"); 
            breatheFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            breatheFont = new Font("Arial", Font.BOLD, 32); 
        }
        return breatheFont;
    }
}
