package me.pierrontmaxi.memoire.bscl.domain;

import java.math.BigInteger;
import java.util.Date;

public class Broadcast {
    private Date startDate;
    private Date endDate;
    private BigInteger contentId;
    private String broadcastOwner;
    private String videoId;

    public Broadcast() {
    }

    public me.pierrontmaxi.memoire.bscl.domain.contract.Broadcast toBroadcastFromContract() {
        return new me.pierrontmaxi.memoire.bscl.domain.contract.Broadcast(
                this.broadcastOwner,
                this.contentId,
                BigInteger.ZERO,
                BigInteger.ZERO,
                this.videoId,
                BigInteger.valueOf(this.startDate.getTime()),
                BigInteger.valueOf(this.endDate.getTime()));
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigInteger getContentId() {
        return contentId;
    }

    public void setContentId(BigInteger contentId) {
        this.contentId = contentId;
    }

    public String getBroadcastOwner() {
        return broadcastOwner;
    }

    public void setBroadcastOwner(String broadcastOwner) {
        this.broadcastOwner = broadcastOwner;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}