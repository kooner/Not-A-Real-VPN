package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * This class is called when the "Connect as client" button is pressed
 */
public class Client implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String status = VPN.globaldao.getStatus();
        if (status == Status.DISCONNECTED) {
            VPN.globaldao.writeToLog("Attempting to connect to the server.");
            VPN.globaldao.setStatus(Status.CLIENT);
            new Thread(new ClientInit()).start();
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
