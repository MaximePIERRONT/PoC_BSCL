package me.pierrontmaxi.memoire.bscl.domain;

import java.math.BigInteger;

public record RawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                             BigInteger value, String data) {
}
