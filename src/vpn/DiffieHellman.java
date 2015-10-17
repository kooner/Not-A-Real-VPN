package vpn;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DiffieHellman {

    public static int KEY_NUMBER_OF_BITS = 1000;
    
    //G and P generated from "BigInteger.probablePrime(1000, new Random()).toString()"
    public static final BigInteger DIFFIE_HELLMAN_G = new BigInteger("8244347829767090182982986107445668763920853865857838314436132756310365043180032183784702993681073067080586389396062204774774400231727672072670924993689394359322346734290366945590350686462887861663949182886171308269680170009207684994191075205457316258655898266705278256490224412785129046235906749744091");
    public static final BigInteger DIFFIE_HELLMAN_P = new BigInteger("8273715073326836536542244915631978951692916231965370664098526053258328932196161607171099451127104524211591857193299833965889889095423610426738610961828579755957593673628971503762626437361929199329081018793049394015165583948958545740881644800638636681715559797262026755601225701449680052398923658989157");

    private BigInteger mySecretExponent;
    private BigInteger peerSecret;
    private Boolean isSecretSent = false;
    private BigInteger sharedSecretKey;
    public SecretKeySpec aesSessionDecryptKey;
    public SecretKeySpec aesSessionEncryptKey;

    DiffieHellman() {
        this.mySecretExponent = DiffieHellman.generateMySecretExponent();
    }

    private static BigInteger generateMySecretExponent() {
        BigInteger result = new BigInteger(KEY_NUMBER_OF_BITS, new Random());
        return result;
    }
    
    private byte[] getMySecretBytes() {
        BigInteger mySecret = DIFFIE_HELLMAN_G.modPow(this.mySecretExponent, DIFFIE_HELLMAN_P);
        return mySecret.toByteArray();
    }

    public void setPeerSecretBytes(byte[] peerSecretBytes) {
        this.setPeerSecret(new BigInteger(peerSecretBytes));
    }

    public void setPeerSecret(BigInteger peerSecret) {
        this.peerSecret = peerSecret;
        this.sharedSecretKey = this.computeSharedSecretKey();
        int keyLength = VPN.globaldao.kKeyLength;
        byte [] sharedSecretKeyBytes = Arrays.copyOfRange(this.sharedSecretKey.toByteArray(), 0, keyLength);
        this.aesSessionDecryptKey = new SecretKeySpec(sharedSecretKeyBytes, "AES");
        this.aesSessionEncryptKey = new SecretKeySpec(sharedSecretKeyBytes, "AES");
    }
    
    public BigInteger getSharedSecretKey() {
        return sharedSecretKey;
    }

    public Boolean isSessionInitialized() {
        return this.aesSessionEncryptKey != null && this.aesSessionDecryptKey != null;
    }
    
    public void endSession() {
        this.aesSessionEncryptKey = null;
        this.aesSessionDecryptKey = null;
        this.isSecretSent = false;
    }

    private BigInteger computeSharedSecretKey() {
        return this.peerSecret.modPow(this.mySecretExponent, DIFFIE_HELLMAN_P);
    }

    public void sendMySecret() {
        if (isSecretSent) {
            return;
        }
        DataOutputStream outputWriter = VPN.globaldao.getOutputWriter();
        Cipher aesCipher = null;
        try {
            aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, VPN.globaldao.getAesEncryptKey());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            VPN.globaldao.writeToLog("Error occurred while setting up encryption cipher!");
            return;
        }
        try {
            byte[] bPlaintext = getMySecretBytes();
            byte[] bCiphertext = aesCipher.doFinal(bPlaintext);
            outputWriter.write(bCiphertext);
            isSecretSent = true;
        } catch (IllegalBlockSizeException | BadPaddingException | IOException e) {
            e.printStackTrace();
            VPN.globaldao.writeToLog("Failure while establishing mutual authentication");
        }
    }
    
    public SecretKeySpec getAesSessionEncryptKey() {
        return this.aesSessionEncryptKey;
    }

    public SecretKeySpec getAesSessionDecryptKey() {
        return this.aesSessionDecryptKey;
    }
}
