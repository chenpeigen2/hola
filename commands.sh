#!/bin/bash
./network.sh up createChannel -c mychannel -ca

# To deploy the TypeScript chaincode implementation
./network.sh deployCC -ccn basic -ccp ../asset-transfer-basic/chaincode-typescript/ -ccl typescript

# To deploy the Go chaincode implementation
./network.sh deployCC -ccn basic -ccp ../asset-transfer-basic/chaincode-go/ -ccl go

# To deploy the Java chaincode implementation
./network.sh deployCC -ccn basic -ccp ../asset-transfer-basic/chaincode-java/ -ccl java

./network.sh deployCC -ccn basic -ccp ../../chaincode-java/ -ccl java

./network.sh down



./apache-jmeter-5.6.2/bin/jmeter.sh -n -t ./jmx/sgx_chaincode测试计划.jmx -l result.jtl -e -o ./ResultReport

docker logs -f c746f3da0d13 | grep "代码执行时长-chaincode" > chaincode.txt