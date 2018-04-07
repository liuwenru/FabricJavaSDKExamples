#!/bin/bash - 
#===============================================================================
#
#          FILE: config.sh
# 
#         USAGE: ./config.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: YOUR NAME (), 
#  ORGANIZATION: 
#       CREATED: 04/03/2018 16:00
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error



export CHANNEL_NAME=epointchannel
export FABRIC_CFG_PATH=$PWD
configtxgen -profile TwoOrgsOrdererGenesis -outputBlock ./genesis.block
configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./${CHANNEL_NAME}.tx -channelID $CHANNEL_NAME




export CORE_PEER_MSPCONFIGPATH=/root/hyperledger1.0.6_conf/crypto-config/peerOrganizations/city1.epoint.com.cn/users/Admin\@city1.epoint.com.cn/msp
export CORE_PEER_ADDRESS=peer0.city1.epoint.com.cn:7051
export CORE_PEER_LOCALMSPID="city1MSP"
peer channel create -o orderer1.epoint.com.cn:7050 -c $CHANNEL_NAME -f ./${CHANNEL_NAME}.tx











