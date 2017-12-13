package ijarvis.intelliq.FabricCA;

//实现初始化调用Fabric-CA模块以及Fabric

import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.net.MalformedURLException;
import java.util.HashMap;

public class TestConfigure {
    public static HashMap<String,SampleOrg> getConfigure() throws MalformedURLException, InvalidArgumentException {
        HashMap<String,SampleOrg> orgHashMap=new HashMap<>();
        SampleOrg org1=new SampleOrg("org1","Org1MSP");
        org1.addPeerLocation("peer0org1","grpc://192.168.188.112:7051");
        org1.addPeerLocation("peer1org1","grpc://192.168.188.113:7051");
        org1.addOrdererLocation("orderer","grpc://192.168.188.111:7050");
        org1.setCALocation("http://192.168.188.110:7054");
        org1.setCAClient(HFCAClient.createNewInstance(org1.getCALocation(),null));
        org1.getCAClient().setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());


        SampleOrg org2=new SampleOrg("org2","Org2MSP");
        org2.addPeerLocation("peer0org2","grpc://192.168.188.114:7051");
        org2.addPeerLocation("peer1org2","grpc://192.168.188.115:7051");
        org2.addOrdererLocation("orderer","grpc://192.168.188.111:7050");
        //org2.setCALocation("http://192.168.188.110:8054");//Org2的CA模块未启用所以暂时不提供


        orgHashMap.put("org1",org1);
        orgHashMap.put("org2",org2);
        return orgHashMap;
    }
}
