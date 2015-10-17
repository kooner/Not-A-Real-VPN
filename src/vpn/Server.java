package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;

/*
 * This class is called when the "Listen as server" button is pressed
 */
public class Server implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String status = VPN.globaldao.getStatus();
        if (status == Status.DISCONNECTED) {
            try {
                VPN.globaldao.setAesBaseEncryptKey(VPN.globaldao.getSharedSecretKey().getBytes("UTF-8"));
                VPN.globaldao.setAesBaseDecryptKey(VPN.globaldao.getSharedSecretKey().getBytes("UTF-8"));
                try {
                    int port = Integer.parseInt(VPN.globaldao.getPort());
                    ServerSocket serverSocket = new ServerSocket(port);
                    DiffieHellman diffieHellman = new DiffieHellman();
                    VPN.globaldao.setServerSocket(serverSocket);
                    VPN.globaldao.writeToLog("Successfully listening on port " + port + ".");
                    VPN.globaldao.setStatus(Status.SERVER);
                    VPN.globaldao.setDiffieHellman(diffieHellman);
                    new Thread(new ServerListener()).start();
                } catch(NumberFormatException nfe) {
                    VPN.globaldao.writeToLog("Please enter a valid port number.");
                } catch(IOException ie) {
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
