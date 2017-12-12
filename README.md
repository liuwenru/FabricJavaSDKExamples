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

使用`SDk`调用链码







