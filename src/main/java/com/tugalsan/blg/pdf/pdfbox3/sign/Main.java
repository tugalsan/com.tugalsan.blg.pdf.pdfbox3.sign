package com.tugalsan.blg.pdf.pdfbox3.sign;

import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.nio.file.*;
import java.security.KeyStore;
import java.security.Security;
import org.apache.pdfbox.examples.signature.CreateVisibleSignature2;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {

    public static void main(String[] args) {
        try {
            //WARMUP
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            //VARIABLES
            var pathStore = Path.of("C:\\dat\\ssl\\mesa\\tomcat.jks");
            CharSequence password = "MyPass";
            var pathPdfInput = Path.of("C:\\git\\blg\\com.tugalsan.blg.pdf.pdfbox3.sign\\HelloWorld.pdf");
            var pathPdfOutput = pathPdfInput.resolveSibling(pathPdfInput.toFile().getName().substring(0, pathPdfInput.toFile().getName().lastIndexOf(".")) + "_signed.pdf");
            System.out.println(pathPdfOutput);
            var rectangle = new Rectangle2D.Float(10, 200, 150, 50);
            var useExternalSignScnerio = false;
            Path optional_pathImgSign = null;
            var signatureFieldName = "abc";
            String tsa = null;

            //KEYSTORE
            KeyStore keystore;
            var strPathStore = pathStore.toString().toUpperCase();
            if (strPathStore.endsWith("JKS")) {
                keystore = KeyStore.getInstance("JKS");
            } else {
                keystore = KeyStore.getInstance("PKCS12");
            }
            try (InputStream is = Files.newInputStream(pathStore)) {
                keystore.load(is, password.toString().toCharArray());
            }

            //SIGNER
            var signer = new CreateVisibleSignature2(keystore, password.toString().toCharArray());
            if (optional_pathImgSign != null) {
                signer.setImageFile(optional_pathImgSign.toFile());
            }
            signer.setExternalSigning(useExternalSignScnerio);
            signer.signPDF(
                    pathPdfInput.toFile(),
                    pathPdfOutput.toFile(),
                    rectangle,
                    tsa,
                    signatureFieldName
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
