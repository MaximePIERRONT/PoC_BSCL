package me.pierrontmaxi.memoire.bscl;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;

public class Signer {

    public static void main(String[] args) {
        String privateKey = "0xd32b6d05a505b731d714bd184aa0699cf07d2e3f0e91fdcb63de4faa7bc0062c"; // Wallet password

        try {
            // Load Ethereum credentials (i.e., the private key)
            Credentials credentials = Credentials.create(privateKey);

            // Nonce or message you want to sign
            String nonce = "5b771ea9-5ecd-433f-82ad-b34d98504b5f";

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

