package ijarvis.intelliq.Fabric;

import org.apache.log4j.Logger;
import org.hyperledger.fabric.protos.common.MspPrincipal;
import org.hyperledger.fabric.protos.common.Policies;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *  链码背书策略测试
 */


public class ChaincodeEndorsementPolicyTest {
    private static Logger logger=Logger.getLogger(ChaincodeEndorsementPolicyTest.class);


    @Test
    public void  TestLoadFromYaml() throws IOException, ChaincodeEndorsementPolicyParseException {

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File("/Users/ijarvis/IdeaProjects/FabricJavaSDKExamples/src/test/resources/chaincodeendorsementpolicy.yaml"));
        Policies.SignaturePolicyEnvelope sigPolEnv = Policies.SignaturePolicyEnvelope.parseFrom(chaincodeEndorsementPolicy.getChaincodeEndorsementPolicyAsBytes());
        List<MspPrincipal.MSPPrincipal> identitiesList = sigPolEnv.getIdentitiesList();

        for (MspPrincipal.MSPPrincipal ident : identitiesList) {
            logger.debug(ident.toString());
        }
    }



}
