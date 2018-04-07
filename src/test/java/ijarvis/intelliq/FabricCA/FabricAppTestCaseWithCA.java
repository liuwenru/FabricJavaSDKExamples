package ijarvis.intelliq.FabricCA;

import java.net.MalformedURLException;

import ijarvis.intelliq.TestConfigure;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;

public class FabricAppTestCaseWithCA {
	private static Logger logger=Logger.getLogger(FabricAppTestCaseWithCA.class);
	//LedgeVersion1 和 LedgeVersion2 是账本中实际记录的数据
	private static LedgerRecord LedgeVersion1=new LedgerRecord("liuwenru","基础设置支持部");
	private static LedgerRecord LedgeVersion2=new LedgerRecord("liuwenru","创新技术研究中心");
	private static SampleUserCA peeruser=null;
	
	// 构建测试环境
	@Before
	public void setup() throws MalformedURLException, InvalidArgumentException, EnrollmentException, CryptoException, org.hyperledger.fabric.sdk.exception.InvalidArgumentException, TransactionException {
		peeruser=new SampleUserCA(TestConfigure.CA1PeerUserName, TestConfigure.CA1PeerUserPass, "city1MSP",TestConfigure.CA1ServerLocation);
		FabricCAApp.init(peeruser, TestConfigure.City1Peer0Url, TestConfigure.Orderer1Url, TestConfigure.CHANNELNAME);
	}
    @Test
    public void TestEpointChainCodeAddKV()throws Exception{
        logger.debug("链码测试........向链码中添加KV");
        FabricCAApp.addKV(LedgeVersion1.getKey(),LedgeVersion1.getValue());
    }
    @Test
    public void TestEpointChainCodeUpdate() throws Exception {
        logger.debug("链码测试........向链码中更新KV");
        FabricCAApp.updateKV(LedgeVersion2.getKey(),LedgeVersion2.getValue());
    }
    @Test
    public void TestEpointChainCodeQuery() throws Exception{
        logger.debug("链码测试........向链码查询key值");
        FabricCAApp.querykv(LedgeVersion1.getKey());
    }
    @Test
    public void TestEpointChainCodeQueryHistory() throws Exception{
        logger.debug("链码测试........向链码查询给定Key的历史值");
        FabricCAApp.queryhistory(LedgeVersion1.getKey());
    }
	
	

}
