package com.codebind;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Style {
    private void import_font() {

        // regular
        try {
            Font poppins_reg = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Bold.ttf"));

            regular = poppins_reg.deriveFont(Font.PLAIN, 14);
        }
        catch(FontFormatException | IOException e) {
            System.out.println("Error loading Poppins Regular font");

            regular =  new Font("Arial", Font.PLAIN, 14);
        }

        // medium
        try {
            Font poppins_med = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Regular.ttf"));

            medium = poppins_med.deriveFont(Font.PLAIN, 14);
        }
        catch(FontFormatException | IOException e) {
            System.out.println("Error loading Poppins Medium font");

            medium =  new Font("Arial", Font.PLAIN, 14);
        }


        // bold
        try {
            Font poppins_bold = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Bold.ttf"));
            bold = poppins_bold.deriveFont(Font.PLAIN, 16);
        }
        catch(FontFormatException | IOException e) {
            System.out.println("Error loading Poppins Bold font");

            bold = new Font("Arial", Font.PLAIN, 16);
        }

    }

    private static Font regular;
    private static Font medium;
    private static Font bold;
    private static HashMap<String, Color> colors = new HashMap<>();

    Style() {
        this.import_font();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(regular);
        ge.registerFont(medium);
        ge.registerFont(bold);

        medium.deriveFont(Font.PLAIN, 14);
        regular.deriveFont(Font.PLAIN, 14);
        bold.deriveFont(Font.PLAIN, 14);

        colors.put("ACCENT", new Color(74, 191, 195 ));
        colors.put("WHITE", new Color(255, 255, 255));
        colors.put("YELLOW", new Color(223, 212, 95));
        colors.put("BLACK", new Color(0, 0, 0));
        colors.put("BACKGROUND", new Color(24, 24, 24));
        colors.put("CONTRAST", new Color(48, 48, 48));
    }

    public JButton buttonStyles(JButton btn) {
        btn.setFont(medium);
        btn.setBackground(colors.get("ACCENT"));
        btn.setForeground(colors.get("WHITE"));
        btn.setBorderPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return btn;
    }

    public JButton buttonStylesActive(JButton btn) {
        btn.setBackground(colors.get("YELLOW"));
        btn.setForeground(colors.get("BLACK"));

        return btn;
    }

    public JTextField textFieldStyles(JTextField field) {
        field.setFont(medium);
        field.setBorder(BorderFactory.createDashedBorder(colors.get("ACCENT")));
        field.setMargin(new Insets(5,5,5,5));
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setForeground(colors.get("WHITE"));
        field.setBackground(colors.get("CONTRAST"));

        return field;
    }

    public JLabel titleStyle(JLabel title) {
        title.setFont(regular);
        title.setForeground(colors.get("WHITE"));

        return title;
    }

    public JPanel setBackground(JPanel panel) {
        panel.setBackground(colors.get("BACKGROUND"));

        return panel;
    }

    public JTextArea setBackground(JTextArea txt) {
        txt.setBackground(colors.get("BACKGROUND"));
        txt.setForeground(colors.get("WHITE"));

        return txt;
    }

    public JDialog setBackground(JDialog modal) {
        modal.getContentPane().setBackground(colors.get("BACKGROUND"));
        modal.getContentPane().setForeground(colors.get("WHITE"));

        return modal;
    }

    public JDialog popupStyle(JDialog popup) {
        popup.getContentPane().setBackground(colors.get("BACKGROUND"));
        popup.getContentPane().setForeground(colors.get("WHITE"));

        return popup;
    }

    public JScrollPane setScrollPane(JScrollPane panel) {
        panel.getViewport().setBackground(colors.get("BACKGROUND"));
        panel.setBackground(colors.get("BACKGROUND"));

        panel.getHorizontalScrollBar().setBackground(colors.get("BACKGROUND"));
        panel.getVerticalScrollBar().setBackground(colors.get("BACKGROUND"));

        panel.setBorder(BorderFactory.createDashedBorder(colors.get("ACCENT")));

        return panel;
    }

    public JList listStyles(JList list) {
        list.setBackground(colors.get("BACKGROUND"));
        list.setForeground(colors.get("WHITE"));
        list.setBorder(BorderFactory.createEmptyBorder());
        list.setBorder(null);

        return list;
    }

    public JLabel listItemStyle(JLabel item) {
        item.setBackground(colors.get("BACKGROUND"));
        item.setForeground(colors.get("WHITE"));
        item.setFont(medium);

        return item;
    }

    public JLabel listItemStyleSelected(JLabel item) {
        item.setBackground(colors.get("ACCENT"));
        item.setForeground(colors.get("WHITE"));
        item.setFont(regular);

        return item;
    }

    public JCheckBox checkboxStyle(JCheckBox ck) {
        ck.setBackground(colors.get("BACKGROUND"));
        ck.setForeground(colors.get("WHITE"));
        ck.setFont(medium);
        ck.setBorder(BorderFactory.createEmptyBorder());

        return ck;
    }

}
