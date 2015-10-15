package vpn;

import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import vpn.ui.AboutPanel;
import vpn.ui.ConnectionPanel;
import vpn.ui.LogPanel;
import vpn.ui.SendPanel;

public class VPN {
    public static GlobalDao globaldao = new GlobalDao();
    
    public static final int x = 1024;
    public static final int y = 768;
    
    public static final String PLACEHOLDERKEY = "1234432112344321";

    private final JFrame frame = new JFrame("CPEN 442 [NOT A REAL] VPN");

    public static void main(String[] args) {
	    (new VPN()).run();
    }
    
    private void run() {
        ConnectionPanel cp = new ConnectionPanel();
        LogPanel lp = new LogPanel();
        SendPanel sp = new SendPanel();
        
        globaldao.setConnectionPanel(cp);
        globaldao.setLogPanel(lp);
        globaldao.setSendPanel(sp);

        // Set AES keys to constant PLACEHOLDERKEY
        // WARNING: Final delivered product should NOT use AES keys set here
        try {
			globaldao.setAesBaseEncryptKey(PLACEHOLDERKEY.getBytes("UTF-8"));
			globaldao.setAesBaseDecryptKey(PLACEHOLDERKEY.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

        JPanel mainPanel = new JPanel();
        mainPanel.add(cp);
        mainPanel.add(lp);
        mainPanel.add(sp);
        mainPanel.add(new AboutPanel());
        
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(x, y);
        frame.setVisible(true);
    }
}
