package vpn.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import vpn.Client;
import vpn.Server;
import vpn.Status;
import vpn.StopAction;
import vpn.VPN;

public class ConnectionPanel extends JPanel {
    
    private JTextField ip;
    private JTextField port;
    private JTextField ssk;
    private JLabel status;
    
    public ConnectionPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(VPN.x-50, VPN.y/4));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Connection"));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension((VPN.x-50)/3, VPN.y/4));
        
        JLabel ipLabel = new JLabel("IP: ");
        ip = new JTextField(15);
        JPanel ipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ipPanel.add(ipLabel);
        ipPanel.add(ip);
        leftPanel.add(ipPanel);

        JLabel portLabel = new JLabel("Port: ");
        port = new JTextField(15);
        JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        portPanel.add(portLabel);
        portPanel.add(port);
        leftPanel.add(portPanel);

        JLabel sskLabel = new JLabel("Shared Secret Key: ");
        ssk = new JTextField(15);
        JPanel sskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sskPanel.add(sskLabel);
        sskPanel.add(ssk);
        leftPanel.add(sskPanel);

        JLabel statusLabel = new JLabel("Status: ");
        status = new JLabel(Status.DISCONNECTED);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        statusPanel.add(status);
        leftPanel.add(statusPanel);
        
        JButton clientButton = new JButton("Connect as client");
        clientButton.addActionListener(new Client());
        JButton serverButton = new JButton("Listen as server");
        serverButton.addActionListener(new Server());
        JButton stopButton = new JButton("Stop Action");
        stopButton.addActionListener(new StopAction());
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension((VPN.x-50)/4, VPN.y/4));
        rightPanel.add(clientButton);
        rightPanel.add(Box.createGlue());
        rightPanel.add(serverButton);
        rightPanel.add(Box.createGlue());
        rightPanel.add(stopButton);
        
        add(leftPanel, BorderLayout.LINE_START);
        add(rightPanel, BorderLayout.CENTER);
    }
    
    public String getIp() {
        return ip.getText();
    }
    
    public String getPort() {
        return port.getText();
    }
    
    public String getSharedSecretKey() {
        return ssk.getText();
    }
    
    public String getStatus() {
        return status.getText();
    }
    
    public void setStatus(String s) {
        status.setText(s);
    }
}
