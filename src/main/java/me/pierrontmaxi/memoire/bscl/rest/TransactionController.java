package me.pierrontmaxi.memoire.bscl.rest;

import me.pierrontmaxi.memoire.bscl.rest.port.output.TransactionResponse;
import me.pierrontmaxi.memoire.bscl.service.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionResponse send(@RequestBody String rawTransaction) {
        return transactionService.processTransaction(rawTransaction);
    }
}
