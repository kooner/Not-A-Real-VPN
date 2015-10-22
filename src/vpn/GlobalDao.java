package vpn;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import vpn.ui.ConnectionPanel;
import vpn.ui.LogPanel;
import vpn.ui.SendPanel;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * This class holds all global data
 */
public class GlobalDao {

    public final int kKeyLength = 16;
    public final int nonceLength = 4;

    private ConnectionPanel connectionPanel;
    private LogPanel logPanel;
    private SendPanel sendPanel;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedInputStream inputStream;
    private DataOutputStream outputWriter;
    private DiffieHellman diffieHellman;
    private SecretKeySpec aesBaseKey;

    private byte[] personalNonce;
    private byte[] externalNonce;

    public void setAesBaseKey(byte[] key) {
        try {
            byte[] newKey = Arrays.copyOf(MessageDigest.getInstance("SHA-1").digest(key), kKeyLength);
            aesBaseKey = new SecretKeySpec(newKey, "AES");
        } catch (NoSuchAlgorithmException e) {
            // Never going to happen
            e.printStackTrace();
            System.exit(1);
        }
    }

    public SecretKeySpec getAesBaseKey() {
        return aesBaseKey;
    }

    public void setPersonalNonce() {
        SecureRandom sr = new SecureRandom();
        this.personalNonce = new byte[nonceLength];
        sr.nextBytes(this.personalNonce);
    }

    public byte[] getPersonalNonce() {
        return this.personalNonce;
    }

    public void setExternalNonce(byte[] extNonce) {
        this.externalNonce = extNonce;
    }

    public byte[] getExternalNonce() {
        return externalNonce;
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
    public synchronized void forceEnd() {
        if (diffieHellman != null) {
            diffieHellman.endSession();
            diffieHellman = null;
        }
        aesBaseKey = null;
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
            clientSocket = null;
        }
        if (outputWriter != null) {
            try {
                outputWriter.close();
            } catch (IOException e) {
            }
            outputWriter = null;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
            inputStream = null;
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
            serverSocket = null;
        }
    }

    public void setDiffieHellman(DiffieHellman diffieHellman) {
        this.diffieHellman = diffieHellman;
    }

    public DiffieHellman getDiffieHellman() {
        return this.diffieHellman;
    }

    public Cipher getAesBaseCipher(int cipherMode) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
            cipher.init(cipherMode, aesBaseKey, iv);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            // Never going to happen
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public Cipher getAesSessionCipher(int cipherMode) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
            cipher.init(cipherMode, diffieHellman.getAesSessionKey(), iv);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            // Never going to happen
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
