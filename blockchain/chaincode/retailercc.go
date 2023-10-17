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

 
 
 
type Retailer struct {       
        ProductId               string `json:"product_id"`       
	CropsId                 string `json:"crops_id"`         
	RetailerName            string `json:"retailer_name"`    
	RetailerTel             string `json:"retailer_tel"`     
	RetailerId              string `json:"retailer_id"`      
	Retailer                string `json:"retailer"`         
	Remarks                 string `json:"remarks"`          
}

/*
 * The Init method is called when the Smart Contract "retailercc" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger(
 */
 
 
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
        return shim.Success(nil)
}

/*
 * The Invoke method is called as a result of an application request to run the Smart Contract "retailercc"
 * The calling application program has also specified the particular smart contract function to be called, with arguments
*/
 
 
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {
        function, args := APIstub.GetFunctionAndParameters()
        if function == "queryRetailerById" { 
             return s.queryRetailerById(APIstub, args)
        }else if function == "initLedger" {  
             return s.initLedger(APIstub)
        }else if function == "createRetailer" { 
             return s.createRetailer(APIstub, args)
        }else if function == "queryRetailerByCropsId" {  
             return s.queryRetailerByCropsId(APIstub,args)
        }
        return shim.Error("Invalid Smart Contract function name.")
}

/**
 * 상품ID조회
 */
func (s *SmartContract) queryRetailerById(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
        if len(args) != 1 {
                return shim.Error("Incorrect number of arguments. Expecting 1")
        }
         
        cropsAsBytes, _ := APIstub.GetState(args[0])
        return shim.Success(cropsAsBytes)
}

 
 
func (s *SmartContract) queryRetailerByCropsId(APIstub shim.ChaincodeStubInterface,args[]string) sc.Response{
        if len(args) != 2 {
                return shim.Error("Incorrect number of arguments. Expecting 2")
        }
         
        CropsId := args[0]
         
	RetailerId := args[1]
         
        queryString := fmt.Sprintf("{\"selector\": {\"crops_id\": {\"$eq\": \"%s\"},\"retailer_id\": {\"$eq\":\"%s\"}}}", CropsId, RetailerId)
         
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
         
        retailer := []Retailer{
                 Retailer{ProductId: "the first product info",
                 CropsId: "crops_id",
                 RetailerName: "Gproject",
		 RetailerId: "steak",
                 RetailerTel: "01000000005",
                 Retailer: "Gp마트",
		 Remarks: "마트초기화",},
                        }
        i := 0
        for i < len(retailer) {
                fmt.Println("i is ", i)
                 
                retailerAsBytes, _ := json.Marshal(retailer[i])
                 
                APIstub.PutState(retailer[i].ProductId, retailerAsBytes)
                fmt.Println("Added", retailer[i])
                i = i + 1
        }
        return shim.Success(nil)
}


/**
 * 상품정보 입력
 */
func (s *SmartContract) createRetailer(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
         if len(args) != 8 {
                return shim.Error("Incorrect number of arguments. Expecting 7")
         }
          
         var retailer = Retailer{
                ProductId:              args[1], 
                CropsId:                args[2], 
                RetailerName:           args[3],
                RetailerId:             args[4], 
                RetailerTel:            args[5],
                Retailer:               args[6], 
                Remarks:                args[7]
        }
          
         retailerAsBytes, _ := json.Marshal(retailer)
          
         APIstub.PutState(args[0], retailerAsBytes)
         return shim.Success(nil)
}

func main() {
         
        err := shim.Start(new(SmartContract))
        if err != nil {
                fmt.Printf("Error creating new Smart Contract: %s", err)
        }

}

