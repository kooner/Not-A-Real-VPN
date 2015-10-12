package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * This class is called when the "Send" button is pressed
 */
public class SendMessage implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = VPN.globaldao.getTextToSend();
        String status = VPN.globaldao.getStatus();
        //if status is connected, encrypt/send message
        // write to log plain/cipher text
        if (status == Status.DISCONNECTED || status == Status.CLIENT || status == Status.SERVER) {
            VPN.globaldao.writeToLog("Can't send message while not connected.");
        } else if (status == Status.CLIENT_CONNECTED) {
            //implement here
            VPN.globaldao.writeToLog("You sent: " + message);
        } else if (status == Status.SERVER_CONNECTED) {
            //implement here
            VPN.globaldao.writeToLog("You sent: " + message);
        }
    }

}
