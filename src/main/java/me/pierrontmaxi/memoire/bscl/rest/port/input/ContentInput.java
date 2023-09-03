package me.pierrontmaxi.memoire.bscl.rest.port.input;

import java.math.BigInteger;

public class ContentInput {
    BigInteger price;
    String contractFileLink;
    String contractHash;

    public ContentInput() {
    }

    public ContentInput(BigInteger price, String contractFileLink, String contractHash) {
        this.price = price;
        this.contractFileLink = contractFileLink;
        this.contractHash = contractHash;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public String getContractFileLink() {
        return contractFileLink;
    }

    public void setContractFileLink(String contractFileLink) {
        this.contractFileLink = contractFileLink;
    }

    public String getContractHash() {
        return contractHash;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash;
    }
}
