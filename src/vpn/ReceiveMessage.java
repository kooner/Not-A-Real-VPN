package vpn;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/*
 * This class waits for messages over the socket and writes them to the log
 */
public class ReceiveMessage implements Runnable {

	public final int kBufferSize = 16000;

    @Override
    public void run() {
        byte [] recvBuffer = new byte [kBufferSize];
		while (true) {
	        BufferedInputStream inputStream = VPN.globaldao.getInputStream();
	        try {
				// Block until inputStream receives a message
				int msgLen = inputStream.read(recvBuffer);
				if (msgLen == -1)
					continue;

				// Copy bytes into new buffer for isolation
				byte [] bCiphertext = new byte [msgLen];
				System.arraycopy(recvBuffer, 0, bCiphertext, 0, bCiphertext.length);
				String sCiphertext = new String(bCiphertext, "UTF-8");
				DiffieHellman diffieHellman = VPN.globaldao.getDiffieHellman();
				if (diffieHellman.isSessionInitialized()) {
				    try {
	                    // Initialize aesCipher
	                    Cipher aesCipher;
	                    aesCipher = Cipher.getInstance("AES");
	                    aesCipher.init(Cipher.DECRYPT_MODE, diffieHellman.getAesSessionDecryptKey());

	                    // Decrypt received message
	                    byte [] bPlaintext = aesCipher.doFinal(bCiphertext);
	                    String sPlaintext = new String(bPlaintext, "UTF-8");

	                    // Write received message to log
	                    VPN.globaldao.writeToLog("Received (ciphertext): " + sCiphertext);
	                    VPN.globaldao.writeToLog("Received (plaintext): " + sPlaintext);
	                    VPN.globaldao.writeToLog("");
	                } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
	                    e.printStackTrace();
	                    VPN.globaldao.writeToLog("Failure while decrypting received message!");
	                }
				} else {
				    try {
	                    Cipher aesCipher;
	                    aesCipher = Cipher.getInstance("AES");
	                    aesCipher.init(Cipher.DECRYPT_MODE, VPN.globaldao.getAesDecryptKey());
	                    byte [] bPlaintext = aesCipher.doFinal(bCiphertext);
	                    // split the deciphered text into the nonce (first 16 bytes) and key (the rest of the bytes)
	                    byte [] bKeyPlainText = new byte [bPlaintext.length - 16];
	                    byte [] bPlainNonce = new byte [4];
	                    System.arraycopy(bPlaintext, 16, bKeyPlainText, 0, bKeyPlainText.length);
	                    System.arraycopy(bPlaintext, 12, bPlainNonce, 0, bPlainNonce.length);
	                    VPN.globaldao.writeToLog("Decrypted Nonce: " + java.nio.ByteBuffer.wrap(bPlainNonce).getInt());
	                    if(!Arrays.equals(VPN.globaldao.getPersonalNonce(),bPlainNonce)){
	                    	throw new Exception("NonceMismatchException");
	                    }
	                    diffieHellman.setPeerSecretBytes(bKeyPlainText);
	                    VPN.globaldao.writeToLog("The shared secret key is: " + diffieHellman.getSharedSecretKey().toString());
	                    diffieHellman.sendMySecret();
	                } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
	                    e.printStackTrace();
	                    VPN.globaldao.writeToLog("Failure while decrypting received message!");
	                } catch (Exception e) {
						e.printStackTrace();
						VPN.globaldao.writeToLog("Nonce mismatch");
					}
				}
				
	        // Connection closed for whatever reason, probably intentionally
	        } catch (IOException e) {
	            VPN.globaldao.forceCloseSockets();
	            VPN.globaldao.writeToLog("Connection closed");
	            VPN.globaldao.setStatus(Status.DISCONNECTED);
	        }
		}
    }
}
