package ijarvis.intelliq;

import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import java.util.Collection;

/**
 * 实现封装一些超级账本的操作方法，注意此版本为未结合使用Fabric-CA模块的代码示例
 *
 */
public class FabricApp{
    private static Logger logger=Logger.getLogger(FabricApp.class);
    public static HFClient client=null;
    public static CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite();
    public static User peer0org1=null;

    /**
     *  初始化超级账本的客户端等相关属性
     */
    public static void init() throws CryptoException, InvalidArgumentException {
        client = HFClient.createNewInstance();
        client.setCryptoSuite(cs);
        String keystorepath= FabricApp.class.getResource("/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp").getPath();
        peer0org1 = new SampleUser(keystorepath, "Admin");
        client.setUserContext(peer0org1);

    }
    /*
    *   实现根绝给定的数据调用链码写入账本中
    * */
    public static void instertFabcar(Channel channel, LedgerRecord record) throws Exception {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("epointchaincodezzk").setVersion("0.1").build();
        req.setChaincodeID(cid);
        req.setFcn("addcard");
        req.setArgs(record.toStringArray());
        logger.debug("addcard data"+record.toStringArray());
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            System.out.println("response: " + payload);
        }
    }
    /*
     *   实现根绝给定的Key查询数据
     * */
    public static void queryFabcar(Channel channel, String key) throws Exception {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("epointchaincodezzk").setVersion("0.1").build();
        req.setChaincodeID(cid);
        req.setFcn("query");
        req.setArgs(new String[] { key });
        System.out.println("Querying for " + key);
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            System.out.println("response: " + payload);
        }
    }

}
