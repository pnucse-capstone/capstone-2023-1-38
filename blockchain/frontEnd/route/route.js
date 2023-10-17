const express = require("express"); 
const router = express.Router(); 

//########################   BlockExplorer   #############

const Blockchain = require("../sysapi/blockexplorer");
const FarmerApi = require("../sysapi/farmer");
const DriverApi  = require("../sysapi/driver");
const MaterialApi = require("../sysapi/material");
const ProductApi = require("../sysapi/product");
const RetailerApi = require("../sysapi/retailer");

//###########################    use  route      #########################

router.use("/blockexplorerapi",Blockchain); 
router.use("/farmerapi",FarmerApi);  
router.use("/driverapi",DriverApi); 
router.use("/materialapi",MaterialApi); 
router.use("/productapi",ProductApi); 
router.use("/retailerapi",RetailerApi); 

//###########################  exports     ###############################
module.exports = router;
