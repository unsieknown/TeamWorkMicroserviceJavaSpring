package com.mordiniaa.authservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyProvider {

    private static final String PRIVATE_KEY_PATH = "/keys/private.pm";
    private static final String PUBLIC_KEY_PATH = "/keys/public.pm";

    @Getter
    private KeyPair keyPair;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        File privateFile = new File(PRIVATE_KEY_PATH);
        File publicFile = new File(PUBLIC_KEY_PATH);

        if (privateFile.exists() && publicFile.exists()) {
            this.keyPair = loadKeys(privateFile, publicFile);
        } else {
            this.keyPair = generateAndSaveKeys(privateFile, publicFile);
        }

    }

    private KeyPair generateAndSaveKeys(File privateFile, File publicFile) throws NoSuchAlgorithmException, IOException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();

        writePem(privateFile, "PRIVATE KEY", keyPair.getPrivate().getEncoded());
        writePem(publicFile, "PUBLIC KEY", keyPair.getPublic().getEncoded());

        return keyPair;
    }

    private void writePem(File file, String type, byte[] content) throws IOException {

        String encoded = Base64.getEncoder().encodeToString(content);
        String pem = "-----BEGIN " + type + "-----\n"
                + encoded.replaceAll("(.{64})", "$1\n")
                + "\n-----END " + type + "-----";

        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(pem);
        }
    }

    private KeyPair loadKeys(File privateFile, File publicFile) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        byte[] privateBytes = readPem(privateFile);
        byte[] publicBytes = readPem(publicFile);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
        PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(publicBytes));

        return new KeyPair(publicKey, privateKey);
    }

    private byte[] readPem(File file) throws IOException {
        String content = new String(
                Files.readAllBytes(file.toPath())
        )
                .replaceAll("-----.*-----", "")
                .replaceAll("\\s", "");

        return Base64.getDecoder().decode(content);
    }
}
