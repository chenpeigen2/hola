#!/bin/bash
cd ./fabric-samples/test-network
sudo su
./network.sh up createChannel -c mychannel -ca
./network.sh deployCC -ccn basic -ccp ../asset-transfer-basic/chaincode-java/ -ccl java