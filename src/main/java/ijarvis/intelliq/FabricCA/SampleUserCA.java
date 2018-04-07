package ijarvis.intelliq.FabricCA;

import ijarvis.intelliq.TestConfigure;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAEnrollment;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.net.MalformedURLException;
import java.util.Set;

public class SampleUserCA implements User {
    private String userName="";
    private String userPass="";
    private String MSPID="";
    private Enrollment enrollment;
    public SampleUserCA(String userName, String userPass, String MSPID) throws MalformedURLException, InvalidArgumentException, EnrollmentException {
        this.userName = userName;
        this.userPass = userPass;
        this.MSPID = MSPID;
        //调用Fabric的接口获取到注册用户的证书信息
        HFCAClient client=HFCAClient.createNewInstance("epointca",TestConfigure.Fabric_CA_Server,null);
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        this.enrollment=client.enroll(this.userName,this.userPass);

    }

    @Override
    public String getName() {
        return this.userName;
    }

    @Override
    public Set<String> getRoles() {
        return null;
    }

    @Override
    public String getAccount() {
        return null;
    }

    @Override
    public String getAffiliation() {
        return null;
    }

    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    @Override
    public String getMspId() {
        return this.MSPID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getMSPID() {
        return MSPID;
    }

    public void setMSPID(String MSPID) {
        this.MSPID = MSPID;
    }
}
