package me.pierrontmaxi.memoire.bscl.rest;

import me.pierrontmaxi.memoire.bscl.service.AuthenticationService;
import me.pierrontmaxi.memoire.bscl.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/nonce/{address}")
    public String getNonce(@PathVariable String address) {
        return authenticationService.generateNonce(address);
    }

    @PostMapping("/authenticate/{address}")
    public ResponseEntity<String> authenticate(@PathVariable String address, @RequestBody String signedNonce) {
        boolean isValid = authenticationService.isValidSignature(address, signedNonce);
        if (isValid) {
            String token = jwtService.issueToken(address);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(406).body("Invalid signature");
        }
    }
}
