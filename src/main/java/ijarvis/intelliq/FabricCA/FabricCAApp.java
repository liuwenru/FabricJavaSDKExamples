package ijarvis.intelliq.FabricCA;

import ijarvis.intelliq.Fabric.FabricApp;
import ijarvis.intelliq.Fabric.SampleUser;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.util.Collection;

public class FabricCAApp {
    private static Logger logger=Logger.getLogger(FabricCAApp.class);
    public static HFClient client=null;
    public static CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite();
    public static void init(User CAUSER) throws CryptoException, InvalidArgumentException {
        client = HFClient.createNewInstance();
        client.setCryptoSuite(cs);
        client.setUserContext(CAUSER);
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
