package vpn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import vpn.ui.ConnectionPanel;
import vpn.ui.LogPanel;
import vpn.ui.SendPanel;

/*
 * This class holds all global data
 */
public class GlobalDao {

    private ConnectionPanel connectionPanel;
    private LogPanel logPanel;
    private SendPanel sendPanel;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inputReader;
    private DataOutputStream outputWriter;
    
    public void setConnectionPanel(ConnectionPanel connectionPanel) {
        this.connectionPanel = connectionPanel;
    }
    
    public void setLogPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
    }
    
    public void setSendPanel(SendPanel sendPanel) {
        this.sendPanel = sendPanel;
    }
    
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    public synchronized void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    public synchronized Socket getClientSocket() {
        return clientSocket;
    }
    
    public synchronized void setInputReader(BufferedReader inputReader) {
        this.inputReader = inputReader;
    }
    
    public synchronized BufferedReader getInputReader() {
        return inputReader;
    }
    
    public synchronized void setOutputWriter(DataOutputStream outputWriter) {
        this.outputWriter = outputWriter;
    }
    
    public synchronized DataOutputStream getOutputWriter() {
        return outputWriter;
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
    
    // Ugly but working way to completely reset the program's state
    public synchronized void forceCloseSockets() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {}
            clientSocket = null;
        }
        if (outputWriter != null) {
            try {
                outputWriter.close();            
            } catch (IOException e) {}
            outputWriter = null;
        }
        if (inputReader != null) {
            try {
                inputReader.close();
            } catch (IOException e) {}
            inputReader = null;
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {}
            serverSocket = null;
        }
    }
}
