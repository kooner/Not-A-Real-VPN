package vpn.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import vpn.VPN;

public class AboutPanel extends JPanel {
    public AboutPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(VPN.x-50, VPN.y/9));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "About"));

        JLabel about = new JLabel("Amitoj Kooner | Isaac Cheng | Alan Larson | Byron Duenas", SwingConstants.CENTER);
        add(about);
    }
}
