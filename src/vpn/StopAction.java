package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String status = VPN.globaldao.getStatus();
        //if status is client or server, stop it
        // set status accordingly after finished
        // write to log
        if (status == Status.DISCONNECTED) {
            VPN.globaldao.writeToLog("You are already disconnected");
        } else if (status == Status.CLIENT) {
            //implement here
            VPN.globaldao.writeToLog("Successfully disconnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        } else if (status == Status.SERVER) {
            //implement here
            VPN.globaldao.writeToLog("Successfully disonnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        } else if (status == Status.CLIENT_CONNECTED) {
            VPN.globaldao.writeToLog("Successfully disonnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        } else if (status == Status.SERVER_CONNECTED) {
            VPN.globaldao.writeToLog("Successfully disonnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        }
    }
}
