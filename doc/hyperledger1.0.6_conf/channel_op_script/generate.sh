#!/bin/bash - 
#===============================================================================
#
#          FILE: generate.sh
# 
#         USAGE: ./generate.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: YOUR NAME (), 
#  ORGANIZATION: 
#       CREATED: 04/03/2018 11:31
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error


cryptogen generate --config=../crypto-config.yaml

