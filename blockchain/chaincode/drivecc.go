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

 
 
type Transport struct {
	TransportId          string `json:"transport_id"`             
	DriverId             string `json:"driver_id"`                
	DriverName           string `json:"driver_name"`              
	DriverTel            string `json:"driver_tel"`               
	DriverDept           string `json:"driver_dept"`              
	CropsId              string `json:"crops_id"`                 
	TransportToChainTime string `json:"transport_to_chain_time"`  
	TransportToAddress   string `json:"transport_to_address"`     
	Remarks              string `json:"remarks"`                  
}

/*
 * The Init method is called when the Smart Contract "drivercc" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger()
 */
 
 
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

/*
 * The Invoke method is called as a result of an application request to run the Smart Contract "drivercc"
 * The calling application program has also specified the particular smart contract function to be called, with arguments
 */
 
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {
	 
	function, args := APIstub.GetFunctionAndParameters()
	if function == "queryTransportByCropsId" {  
		return s.queryTransportByCropsId(APIstub, args)
	} else if function == "initLedger" {  
		return s.initLedger(APIstub)
	} else if function == "createTransport" {  
		return s.createTransport(APIstub, args)
	} else if function == "queryTransportById" {  
		return s.queryTransportById(APIstub, args)
	}
	return shim.Error("Invalid Smart Contract function name.")
}

 
func (s *SmartContract) queryTransportByCropsId(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	 
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
 * 운송id에따라 운송정보 조회
 */
 func (s *SmartContract) queryTransportById(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	 
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	 
	 
	cropsAsBytes, _ := APIstub.GetState(args[0])
	
	 
	return shim.Success(cropsAsBytes)
}


/**
 * 초기화
 */
func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	 
	transport := []Transport{
		Transport{
			TransportId: "the first transportId",
			DriverId:             "2319492349",
			DriverName:           "기사1",
			DriverTel:            "01000000003",
			DriverDept:           "Gproject회사",
			CropsId:              "123456",
			TransportToChainTime: "2023.7.10",
			TransportToAddress:   "한국",
			Remarks:              "운송초기화"
		},
	}
	 
	i := 0
	for i < len(transport) {
		fmt.Println("i is ", i)
		 
		transportAsBytes, _ := json.Marshal(transport[i])
		 
		APIstub.PutState(transport[i].TransportId, transportAsBytes)
		fmt.Println("Added", transport[i])
		i = i + 1
	}
	
	 
	return shim.Success(nil)
}

/**
 * 운송전보 입력
 */
 func (s *SmartContract) createTransport(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	 
	if len(args) != 10 {
		return shim.Error("Incorrect number of arguments. Expecting 10")
	}
	 
	var transport = Transport{
		TransportId:           args[1],
		DriverId:              args[2],
		DriverName:            args[3],
		DriverTel:             args[4],
		DriverDept:            args[5],
		TransportToChainTime:  args[6],
		TransportToAddress:    args[7],
		CropsId:               args[8],
		Remarks:               args[9],
	}
	 
	transportAsBytes, _ := json.Marshal(transport)

	/* 변환된 JSON 바이트 배열 'transportAsBytes'를 블록체인에 저장합니다.
	 * 이때, 'args[0]' (운영 ID)를 키로 사용하여 저장합니다.
	 * 이렇게 하면 매번 이 메서드를 호출할 때마다 새로운 물류 정보가 블록체인에 기록됩니다.
	 */
	 APIstub.PutState(args[0], transportAsBytes)

	 
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

