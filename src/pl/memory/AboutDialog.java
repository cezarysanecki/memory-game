package pl.memory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class AboutDialog extends JDialog {
    public AboutDialog(JFrame owner) {
        super(owner, "O programie", true);
        setSize(400,200);
        setLayout(new GridLayout(4,1));

        JPanel panelName = new JPanel();
        Font font = new Font("Serif", Font.BOLD, 25);
        JLabel text = new JLabel("Memory: Premier League");
        text.setFont(font);
        panelName.add(text, BorderLayout.SOUTH);
        add(panelName);
        add(new ComponentLine());

        panelName = new JPanel();
        font = new Font("Serif", Font.ITALIC, 18);
        text = new JLabel("\u00AE Cezary Sanecki");
        text.setFont(font);
        panelName.add(text, BorderLayout.SOUTH);
        add(panelName);

        JButton buttonOk = new JButton("OK");
        buttonOk.addActionListener(e -> setVisible(false));

        JPanel panel = new JPanel();
        panel.add(buttonOk);
        add(panel);
        setLocation(owner.getLocation().x + (owner.getWidth() - getWidth()) / 2, owner.getLocation().y +
                (owner.getHeight() - getHeight()) / 2);
        setResizable(false);
        setVisible(true);
    }

    private class ComponentLine extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Line2D line = new Line2D.Double(0,20,getWidth(),20);
            String text = new String("Memory: Premier League");
            Font font = new Font("Serif", Font.BOLD, 25);
            g2.setFont(font);
            g2.drawString(text,0,110);
            g2.draw(line);
        }
    }
}
