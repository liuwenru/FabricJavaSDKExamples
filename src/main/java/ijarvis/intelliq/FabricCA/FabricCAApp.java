package ijarvis.intelliq.FabricCA;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;

/**
 * 实际干活的类，负责调用链码等操作
 * @author ijarvis
 * TODO 需要测试与实验如何进行多个节点背书策略时调用方法
 */
public class FabricCAApp
{
    private static Logger logger = Logger.getLogger(FabricCAApp.class);
    public static HFClient client = null;
    public static CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite(); //使用的加密库
    public static ChaincodeID cid = null;
    private static Channel CHANNEL;

    /**
     * 
     * @param fabricuser   使用SampleUserCA对象实例化获取过CA签名的东西
     * @throws CryptoException   
     * @throws InvalidArgumentException
     * @throws MalformedURLException
     * @throws EnrollmentException
     * @throws org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException
     * @throws TransactionException 
     */
    public static void init(User fabricuser,String peerUrl,String ordererUrl,String channelname,String chaincodename,String chaincodeversion) throws CryptoException, InvalidArgumentException, TransactionException {
        // 创建一个Client客户端
        client = HFClient.createNewInstance();
        client.setCryptoSuite(cs);
        client.setUserContext(fabricuser);
        CHANNEL = FabricCAApp.client.newChannel(channelname);
        for (String item : peerUrl.split(","))
            CHANNEL.addPeer(FabricCAApp.client.newPeer("peer", item));
        CHANNEL.addOrderer(FabricCAApp.client.newOrderer("orderer", ordererUrl));

        cid=ChaincodeID.newBuilder().setName(chaincodename).setVersion(chaincodeversion).build();
        CHANNEL.initialize();
    }




    /**
     * 实现根绝给定的Key查询数据
     */
    public static String querykv(String key) throws Exception {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("query");
        req.setArgs(new String[] {key });
        Collection<ProposalResponse> resps = CHANNEL.queryByChaincode(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
            return payload;
        }
        return "";
    }

    /**
     * 调用链码添加KV结构
     */
    public static boolean addKV(String key, String value) throws ProposalException, InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException {
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("addkv");
        req.setArgs(new String[] {key, value });
        // TODO 该段代码必须调用，但是未在官方的代码中找到相关的代码说明
        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        req.setTransientMap(tm2);
        //TODO 链码调用策略指定说明
        //ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        //chaincodeEndorsementPolicy.fromYamlFile(new File("/Users/ijarvis/IdeaProjects/FabricJavaSDKExamples/src/test/resources/chaincodeendorsementpolicy.yaml"));
        //req.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
        Collection<ProposalResponse> resps = CHANNEL.sendTransactionProposal(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
        }

        CHANNEL.sendTransaction(resps);
        return true;
    }
    /**
     * 调用链码更新
     */
    public static void updateKV(String key, String value) throws ProposalException, InvalidArgumentException {
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("updatekv");
        req.setArgs(new String[] {key, value });
        Collection<ProposalResponse> resps = CHANNEL.sendTransactionProposal(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
        }
        CHANNEL.sendTransaction(resps);
    }

    /**
     * 调用链码查询给定Key的历史值
     */
    public static String queryhistory(String key) throws ProposalException, InvalidArgumentException {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("queryhistory");
        req.setArgs(new String[] {key });
        Collection<ProposalResponse> resps = CHANNEL.queryByChaincode(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
            if (payload.equals("null"))
                return "";
            return payload;
        }
        return "";
    }
}
