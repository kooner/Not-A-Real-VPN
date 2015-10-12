package vpn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/*
 * This class waits for a client to connect, then starts a new Thread which reads
 * input from the client and writes it to the log.
 */
public class ServerListener implements Runnable {
    @Override
    public void run() {
        try {
            // accept() is a blocking call
            Socket clientSocket = VPN.globaldao.getServerSocket().accept();
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outputWriter = new DataOutputStream(clientSocket.getOutputStream());
            VPN.globaldao.setClientSocket(clientSocket);
            VPN.globaldao.setInputReader(inputReader);
            VPN.globaldao.setOutputWriter(outputWriter);
            VPN.globaldao.writeToLog("Connected to client");
            VPN.globaldao.setStatus(Status.SERVER_CONNECTED);
            new Thread(new ReceiveMessage()).start();
        } catch(SocketException se) {
            // server socket closed via StopAction
        } catch (IOException ie) {
            VPN.globaldao.writeToLog("Error occurred while accepting client socket: " + ie.getMessage());
        }
    }
}
