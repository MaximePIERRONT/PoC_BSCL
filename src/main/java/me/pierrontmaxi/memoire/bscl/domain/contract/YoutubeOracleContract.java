package me.pierrontmaxi.memoire.bscl.domain.contract;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.0.
 */
@SuppressWarnings("rawtypes")
public class YoutubeOracleContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610ac2806100a26000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063076a6a301461005c578063a75e83b51461007a578063ad0fba81146100aa578063bd65c11c146100c6578063f4851eab146100e2575b600080fd5b6100646100fe565b604051610071919061054b565b60405180910390f35b610094600480360381019061008f91906105b0565b610124565b6040516100a1919061066d565b60405180910390f35b6100c460048036038101906100bf91906106cd565b6101ce565b005b6100e060048036038101906100db91906106fa565b6102e1565b005b6100fc60048036038101906100f791906105b0565b610402565b005b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6060600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166331d2cb12836040518263ffffffff1660e01b81526004016101819190610749565b600060405180830381865afa15801561019e573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052508101906101c7919061088a565b9050919050565b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461025c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161025390610945565b60405180910390fd5b80600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461036f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610366906109b1565b60405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16635874296783836040518363ffffffff1660e01b81526004016103cc9291906109d1565b600060405180830381600087803b1580156103e657600080fd5b505af11580156103fa573d6000803e3d6000fd5b505050505050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610492576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161048990610a6c565b60405180910390fd5b7f09adcb10e21e5add6bf71901eeac52f6f1255799cb14a24d951fece43b54680a816040516104c19190610749565b60405180910390a150565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b600061051161050c610507846104cc565b6104ec565b6104cc565b9050919050565b6000610523826104f6565b9050919050565b600061053582610518565b9050919050565b6105458161052a565b82525050565b6000602082019050610560600083018461053c565b92915050565b6000604051905090565b600080fd5b600080fd5b6000819050919050565b61058d8161057a565b811461059857600080fd5b50565b6000813590506105aa81610584565b92915050565b6000602082840312156105c6576105c5610570565b5b60006105d48482850161059b565b91505092915050565b600081519050919050565b600082825260208201905092915050565b60005b838110156106175780820151818401526020810190506105fc565b60008484015250505050565b6000601f19601f8301169050919050565b600061063f826105dd565b61064981856105e8565b93506106598185602086016105f9565b61066281610623565b840191505092915050565b600060208201905081810360008301526106878184610634565b905092915050565b600061069a826104cc565b9050919050565b6106aa8161068f565b81146106b557600080fd5b50565b6000813590506106c7816106a1565b92915050565b6000602082840312156106e3576106e2610570565b5b60006106f1848285016106b8565b91505092915050565b6000806040838503121561071157610710610570565b5b600061071f8582860161059b565b92505060206107308582860161059b565b9150509250929050565b6107438161057a565b82525050565b600060208201905061075e600083018461073a565b92915050565b600080fd5b600080fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6107a682610623565b810181811067ffffffffffffffff821117156107c5576107c461076e565b5b80604052505050565b60006107d8610566565b90506107e4828261079d565b919050565b600067ffffffffffffffff8211156108045761080361076e565b5b61080d82610623565b9050602081019050919050565b600061082d610828846107e9565b6107ce565b90508281526020810184848401111561084957610848610769565b5b6108548482856105f9565b509392505050565b600082601f83011261087157610870610764565b5b815161088184826020860161081a565b91505092915050565b6000602082840312156108a05761089f610570565b5b600082015167ffffffffffffffff8111156108be576108bd610575565b5b6108ca8482850161085c565b91505092915050565b7f4f6e6c7920746865206f776e65722063616e20736574207468652062726f616460008201527f6361737420636f6e747261637420616464726573730000000000000000000000602082015250565b600061092f6035836105e8565b915061093a826108d3565b604082019050919050565b6000602082019050818103600083015261095e81610922565b9050919050565b7f4f6e6c7920746865206f776e65722063616e2073657420746865207669657773600082015250565b600061099b6020836105e8565b91506109a682610965565b602082019050919050565b600060208201905081810360008301526109ca8161098e565b9050919050565b60006040820190506109e6600083018561073a565b6109f3602083018461073a565b9392505050565b7f4f6e6c7920746865206f776e65722063616e206d616b6520612072657175657360008201527f7400000000000000000000000000000000000000000000000000000000000000602082015250565b6000610a566021836105e8565b9150610a61826109fa565b604082019050919050565b60006020820190508181036000830152610a8581610a49565b905091905056fea26469706673582212205c3a4c01d7781a9466123a26004ed42c54ae34a618d5dd87d2a9093a99e00b8964736f6c63430008120033";

    public static final String FUNC_BROADCASTCONTRACT = "broadcastContract";

    public static final String FUNC_FETCHVIDEOVIEWS = "fetchVideoViews";

    public static final String FUNC_GETBROADCASTBYID = "getBroadcastById";

    public static final String FUNC_SETBROADCASTCONTRACTADDRESS = "setBroadcastContractAddress";

    public static final String FUNC_SETVIDEOVIEWS = "setVideoViews";

    public static final Event NEWYOUTUBEREQUEST_EVENT = new Event("NewYoutubeRequest",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected YoutubeOracleContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected YoutubeOracleContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected YoutubeOracleContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected YoutubeOracleContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<NewYoutubeRequestEventResponse> getNewYoutubeRequestEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = (List<EventValuesWithLog>) staticExtractEventParametersWithLog(NEWYOUTUBEREQUEST_EVENT, transactionReceipt.getLogs().get(0));
        ArrayList<NewYoutubeRequestEventResponse> responses = new ArrayList<NewYoutubeRequestEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewYoutubeRequestEventResponse typedResponse = new NewYoutubeRequestEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._broadcastId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static NewYoutubeRequestEventResponse getNewYoutubeRequestEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(NEWYOUTUBEREQUEST_EVENT, log);
        NewYoutubeRequestEventResponse typedResponse = new NewYoutubeRequestEventResponse();
        typedResponse.log = log;
        typedResponse._broadcastId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<NewYoutubeRequestEventResponse> newYoutubeRequestEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getNewYoutubeRequestEventFromLog(log));
    }

    public Flowable<NewYoutubeRequestEventResponse> newYoutubeRequestEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWYOUTUBEREQUEST_EVENT));
        return newYoutubeRequestEventFlowable(filter);
    }

    public RemoteFunctionCall<String> broadcastContract() {
        final Function function = new Function(FUNC_BROADCASTCONTRACT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> fetchVideoViews(BigInteger _broadcastId) {
        final Function function = new Function(
                FUNC_FETCHVIDEOVIEWS,
                Arrays.<Type>asList(new Uint256(_broadcastId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getBroadcastById(BigInteger _broadcastId) {
        final Function function = new Function(FUNC_GETBROADCASTBYID,
                Arrays.<Type>asList(new Uint256(_broadcastId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setBroadcastContractAddress(String _broadcastContractAddress) {
        final Function function = new Function(
                FUNC_SETBROADCASTCONTRACTADDRESS,
                Arrays.<Type>asList(new Address(160, _broadcastContractAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setVideoViews(BigInteger _broadcastId, BigInteger _views) {
        final Function function = new Function(
                FUNC_SETVIDEOVIEWS,
                Arrays.<Type>asList(new Uint256(_broadcastId),
                        new Uint256(_views)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static YoutubeOracleContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new YoutubeOracleContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static YoutubeOracleContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new YoutubeOracleContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static YoutubeOracleContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new YoutubeOracleContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static YoutubeOracleContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new YoutubeOracleContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<YoutubeOracleContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(YoutubeOracleContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<YoutubeOracleContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(YoutubeOracleContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<YoutubeOracleContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(YoutubeOracleContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<YoutubeOracleContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(YoutubeOracleContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class NewYoutubeRequestEventResponse extends BaseEventResponse {
        public BigInteger _broadcastId;
    }
}
