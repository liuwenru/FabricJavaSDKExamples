package ijarvis.intelliq.Fabric;

import org.apache.log4j.Logger;
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
    private static Logger logger=Logger.getLogger(SampleUser.class);
    public  String CERTDIR=FabricApp.class.getResource("/").getPath();
    private  String certfilepath;
    private  String keyfilepath;
    private final String userName;
    private String mspid;
    public SampleUser(String USERTYPE, String userName,String mspid) {
        String certfilepath1;
        if("peer".equals(USERTYPE)){
            CERTDIR=CERTDIR+"/crypto-config/peerOrganizations";
        }else if ("orderer".equals(USERTYPE)){
            CERTDIR=CERTDIR+"/crypto-config/ordererOrganizations";
        }
        if (mspid.equals("Org1MSP")){
            certfilepath =CERTDIR+"/org1.example.com/users/"+userName+"@org1.example.com/msp/signcerts/";
            File skfile=new File(certfilepath);
            certfilepath =certfilepath+skfile.listFiles()[0].getName();

            keyfilepath =CERTDIR+"/org1.example.com/users/"+userName+"@org1.example.com/msp/keystore/";
            File keyfile=new File(keyfilepath);
            keyfilepath =keyfilepath+keyfile.listFiles()[0].getName();
        }else {
            certfilepath =CERTDIR+"/org2.example.com/users/"+userName+"@org2.example.com/msp/signcerts/";
            File skfile=new File(certfilepath);
            certfilepath =certfilepath+skfile.listFiles()[0].getName();

            keyfilepath =CERTDIR+"/org2.example.com/users/"+userName+"@org2.example.com/msp/keystore/";
            File keyfile=new File(keyfilepath);
            keyfilepath =keyfilepath+keyfile.listFiles()[0].getName();
        }

        this.userName = userName;
        this.mspid=mspid;
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
                    return loadPrivateKey(Paths.get(keyfilepath));
                } catch (Exception e) {
                    return null;
                }
            }
            @Override
            public String getCert() {
                try {
                    return new String(Files.readAllBytes(Paths.get(certfilepath)));
                } catch (Exception e) {
                    return "";
                }
            }
        };
    }

    @Override
    public String getMspId() {
        return this.mspid;
    }


    /***
     *     */
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
