package vpn.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import vpn.VPN;

public class LogPanel extends JPanel {

    private JTextArea textArea;

    public LogPanel() {
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JScrollPane textAreaScroll = new JScrollPane(textArea);
        textAreaScroll.setPreferredSize(new Dimension(VPN.x - 50, VPN.y / 3));
        textAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(textAreaScroll);
    }

    public void writeToLog(String s) {
        textArea.append(s + "\n");
    }
}
