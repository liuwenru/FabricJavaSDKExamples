package ijarvis.intelliq;


import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private static Logger logger=Logger.getLogger(AppTest.class);

    private static String CONNFIG_Orderer="grpc://192.168.188.111:7050";
    private static String CONNFIG_Peer0Org1="grpc://192.168.188.112:7051";
    private static String CHANNELID="epointchannel";
    @Before
    public void Setup() throws CryptoException, InvalidArgumentException {
        logger.debug("Fabric Test Init........");
        FabricApp fabricApp=new FabricApp();
        FabricApp.init();

    }
    @Test
    public void TestEpointChainCodeQuery() throws Exception {
        Channel channel = FabricApp.client.newChannel(CHANNELID);
        channel.addPeer(FabricApp.client.newPeer("peer", CONNFIG_Peer0Org1));
        channel.addOrderer(FabricApp.client.newOrderer("orderer", CONNFIG_Orderer));
        channel.initialize();
        FabricApp.queryFabcar(channel, "liubo");
    }

}
