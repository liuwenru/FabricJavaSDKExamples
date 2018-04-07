package ijarvis.intelliq.FabricCA;


import ijarvis.intelliq.LedgerRecord;
import ijarvis.intelliq.TestConfigure;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.HashMap;

public class FabricCATestCaseWithCAServer {
    private static Logger logger=Logger.getLogger(FabricCATestCaseWithCAServer.class);
    HashMap<String,SampleOrg> orgHashMap=new HashMap<>();
    private static LedgerRecord PERSONINFO=new LedgerRecord("liuwenru","基础设置支持部");
    @Before
    public void Setup() throws EnrollmentException, InvalidArgumentException, CryptoException, org.hyperledger.fabric.sdk.exception.InvalidArgumentException, MalformedURLException {
        logger.debug("Fabric CA Test Start.......");
        orgHashMap=TestConfigure.getConfigure();
        FabricCAApp fabricCAApp=new FabricCAApp();
        SampleUserCA user1=new SampleUserCA("city1user0","epoint","city1MSP");
        HFCAClient hfcaClient=orgHashMap.get("city1").getCAClient();
        FabricCAApp.init(user1);
    }
    @Test
    public void TestEpointChainCodeAddKV()throws Exception{
        logger.debug("链码测试........向链码中添加KV");
        Channel channel = FabricCAApp.client.newChannel(TestConfigure.CHANNLNAME);
        channel.addPeer(FabricCAApp.client.newPeer("peer", orgHashMap.get("city1").getPeerLocation("peer0city1")));
        channel.addOrderer(FabricCAApp.client.newOrderer("orderer1", orgHashMap.get("city1").getOrdererLocation("orderer1")));
        channel.initialize();
        FabricCAApp.addKV(channel, PERSONINFO);
    }
    @Test
    public void TestEpointChainCodeQuery() throws Exception{
        logger.debug("链码测试........向链码查询key值");
        Channel channel = FabricCAApp.client.newChannel(TestConfigure.CHANNLNAME);
        channel.addPeer(FabricCAApp.client.newPeer("peer", orgHashMap.get("city1").getPeerLocation("peer0city1")));
        channel.addOrderer(FabricCAApp.client.newOrderer("orderer1", orgHashMap.get("city1").getOrdererLocation("orderer1")));
        channel.initialize();
        FabricCAApp.querykv(channel, PERSONINFO.getKey());
    }
}
