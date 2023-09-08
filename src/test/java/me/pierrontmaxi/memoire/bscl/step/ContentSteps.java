package me.pierrontmaxi.memoire.bscl.step;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.pierrontmaxi.memoire.bscl.CucumberSpringConfiguration;
import me.pierrontmaxi.memoire.bscl.domain.Broadcast;
import me.pierrontmaxi.memoire.bscl.domain.RawTransaction;
import me.pierrontmaxi.memoire.bscl.rest.port.input.ContentInput;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContentSteps extends CucumberSpringConfiguration {

    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIweDEyMzEzNTMxQTlEOERmMTlDNjg4Nzg1NjgxMkFkQTI0ODQwREZhMjIiLCJpc3MiOiJ5b3VyLWFwcC1uYW1lIn0.kDEumGPZXfrmO6KEMxj4JGI-oNPlfIbxd3E-8hTiYvqrPIVW-uBHtBKzxQtTZwv27rXIMAzc9ruejIYEL9HLhb6Er6G35baLulXf3oV5PuIUj28jdjTkolUZa6HOikUEPyZCEdRkEZYAScyYt_G4nYq_Pp1o8jLtYQm8VWXEqoqI_nvVchUhQQgfxaTPAjAHqkYUxUczYggXJdZZwn_FBIYWuwCLxUHMDH-WNM_UiaO1o4zQdGg5rzutNL_0dAcnNTXUEtgKWEr2D53nQdDUchXJ9G40qD-kbibtMoq9nNkAvcE3YmOPFnrG-X0rxuu_KEAkoztvH1zZwxEHLENOEg";
    public static final String BROADCAST_OWNER = "0x12313531A9D8Df19C6887856812AdA24840DFa22";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080";
    private String token;
    private RawTransaction response;
    private ResponseEntity<List> contents;

    @Given("I am a user with a valid token")
    public void iAmAUserWithAValidToken() {
        token = TOKEN;
    }

    @When("I request a list of all contents")
    public void iRequestAListOfAllContents() {
        String url = BASE_URL + "/content";
        HttpEntity<String> entity = getStringHttpEntity();
        ResponseEntity<List> contents = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        assertNotNull(contents);
    }

    @Then("I should receive a list of all contents")
    public void iShouldReceiveAListOfAllContents() {

    }

    @When("I register a new content with price {string} and contract link {string} and contract hash {string}")
    public void iRegisterANewContentWithPriceAndContractLinkAndVideoIDAndStartDateAndEndDate(String price, String contractFileLink, String contractHash) {
        String url = BASE_URL + "/content/register";
        ContentInput contentInput = new ContentInput();
        contentInput.setPrice(new BigInteger(price));
        contentInput.setContractFileLink(contractFileLink);
        contentInput.setContractHash(contractHash);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<ContentInput> entity = new HttpEntity<>(contentInput, headers);
        response = restTemplate.postForObject(url, entity, RawTransaction.class);
        assertNotNull(response);
    }

    @Then("I should receive a raw transaction")
    public void iShouldReceiveARawTransaction() {

    }

    @When("I purchase a content with ID {string} and video ID {string}")
    public void iPurchaseAContent(String contentId, String videoId) {
        String url = BASE_URL + "/content/purchase";
        Broadcast broadcast = new Broadcast();
        broadcast.setContentId(new BigInteger(contentId));
        broadcast.setVideoId(videoId);
        broadcast.setStartDate(new Date());
        broadcast.setEndDate(new Date());
        broadcast.setBroadcastOwner(BROADCAST_OWNER);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Broadcast> entity = new HttpEntity<>(broadcast, headers);
        response = restTemplate.postForObject(url, entity, RawTransaction.class);
        assertNotNull(response);
    }

    @When("I set content with ID {string} as unavailable")
    public void iSetContentAsUnavailable(String contentId) {
        String url = BASE_URL + "/content/setUnavailable/" + contentId;
        HttpEntity<String> entity = getStringHttpEntity();
        response = restTemplate.postForObject(url + "?userAddress=" + BROADCAST_OWNER, entity, RawTransaction.class);
        assertNotNull(response);
    }

    @When("I request my contents")
    public void iRequestMyContents() {
        String url = BASE_URL + "/content/myContents";
        HttpEntity<String> entity = getStringHttpEntity();
        contents = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        assertNotNull(contents);
    }

    @NotNull
    private HttpEntity<String> getStringHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    @Then("I should receive a raw transaction for the purchase")
    public void iShouldReceiveARawTransactionForThePurchase() {
        assert response != null;
    }


    @Then("I should receive a valid response")
    public void iShouldReceiveAValidResponse() {
        assert response != null;
    }

    @Then("I should receive a valid response of Contents")
    public void iShouldReceiveAValidResponseOfContents() {
        assert contents != null;
        assertTrue(contents.getStatusCode().is2xxSuccessful());
    }
}

