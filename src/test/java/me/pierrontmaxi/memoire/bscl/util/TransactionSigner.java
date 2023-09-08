package me.pierrontmaxi.memoire.bscl.util;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.io.InputStream;
import java.util.Scanner;

public class TransactionSigner {
    public static void main(String[] args) {
        String privateKey = "0xfc09ef338b1452562c9f203e33d337728de24d18b9ee21ddbeceb7fc822a7725";
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
