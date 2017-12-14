package ijarvis.intelliq.FabricCA;

import ijarvis.intelliq.Fabric.AppTest;
import ijarvis.intelliq.Fabric.FabricApp;
import ijarvis.intelliq.LedgerRecord;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;

public class FabricCATestUseStatic {
    private static Logger logger=Logger.getLogger(AppTest.class);
    private static String CONNFIG_Orderer="grpc://192.168.188.111:7050";
    private static String CONNFIG_Peer0Org1="grpc://192.168.188.112:7051";
    private static String CONNFIG_Peer1Org1="grpc://192.168.188.113:7051";
    private static String CONNFIG_Peer0Org2="grpc://192.168.188.114:7051";
    private static String CONNFIG_Peer1Org2="grpc://192.168.188.115:7051";
    private static String CHANNELID="epointchannel";
    private static String keypath="";
    private static LedgerRecord PERSONINFO=new LedgerRecord("liuwenru","刘东");
    @Before
    public void Setup() throws CryptoException, InvalidArgumentException {
        logger.debug("Fabric Test Init........");
        FabricApp fabricApp=new FabricApp();
        FabricApp.keypath="/FabricCAcert";
        FabricApp.initCA();
    }
    @Test
    public void TestEpointChainCodeQuery() throws Exception {
        logger.debug("测试Fabric 查询功能");
        Channel channel = FabricApp.client.newChannel(CHANNELID);
        channel.addPeer(FabricApp.client.newPeer("peer", CONNFIG_Peer0Org1));
        channel.addOrderer(FabricApp.client.newOrderer("orderer", CONNFIG_Orderer));
        channel.initialize();
        FabricApp.queryFabcar(channel, PERSONINFO.getKey());
    }



}
