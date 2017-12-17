package ijarvis.intelliq.FabricCA;

import ijarvis.intelliq.Fabric.FabricApp;
import ijarvis.intelliq.LedgerRecord;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * 使用调用Fabric-CA的方式进行调用链码操作
 */
public class FabricCATestUseCAServer {
    private static Logger logger=Logger.getLogger(FabricCATestUseCAServer.class);
    HashMap<String,SampleOrg> orgHashMap=new HashMap<>();
    private static LedgerRecord PERSONINFO=new LedgerRecord("liuwenru","基础设置支持部");

    @Before
    public void Setup() throws EnrollmentException, InvalidArgumentException, CryptoException, org.hyperledger.fabric.sdk.exception.InvalidArgumentException, MalformedURLException {
        logger.debug("Fabric CA Test Start.......");
        orgHashMap=TestConfigure.getConfigure();
        FabricCAApp fabricCAApp=new FabricCAApp();
        SampleUser user1=new SampleUser("admin","org1");
        user1.setMspId("Org1MSP");
        HFCAClient hfcaClient=orgHashMap.get("org1").getCAClient();
        user1.setEnrollment(hfcaClient.enroll(user1.getName(), "admin"));
        FabricCAApp.init(user1);
    }
    @Test
    public void TestEpointChainCodeAddKV()throws Exception{
        logger.debug("链码测试........向链码中添加KV");
        Channel channel = FabricCAApp.client.newChannel(TestConfigure.CHANNLNAME);
        channel.addPeer(FabricCAApp.client.newPeer("peer", orgHashMap.get("org1").getPeerLocation("peer0org1")));
        channel.addOrderer(FabricCAApp.client.newOrderer("orderer", orgHashMap.get("org1").getOrdererLocation("orderer")));
        channel.initialize();
        FabricCAApp.addKV(channel, PERSONINFO);

    }
    @Test
    public void TestEpointChainCodeUpdate() throws Exception {
        logger.debug("链码测试........向链码中更新KV");
        LedgerRecord tmp=new LedgerRecord("liuwenru","刘美丽，刘帅帅");
        Channel channel = FabricCAApp.client.newChannel(TestConfigure.CHANNLNAME);
        channel.addPeer(FabricCAApp.client.newPeer("peer", orgHashMap.get("org1").getPeerLocation("peer0org1")));
        channel.addOrderer(FabricCAApp.client.newOrderer("orderer", orgHashMap.get("org1").getOrdererLocation("orderer")));
        channel.initialize();
        FabricCAApp.updateKV(channel, tmp);
    }

    @Test
    public void TestEpointChainCodeQuery() throws Exception{
        logger.debug("链码测试........向链码查询key值");
        Channel channel = FabricCAApp.client.newChannel(TestConfigure.CHANNLNAME);
        channel.addPeer(FabricCAApp.client.newPeer("peer", orgHashMap.get("org1").getPeerLocation("peer0org1")));
        channel.addOrderer(FabricCAApp.client.newOrderer("orderer", orgHashMap.get("org1").getOrdererLocation("orderer")));
        channel.initialize();
        FabricCAApp.querykv(channel, PERSONINFO.getKey());
    }

    @Test
    public void TestEpointChainCodeQueryHistory() throws Exception{
        logger.debug("链码测试........向链码查询给定Key的历史值");
        Channel channel = FabricCAApp.client.newChannel(TestConfigure.CHANNLNAME);
        channel.addPeer(FabricCAApp.client.newPeer("peer", orgHashMap.get("org1").getPeerLocation("peer0org1")));
        channel.addOrderer(FabricCAApp.client.newOrderer("orderer", orgHashMap.get("org1").getOrdererLocation("orderer")));
        channel.initialize();
        FabricCAApp.queryhistory(channel, PERSONINFO.getKey());
    }
}
