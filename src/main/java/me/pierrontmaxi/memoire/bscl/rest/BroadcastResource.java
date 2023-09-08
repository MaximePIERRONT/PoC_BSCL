package me.pierrontmaxi.memoire.bscl.rest;

import me.pierrontmaxi.memoire.bscl.domain.RawTransaction;
import me.pierrontmaxi.memoire.bscl.domain.contract.Broadcast;
import me.pierrontmaxi.memoire.bscl.rest.port.input.ContentInput;
import me.pierrontmaxi.memoire.bscl.service.JwtService;
import me.pierrontmaxi.memoire.bscl.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static me.pierrontmaxi.memoire.bscl.domain.contract.BroadcastContract.*;

@RestController
@RequestMapping("/broadcast")
public class BroadcastResource extends Resource {

    @GetMapping("/byContent/{contentId}")
    public List<Broadcast> getBroadCastByContent(@RequestHeader(name = "Authorization") String token, @PathVariable Long contentId) throws Exception {
        String address = JwtService.getSubjectFromHeaderInToken(token);
        return this.contract.getBroadcastsByContentId(BigInteger.valueOf(contentId), address).send();
    }

    @PostMapping("/reject/{broadcastId}")
    public RawTransaction reject(@PathVariable Long broadcastId, @RequestHeader(name = "Authorization") String token) throws IOException {
        String address = JwtService.getSubjectFromHeaderInToken(token);
        final Function function = new Function(
                FUNC_REJECTBROADCAST,
                List.of(new Uint256(broadcastId)),
                Collections.emptyList());
        return TransactionService.getRawTransaction(this, address, function);
    }

    @PostMapping("/approve/{broadcastId}")
    public RawTransaction approve(@PathVariable Long broadcastId, @RequestHeader(name = "Authorization") String token) throws IOException {
        String address = JwtService.getSubjectFromHeaderInToken(token);
        final Function function = new Function(
                FUNC_APPROVEBROADCAST,
                List.of(new Uint256(broadcastId)),
                Collections.emptyList());
        return TransactionService.getRawTransaction(this, address, function);
    }

    @PostMapping("/setBroadcastOwnerProvider/{providerAddress}")
    public RawTransaction setBroadcastOwnerProvider(@PathVariable String providerAddress, @RequestHeader(name = "Authorization") String token) throws IOException {
        String address = JwtService.getSubjectFromHeaderInToken(token);
        final Function function = new Function(
                FUNC_LINKBROADCASTOWNERTOSERVICEPROVIDER,
                List.of(new Address(160, providerAddress)),
                Collections.emptyList());
        return TransactionService.getRawTransaction(this, address, function);
    }
}

