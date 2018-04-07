package ijarvis.intelliq.FabricCA;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
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
    public static String CHAINCODENAME = "epointchaincodecommon";
    public static String CHAINCODEVERSION = "0.1";

    public static HFClient client = null;
    public static CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite(); //使用的加密库
    public static ChaincodeID cid = ChaincodeID.newBuilder().setName(CHAINCODENAME).setVersion(CHAINCODEVERSION).build();
    

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
    public static void init(User fabricuser,String peerUrl,String ordererUrl,String channelname) throws CryptoException, InvalidArgumentException, MalformedURLException, EnrollmentException,org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, TransactionException {
        // 创建一个Client客户端
        client = HFClient.createNewInstance();
        client.setCryptoSuite(cs);
        client.setUserContext(fabricuser);
        InitChannel(peerUrl, ordererUrl, channelname);
    }

    private static Channel CHANNEL;
    /**
     * 初使化通道,因为一个节点可以加入到多个通道中
     */
    public static void InitChannel(String peerUrl, String ordererUrl,String channelname)throws InvalidArgumentException, TransactionException {
    	CHANNEL = FabricCAApp.client.newChannel(channelname);
        for (String item : peerUrl.split(","))
        	CHANNEL.addPeer(FabricCAApp.client.newPeer("peer", item));
        
        CHANNEL.addOrderer(FabricCAApp.client.newOrderer("orderer", ordererUrl));
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
        // System.out.println("Querying for " + key);
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
    public static boolean addKV(String key, String value) throws ProposalException, InvalidArgumentException {
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
    public static void updateKV(String key, String value)
            throws ProposalException, InvalidArgumentException {
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
