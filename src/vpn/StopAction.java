package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * This class is called when the "Stop Action" button is called
 */
public class StopAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String status = VPN.globaldao.getStatus();
        if (status == Status.DISCONNECTED) {
            VPN.globaldao.writeToLog("You are already disconnected");
        } else {
            VPN.globaldao.forceEnd();
            VPN.globaldao.writeToLog("Successfully disconnected");
            VPN.globaldao.setStatus(Status.DISCONNECTED);
        }
    }
}
