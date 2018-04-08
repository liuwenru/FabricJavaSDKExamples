package ijarvis.intelliq;


/**
 * 
 * @author ijarvis
 *	测试配置类，记录测试环境的配置信息
 */


public class TestConfigure {
	
	// CA1的配置
	public static String CA1ServerLocation="http://192.168.188.112:7054";
	public static String CA1PeerUserName="city1epoint";
	public static String CA1PeerUserPass="epoint";
	public static String CA1CAName="epointca";

	// CA1的配置
	public static String CA2ServerLocation="http://192.168.188.114:7054";
	public static String CA2PeerUserName="city2epoint";
	public static String CA2PeerUserPass="epoint";
	public static String CA2CAName="epointca";
	
	
	//TODO 需要调研一下如果通过代码获取到链码名称和版本，这样以后可以选择运行指定的智能合约
	public static String CHAINCODENAME="epointchaincodecommon10"; // 当前的链码名称
	public static String CHAINCODEVERSION="0.1";  //     当前的链码版本
	public static String CHANNELNAME="epointchannel";
	
	// 集群节点位置信息
	public static String City1Peer0Url="grpc://192.168.188.111:7051";
	public static String City1Peer1Url="grpc://192.168.188.112:7051";
	public static String City1peersUrl=City1Peer0Url+","+City1Peer1Url;
	public static String City2Peer0Url="grpc://192.168.188.113:7051";
	public static String City2Peer1Url="grpc://192.168.188.114:7051";
	public static String City2peersUrl=City2Peer0Url+","+City2Peer1Url;

	public static String Orderer1Url="grpc://192.168.188.110:7050";
	
	
	
	

}
