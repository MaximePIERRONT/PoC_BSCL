package me.pierrontmaxi.memoire.bscl.domain.contract;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

import java.math.BigInteger;

public class Broadcast extends DynamicStruct {
    public String broadcastOwner;

    public BigInteger contentId;

    public BigInteger status;

    public BigInteger viewers;

    public String videoId;

    public BigInteger startDate;

    public BigInteger endDate;

    public Broadcast(String broadcastOwner, BigInteger contentId, BigInteger status, BigInteger viewers, String videoId, BigInteger startDate, BigInteger endDate) {
        super(new Address(160, broadcastOwner),
                new Uint256(contentId),
                new Uint8(status),
                new Uint256(viewers),
                new Utf8String(videoId),
                new Uint256(startDate),
                new Uint256(endDate));
        this.broadcastOwner = broadcastOwner;
        this.contentId = contentId;
        this.status = status;
        this.viewers = viewers;
        this.videoId = videoId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Broadcast(Address broadcastOwner, Uint256 contentId, Uint8 status, Uint256 viewers, Utf8String videoId, Uint256 startDate, Uint256 endDate) {
        super(broadcastOwner, contentId, status, viewers, videoId, startDate, endDate);
        this.broadcastOwner = broadcastOwner.getValue();
        this.contentId = contentId.getValue();
        this.status = status.getValue();
        this.viewers = viewers.getValue();
        this.videoId = videoId.getValue();
        this.startDate = startDate.getValue();
        this.endDate = endDate.getValue();
    }
}
