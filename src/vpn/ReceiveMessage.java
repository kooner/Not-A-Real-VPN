package vpn;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.DatatypeConverter;

/*
 * This class waits for messages over the socket and writes them to the log
 */
public class ReceiveMessage implements Runnable {

    public final int kBufferSize = 16000;

    @Override
    public void run() {
        byte[] recvBuffer = new byte[kBufferSize];
        while (true) {
            BufferedInputStream inputStream = VPN.globaldao.getInputStream();
            try {
                // Block until inputStream receives a message
                int msgLen = inputStream.read(recvBuffer);
                if (msgLen == -1) {
                    continue;
                }

                // Copy bytes into new buffer for isolation
                byte[] bCiphertext = new byte[msgLen];
                System.arraycopy(recvBuffer, 0, bCiphertext, 0, bCiphertext.length);
                String sCiphertext = DatatypeConverter.printHexBinary(bCiphertext);

                Cipher aesCipher = VPN.globaldao.getAesSessionCipher(Cipher.DECRYPT_MODE);

                // Decrypt received message
                byte[] bPlaintext = aesCipher.doFinal(bCiphertext);
                int textHash = ByteBuffer.wrap(Arrays.copyOfRange(bPlaintext, bPlaintext.length - 4, bPlaintext.length)).getInt();
                String sPlaintext = new String(Arrays.copyOfRange(bPlaintext, 0, bPlaintext.length - 4), "UTF-8");
                // Write received message to log
                VPN.globaldao.writeToLog("Received (ciphertext): " + sCiphertext);
                VPN.globaldao.writeToLog("Received (plaintext): " + sPlaintext);
                VPN.globaldao.writeToLog("Received (texthash): " + textHash);
                if (sPlaintext.hashCode() != textHash) {
                	VPN.globaldao.forceEnd();
                    VPN.globaldao.writeToLog("Message integrity lost, disconnecting");
                    VPN.globaldao.setStatus(Status.DISCONNECTED);
                    break;
                }

            } catch (IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
                VPN.globaldao.writeToLog("Failure while decrypting received message!");
            } catch (IOException e) {
                // Connection closed for whatever reason, probably intentionally
                VPN.globaldao.forceEnd();
                VPN.globaldao.writeToLog("Connection closed");
                VPN.globaldao.setStatus(Status.DISCONNECTED);
                break;
            }
        }
    }
}
