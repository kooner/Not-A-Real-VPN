package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

/*
 * This class is called when the "Stop Action" button is called
 */
public class StopAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String status = VPN.globaldao.getStatus();
        if (status == Status.DISCONNECTED) {
            VPN.globaldao.writeToLog("You are already disconnected");
        } else if (status == Status.CLIENT) {
            VPN.globaldao.forceCloseSockets();
            VPN.globaldao.writeToLog("Successfully disconnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        } else if (status == Status.SERVER) {
            VPN.globaldao.forceCloseSockets();
            VPN.globaldao.writeToLog("Successfully disconnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        } else if (status == Status.CLIENT_CONNECTED) {
            VPN.globaldao.forceCloseSockets();
            VPN.globaldao.writeToLog("Successfully disonnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        } else if (status == Status.SERVER_CONNECTED) {
            VPN.globaldao.forceCloseSockets();
            VPN.globaldao.writeToLog("Successfully disconnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        }
    }
}
