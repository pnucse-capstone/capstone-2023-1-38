 
 
const express = require("express");
const router = express.Router();
var co = require('co');
var Fabric_Client = require('fabric-client');
var path = require('path');
var util = require('util');
var os = require('os');

 
 
var fabric_client = new Fabric_Client();
var channel = fabric_client.newChannel('tracechannel');
var order = fabric_client.newOrderer('grpc://localhost:7050');
var peer = fabric_client.newPeer('grpc://localhost:7051');
var member_user = null;
var store_path = path.join(__dirname, '../../hfc-key-store');
channel.addPeer(peer);
channel.addOrderer(order);

 
 
router.get("/queryMachiningByCropsId", async function (req,res) {
  try {
     
    var state_store = await Fabric_Client.newDefaultKeyValueStore({path: store_path});
     
    fabric_client.setStateStore(state_store);
   
    /* use the same location for the state store (where the users' certificate are kept)
    * and the crypto store (where the users' keys are kept)
    */  
    var crypto_suite = Fabric_Client.newCryptoSuite();
    var crypto_store = Fabric_Client.newCryptoKeyStore({path: store_path});
    crypto_suite.setCryptoKeyStore(crypto_store);
    fabric_client.setCryptoSuite(crypto_suite);

     
     
    var user_from_store = await fabric_client.getUserContext('user1', true);
     
    if (user_from_store && user_from_store.isEnrolled()) {
       
      console.log('Successfully loaded user1 from persistence');
      member_user = user_from_store;
    } else {
       
      throw new Error('Failed to get user1.... run registerUser.js');
    }
    const request = {
       
       
      chaincodeId: 'materialcc',
       
      fcn: 'queryMachiningByCropsId',
       
      args: [req.query.id]
    };

     
     
    var query_responses = await channel.queryByChaincode(request);
    console.log("Query has completed, checking results");
     
     
    if (query_responses && query_responses.length === 1) {
       
      if (query_responses[0] instanceof Error) {
        res.send(query_responses[0])
         
        console.error("error from query = ", query_responses[0]);
      } else {
         
        res.send(query_responses[0].toString())
        console.log("Response is ", query_responses[0].toString());
      }
    } else {
       
      console.log("No payloads were returned from query");
    }
  }catch (e) {
     
    console.error('Failed to query successfully :: ' + e);
  }
});


 
 
router.post("/createMaching", async function(req,res){
   
  console.log("machingInfo    "+JSON.stringify(req.body.checkInfoArray));
  try {
     
    var state_store = await Fabric_Client.newDefaultKeyValueStore({path: store_path});
     
    fabric_client.setStateStore(state_store);
     
    var crypto_suite = Fabric_Client.newCryptoSuite();
     
    var crypto_store = Fabric_Client.newCryptoKeyStore({path: store_path});
     
    crypto_suite.setCryptoKeyStore(crypto_store);
     
    fabric_client.setCryptoSuite(crypto_suite);
     
    var user_from_store = await fabric_client.getUserContext('user1', true);
    
     
    if (user_from_store && user_from_store.isEnrolled()) {
       
      console.log('Successfully loaded user1 from persistence');
      member_user = user_from_store;
    } else {
       
      throw new Error('Failed to get user1.... run registerUser.js');
    }
     
    let tx_id = fabric_client.newTransactionID();
     
    const request = {
      chaincodeId: 'materialcc',  
      fcn: 'createMachining',  
      txId: tx_id,  
      args: req.body.checkInfoArray,  
      chainId: 'tracechannel'  
    };
     
     
    let chaincodeinvokeresult  = await channel.sendTransactionProposal(request);
     
    var proposalResponses = chaincodeinvokeresult[0];
     
    var proposal = chaincodeinvokeresult[1];
     
    var header = chaincodeinvokeresult[2];
    var all_good = true;
     
    for(var i in proposalResponses){
      let one_good = false;
       
      if(proposalResponses && proposalResponses[0].response && proposalResponses[0].response.status === 200){
        one_good = true;
        console.info('transaction proposal was good');
      }else{
        console.error('transaction proposal was bad');
      }
       
      all_good = all_good & one_good;
    }
    if(all_good){
       
      console.info(util.format(
        'Successfully : Status - %s, message - %s, metadata - %s, endorsement signature - %s',
        proposalResponses[0].response.status,  
        proposalResponses[0].response.message,  
        proposalResponses[0].response.payload,  
        proposalResponses[0].endorsement.signature  
        ));
         
        var request2 = {
          proposalResponses: proposalResponses, 
          proposal: proposal,  
          header: header  
        };
         
        channel.sendTransaction(request2)
        const resBody = {
          status:200,  
          msg:"데이터 블록체인에 성공적으로 업로드되었습니다"
        };
         
        res.send(resBody);
      }
    }catch (e) {
       
      console.error('Failed to query successfully :: ' + e);
    }
  });

module.exports = router;

