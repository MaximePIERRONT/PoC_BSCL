package me.pierrontmaxi.memoire.bscl.service;

import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthenticationService {

    private final Map<String, String> nonces = new ConcurrentHashMap<>(); // Pour la simplicité, on utilise une map. En production, envisagez une base de données.

    public String generateNonce(String address) {
        String nonce = UUID.randomUUID().toString();
        nonces.put(address, nonce);
        return nonce;
    }

    public boolean isValidSignature(String address, String receivedSignature) {
        String nonceOriginal = this.nonces.get(address);
        try {
            byte[] messageHash = Hash.sha3(nonceOriginal.getBytes(StandardCharsets.UTF_8));

            if (receivedSignature.startsWith("0x")) {
                receivedSignature = receivedSignature.substring(2);
            }

            byte[] signatureBytes = Numeric.hexStringToByteArray(receivedSignature);
            byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
            byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);
            byte v = signatureBytes[64];

            Sign.SignatureData sigData = new Sign.SignatureData(v, r, s);

            BigInteger publicKey = Sign.signedMessageToKey(messageHash, sigData);

            String derivedAddress  = "0x" + org.web3j.crypto.Keys.getAddress(publicKey);
            return address.toLowerCase().equals(derivedAddress);
        } catch (Exception e) {
            return false;
        }
    }
}
