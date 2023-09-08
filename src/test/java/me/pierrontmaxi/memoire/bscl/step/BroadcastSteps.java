package me.pierrontmaxi.memoire.bscl.step;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.pierrontmaxi.memoire.bscl.domain.RawTransaction;
import me.pierrontmaxi.memoire.bscl.domain.contract.Broadcast;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

public class BroadcastSteps {

    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIweDEyMzEzNTMxQTlEOERmMTlDNjg4Nzg1NjgxMkFkQTI0ODQwREZhMjIiLCJpc3MiOiJ5b3VyLWFwcC1uYW1lIn0.kDEumGPZXfrmO6KEMxj4JGI-oNPlfIbxd3E-8hTiYvqrPIVW-uBHtBKzxQtTZwv27rXIMAzc9ruejIYEL9HLhb6Er6G35baLulXf3oV5PuIUj28jdjTkolUZa6HOikUEPyZCEdRkEZYAScyYt_G4nYq_Pp1o8jLtYQm8VWXEqoqI_nvVchUhQQgfxaTPAjAHqkYUxUczYggXJdZZwn_FBIYWuwCLxUHMDH-WNM_UiaO1o4zQdGg5rzutNL_0dAcnNTXUEtgKWEr2D53nQdDUchXJ9G40qD-kbibtMoq9nNkAvcE3YmOPFnrG-X0rxuu_KEAkoztvH1zZwxEHLENOEg";
    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/broadcast";
    private String userAddress;
    private String broadcastId;
    private String approveAddress;
    private String providerAddress;
    private RawTransaction response;
    private ResponseEntity<List> broadcasts;

    @Given("I have a broadcast with ID {string}")
    public void iHaveABroadcastWithID(String id) {
        this.broadcastId = id;
    }

    @Given("I have an address {string}")
    public void iHaveAnAddress(String address) {
        this.approveAddress = address;
    }

    @Given("I have a provider address {string}")
    public void iHaveAProviderAddress(String address) {
        this.providerAddress = address;
    }

    @When("I request broadcasts for content ID {string}")
    public void iRequestBroadcastsForContentID(String contentId) {
        String url = BASE_URL + "/byContent/" + contentId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        broadcasts = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), List.class);
        assertNotNull(broadcasts);
    }

    @When("I reject the broadcast with ID {string}")
    public void iRejectTheBroadcastWithID(String id) throws Exception {
        String url = BASE_URL + "/reject/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.postForObject(url, entity, RawTransaction.class);
        assertNotNull(response);
    }

    @When("I approve the broadcast with ID {string}")
    public void iApproveTheBroadcastWithIDForAddress(String id) {
        String url = BASE_URL + "/approve/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.postForObject(url, entity, RawTransaction.class);
        assertNotNull(response);
    }

    @When("I set the broadcast owner provider to {string}")
    public void iSetTheBroadcastOwnerProviderTo(String address) throws Exception {
        String url = BASE_URL + "/setBroadcastOwnerProvider/" + address;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.postForObject(url, entity, RawTransaction.class);
        assertNotNull(response);
    }

    @Then("I should receive a list of broadcasts")
    public void iShouldReceiveAListOfBroadcasts() {
        assert broadcasts != null;
    }
    @And("I am authenticated as user")
    public void iAmAuthenticatedAsUser() {
        this.token = TOKEN;
    }

    @Then("I get a raw the transaction")
    public void iGetARawTransaction() {
        assert response != null;
    }
}

