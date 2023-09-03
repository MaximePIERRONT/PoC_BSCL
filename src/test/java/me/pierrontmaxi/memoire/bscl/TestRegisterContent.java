package me.pierrontmaxi.memoire.bscl;

import me.pierrontmaxi.memoire.bscl.domain.contract.BroadcastContract;
import me.pierrontmaxi.memoire.bscl.domain.contract.Content;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class TestRegisterContent {

    public static void main(String[] args) throws Exception {
        BroadcastContract contract = BroadcastContract.load(
                "0xf1267e497651736b7a529aa4d03ace925eebbf85",
                Web3j.build(new HttpService("http://localhost:7545")),
                Credentials.create("0x90e007de2f373e9a4f70b8aab2a4acfb347ca80e2e23543778a4c18578a39dbc"),
                new StaticGasProvider(BigInteger.valueOf(2000000000), BigInteger.valueOf(6721975))
        );
        Content send = contract.getContent(BigInteger.valueOf(0)).send();
        System.out.printf("Content %s\n", send.contractFileLink);
    }
}
