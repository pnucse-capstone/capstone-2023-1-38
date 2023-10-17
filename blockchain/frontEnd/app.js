 
 
const express = require("express");
const app = new express();
const bodyParser = require('body-parser');

 
 
app.use(bodyParser.urlencoded({ extended: false }));
 
 
app.use(bodyParser.json());

 

 
app.all("*",function(req,res,next){
     
     
    res.header("Access-Control-Allow-Origin","*");
     
     
    res.header("Access-Control-Allow-Headers","content-type");
     
     
    res.header("Access-Control-Allow-Methods","DELETE,PUT,POST,GET,OPTIONS");
    if (req.method.toLowerCase() === 'options')
     
     
    res.send(200); 
    else
        next();
});      

 
 
const route  = require("./route/route");
 
const port = 8080

 
  
app.use("/route",route);
 
app.listen(port, () => console.log(`Example app listening on port ${port}!`))
