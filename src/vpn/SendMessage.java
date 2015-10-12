package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

/*
 * This class is called when the "Send" button is pressed
 */
public class SendMessage implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String status = VPN.globaldao.getStatus();
        if (status == Status.DISCONNECTED || status == Status.CLIENT || status == Status.SERVER) {
            VPN.globaldao.writeToLog("Can't send message while not connected.");
        } else if (status == Status.CLIENT_CONNECTED || status == Status.SERVER_CONNECTED) {
            String message = VPN.globaldao.getTextToSend();
            // TODO: encryption goes here, write both cipher text and plain text to log
            DataOutputStream outputWriter = VPN.globaldao.getOutputWriter();
            try {
                outputWriter.writeBytes(message + "\n");
                VPN.globaldao.writeToLog("Sent: " + message);
            } catch (IOException ie) {
                VPN.globaldao.writeToLog("Error occurred while sending messages: " + ie.getMessage());
            }
        }
    }

}
