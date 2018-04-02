#!/bin/bash - 
#===============================================================================
#
#          FILE: peer0city1.epoint.com.cn_env.sh
# 
#         USAGE: ./peer0city1.epoint.com.cn_env.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: YOUR NAME (), 
#  ORGANIZATION: 
#       CREATED: 03/22/2018 17:15
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error


export CHANNELNAME=epointchannel
export FABRIC_CFG_PATH=/root/epoint-hyperledger



export CORE_PEER_LOCALMSPID=city1MSP
export CORE_PEER_ADDRESS=peer0.city1.epoint.com.cn:7051
export CORE_PEER_MSPCONFIGPATH=/root/epoint-hyperledger/crypto-config/peerOrganizations/city1.epoint.com.cn/users/Admin@city1.epoint.com.cn/msp
peer channel update -o order1.epoint.com.cn:7050 -c ${CHANNELNAME} -f ../city1.tx


export CORE_PEER_LOCALMSPID=city2MSP
export CORE_PEER_ADDRESS=peer0.city2.epoint.com.cn:7051
export CORE_PEER_MSPCONFIGPATH=/root/epoint-hyperledger/crypto-config/peerOrganizations/city2.epoint.com.cn/users/Admin@city2.epoint.com.cn/msp
peer channel update -o order1.epoint.com.cn:7050 -c ${CHANNELNAME} -f ../city2.tx


