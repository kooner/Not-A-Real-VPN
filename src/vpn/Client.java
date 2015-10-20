package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/*
 * This class is called when the "Connect as client" button is pressed
 */
public class Client implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String status = VPN.globaldao.getStatus();
        if (status == Status.DISCONNECTED) {
            try {
                VPN.globaldao.setAesBaseEncryptKey(VPN.globaldao.getSharedSecretKey().getBytes("UTF-8"));
                VPN.globaldao.setAesBaseDecryptKey(VPN.globaldao.getSharedSecretKey().getBytes("UTF-8"));
                try {
                    String ip = VPN.globaldao.getIp();
                    int port = Integer.parseInt(VPN.globaldao.getPort());
                    VPN.globaldao.writeToLog("Attempting to connect to the server.");
                    VPN.globaldao.setStatus(Status.CLIENT);

                    // TODO: this should be done in a separate thread
                    Socket clientSocket = new Socket(ip, port);
                    BufferedInputStream inputStream = new BufferedInputStream(clientSocket.getInputStream());
                    DataOutputStream outputWriter = new DataOutputStream(clientSocket.getOutputStream());
                    VPN.globaldao.setClientSocket(clientSocket);
                    VPN.globaldao.setInputStream(inputStream);
                    VPN.globaldao.setOutputWriter(outputWriter);
                    // set personal nonce to send
                    VPN.globaldao.setPersonalNonce();
                    outputWriter.write(VPN.globaldao.getPersonalNonce());
                    VPN.globaldao.writeToLog("Sent personal nonce: " + java.nio.ByteBuffer.wrap(VPN.globaldao.getPersonalNonce()).getInt());
                    // receive external nonce to encrypt and send with key
                    byte [] recvNonce = new byte [VPN.globaldao.nonceLength];
                    inputStream.read(recvNonce);
                    VPN.globaldao.setExternalNonce(recvNonce);
                    VPN.globaldao.writeToLog("Received External nonce: " + java.nio.ByteBuffer.wrap(VPN.globaldao.getExternalNonce()).getInt());
                    VPN.globaldao.getDiffieHellman().sendMySecret();
                    VPN.globaldao.writeToLog("Connected to the server.");
                    VPN.globaldao.setStatus(Status.CLIENT_CONNECTED);
                    new Thread(new ReceiveMessage()).start();
                } catch (NumberFormatException nfe) {
                    VPN.globaldao.writeToLog("Please enter a valid port number.");
                } catch(IOException ie) {
                    VPN.globaldao.setStatus(Status.DISCONNECTED);
                    VPN.globaldao.writeToLog("An issue occured while trying to bind to the port: " + ie.getMessage());
                }
            } catch (UnsupportedEncodingException uee) {
                uee.printStackTrace();
            }
            
        } else if (status == Status.CLIENT) {
            VPN.globaldao.writeToLog("You are currently attempting to connect. Disconnect first.");
        } else if (status == Status.SERVER) {
            VPN.globaldao.writeToLog("You are currently listening. Stop listening first.");
        } else if (status == Status.CLIENT_CONNECTED) {
            VPN.globaldao.writeToLog("You are currently connected to server. Disconnect first.");
        } else if (status == Status.SERVER_CONNECTED) {
            VPN.globaldao.writeToLog("You are currently connected to client. Disconnect first.");
        }
    }

}
