package me.pierrontmaxi.memoire.bscl.rest;

import me.pierrontmaxi.memoire.bscl.domain.RawTransaction;
import me.pierrontmaxi.memoire.bscl.domain.contract.Broadcast;
import me.pierrontmaxi.memoire.bscl.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Broadcast> getBroadCastByContent(Authentication authentication, @PathVariable Long contentId) throws Exception {
        String address = authentication.getName();
        return this.contract.getBroadcastsByContentId(BigInteger.valueOf(contentId), address).send();
    }

    @GetMapping("/reject/{broadcastId}")
    public RawTransaction reject(@PathVariable Long broadcastId) throws IOException {
        final Function function = new Function(
                FUNC_REJECTBROADCAST,
                List.of(new Uint256(broadcastId)),
                Collections.emptyList());
        return TransactionService.getRawTransaction(this, broadcastId.toString(), function);
    }

    @GetMapping("/approve/{broadcastId}")
    public RawTransaction approve(@PathVariable Long broadcastId, @RequestParam String address) throws IOException {
        final Function function = new Function(
                FUNC_APPROVEBROADCAST,
                List.of(new Uint256(broadcastId)),
                Collections.emptyList());
        return TransactionService.getRawTransaction(this, address, function);
    }

    @GetMapping("/setBroadcastOwnerProvider/{providerAddress}")
    public RawTransaction setBroadcastOwnerProvider(@PathVariable String providerAddress) throws IOException {
        final Function function = new Function(
                FUNC_LINKBROADCASTOWNERTOSERVICEPROVIDER,
                List.of(new Address(160, providerAddress)),
                Collections.emptyList());
        return TransactionService.getRawTransaction(this, providerAddress, function);
    }
}

