#!/bin/bash
./network.sh up createChannel -c mychannel -ca

# To deploy the TypeScript chaincode implementation
./network.sh deployCC -ccn basic -ccp ../asset-transfer-basic/chaincode-typescript/ -ccl typescript

# To deploy the Go chaincode implementation
./network.sh deployCC -ccn basic -ccp ../asset-transfer-basic/chaincode-go/ -ccl go

# To deploy the Java chaincode implementation
./network.sh deployCC -ccn basic -ccp ../asset-transfer-basic/chaincode-java/ -ccl java

./network.sh down