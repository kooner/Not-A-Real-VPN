package vpn;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.BufferedInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import vpn.ui.ConnectionPanel;
import vpn.ui.LogPanel;
import vpn.ui.SendPanel;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/*
 * This class holds all global data
 */
public class GlobalDao {

    public final int kKeyLength = 16;

    private ConnectionPanel connectionPanel;
    private LogPanel logPanel;
    private SendPanel sendPanel;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedInputStream inputStream;
    private DataOutputStream outputWriter;
    private DiffieHellman diffieHellman;

    // Base AES encryption keys
    // These must be the length of our AES Cipher Block Length (16 bytes)
    private byte [] aesBaseEncryptKey;
    private byte [] aesBaseDecryptKey;
    // TODO: Add additional keys here (shared key, etc.)

    // In: 16-byte key
    public void setAesBaseEncryptKey(byte [] encryptKey) throws UnsupportedEncodingException {
        if (encryptKey.length == kKeyLength) {
            this.aesBaseEncryptKey = encryptKey;
        }
        else {
            VPN.globaldao.writeToLog("AES Encrypt Key not set! Invalid key length! Valid Length: 16 bytes");
            throw new UnsupportedEncodingException();
        }
    }

    // In: 16-byte key
    public void setAesBaseDecryptKey(byte [] decryptKey) throws UnsupportedEncodingException {
        if (decryptKey.length == kKeyLength) {
            this.aesBaseDecryptKey = decryptKey;
        }
        else {
            VPN.globaldao.writeToLog("AES Decrypt Key not set! Invalid key length! Valid Length: 16 bytes");
            throw new UnsupportedEncodingException();
        }
    }

    // TODO: Implement additional keys here (hint: private shared key)
    // (You can use an overloaded SecretKeySpec)
    public SecretKeySpec getAesEncryptKey() {
        return new SecretKeySpec(this.aesBaseEncryptKey, "AES");
    }

    // TODO: Implement additional keys here (hint: private shared key)
    // (You can use an overloaded SecretKeySpec)
    public SecretKeySpec getAesDecryptKey() {
        return new SecretKeySpec(this.aesBaseDecryptKey, "AES");
    }

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
    
    public synchronized void setInputStream(BufferedInputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    public synchronized BufferedInputStream getInputStream() {
        return inputStream;
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
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {}
            inputStream = null;
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {}
            serverSocket = null;
        }
    }
    
    public void setDiffieHellman(DiffieHellman diffieHellman) {
        this.diffieHellman = diffieHellman;
    }
    
    public DiffieHellman getDiffieHellman() {
        return this.diffieHellman;
    }
}
