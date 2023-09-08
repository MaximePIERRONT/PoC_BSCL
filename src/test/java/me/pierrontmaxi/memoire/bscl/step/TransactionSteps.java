package me.pierrontmaxi.memoire.bscl.step;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import me.pierrontmaxi.memoire.bscl.CucumberSpringConfiguration;
import me.pierrontmaxi.memoire.bscl.rest.port.output.TransactionResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.Assert.*;

public class TransactionSteps extends CucumberSpringConfiguration {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIweDEyMzEzNTMxQTlEOERmMTlDNjg4Nzg1NjgxMkFkQTI0ODQwREZhMjIiLCJpc3MiOiJ5b3VyLWFwcC1uYW1lIn0.kDEumGPZXfrmO6KEMxj4JGI-oNPlfIbxd3E-8hTiYvqrPIVW-uBHtBKzxQtTZwv27rXIMAzc9ruejIYEL9HLhb6Er6G35baLulXf3oV5PuIUj28jdjTkolUZa6HOikUEPyZCEdRkEZYAScyYt_G4nYq_Pp1o8jLtYQm8VWXEqoqI_nvVchUhQQgfxaTPAjAHqkYUxUczYggXJdZZwn_FBIYWuwCLxUHMDH-WNM_UiaO1o4zQdGg5rzutNL_0dAcnNTXUEtgKWEr2D53nQdDUchXJ9G40qD-kbibtMoq9nNkAvcE3YmOPFnrG-X0rxuu_KEAkoztvH1zZwxEHLENOEg";

    private String rawTransaction;
    private ResponseEntity<TransactionResponse> response;

    @Given("I have a valid transaction")
    public void iHaveAValidTransaction() {
        rawTransaction = "0xf9014a808504a817c800836691b79417c2b50af519f30f021930af4f2a078e3fb9afe380b8e47d558d680000000000000000000000000000000000000000000000000000000000004e20000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000047465737400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000474657374000000000000000000000000000000000000000000000000000000001ba0f587317413d693f4a386a3290be4f5b98c7c430926ecac366db0a961219673d2a0674eb875a0c3cd7fd9660d3eff3dff8c577c11b1465930ce78ef4293de2f7347"; // Replace with actual transaction data
    }

    @Given("I have an invalid transaction")
    public void iHaveAnInvalidTransaction() {
        rawTransaction = "0x";
    }

    @When("I submit the transaction")
    public void iSubmitTheTransaction() {
        String url = "http://localhost:8080" + "/transaction";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(TOKEN);
        headers.set("Content-Type", "text/plain");
        HttpEntity<String> entity = new HttpEntity<>(rawTransaction, headers);
        response = restTemplate.postForEntity(url, entity, TransactionResponse.class);
        System.out.println(response.getBody());
    }

    @Then("I should receive a confirmation response")
    public void iShouldReceiveAConfirmationResponse() {
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Then("I should receive an error response")
    public void iShouldReceiveAnErrorResponse() {
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("ERROR", Objects.requireNonNull(response.getBody()).getStatus());
    }
}

