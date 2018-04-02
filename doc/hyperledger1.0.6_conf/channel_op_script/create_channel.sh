#!/bin/bash - 
#===============================================================================
#
#          FILE: create_channel.sh
# 
#         USAGE: ./create_channel.sh 
# 
#   DESCRIPTION: 实现向已有超级账本中创建一个通道用来进行账本的同步
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: YOUR NAME (), 
#  ORGANIZATION: 
#       CREATED: 03/22/2018 16:14
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error

export CHANNELNAME=epointchannel
export CORE_PEER_LOCALMSPID=city1MSP
export CORE_PEER_MSPCONFIGPATH=/root/epoint-hyperledger/crypto-config/peerOrganizations/city1.epoint.com.cn/users/Admin@city1.epoint.com.cn/msp



peer channel create -o order1.epoint.com.cn:7050  -c ${CHANNELNAME} -f ./epointchannel.tx
