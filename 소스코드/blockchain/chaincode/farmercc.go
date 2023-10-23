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

 
 
type Crops struct {	
	CropsId 			string `json:"crops_id"` 			 
	CropsName 			string `json:"crops_name"`			 
	Address 			string `json:"address"`				 
	RegisterTime 		string `json:"register_time"`		 
	Year 				string `json:"year"`				 
	FarmerName			string `json:"farmer_name"`			 
	FarmerID 			string `json:"farmer_id"`			 
	FarmerTel 			string `json:"farmer_tel"`			 
	FertilizerName 		string `json:"fertilizer_name"`		 
	PlatMode 			string `json:"plant_mode"`			 
	greenhouse		string `json:"greenhouse_status"` 		 
	GrowSeedlingsCycle 	string `json:"grow_seedlings_cycle"` 
	IrrigationCycle 	string `json:"irrigation_cycle"`	 
	ApplyFertilizerCycle 	string `json:"apply_fertilizer_cycle"`  
	WeedCycle 			string `json:"weed_cycle"`			 
	Remarks 			string `json:"remarks"` 			 
}

 
 
 
type CropsGrowInfo struct {	
	CropsGrowId 		string `json:"crops_grow_id"`		 
	CropsBakId 			string `json:"crops_bak_id"`		 
	RecordTime 			string `json:"record_time"`			 
	CropsGrowPhotoUrl 	string `json:"crops_grow_photo_url"` 
	Temperature 		string `json:"temperature"`			 
	GrowStatus 			string `json:"grow_status"` 		 
	WaterContent 		string `json:"water_content"`		 
	IlluminationStatus 	string `json:"illumination_status"`	 
	Remarks 			string `json:"remarks"`				 
}

/*
 * The Init method is called when the Smart Contract "farmercc" is instantiated by the blockchain network
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
	 
	if function == "queryCropsById" { 
		return s.queryCropsById(APIstub, args)
	} else if function == "initLedger" {  
		return s.initLedger(APIstub)
	} else if function == "createCrops" { 
		return s.createCrops(APIstub, args) 
	} else if function == "queryCropsProcessByCropsId" { 
		return s.queryCropsProcessByCropsId(APIstub, args) 
	} else if function == "recordCropsGrow" { 
		return s.recordCropsGrow(APIstub, args) 
	}
	return shim.Error("Invalid Smart Contract function name.")
}

/**
 * 농산물 ID에 따라 정보조회
 */
func (s *SmartContract) queryCropsById(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	 
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}
	 
	cropsAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(cropsAsBytes)
}

 
func (s *SmartContract) queryCropsProcessByCropsId(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}
	 
	CropsBakId := args[0]
	 
	queryString := fmt.Sprintf("{\"selector\":{\"crops_bak_id\":{\"$eq\":\"%s\"}}}", CropsBakId)
	 
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

	 
	fmt.Printf("- queryAllCrops:\n%s\n", buffer.String())

	 
	return shim.Success(buffer.Bytes())
}

/**
 * 初始化账本
 * 초기화
 */
func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	 
	crops := []Crops{
		Crops{CropsId: "steak_liu_first_record",
			CropsName:            "tomato",
			Address:              "Busan",
			RegisterTime:         "2023.7.8",
			Year:                 "2023",
			FarmerName:           "Gproject",
			FarmerID:             "2319492349",
			FertilizerName:       "비료",
			PlatMode:             "토양재배",
			greenhouseStatus:        "yes",
			GrowSeedlingsCycle:   "30일",
			IrrigationCycle:      "4일",
			ApplyFertilizerCycle: "배료없음",
			WeedCycle:            "5일",
			Remarks:              "유기농"},
	}
	 
	i := 0 
	for i < len(crops) { 
		fmt.Println("i is ", i)
		 
		cropsAsBytes, _ := json.Marshal(crops[i]) 
		 
		APIstub.PutState(crops[i].CropsId, cropsAsBytes)
		fmt.Println("Added", crops[i])
		i = i + 1
	}
	 
	return shim.Success(nil)
}

/*
 * 농산물 입력
 */
 func (s *SmartContract) createCrops(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) != 16 {
	 
	return shim.Error("Incorrect number of arguments.")
	}

	var crops = Crops{
		CropsId: 				args[1], 
		CropsName: 				args[2], 
		Address: 				args[3], 
		FarmerName: 			args[4], 
		FertilizerName: 		args[5], 
		PlatMode: 				args[6], 
		greenhouseStatus: 			args[7], 
		GrowSeedlingsCycle:	 	args[8], 
		IrrigationCycle: 		args[9], 
		ApplyFertilizerCycle: 	args[10], 
		WeedCycle: 				args[11], 
		Remarks: 				args[12], 
		RegisterTime: 			args[13], 
		Year: 					args[14], 
		FarmerTel: 				args[15]
	}
	 
	cropsAsBytes, _ := json.Marshal(crops)
	/* 변환된 JSON 바이트 배열 'cropsAsBytes'를 블록체인에 저장합니다.
	 * 이때, 'args[0]' (농산물 ID)를 키로 사용하여 저장합니다.
	 * 이렇게 하면 매번 이 메서드를 호출할 때마다 새로운 농산물 정보가 블록체인에 기록됩니다.
	 */
	APIstub.PutState(args[0], cropsAsBytes)
	return shim.Success(nil)
}


/**
 * 농산물 재배 정보 입력
 */
func (s *SmartContract) recordCropsGrow(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) != 10 {
		return shim.Error("Incorrect number of arguments. Expecting 10")
	}

	var cropsGrowInfo = CropsGrowInfo{
		CropsGrowId: 		args[1], 
		CropsBakId: 		args[2], 
		RecordTime: 		args[3], 
		CropsGrowPhotoUrl: 	args[4], 
		Temperature: 		args[5], 
		GrowStatus: 		args[6], 
		WaterContent: 		args[7], 
		IlluminationStatus: args[8], 
		Remarks: 			args[9]
	}
	 
	cropsGrowInfoAsBytes, _ := json.Marshal(cropsGrowInfo)
	
	/* 변환된 JSON 바이트 배열 'cropsGrowInfoAsBytes'를 블록체인에 저장합니다.
	 * 이때, 'args[0]' (농산물 재배 ID)를 키로 사용하여 저장합니다.
	 * 이렇게 하면 매번 이 메서드를 호출할 때마다 새로운 농산물 재배 정보가 블록체인에 기록됩니다.
	 */	
	APIstub.PutState(args[0], cropsGrowInfoAsBytes)
	return shim.Success(nil)
}

 
func (s *SmartContract) queryAllCropsByFarmerID(APIstub shim.ChaincodeStubInterface, args string) sc.Response {

	 
	startKey := "CAR0" 
	endKey := "CAR999" 

 
resultsIterator, err := APIstub.GetStateByRange(startKey, endKey)
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
}
