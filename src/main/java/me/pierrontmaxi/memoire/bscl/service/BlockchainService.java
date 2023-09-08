package me.pierrontmaxi.memoire.bscl.service;

import jakarta.annotation.PostConstruct;
import me.pierrontmaxi.memoire.bscl.domain.contract.BroadcastContract;
import me.pierrontmaxi.memoire.bscl.domain.contract.Content;
import me.pierrontmaxi.memoire.bscl.domain.contract.YoutubeOracleContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class BlockchainService {

    @Value("${application.blockchain.connection-url}")
    String connectionUrl;

    @Value("${application.blockchain.contracts.broadcast.address}")
    String broadcastContractAddress;

    @Value("${application.blockchain.contracts.oracle.address}")
    String oracleContractAddress;

    @Value("${application.blockchain.private-key}")
    String privateKey;

    @Value("${application.blockchain.gas-price}")
    BigInteger gasPrice;

    @Value("${application.blockchain.gas-limit}")
    BigInteger gasLimit;

    private Web3j web3j;
    private Credentials credentials;
    BroadcastContract broadcastContract;

    @PostConstruct
    public void init() throws Exception {
        Logger.getLogger(BlockchainService.class.getName()).info("Initializing blockchain service");
        Logger.getLogger(BlockchainService.class.getName()).info("Connection URL: " + connectionUrl);
        //Content content = TestService.get();
        web3j = Web3j.build(new HttpService(connectionUrl));
        credentials = Credentials.create(privateKey);
        if (Objects.equals(oracleContractAddress, "0x") || this.isNotContractDeployed(oracleContractAddress)) {
            oracleContractAddress = this.deployOracleContract();
            if (Objects.equals(broadcastContractAddress, "0x") || this.isNotContractDeployed(broadcastContractAddress)) {
                broadcastContractAddress = this.deployBroadcastContract(oracleContractAddress);
                this.setBroadcastContractAddressOnOracleContract(broadcastContractAddress);
                Logger.getLogger(BlockchainService.class.getName()).warning("You should update the application.properties file with the new contract addresses");
                Logger.getLogger(BlockchainService.class.getName()).warning("And restart the application");
                System.exit(0);
            }
        }
    }

    public boolean isNotContractDeployed(String address) {
        try {
            EthGetCode send = web3j.ethGetCode(address, DefaultBlockParameterName.LATEST).send();
            String code = send.getCode();
            return code.equals("0x");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification du smart contract " + address, e);
        }
    }

    public String deployOracleContract() {
        try {
            ContractGasProvider gasProvider = new StaticGasProvider(gasPrice,gasLimit);

            YoutubeOracleContract contract = YoutubeOracleContract.deploy(
                    web3j,
                    credentials,
                    gasProvider
            ).send();

            if (contract.isValid()) {
                oracleContractAddress = contract.getContractAddress();
                Logger.getLogger(BlockchainService.class.getName()).info("Oracle contract address: " + oracleContractAddress);
                return oracleContractAddress;
            } else {
                throw new RuntimeException("Le contrat n'a pas été déployé correctement.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du deploiement du smart contract", e);
        }
    }

    public String deployBroadcastContract(String oracleContractAddress) {
        try {
            ContractGasProvider gasProvider = new StaticGasProvider(gasPrice,gasLimit);

            broadcastContract = BroadcastContract.deploy(
                    web3j,
                    credentials,
                    gasProvider,
                    oracleContractAddress
            ).send();

            if (broadcastContract.isValid()) {
                broadcastContractAddress = broadcastContract.getContractAddress();
                Logger.getLogger(BlockchainService.class.getName()).info("Broadcast contract address: " + broadcastContractAddress);
                return broadcastContractAddress;
            } else {
                throw new RuntimeException("Le contrat n'a pas été déployé correctement.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du déploiement du smart contract", e);
        }
    }

    public void setBroadcastContractAddressOnOracleContract(String broadcastContractAddress) {
        try {
            ContractGasProvider gasProvider = new StaticGasProvider(gasPrice,gasLimit);

            YoutubeOracleContract contract = YoutubeOracleContract.load(
                    oracleContractAddress,
                    web3j,
                    credentials,
                    gasProvider
            );

            contract.setBroadcastContractAddress(broadcastContractAddress).send();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'adresse du contrat de diffusion sur le contrat oracle", e);
        }
    }

    @Scheduled(fixedRate = 600000)  // 10 minutes in milliseconds
    public void check() throws Exception {
        if (broadcastContract == null) {
            broadcastContract = BroadcastContract.load(
                    broadcastContractAddress,
                    web3j,
                    credentials,
                    new StaticGasProvider(gasPrice, gasLimit)
            );
        }
        Logger.getLogger(BlockchainService.class.getName()).info("Checking blockchain terminated broadcasts");
        broadcastContract.checkAndTerminateAllBroadcasts().send();
    }
}

