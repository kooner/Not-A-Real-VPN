package vpn;

import vpn.ui.ConnectionPanel;
import vpn.ui.LogPanel;
import vpn.ui.SendPanel;

public class GlobalDao {

    private ConnectionPanel connectionPanel;
    private LogPanel logPanel;
    private SendPanel sendPanel;

    public void setConnectionPanel(ConnectionPanel connectionPanel) {
        this.connectionPanel = connectionPanel;
    }
    
    public void setLogPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
    }
    
    public void setSendPanel(SendPanel sendPanel) {
        this.sendPanel = sendPanel;
    }
    
    public String getTextToSend() {
        return sendPanel.getTextToSend();
    }
    
    public synchronized void writeToLog(String s) {
        logPanel.writeToLog(s);
    }
    
    public String getIp() {
        return connectionPanel.getIp();
    }
    
    public String getPort() {
        return connectionPanel.getPort();
    }
    
    public String getSharedSecretKey() {
        return connectionPanel.getSharedSecretKey();
    }
    
    public synchronized String getStatus() {
        return connectionPanel.getStatus();
    }
    
    public synchronized void setStatus(String s) {
        connectionPanel.setStatus(s);
    }
}
