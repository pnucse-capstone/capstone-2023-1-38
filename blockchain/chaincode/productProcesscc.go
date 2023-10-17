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

 
 
 
type Operation struct {  
        OperationId             string `json:"operation_id"`             
	CropsId                 string `json:"crops_id"`                 
	OperationPeopleName     string `json:"operation_people_name"`    
	OperationPeopleTel      string `json:"operation_people_tel"`     
	Time                    string `json:"time"`                     
	WorkContent             string `json:"work_content"`             
	Remarks                 string `json:"remarks"`                  
}

/*
 * The Init method is called when the Smart Contract "produtprocesscc" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger()

 */
 
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
        return shim.Success(nil)
}

/*
 * The Invoke method is called as a result of an application request to run the Smart Contract "productprocesscc"
 * The calling application program has also specified the particular smart contract function to be called, with arguments
 
*/
 
 
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {
        function, args := APIstub.GetFunctionAndParameters()
        if function == "queryOperationInfoById" { 
             return s.queryOperationById(APIstub, args)
        }else if function == "initLedger" { 
             return s.initLedger(APIstub)
        }else if function == "createOperation" { 
             return s.createOperation(APIstub, args)
        }else if function == "queryOperationByCropsId" { 
             return s.queryOperationByCropsId(APIstub,args)
        }
        return shim.Error("Invalid Smart Contract function name.")
}

/**
 * 작업정보 조회
 */
func (s *SmartContract) queryOperationById(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
        if len(args) != 1 {
                return shim.Error("Incorrect number of arguments. Expecting 1")
        }
         
        cropsAsBytes, _ := APIstub.GetState(args[0])
        return shim.Success(cropsAsBytes)
}

 
 
func (s *SmartContract) queryOperationByCropsId(APIstub shim.ChaincodeStubInterface,args[]string) sc.Response{
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
         
        operation := []Operation{
                 Operation{OperationId: "the first operation info",
                 CropsId: "crops_id",
                 OperationPeopleName: "간식",
                 OperationPeopleTel: "01000000002",
		 Time: "2023.7.11",
	         WorkContent: "업무 내용",
		 Remarks: "식품생산공장직원초기화"},
                        }
        i := 0
        for i < len(operation) {
                fmt.Println("i is ", i)
                 
                operationAsBytes, _ := json.Marshal(operation[i])
                 
                APIstub.PutState(operation[i].OperationId, operationAsBytes)
                fmt.Println("Added", operation[i])
                i = i + 1
        }
        return shim.Success(nil)
}


/**
 * 작업 입력
 */
func (s *SmartContract) createOperation(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
         if len(args) != 8 {
                return shim.Error("Incorrect number of arguments. Expecting 8")
         }
          
         var operation = Operation{
                OperationId:            args[1], 
                CropsId:                args[2], 
                OperationPeopleName:    args[3],
                OperationPeopleTel:     args[4],
                Time:                   args[5], 
                WorkContent:            args[6],
                Remarks:                args[7]
        }
          
         operationAsBytes, _ := json.Marshal(operation)
          
         APIstub.PutState(args[0], operationAsBytes)
         return shim.Success(nil)
}

func main() {
         
        err := shim.Start(new(SmartContract))
        if err != nil {
                fmt.Printf("Error creating new Smart Contract: %s", err)
        }

}

