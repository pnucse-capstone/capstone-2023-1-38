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

 
 
 
type ProductInfo struct {        
        ProductId               string `json:"product_id"`       
	CropsId                 string `json:"crops_id"`         
	ProductName             string `json:"product_name"`     
	MixedIngredients        string `json:"mixed_ingredients"` 
	Leader                  string `json:"leader"`           
	LeaderTel               string `json:"leader_tel"`       
	Workshop                string `json:"workshop"`         
	Factory                 string `json:"factory"`          
	WorkHours               string `json:"work_hours"`       
	Time                    string `json:"time"`             
	KeepMethod              string `json:"keep_mathod"`      
	NetContent              string `json:"Net_Content"`      
	CookingRecommend        string `json:"cooking_recommend"` 
	Remarks                 string `json:"remarks"`          
}

/*
 * The Init method is called when the Smart Contract "produclnfocc" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger()
 *
 */
 
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
        return shim.Success(nil)
}


 
 
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {
        function, args := APIstub.GetFunctionAndParameters()
        if function == "queryProductInfoById" { 
             return s.queryProductInfoById(APIstub, args)
        }else if function == "initLedger" { 
             return s.initLedger(APIstub)
        }else if function == "createProductInfo" { 
             return s.createProductInfo(APIstub, args)
        }else if function == "queryProductInfoByCropsId" {  
             return s.queryProductInfoByCropsId(APIstub,args)
        }
        return shim.Error("Invalid Smart Contract function name.")
}

/**
 * 제품정보 조회
 */
func (s *SmartContract) queryProductInfoById(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
        if len(args) != 1 {
                return shim.Error("Incorrect number of arguments. Expecting 1")
        }
         
        cropsAsBytes, _ := APIstub.GetState(args[0])
        return shim.Success(cropsAsBytes)
}

 
 
func (s *SmartContract) queryProductInfoByCropsId(APIstub shim.ChaincodeStubInterface,args[]string) sc.Response{
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
         
        productInfo := []ProductInfo{ 
                 ProductInfo{ProductId: "the first product info",
                 CropsId: "crops_id",
                 ProductName: "G제품",
                 MixedIngredients: "간식",
                 Leader: "박사장",
                 LeaderTel: "01000000001",
                 Workshop: "제1공장",
		 Factory: "가공공장1",
		 WorkHours: "36h",
		 Time: "2023.7.11",
		 KeepMethod: "건조",
		 NetContent: "500g",
		 CookingRecommend: "바로 먹기",
		 Remarks: "식품생산공장초기화" },
                        }
        i := 0
        for i < len(productInfo) {
                fmt.Println("i is ", i)
                 
                productInfoAsBytes, _ := json.Marshal(productInfo[i])
                 
                APIstub.PutState(productInfo[i].ProductId, productInfoAsBytes)
                fmt.Println("Added", productInfo[i])
                i = i + 1
        }
        return shim.Success(nil)
}


/**
 * 제품정보 입력
 */
func (s *SmartContract) createProductInfo(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
         if len(args) != 15 {
                return shim.Error("Incorrect number of arguments. Expecting 15")
         }
          
         var productInfo = ProductInfo{
                ProductId:              args[1], 
                CropsId:                args[2], 
                ProductName:            args[3],
                MixedIngredients:       args[4], 
                Leader:                 args[5], 
                LeaderTel:              args[6],
                Workshop:               args[7],
                Factory:                args[8],
                WorkHours:              args[9],
                Time:                   args[10],
                KeepMethod:             args[11],
                NetContent:             args[12],
                CookingRecommend:       args[13],
                Remarks:                args[14]
        }
          
         productInfoAsBytes, _ := json.Marshal(productInfo)
          
         APIstub.PutState(args[0], productInfoAsBytes)
         return shim.Success(nil)
}

func main() {
         
	 
         
         
        err := shim.Start(new(SmartContract))
        if err != nil {
                fmt.Printf("Error creating new Smart Contract: %s", err)
        }

}

