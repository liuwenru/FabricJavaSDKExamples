#!/bin/bash - 
#===============================================================================
#
#          FILE: configtxgen_genesisblock.sh
# 
#         USAGE: ./configtxgen_genesisblock.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: YOUR NAME (), 
#  ORGANIZATION: 
#       CREATED: 04/03/2018 11:33
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error

export FABRIC_CFG_PATH=$PWD/../

configtxgen -profile TwoOrgsOrdererGenesis -outputBlock ./genesis.block
