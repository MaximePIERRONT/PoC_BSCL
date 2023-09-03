package me.pierrontmaxi.memoire.bscl.domain.contract;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;

public class Content extends DynamicStruct {
    public String producer;

    public BigInteger price;

    public String contractFileLink;

    public String contractHash;

    public Boolean available;

    public Content(String producer, BigInteger price, String contractFileLink, String contractHash, Boolean available) {
        super(new Address(160, producer),
                new Uint256(price),
                new Utf8String(contractFileLink),
                new Utf8String(contractHash),
                new Bool(available));
        this.producer = producer;
        this.price = price;
        this.contractFileLink = contractFileLink;
        this.contractHash = contractHash;
        this.available = available;
    }

    public Content(Address producer, Uint256 price, Utf8String contractFileLink, Utf8String contractHash, Bool available) {
        super(producer, price, contractFileLink, contractHash, available);
        this.producer = producer.getValue();
        this.price = price.getValue();
        this.contractFileLink = contractFileLink.getValue();
        this.contractHash = contractHash.getValue();
        this.available = available.getValue();
    }
}
