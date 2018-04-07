package ijarvis.intelliq.FabricCA;

import java.net.MalformedURLException;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

/**
 * 抽象组织节点中的用户信息，该用户可以实现从Fabric CA模块中获取CA证书内容
 * @author ijarvis
 *
 */
public class SampleUserCA implements User {
    private String userName="";
    private String userPass="";
    private String MSPID="";
    private Enrollment enrollment;
    /**
     * 实例化用户
     * @param userName 用户在CA中注册的用户名
     * @param userPass 用户在CA中注册的密码
     * @param MSPID    用户所在的MSPID
     * @param CALocaltion 用户所在CA节点位置信息
     * @throws MalformedURLException 
     * @throws InvalidArgumentException
     * @throws EnrollmentException
     */
    public SampleUserCA(String userName, String userPass, String MSPID,String CALocaltion) throws MalformedURLException, InvalidArgumentException, EnrollmentException {
        this.userName = userName;
        this.userPass = userPass;
        this.MSPID = MSPID;
        //调用Fabric的接口获取到注册用户的证书信息，请注意在调用前需要确保你注册的用户已经在CA结构中注册，如果没有注册则反馈报错
        HFCAClient client=HFCAClient.createNewInstance("epointca",CALocaltion,null);//TestConfigure.CAName
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