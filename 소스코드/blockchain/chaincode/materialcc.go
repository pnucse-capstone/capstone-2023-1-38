package main

/* Imports
 * 4 utility libraries for formatting, handling bytes, reading and writing JSON, and string manipulation
 * 2 specific Hyperledger Fabric specific libraries for Smart Contracts
 */

import (
        "bytes" 
        "encoding/json"
        "fmt"
        
        "github.com/hyperledger/fabric/core/chaincode/shim"
        sc "github.com/hyperledger/fabric/protos/peer"
)

 
 
type SmartContract struct {
}

 
 
type Machining struct {       
        MachiningId             string `json:"machining_id"`     
	Leader                  string `json:"leader"`           
	CropsId                 string `json:"crops_id"`         
	LeaderTel               string `json:"leader_tel"`       
	FactoryName             string `json:"factory_name"`     
	TestingResult           string `json:"testing_result"`   
	InFactoryTime           string `json:"in_factory_time"`  
	OutFactoryTime          string `json:"out_factory_time"` 
	TestingPhotoUrl         string `json:"testing_photo_url"` 
	Remarks                 string `json:"remarks"`          
}

/*
 * The Init method is called when the Smart Contract "materialcc" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger()
 */
 
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
        return shim.Success(nil)
}

/*
 * The Invoke method is called as a result of an application request to run the Smart Contract "farmercc"
 * The calling application program has also specified the particular smart contract function to be called, with arguments
*/
 
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {
         
        function, args := APIstub.GetFunctionAndParameters()
         
        if function == "queryMachiningByCropsId" { 
             return s.queryMachiningByCropsId(APIstub, args)
        }else if function == "initLedger" { 
             return s.initLedger(APIstub)
        }else if function == "createMachining" { 
             return s.createMachining(APIstub, args)
        }else if function == "queryMachiningById" {  
             return s.queryMachiningById(APIstub,args)
        }
        return shim.Error("Invalid Smart Contract function name.")
}

/**
 * 가공 ID에 따라 품질 정보조회
 */
func (s *SmartContract) queryMachiningById(APIstub shim.ChaincodeStubInterface,args[]string) sc.Response {
        if len(args) != 1 {
                return shim.Error("Incorrect number of arguments. Expecting 1")
        }
         
        cropsAsBytes, _ := APIstub.GetState(args[0])
        return shim.Success(cropsAsBytes)  
}

 
func (s *SmartContract) queryMachiningByCropsId(APIstub shim.ChaincodeStubInterface,args[]string) sc.Response{
        if len(args) != 1 {
                return shim.Error("Incorrect number of arguments. Expecting 1")
        }
         
        CropsId := args[0]
         
        queryString := fmt.Sprintf("{\"selector\":{\"crops_id\":{\"$eq\":\"%s\"}}}", CropsId)
	 
        resultsIterator, err := APIstub.GetQueryResult(queryString)
        if err != nil {
                return shim.Error(err.Error())
        }
        defer resultsIterator.Close() 

         
        var buffer bytes.Buffer
        buffer.WriteString("[")

        bArrayMemberAlreadyWritten := false
        for resultsIterator.HasNext() {
                queryResponse, err := resultsIterator.Next()
                if err != nil {
                        return shim.Error(err.Error())
                }
                 
                if bArrayMemberAlreadyWritten == true {
                        buffer.WriteString(",")
                }
		 
		 
                buffer.WriteString("{\"Key\":")
                buffer.WriteString("\"")
                buffer.WriteString(queryResponse.Key)
                buffer.WriteString("\"")

                buffer.WriteString(", \"Record\":")
                 
                buffer.WriteString(string(queryResponse.Value))
                buffer.WriteString("}")
                bArrayMemberAlreadyWritten = true
        }
        buffer.WriteString("]")

        fmt.Printf("- queryAllCars:\n%s\n", buffer.String())
	
         
        return shim.Success(buffer.Bytes())
}

/**
 * 초기화
 */
func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	 
        machining := []Machining{
                 Machining{
                        MachiningId: "the first maching",
                        Leader: "Gproject",
                        LeaderTel: "01000000000",
                        FactoryName: "토미토원료공장",
                        CropsId: "123456",
                        TestingResult: "통과",
                        InFactoryTime: "2023.7.10",
                        OutFactoryTime: "2023.7.10",
                        TestingPhotoUrl: "품질검사사진",
                        Remarks: "원료공장초기화"
                },
        }
	 
        i := 0
        for i < len(machining) {
                fmt.Println("i is ", i)
		 
                machiningAsBytes, _ := json.Marshal(machining[i])
		 
                APIstub.PutState(machining[i].MachiningId, machiningAsBytes)
                fmt.Println("Added", machining[i])
                i = i + 1
        }
	 
        return shim.Success(nil)
}


/**
 *添加原料厂商质检信息 
 * 품질정보 추가
 */
func (s *SmartContract) createMachining(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
         if len(args) != 11 {
                return shim.Error("Incorrect number of arguments. Expecting 11")
         }
	 
         var machining = Machining{
                MachiningId:            args[1], 
                Leader:                 args[2], 
                LeaderTel:              args[3],
                FactoryName:            args[4],
                CropsId:                args[5], 
                TestingResult:          args[6],
                InFactoryTime:          args[7],
                OutFactoryTime:         args[8],
                TestingPhotoUrl:        args[9],
                Remarks:                args[10]
        }
	 
         machiningAsBytes, _ := json.Marshal(machining)
	/* 변환된 JSON 바이트 배열 'machiningAsBytes'를 블록체인에 저장합니다.
	 * 이때, 'args[0]' (가공 ID)를 키로 사용하여 저장합니다.
	 * 이렇게 하면 매번 이 메서드를 호출할 때마다 새로운 폼질검사 정보가 블록체인에 기록됩니다.
	 */	
         APIstub.PutState(args[0], machiningAsBytes)
         return shim.Success(nil)
}

func main() {
	/* 스마트 계약의 인스턴스를 생성하여 'SmartContract'의 새로운 객체를 생성합니다.
	 * 이 객체는 스마트 계약의 모든 메서드를 포함하고 있습니다.
	 * 스마트 계약의 모든 로직과 블록체인과 상호작용하는 데 사용되는 코드가 포함되어 있습니다.
	 * 이 객체를 통해 스마트 계약의 함수들을 호출할 수 있습니다.
	*/
	err := shim.Start(new(SmartContract))
	if err != nil {
		 
		fmt.Printf("Error creating new Smart Contract: %s", err)
	}
}

