package me.pierrontmaxi.memoire.bscl.service;

import jakarta.annotation.PostConstruct;
import me.pierrontmaxi.memoire.bscl.domain.contract.BroadcastContract;
import me.pierrontmaxi.memoire.bscl.domain.contract.YoutubeOracleContract;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class YoutubeOracleService {

    @Value("${application.blockchain.contracts.oracle.address}")
    private String oracleAddress;

    @Value("${application.blockchain.contracts.broadcast.address}")
    private String broadcastContractAddress;

    @Value("${application.blockchain.private-key}")
    private String privateKey;

    @Value("${application.blockchain.connection-url}")
    private String connectionUrl;

    @Value("${application.api.youtube-api-key}")
    private String apiKey;

    @Value("${application.blockchain.gas-price}")
    private BigInteger gasPrice;

    @Value("${application.blockchain.gas-limit}")
    private BigInteger gasLimit;

    private YoutubeOracleContract youtubeOracleContract;

    @PostConstruct
    public void onStartup() {
        Web3j web3j = Web3j.build(new HttpService(connectionUrl));
        Credentials credentials = Credentials.create(privateKey);
        youtubeOracleContract = YoutubeOracleContract.load(
                oracleAddress,
                web3j,
                credentials,
                new StaticGasProvider(gasPrice, gasLimit)
        );
        listenToNewYoutubeRequests();
    }

    private void listenToNewYoutubeRequests() {
        youtubeOracleContract.newYoutubeRequestEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(event -> {
                    BigInteger broadcastId = event._broadcastId;
                    String videoId = youtubeOracleContract.getBroadcastById(broadcastId).send();

                    try {
                        URL url = new URL("https://youtube.googleapis.com/youtube/v3/videos?part=statistics&id=" + videoId + "&key=" + apiKey);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        BigInteger views = new BigInteger(jsonResponse.getJSONArray("items").getJSONObject(0).getJSONObject("statistics").getString("viewCount"));

                        youtubeOracleContract.setVideoViews(broadcastId, views).send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}

