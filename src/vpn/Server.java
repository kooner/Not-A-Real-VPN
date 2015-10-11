package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Server implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String port = VPN.globaldao.getPort();
        String status = VPN.globaldao.getStatus();
        //if status is disconnected, start listening..
        // set status accordingly after finished
        // write to log that we are now listening
        if (status == Status.DISCONNECTED) {
            //implement here
            VPN.globaldao.writeToLog("Successfully listening.");
            VPN.globaldao.setStatus(Status.SERVER);
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
