package me.pierrontmaxi.memoire.bscl.util;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;

public class Signer {

    public static void main(String[] args) {
        String privateKey = "0xfc09ef338b1452562c9f203e33d337728de24d18b9ee21ddbeceb7fc822a7725"; // Wallet password
        String nonce = "4cda5793-8a51-47b6-84b8-8e214a93d026";
        getSignNonce(privateKey, nonce);
    }

    public static String getSignNonce(String privateKey, String nonce) {
        try {
            // Load Ethereum credentials (i.e., the private key)
            Credentials credentials = Credentials.create(privateKey);

            // Convert the nonce/message to a SHA3 hash
            byte[] messageHash = Hash.sha3(nonce.getBytes(StandardCharsets.UTF_8));

            // Sign the message hash with your credentials (private key)
            Sign.SignatureData signature = Sign.signMessage(messageHash, credentials.getEcKeyPair());

            // Convert signature parts to a single concatenated string
            // This is when signature.getV() returns a byte.
            byte v = signature.getV()[0];
            String signatureString = Numeric.toHexString(signature.getR())
                    + Numeric.toHexStringNoPrefix(signature.getS())
                    + String.format("%02x", (v & 0xFF));


            System.out.println("Signature: " + signatureString);
            return signatureString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

