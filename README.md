# FabricJavaSDKExamples
 Fabric JAVA SDK测试样例


## 一、调用链码说明(不使用Fabric-CA模块)

### 1.1 测试代码结构说明

```bash
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── ijarvis
│   │   │       └── intelliq
│   │   │           ├── FabricApp.java     # 初始化Fabric Client对象以及连接组织信息
│   │   │           ├── LedgerRecord.java  # 链码中数据结构Bean
│   │   │           └── SampleUser.java    # 抽象超级账本中用户信息(实际就是MSP下user文件夹中生成的用户身份文件)
│   │   └── resources
│   │       └── log4j.xml  
│   └── test
│       ├── java
│       │   └── ijarvis
│       │       └── intelliq
│       │           └── AppTest.java       # Junit测试代码文件
│       └── resources
│           ├── crypto-config              # 测试环境下所有证书以及MSP文件集合
│           │   ├── ordererOrganizations
│           │   │   └── example.com
│           │   └── peerOrganizations
│           │       ├── org1.example.com
│           │       └── org2.example.com
│           └── log4j.xml
```

由于调用链码调用初始化需要初始化`fhclient`等等一系列的对象，所以建议使用`Junit`运行代码。


### 1.2、 核心代码说明

使用`SDk`调用链码主要的步骤是：
* 1、初始化HFClient
* 2、实例化已经实现接口`User`的类加载已有的证书文件以及连接用户的私钥文件
* 3、调用`client.setUserContext(peer0org1);`方法更新`client`的配置文件
* 4、通过`client`获取通道对象(`channel`)，客户端设置发起交易需要使用的`Orderer`以及`Peer`对象
* 5、通过`channel`对象设置调用链码的名称参数等等信息发起调用链码

```java
package ijarvis.intelliq.Fabric;
// 省略相关的包导入
public class SampleUser implements User {
    private final String certFolder;
    private final String userName;
    public SampleUser(String certFolder, String userName) {
        this.certFolder = certFolder;
        this.userName = userName;
    }
    //省略相关Set和Get方法
    
    /**
    * 
    * 重要实现方法，不使用CA需要自己实现如何加载私钥以及证书信息
    */
    @Override
    public Enrollment getEnrollment() {
        return new Enrollment() {

            @Override
            public PrivateKey getKey() {
                try {
                    return loadPrivateKey(Paths.get(certFolder, "/keystore/ea2db84973c9c54436c47d7e10b9b63420f654ecd7c541fab14646e976294393_sk"));
                } catch (Exception e) {
                    return null;
                }
            }
            @Override
            public String getCert() {
                try {
                    return new String(Files.readAllBytes(Paths.get(certFolder, "/signcerts/Admin@org1.example.com-cert.pem")));
                } catch (Exception e) {
                    return "";
                }
            }
        };
    }
    
    //测试代码所以固定写死相关的MSPID
    @Override
    public String getMspId() {
        return "Org1MSP";
    }
    /***
     * 实现加载证书服务
     */
    public static PrivateKey loadPrivateKey(Path fileName) throws IOException, GeneralSecurityException {
        PrivateKey key = null;
        InputStream is = null;
        try {
            is = new FileInputStream(fileName.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            boolean inKey = false;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (!inKey) {
                    if (line.startsWith("-----BEGIN ") && line.endsWith(" PRIVATE KEY-----")) {
                        inKey = true;
                    }
                    continue;
                } else {
                    if (line.startsWith("-----END ") && line.endsWith(" PRIVATE KEY-----")) {
                        inKey = false;
                        break;
                    }
                    builder.append(line);
                }
            }
            //
            byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("ECDSA");
            key = kf.generatePrivate(keySpec);
        } finally {
            is.close();
        }
        return key;
    }
}
```


### 1.3、其他说明
如果觉得所给测试用例代码不够明确，可以参考[官方网站此处的代码示例](https://github.com/hyperledger/fabric-sdk-java/blob/master/src/test/java/org/hyperledger/fabric/sdkintegration/End2endAndBackAgainIT.java)
本代码只是将官方代码进行简化方便做实验以及测试。



## 二、使用CA Server调用链码

与不通过CA模块调用的链码相比其实只差一个步骤，在不使用`CA`模块时构造`SampleUser`时我们要自己实现`getEnrollment`的方法，在该方法中需要我们自定义去加载私钥以及证书字节，但是有了`CA`模块后我们只需要在构建`Sampleuser`时调用
```bash
user1.setEnrollment(hfcaClient.enroll(user1.getName(), "admin")); 
```
即可实现从`CA`中获取到私钥以及证书，发起`ECer`交易。详细的测试可以参见代码`FabricExample/src/test/java/ijarvis/intelliq/FabricCA/FabricCATestUseCAServer.java`


## 三、链码操作

本节主要介绍测试环境中链码的安装部署与实例化操作，希望通过该方法可以快速的验证测试链码环境中的问题

```bash
# 安装链码
Shell> peer chaincode install -n epointchaincodecommon   -p epointchaincodecommon -v 0.1
# 实例化链码操作
Shell> peer chaincode instantiate -o orderer.example.com:7050  -C $CHANNEL_NAME  -c '{"Args":["init"]}' -P "OR  ('Org1MSP.member','Org2MSP.member')" -n epointchaincodecommon -v 0.1
# 调用插入KV操作链码
Shell> peer chaincode invoke -o orderer.example.com:7050   -C $CHANNEL_NAME -n epointchaincodecommon -v 0.1 -c '{"Args":["addkv","liuwenru","刘文儒"]}'
# 调用更新KV操作链码
Shell> peer chaincode invoke -o orderer.example.com:7050   -C $CHANNEL_NAME -n epointchaincodecommon -v 0.1 -c '{"Args":["updatekv","liuwenru","刘美丽"]}'
# 调用查询KV操作链码
Shell> peer chaincode invoke -o orderer.example.com:7050   -C $CHANNEL_NAME -n epointchaincodecommon -v 0.1 -c '{"Args":["query","liuwenru"]}'
# 根绝给定的Key查询该账本中所有的历史操作
Shell> peer chaincode invoke -o orderer.example.com:7050   -C $CHANNEL_NAME -n epointchaincodecommon -v 0.1 -c '{"Args":["queryhistory","liuwenru"]}'
# 调用删除链码操作，注意，此删除操作只会删除账本中当前的值，对于账本中的历史值是不会删除的
Shell> peer chaincode invoke -o orderer.example.com:7050   -C $CHANNEL_NAME -n epointchaincodecommon -v 0.1 -c '{"Args":["delkv","liuwenru"]}'
```

