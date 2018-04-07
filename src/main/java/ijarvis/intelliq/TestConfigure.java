package ijarvis.intelliq;

/**
 * 实现初始化调用Fabric-CA模块以及Fabric
 * 主要是实现加载初始化系统使用的到的一些环境变量以及通用配置等信息
 */




import ijarvis.intelliq.FabricCA.SampleOrg;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.net.MalformedURLException;
import java.util.HashMap;

public class TestConfigure {
    public static String CHAINCODENAME="epointchaincodecommon"; //链码名称
    public static String CHAINCODEVERSION="0.1";                //链码版本
    public static String CHANNLNAME="epointchannel";            //链码通道名称
    public static String Fabric_CA_NAME="epoint";
    public static String Fabric_CA_PASS="epoint";
    public static String Fabric_CA_Server="http://192.168.186.151:7054";
    public static HashMap<String,SampleOrg> getConfigure() throws MalformedURLException, InvalidArgumentException {
        HashMap<String,SampleOrg> orgHashMap=new HashMap<>();
        SampleOrg org1=new SampleOrg("city1","city1MSP");
        org1.addPeerLocation("peer0city1","grpc://192.168.186.153:7051");
        org1.addPeerLocation("peer1city1","grpc://192.168.186.154:7051");
        org1.addOrdererLocation("orderer1","grpc://192.168.186.152:7050");
        org1.setCALocation("http://192.168.186.151:7054");
        org1.setCAClient(HFCAClient.createNewInstance(org1.getCALocation(),null));
        org1.getCAClient().setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

        SampleOrg org2=new SampleOrg("city2","city2MSP");
        org2.addPeerLocation("peer0city2","grpc://192.168.186.155:7051");
        org2.addPeerLocation("peer1city2","grpc://192.168.186.156:7051");
        org2.addOrdererLocation("orderer1","grpc://192.168.186.152:7050");
        //org2.setCALocation("http://192.168.188.110:8054");//Org2的CA模块未启用所以暂时不提供

        orgHashMap.put("city1",org1);
        orgHashMap.put("city2",org2);
        return orgHashMap;
    }
}
