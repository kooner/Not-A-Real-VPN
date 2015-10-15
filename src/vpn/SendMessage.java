package vpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
			Cipher aesCipher = null;
			try {
				aesCipher = Cipher.getInstance("AES");
				aesCipher.init(Cipher.ENCRYPT_MODE, VPN.globaldao.getAesEncryptKey());
			} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e1) {
				e1.printStackTrace();
                VPN.globaldao.writeToLog("Error occurred while setting up encryption cipher!");
                return;
			}

            // Encrypt the message
			byte [] bCiphertext = null;
            try {
				String sPlaintext = VPN.globaldao.getTextToSend();
				String sCiphertext = "";
    			byte [] bPlaintext = sPlaintext.getBytes("UTF-8");

				// Check if padding is needed, and add it if needed
				if (bPlaintext.length % VPN.globaldao.kKeyLength != 0) {
					// Align new buffer to AES block size (16 bytes)
					byte [] newMessageBytes = new byte [((bPlaintext.length / VPN.globaldao.kKeyLength) + 1) * (VPN.globaldao.kKeyLength)];

					// Copy message and overwrite original pointer
					System.arraycopy(bPlaintext, 0, newMessageBytes, 0, bPlaintext.length);
					bPlaintext = newMessageBytes;
				}

				// Encrypt text
				bCiphertext = aesCipher.doFinal(bPlaintext);
				sCiphertext = new String(bCiphertext, "UTF-8");

                // Send ciphertext
				outputWriter.write(bCiphertext);
				VPN.globaldao.writeToLog("Sent (Encrypted): " + sCiphertext);
				VPN.globaldao.writeToLog("Sent (Plaintext): " + sPlaintext);
				VPN.globaldao.writeToLog("");
			} catch (IllegalBlockSizeException | BadPaddingException | IOException e1) {
				e1.printStackTrace();
				VPN.globaldao.writeToLog("Failure while encrypting and sending message!");
			}
        }
    }

}
