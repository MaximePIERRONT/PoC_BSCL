package me.pierrontmaxi.memoire.bscl.rest;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.pierrontmaxi.memoire.bscl.domain.Broadcast;
import me.pierrontmaxi.memoire.bscl.domain.RawTransaction;
import me.pierrontmaxi.memoire.bscl.domain.contract.Content;
import me.pierrontmaxi.memoire.bscl.rest.port.input.ContentInput;
import me.pierrontmaxi.memoire.bscl.rest.port.output.ContentOutput;
import me.pierrontmaxi.memoire.bscl.service.JwtService;
import me.pierrontmaxi.memoire.bscl.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.RemoteFunctionCall;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static me.pierrontmaxi.memoire.bscl.domain.contract.BroadcastContract.*;

@RestController
@RequestMapping("/content")
public class ContentResource extends Resource {

    @GetMapping
    public List<Content> list() throws Exception {
        return contract.getContents().send();
    }

    @GetMapping("/{id}")
    public Content get(@PathVariable BigInteger id) throws Exception {
        RemoteFunctionCall<Content> content = contract.getContent(id);
        Content send = content.send();
        System.out.printf("Content %s\n", send.toString());
        return send;
    }

    @PostMapping("/register")
    public RawTransaction register( @RequestHeader (name="Authorization") String token, @RequestBody ContentInput content) throws IOException {
        String address = JwtService.getSubjectFromHeaderInToken(token);
        final Function function = new Function(
                FUNC_REGISTERCONTENT,
                Arrays.asList(new Uint256(content.getPrice()),
                        new Utf8String(content.getContractFileLink()),
                        new Utf8String(content.getContractHash())),
                Collections.emptyList());

        return TransactionService.getRawTransaction(this, address, function);
    }

    @PostMapping("/setUnavailable/{contentId}")
    public RawTransaction setStatus(@PathVariable BigInteger contentId, @RequestParam String userAddress) throws IOException {
        final Function function = new Function(
                FUNC_SETUNAVAILABLECONTENT,
                List.of(new Uint256(contentId), new Utf8String(userAddress)),
                Collections.emptyList());
        return TransactionService.getRawTransaction(this, userAddress, function);
    }

    @PostMapping("/purchase")
    public RawTransaction purchase(@RequestBody Broadcast broadcast) throws IOException {
        me.pierrontmaxi.memoire.bscl.domain.contract.Broadcast broadcastFromContract = broadcast.toBroadcastFromContract();
        Logger.getAnonymousLogger().info(broadcastFromContract.startDate.toString());
        Logger.getAnonymousLogger().info(broadcastFromContract.endDate.toString());
        final Function function = new Function(
                FUNC_PURCHASECONTENT,
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(broadcastFromContract.contentId),
                        new org.web3j.abi.datatypes.generated.Uint256(broadcastFromContract.startDate),
                        new org.web3j.abi.datatypes.generated.Uint256(broadcastFromContract.endDate),
                        new org.web3j.abi.datatypes.Utf8String(broadcastFromContract.videoId)),
                Collections.emptyList());
        return TransactionService.getRawTransaction(this, broadcastFromContract.broadcastOwner, function);
    }

    @GetMapping("/myContents")
    public List getMyContents(Authentication authentication) throws Exception {
        String address = authentication.getName();
        return contract.getMyContents(address).send();
    }
}

