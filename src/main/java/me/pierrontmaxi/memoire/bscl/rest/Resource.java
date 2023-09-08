package me.pierrontmaxi.memoire.bscl.rest;

import jakarta.annotation.PostConstruct;
import me.pierrontmaxi.memoire.bscl.domain.contract.BroadcastContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@Component
public class Resource {

    @Value("${application.blockchain.contracts.broadcast.address}")
    private String broadcastContractAddress;

    @Value("${application.blockchain.private-key}")
    private String privateKey;

    @Value("${application.blockchain.connection-url}")
    private String connectionUrl;

    @Value("${application.blockchain.contracts.broadcast.address}")
    private String contractAddress;

    @Value("${application.blockchain.gas-price}")
    private BigInteger gasPrice;

    @Value("${application.blockchain.gas-limit}")
    private BigInteger gasLimit;

    BroadcastContract contract;

    @PostConstruct
    public void init() {
        if (!(contractAddress.equals("0x") || contractAddress.isEmpty()))
            contract = BroadcastContract.load(broadcastContractAddress, this.getWeb3j(), Credentials.create(privateKey), new StaticGasProvider(gasPrice, gasLimit));
    }

    public Web3j getWeb3j() {
        return Web3j.build(new HttpService(connectionUrl));
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }
}

