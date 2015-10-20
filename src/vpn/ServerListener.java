package vpn;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
