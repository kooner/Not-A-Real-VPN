package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
                String ip = VPN.globaldao.getIp();
                int port = Integer.parseInt(VPN.globaldao.getPort());
                VPN.globaldao.writeToLog("Attempting to connect to the server.");
                VPN.globaldao.setStatus(Status.CLIENT);

                // TODO: this should be done in a separate thread
                Socket clientSocket = new Socket(ip, port);
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream outputWriter = new DataOutputStream(clientSocket.getOutputStream());
                VPN.globaldao.setClientSocket(clientSocket);
                VPN.globaldao.setInputReader(inputReader);
                VPN.globaldao.setOutputWriter(outputWriter);
                VPN.globaldao.writeToLog("Connected to the server.");
                VPN.globaldao.setStatus(Status.CLIENT_CONNECTED);
                new Thread(new ReceiveMessage()).start();
            } catch (NumberFormatException nfe) {
                VPN.globaldao.writeToLog("Please enter a valid port number.");
            } catch(IOException ie) {
                VPN.globaldao.setStatus(Status.DISCONNECTED);
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
