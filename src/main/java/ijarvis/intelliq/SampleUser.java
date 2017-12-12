package ijarvis.intelliq;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashSet;
import java.util.Set;

public class SampleUser implements User {

    private final String certFolder;
    private final String userName;

    public SampleUser(String certFolder, String userName) {
        this.certFolder = certFolder;
        this.userName = userName;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public Set<String> getRoles() {
        return new HashSet<String>();
    }

    @Override
    public String getAccount() {
        return "";
    }

    @Override
    public String getAffiliation() {
        return "";
    }

    @Override
    public Enrollment getEnrollment() {
        return new Enrollment() {

            @Override
            public PrivateKey getKey() {
                try {
                    return loadPrivateKey(Paths.get(certFolder, "/keystore/ea2db84973c9c54436c47d7e10b9b63420f654ecd7c541fab14646e976294393_sk"));
                } catch (Exception e) {
                    return null;
                }
            }
            @Override
            public String getCert() {
                try {
                    return new String(Files.readAllBytes(Paths.get(certFolder, "/signcerts/Admin@org1.example.com-cert.pem")));
                } catch (Exception e) {
                    return "";
                }
            }
        };
    }

    @Override
    public String getMspId() {
        return "Org1MSP";
    }
    /***
     * loading private key from .pem-formatted file, ECDSA algorithm
     * (from some example on StackOverflow, slightly changed)
     * @param fileName - file with the key
     * @return Private Key usable
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static PrivateKey loadPrivateKey(Path fileName) throws IOException, GeneralSecurityException {
        PrivateKey key = null;
        InputStream is = null;
        try {
            is = new FileInputStream(fileName.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            boolean inKey = false;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (!inKey) {
                    if (line.startsWith("-----BEGIN ") && line.endsWith(" PRIVATE KEY-----")) {
                        inKey = true;
                    }
                    continue;
                } else {
                    if (line.startsWith("-----END ") && line.endsWith(" PRIVATE KEY-----")) {
                        inKey = false;
                        break;
                    }
                    builder.append(line);
                }
            }
            //
            byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("ECDSA");
            key = kf.generatePrivate(keySpec);
        } finally {
            is.close();
        }
        return key;
    }
}
