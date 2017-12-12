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


### 1.3、其他说明
如果觉得所给测试用例代码不够明确，可以参考[官方网站此处的代码示例](https://github.com/hyperledger/fabric-sdk-java/blob/master/src/test/java/org/hyperledger/fabric/sdkintegration/End2endAndBackAgainIT.java)
本代码只是将官方代码进行简化方便做实验以及测试。





