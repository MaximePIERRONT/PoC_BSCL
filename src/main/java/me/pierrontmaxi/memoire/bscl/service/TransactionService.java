package me.pierrontmaxi.memoire.bscl.service;

import jakarta.annotation.PostConstruct;
import me.pierrontmaxi.memoire.bscl.domain.RawTransaction;
import me.pierrontmaxi.memoire.bscl.rest.Resource;
import me.pierrontmaxi.memoire.bscl.rest.port.TransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

@Service
public class TransactionService {
    @Value("${application.blockchain.connection-url}")
    String connectionUrl;

    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String FAILURE_STATUS = "FAILURE";
    private static final String ERROR_STATUS = "ERROR";
    private static final String TRANSACTION_FAIL_MSG = "Transaction failed without specific error.";
    private static final String RECEIPT_NOT_FOUND_MSG = "Transaction receipt not found.";

    private Web3j web3j;

    @PostConstruct
    public void init() {
        web3j = Web3j.build(new HttpService(connectionUrl));
    }

    public TransactionResponse processTransaction(String rawTransaction) {
        try {
            String transactionHash = sendRawTransaction(rawTransaction);
            return checkTransactionStatus(transactionHash);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    private String sendRawTransaction(String rawTransaction) throws Exception {
        Request<?, EthSendTransaction> ethSendTransactionRequest = web3j.ethSendRawTransaction(rawTransaction);
        EthSendTransaction ethSendTransaction = ethSendTransactionRequest.send();

        if (ethSendTransaction.hasError()) {
            throw new Exception(ethSendTransaction.getError().getMessage());
        }
        return ethSendTransaction.getTransactionHash();
    }

    private TransactionResponse checkTransactionStatus(String transactionHash) throws Exception {
        EthGetTransactionReceipt receiptResponse = web3j.ethGetTransactionReceipt(transactionHash).send();
        if (receiptResponse.getTransactionReceipt().isPresent()) {
            TransactionReceipt receipt = receiptResponse.getTransactionReceipt().get();
            System.out.println("Transaction logs: " + receipt.getLogs());
            TransactionResponse response = new TransactionResponse();
            response.setTransactionHash(transactionHash);

            response.setBlockHash(receipt.getBlockHash());
            response.setBlockNumber(receipt.getBlockNumber());

            if ("0x1".equals(receipt.getStatus())) {
                response.setStatus(SUCCESS_STATUS);
                //log transaction receipt
                System.out.println(Arrays.toString(receipt.getLogs().toArray()));
            } else {
                response.setStatus(FAILURE_STATUS);
                response.setErrorMessage(TRANSACTION_FAIL_MSG);
            }
            return response;
        } else {
            throw new Exception(RECEIPT_NOT_FOUND_MSG);
        }
    }

    private TransactionResponse createErrorResponse(String errorMessage) {
        TransactionResponse response = new TransactionResponse();
        response.setStatus(ERROR_STATUS);
        response.setErrorMessage(errorMessage);
        return response;
    }

    public static RawTransaction getRawTransaction(Resource resource, String authorAddress, Function function) throws IOException {
        Web3j web3j = resource.getWeb3j();
        BigInteger nonce = web3j.ethGetTransactionCount(authorAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();
        return new RawTransaction(
                nonce,
                resource.getGasPrice(),
                resource.getGasLimit(),
                resource.getContractAddress(),
                BigInteger.valueOf(0),
                FunctionEncoder.encode(function)
        );
    }
}
