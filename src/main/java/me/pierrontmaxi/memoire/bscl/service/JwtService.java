package me.pierrontmaxi.memoire.bscl.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class JwtService {
    public String issueToken(String address) {
        try {
            PrivateKey privateKey = loadPrivateKeyFromResource();
            PublicKey publicKey = loadPublicKeyFromResource();
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);
            return JWT.create()
                    .withSubject(address)
                    .withIssuer("your-app-name")
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du JWT", e);
        }
    }

    public static PublicKey loadPublicKeyFromResource() throws Exception {
        File file = new ClassPathResource("/jwt/public_key.pem").getFile();
        String key = Files.readString(file.toPath(), Charset.defaultCharset());

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.decodeBase64(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return keyFactory.generatePublic(keySpec);
    }

    private static PrivateKey loadPrivateKeyFromResource() throws Exception {
        File file = new ClassPathResource("/jwt/private_key.pem").getFile();
        String key = Files.readString(file.toPath(), Charset.defaultCharset());

        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.decodeBase64(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return keyFactory.generatePrivate(keySpec);
    }

    public static String getSubjectFromHeaderInToken(String tokenInHeader){
        String token = tokenInHeader.substring(7);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }
}


