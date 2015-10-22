package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.DatatypeConverter;

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
            DataOutputStream outputWriter = VPN.globaldao.getOutputWriter();

            // Initialize AES encryption cipher
            Cipher aesCipher = VPN.globaldao.getAesSessionCipher(Cipher.ENCRYPT_MODE);

            // Encrypt the message
            byte[] bCiphertext = null;
            try {
                String sPlaintext = VPN.globaldao.getTextToSend();
                String sCiphertext = "";
                byte[] bPlaintext = sPlaintext.getBytes("UTF-8");

                // Encrypt text
                bCiphertext = aesCipher.doFinal(bPlaintext);
                sCiphertext = DatatypeConverter.printHexBinary(bCiphertext);

                // Send ciphertext
                outputWriter.write(bCiphertext);
                VPN.globaldao.writeToLog("Sent (ciphertext): " + sCiphertext);
                VPN.globaldao.writeToLog("Sent (plaintext): " + sPlaintext);
            } catch (IllegalBlockSizeException | BadPaddingException | IOException e1) {
                e1.printStackTrace();
                VPN.globaldao.writeToLog("Failure while encrypting and sending message!");
            }
        }
    }

}
