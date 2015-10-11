package vpn.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import vpn.SendMessage;
import vpn.VPN;

public class SendPanel extends JPanel {

    private JTextArea textArea;

    public SendPanel() {
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setText("Type message to send here...");
        JScrollPane textAreaScroll = new JScrollPane(textArea);
        textAreaScroll.setPreferredSize(new Dimension((VPN.x-50)*5/6, VPN.y / 5));

        JPanel leftPanel = new JPanel();
        leftPanel.add(textAreaScroll);
        
        JButton sendButton = new JButton("SEND");
        sendButton.addActionListener(new SendMessage());
        sendButton.setPreferredSize(new Dimension((VPN.x-50)/6, VPN.y / 5));
        JPanel rightPanel = new JPanel();
        rightPanel.add(sendButton);
        
        add(leftPanel, BorderLayout.LINE_START);
        add(rightPanel, BorderLayout.LINE_END);
    }
    
    public String getTextToSend() {
        return textArea.getText();
    }
}
