/*
* 블록체인 정보 조회
*/

 
 
const express = require("express");
const router = express.Router();
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
var tx_id = null;
channel.addOrderer(order);
channel.addPeer(peer);

 
 
router.get("/channelBlockInfo",async function (req,res) {
try {
   
  var state_store = await Fabric_Client.newDefaultKeyValueStore({path: store_path});
   
  fabric_client.setStateStore(state_store);

  /* use the same location for the state store (where the users' certificate are kept)
  * and the crypto store (where the users' keys are kept)
  * 상태 저장소에 동일한 위치 사용 (사용자 인증서 보관) 
  * 암호화 저장소에 동일한 위치 사용(사용자 키 보관) 
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
     
    throw new Error('Failed to get user1....');
  }
   
   
  var query_responses = await channel.queryInfo(peer);
   
  res.send(query_responses)
}catch (e) {
  console.error('Failed to query successfully :: ' + e);
}

});

 
 
router.get("/queryBlockInfo",async function(req,res){
 
console.log("number   "+req.query.number)
try {
   
  var state_store = await Fabric_Client.newDefaultKeyValueStore({path: store_path});
   
  fabric_client.setStateStore(state_store);

  /* use the same location for the state store (where the users' certificate are kept)
  * and the crypto store (where the users' keys are kept)
  * 상태 저장소에 동일한 위치 사용 (사용자 인증서 보관) 
  * 암호화 저장소에 동일한 위치 사용(사용자 키 보관) 
  */  
  var crypto_suite = Fabric_Client.newCryptoSuite();  
  var crypto_store = Fabric_Client.newCryptoKeyStore({path: store_path});  
  crypto_suite.setCryptoKeyStore(crypto_store);  
  fabric_client.setCryptoSuite(crypto_suite); 

   
   
  var user_from_store = await fabric_client.getUserContext('user1', true);
   
  if (user_from_store && user_from_store.isEnrolled()) {
     
    console.log('Successfully loaded user1 from persistence.');
    member_user = user_from_store;
  } else {
     
    throw new Error('Failed to get user1.... ');
  }

   
   
  var query_responses = await channel.queryBlock(Number(req.query.number),peer,null);
   
  res.send(query_responses)
}catch (e) {
  console.error('Failed to query successfully :: ' + e);
}

});


 
 
router.get("/queryBlockBuHash",async function(req,res){
 
console.log("number   "+req.query.hash)
try {
   
  var state_store = await Fabric_Client.newDefaultKeyValueStore({path: store_path});
   
  fabric_client.setStateStore(state_store);

    /* use the same location for the state store (where the users' certificate are kept)
    * and the crypto store (where the users' keys are kept)
    * 상태 저장소에 동일한 위치 사용 (사용자 인증서 보관) 
    * 암호화 저장소에 동일한 위치 사용(사용자 키 보관) 
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
     
    throw new Error('Failed to get user1....');
  }

   
   
  var query_responses = await channel.queryBlockByHash(Buffer.from(req.query.hash, "hex"),peer);
   
  res.send(query_responses)
}catch (e) {
  console.error('Failed to query successfully :: ' + e);
}

});

module.exports = router;
