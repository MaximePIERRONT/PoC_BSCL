package me.pierrontmaxi.memoire.bscl.rest.port.output;

import me.pierrontmaxi.memoire.bscl.domain.contract.Content;

public class ContentOutput {
    private String contractFileLink;
    private String contractHash;
    private Long price;
    private String producer;

    private boolean available;

    public ContentOutput() {
    }

    public ContentOutput(Content content) {
        this.contractFileLink = content.contractFileLink;
        this.contractHash = content.contractHash;
        this.price = content.price.longValue();
        this.producer = content.producer;
        this.available = content.available;
    }

    public String getContractFileLink() {
        return contractFileLink;
    }

    public String getContractHash() {
        return contractHash;
    }

    public Long getPrice() {
        return price;
    }

    public String getOwner() {
        return producer;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
