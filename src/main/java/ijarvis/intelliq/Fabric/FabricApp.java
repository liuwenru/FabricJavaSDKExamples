package ijarvis.intelliq.Fabric;

import ijarvis.intelliq.LedgerRecord;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 实现封装一些超级账本的操作方法，注意此版本为未结合使用Fabric-CA模块的代码示例
 *
 */
public class FabricApp{
    private static Logger logger=Logger.getLogger(FabricApp.class);
    public static HFClient client=null;
    public static CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite();
    public static HashMap<String,SampleOrg> orgHashMap=null;
    public static ChaincodeID cid = ChaincodeID.newBuilder().setName(TestConfigure.CHAINCODENAME).setVersion(TestConfigure.CHAINCODEVERSION).build();
    public static User peer0org1=null;
    /**
     *  初始化超级账本的客户端等相关属性
     */
    public static void init() throws CryptoException, InvalidArgumentException,MalformedURLException,org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException
    {
        client = HFClient.createNewInstance();
        client.setCryptoSuite(cs);
        orgHashMap=TestConfigure.getConfigure();
        peer0org1 = orgHashMap.get("org1").getAdmin();
        client.setUserContext(peer0org1);

    }
    /*
    *   实现根绝给定的数据调用链码写入账本中
    * */
    public static void instertFabcar(Channel channel, LedgerRecord record) throws Exception {
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("addkv");
        req.setArgs(record.toStringArray());
        //TODO 该段代码必须调用，但是未在官方的代码中找到相关的代码说明
        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        req.setTransientMap(tm2);
        Collection<ProposalResponse> resps = channel.sendTransactionProposal(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
        }
        channel.sendTransaction(resps);
    }
    /*
     *   实现根绝给定的Key查询数据
     * */
    public static void queryFabcar(Channel channel, String key) throws Exception {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName(TestConfigure.CHAINCODENAME).setVersion(TestConfigure.CHAINCODEVERSION).build();
        req.setChaincodeID(cid);
        req.setFcn("query");
        req.setArgs(new String[] { key });
        System.out.println("Querying for " + key);
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
        }
    }

}
