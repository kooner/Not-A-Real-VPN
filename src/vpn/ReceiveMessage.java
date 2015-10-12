package vpn;

import java.io.BufferedReader;
import java.io.IOException;

/*
 * This class waits for messages over the socket and writes them to the log
 */
public class ReceiveMessage implements Runnable {
    @Override
    public void run() {
        BufferedReader inputReader = VPN.globaldao.getInputReader();
        String line;
        try {
            while ((line = inputReader.readLine()) != null) {
                // TODO: decryption goes here, write both cipher and plain text to log
                VPN.globaldao.writeToLog("Received: " + line);
            }
        } catch (IOException e) {
            // Connection closed for whatever reason, probably intentionally
            VPN.globaldao.forceCloseSockets();
            VPN.globaldao.writeToLog("Connection closed");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        }
    }
}
