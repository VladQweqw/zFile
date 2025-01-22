package app;

import app.pages.Group.GroupBy;
import app.pages.Rename.Rename;
import com.formdev.flatlaf.IntelliJTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URI;

public class CreateUI {
    private static JFrame frame = new JFrame("zFile");
    private final String version = "1.3.1";
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel rename_app;
    private JPanel groupBy_app;

    public static void loadResources() {
        try {
            ImageIcon icon = new ImageIcon("src/resources/images/logo.png");
            frame.setIconImage(icon.getImage());
        }catch(Exception ex) {
            System.out.println("Cannot set app icon");
        }

        try {
            InputStream regularStream = Rename.class.getResourceAsStream("/fonts/Poppins-Regular.ttf");
            Font poppinsRegular = Font.createFont(Font.TRUETYPE_FONT, regularStream).deriveFont(14f);

            InputStream mediumStream = Rename.class.getResourceAsStream("/fonts/Poppins-Medium.ttf");
            Font poppinsMedium = Font.createFont(Font.TRUETYPE_FONT, mediumStream).deriveFont(14f);

            UIManager.put("defaultFont", poppinsRegular);
            UIManager.put("Label.font", poppinsMedium);
            UIManager.put("Button.font", poppinsMedium);

        } catch (Exception e) {
            System.err.println("Failed to load custom font. Using default font.");
            e.printStackTrace();
        }

        try {
            IntelliJTheme.setup(
                    Rename.class.getResourceAsStream("/themes/zFile.theme.json")
            );


        }catch (Exception e) {
            System.out.println("Failed to load theme");
        }
    }

    private JButton rename_btn;

    private JButton group_btn;

    private JPanel Header() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setPreferredSize(new Dimension(500, 90));

        JLabel menu_title = new JLabel("Menu");
        menu_title.setPreferredSize(new Dimension(400, 30));
        menu_title.setHorizontalAlignment(SwingConstants.CENTER);

        this.rename_btn = new JButton("Rename files");
        this.group_btn = new JButton("Group files");

        rename_btn.addActionListener(e -> {
            cardLayout.show(this.mainPanel, "rename");

            rename_btn.setBackground(new Color(0x723F90));
            group_btn.setBackground(new Color(0x45405C));

        });

        group_btn.addActionListener(e -> {
            cardLayout.show(this.mainPanel, "group");

            group_btn.setBackground(new Color(0x723F90));
            rename_btn.setBackground(new Color(0x45405C));
        });

        panel.add(menu_title);
        panel.add(rename_btn);
        panel.add(group_btn);

        return panel;
    }

    private JPanel Footer() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setPreferredSize(new Dimension(500, 50));

        JLabel version = new JLabel("zFile v." + this.version);
        version.setPreferredSize(new Dimension(150, 40));
        version.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel website = new JLabel("Website support");
        website.setPreferredSize(new Dimension(150, 40));
        website.setHorizontalAlignment(SwingConstants.CENTER);

        website.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(
                            new URI("https://zfile.netlify.app/")
                    );
                }catch(Exception ex) {
                    System.out.println("Website doesnt open");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                website.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // Change cursor to hand
            }

            @Override
            public void mouseExited(MouseEvent e) {
                website.setCursor(Cursor.getDefaultCursor());  // Change cursor back to default
            }


        });


        panel.add(version);
        panel.add(website);

        return panel;
    }

    public CreateUI() {
        loadResources();

        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        this.rename_app = new Rename(frame).getPanel();
        mainPanel.add(rename_app, "rename");

        this.groupBy_app = new GroupBy(frame).getPanel();
        mainPanel.add(groupBy_app, "group");

        frame.setLayout(new BorderLayout());
        frame.add(Header(), BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(Footer(), BorderLayout.SOUTH);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 725);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

}
