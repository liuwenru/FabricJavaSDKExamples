package main

import (
	"encoding/json"
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type SimpleChaincode struct {
}
//记录当前链码上有多少个记录
var RNO = 0
var ChainCodeVersion = "v0.1"

type Record struct {
	Key       string
	Value       string
}

/*
  智能合约初始化调用,一个链码版本只运行一次

*/
func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
        fmt.Println("Init ChainCode...............",ChainCodeVersion)
	return shim.Success(nil)
}

/*
	智能合约发起请求时调用 {"Args":["invoke","a","b","10"]}
	func (stub *ChaincodeStub) GetFunctionAndParameters() (function string, params []string)
  返回参数 function 标记调用方法的名称"invoke"
	返回参数 params   标记调用方法的参数数组["a","b","10"]
*/

func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	// 获取请求调用智能合约的方法和参数
	function, args := stub.GetFunctionAndParameters()
        fmt.Println("recive params....",function,"          ",args)
	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "addkv" {
        	fmt.Println("run function addcard",function,"          ",args)
		return t.addkv(stub, args)
	} else if function == "updatekv" {
        	fmt.Println("run function updatecard",function,"          ",args)
		return t.updatekv(stub, args)
	} else if function == "delkv" {
        	fmt.Println("run function deletecard",function,"          ",args)
		return t.delkv(stub, args)
	} else if function == "query" {
        	fmt.Println("run function query",function,"          ",args)
		return t.query(stub, args)
	} else if function == "queryhistory" {
        	fmt.Println("run function queryhistory",function,"          ",args)
		return t.queryhistory(stub, args)
	}
	return shim.Success(nil)
}

/*
  向链码中添加一个KV结构
*/
func (t *SimpleChaincode) addkv(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("向账本中添加k-v   ",args[0],args[1])
	var pertmp Record
	pertmp = Record{Key: args[0], Value: args[1]}
	fmt.Println("Record is ",pertmp)
	jsonrecode,_ := json.Marshal(&pertmp)
	err:= stub.PutState(pertmp.Key, jsonrecode)
        fmt.Println("put key is ", pertmp.Key)
        if err != nil {
        	return shim.Error("Error retrieving data")
	}
	return shim.Success(jsonrecode)
}

/*
	向链码中已经存在的记录做一次更新操作

*/
func (t *SimpleChaincode) updatekv(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	t.addkv(stub,args)
	return shim.Success(nil)
}

/*
	删除链码中的一个记录,调用DelState函数操作
*/
func (t *SimpleChaincode) delkv(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("删除账本中的一条数据     Keys:",args[0])
	deletekey:=args[0]
	err:=stub.DelState(deletekey)
        if err != nil {
        	return shim.Error("Error retrieving data")
	}
	fmt.Println("删除成功")
	return shim.Success(nil)
}

/*
	查询链码中的数据
*/
func (t *SimpleChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("查询账本中的数据 Keys:",args[0])
	queryparams:=args[0]
        redBytes, err := stub.GetState(queryparams)
	if err != nil {
		shim.Error("Error retrieving data")
	}
        fmt.Println("query resoult"+string(redBytes))
	return shim.Success(redBytes)
}
/*
	根据给定的键key，查询该键的所有历史记录

*/
func (t *SimpleChaincode) queryhistory(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var records []Record
	fmt.Println("query key histroy......................",args[0])
	historyIer, err := stub.GetHistoryForKey(args[0])
	if err != nil {
	    fmt.Println(err)
	    return shim.Error("query error")
	}
	for historyIer.HasNext() {
	    var tmp Record=Record{}
	    modification, err := historyIer.Next()
	    if err != nil {
	        fmt.Println(err)
	        return shim.Error("query error")
	    }
	    fmt.Println("Returning information about", string(modification.Value))
	    err=json.Unmarshal(modification.Value,&tmp)
	    records=append(records,tmp)
	}
	jsonrecode,_ := json.Marshal(&records)
	return shim.Success(jsonrecode)
}
func main() {
	fmt.Println("Fabric Epoint Common Chain Code ..........")
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
