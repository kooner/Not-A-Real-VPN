package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/*
 * This class is called when the "Listen as server" button is pressed
 */
public class Server implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String status = VPN.globaldao.getStatus();
        //if status is disconnected, start listening..
        // set status accordingly after finished
        // write to log that we are now listening
        if (status == Status.DISCONNECTED) {
            try {
                int port = Integer.parseInt(VPN.globaldao.getPort());
                ServerSocket serverSocket = new ServerSocket(port);
                VPN.globaldao.setServerSocket(serverSocket);
                VPN.globaldao.writeToLog("Successfully listening on port " + port + ".");
                VPN.globaldao.setStatus(Status.SERVER);
                new Thread(new ServerListener()).start();
            } catch(NumberFormatException nfe) {
                VPN.globaldao.writeToLog("Please enter a valid port number.");
            } catch(IOException ie) {
                VPN.globaldao.writeToLog("An issue occured while trying to bind to the port: " + ie.getMessage());
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
