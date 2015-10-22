package vpn;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/*
 * This class waits for a client to connect, then starts a new Thread which reads
 * input from the client and writes it to the log.
 */
public class ServerListener implements Runnable {
    @Override
    public void run() {
        try {
            // accept() is a blocking call
            Socket clientSocket = VPN.globaldao.getServerSocket().accept();
            BufferedInputStream inputStream = new BufferedInputStream(clientSocket.getInputStream());
            DataOutputStream outputWriter = new DataOutputStream(clientSocket.getOutputStream());
            VPN.globaldao.setClientSocket(clientSocket);
            VPN.globaldao.setInputStream(inputStream);
            VPN.globaldao.setOutputWriter(outputWriter);

            // set and send personal nonce
            VPN.globaldao.setPersonalNonce();
            outputWriter.write(VPN.globaldao.getPersonalNonce());
            VPN.globaldao
                    .writeToLog("Sent personal nonce: " + ByteBuffer.wrap(VPN.globaldao.getPersonalNonce()).getInt());

            // receive external nonce to encrypt and send with key
            byte[] recvNonce = new byte[VPN.globaldao.nonceLength];
            inputStream.read(recvNonce);
            VPN.globaldao.setExternalNonce(recvNonce);
            VPN.globaldao.writeToLog(
                    "Received External nonce: " + ByteBuffer.wrap(VPN.globaldao.getExternalNonce()).getInt());
            VPN.globaldao.getDiffieHellman().sendMySecret();

            byte[] recvBuffer = new byte[16000];
            int msgLen = inputStream.read(recvBuffer);
            byte[] bCiphertext = new byte[msgLen];
            System.arraycopy(recvBuffer, 0, bCiphertext, 0, bCiphertext.length);
            DiffieHellman diffieHellman = VPN.globaldao.getDiffieHellman();

            Cipher aesCipher = VPN.globaldao.getAesBaseCipher(Cipher.DECRYPT_MODE);
            byte[] bPlaintext = aesCipher.doFinal(bCiphertext);

            // split the deciphered text into the nonce (first 16 bytes) and key
            // (the rest of the bytes)
            byte[] bKeyPlainText = new byte[bPlaintext.length - 16];
            byte[] bPlainNonce = new byte[4];
            System.arraycopy(bPlaintext, 12, bPlainNonce, 0, bPlainNonce.length);
            System.arraycopy(bPlaintext, 16, bKeyPlainText, 0, bKeyPlainText.length);
            VPN.globaldao.writeToLog("Decrypted Nonce: " + ByteBuffer.wrap(bPlainNonce).getInt());
            if (!Arrays.equals(VPN.globaldao.getPersonalNonce(), bPlainNonce)) {
                VPN.globaldao.forceEnd();
                VPN.globaldao.writeToLog("Nonce mismatched. Disconnecting.");
                VPN.globaldao.setStatus(Status.DISCONNECTED);
                return;
            }
            diffieHellman.setPeerSecretBytes(bKeyPlainText);
            VPN.globaldao.writeToLog("The shared session key is: " + diffieHellman.getSharedSecretKey());

            VPN.globaldao.writeToLog("Connected to client");
            VPN.globaldao.setStatus(Status.SERVER_CONNECTED);
            new Thread(new ReceiveMessage()).start();
        } catch (SocketException se) {
            // server socket closed via StopAction
        } catch (BadPaddingException | IllegalBlockSizeException | IOException e) {
            VPN.globaldao.writeToLog("Error occurred while accepting client socket: " + e.getMessage());
        }
    }
}
