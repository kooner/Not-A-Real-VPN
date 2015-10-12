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
                VPN.globaldao.writeToLog("Received: " + line);
            }
        } catch (IOException e) {
            // TODO: This might output everytime we press "Stop Action". Delete it if it does.
            VPN.globaldao.writeToLog("Error occurred while receiving messages: " + e.getMessage());
        }
    }
}
