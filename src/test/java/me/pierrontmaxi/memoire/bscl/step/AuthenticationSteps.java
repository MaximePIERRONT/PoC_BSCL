package me.pierrontmaxi.memoire.bscl.step;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import me.pierrontmaxi.memoire.bscl.CucumberSpringConfiguration;
import me.pierrontmaxi.memoire.bscl.util.Signer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationSteps extends CucumberSpringConfiguration {

    private static final String PRIVATE_KEY = "0xfc09ef338b1452562c9f203e33d337728de24d18b9ee21ddbeceb7fc822a7725";

    private final String BASE_URL = "http://localhost:8080";
    private String address;
    private String signedNonce;
    private ResponseEntity<String> response;
    private final RestTemplate restTemplate = new RestTemplate();

    @Given("I have a valid address")
    public void iHaveAValidAddress() {
        address = "0x12313531A9D8Df19C6887856812AdA24840DFa22";
    }

    @When("I request a nonce for the address")
    public void iRequestANonceForTheAddress() {
        String url = BASE_URL + "/auth/nonce/" + address;
        response = restTemplate.postForEntity(url, null, String.class);
    }

    @Then("the system should return a nonce for the address")
    public void theSystemShouldReturnANonceForTheAddress() {
        assertNotNull(response.getBody());
    }

    @Given("I have a valid address and a signed nonce")
    public void iHaveAValidAddressAndASignedNonce() {
        address = "someValidAddress";
        signedNonce = "someValidSignedNonce";
    }

    @When("I authenticate using the address and the signed nonce")
    public void iAuthenticateUsingTheAddressAndTheSignedNonce() {
        String url = BASE_URL + "/auth/authenticate/" + address;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(signedNonce, headers);
        response = restTemplate.postForEntity(url, entity, String.class);
    }

    @Then("the system should return a JWT token")
    public void theSystemShouldReturnAJWTToken() {
        assertNotNull(response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Given("I have a valid address and an invalid signed nonce")
    public void iHaveAValidAddressAndAnInvalidSignedNonce() {
        address = "someValidAddress";
        signedNonce = "invalidSignedNonce"; // Intentionally set to an invalid nonce
    }

    @When("I attempt to authenticate using the address and the invalid signed nonce")
    public void iAttemptToAuthenticateUsingTheAddressAndTheInvalidSignedNonce() {
        String url = BASE_URL + "/auth/authenticate/" + address;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(signedNonce, headers);
        try {
            response = restTemplate.postForEntity(url, entity, String.class);
        } catch (HttpClientErrorException.NotAcceptable e) {
            // Handle the 406 error here if needed
            String responseBodyAsString = e.getResponseBodyAsString();
            response = new ResponseEntity<>(responseBodyAsString, e.getStatusCode());
        }
    }


    @Then("the system should return an error 406, Invalid signature")
    public void theSystemShouldReturnAnInvalidSignatureError() {
        assertEquals(406, response.getStatusCode().value());
        assertEquals("Invalid signature", response.getBody());
    }

    @Given("I have already get a nonce for my address and sign it")
    public void iHaveAlreadyGetANonceForMyAddressAndSignIt() {
        iHaveAValidAddress();
        iRequestANonceForTheAddress();
        signedNonce = Signer.getSignNonce(PRIVATE_KEY, response.getBody());
    }
}

