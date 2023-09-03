package me.pierrontmaxi.memoire.bscl;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.io.InputStream;
import java.util.Scanner;

public class TransactionSigner {
    public static void main(String[] args) {
        String privateKey = "0xd32b6d05a505b731d714bd184aa0699cf07d2e3f0e91fdcb63de4faa7bc0062c";
        Credentials credentials = Credentials.create(privateKey);
        JSONObject rawTransactionJson = readJsonFromFileRawTransaction();
        org.web3j.crypto.RawTransaction rawTransaction = org.web3j.crypto.RawTransaction.createTransaction(
                rawTransactionJson.getBigInteger("nonce"),
                rawTransactionJson.getBigInteger("gasPrice"),
                rawTransactionJson.getBigInteger("gasLimit"),
                rawTransactionJson.getString("to"),
                rawTransactionJson.getBigInteger("value"),
                rawTransactionJson.getString("data")
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        System.out.println("Signed transaction: " + hexValue);
    }

    private static JSONObject readJsonFromFileRawTransaction() {
        InputStream is = TransactionSigner.class.getResourceAsStream("/rawTransaction.json");
        if (is == null) {
            throw new IllegalArgumentException("File not found: " + "rawTransaction.json");
        }

        Scanner scanner = new Scanner(is).useDelimiter("\\A");
        String jsonString = scanner.hasNext() ? scanner.next() : "";
        return new JSONObject(jsonString);
    }
}
