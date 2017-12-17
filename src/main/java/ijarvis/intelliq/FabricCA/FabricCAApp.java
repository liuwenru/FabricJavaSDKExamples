package ijarvis.intelliq.FabricCA;

import ijarvis.intelliq.Fabric.FabricApp;
import ijarvis.intelliq.Fabric.SampleUser;
import ijarvis.intelliq.LedgerRecord;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FabricCAApp {
    private static Logger logger=Logger.getLogger(FabricCAApp.class);
    public static HFClient client=null;
    public static CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite();
    public static ChaincodeID cid = ChaincodeID.newBuilder().setName(TestConfigure.CHAINCODENAME).setVersion(TestConfigure.CHAINCODEVERSION).build();
    public static void init(User CAUSER) throws CryptoException, InvalidArgumentException {
        //创建一个Client客户端
        client = HFClient.createNewInstance();
        client.setCryptoSuite(cs);
        client.setUserContext(CAUSER);
    }

    /**
     * 调用链码添加KV结构
     */
    public static void addKV(Channel channel, LedgerRecord record) throws ProposalException, InvalidArgumentException {
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
    /**
     * 调用链码更新
     */
    public static void updateKV(Channel channel,LedgerRecord record) throws ProposalException, InvalidArgumentException {
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("updatekv");
        req.setArgs(record.toStringArray());
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
    public static void querykv(Channel channel, String key) throws Exception {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
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
    /**
     *  调用链码查询给定Key的历史值
     */
    public static void queryhistory(Channel channel,String key) throws ProposalException, InvalidArgumentException {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("queryhistory");
        req.setArgs(new String[]{key});
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
        }
    }
}
