var express = require('express');
var router = express.Router();
const dbHelper = require('../database/dbHelper');
var nodemailer = require("nodemailer");
var smtpTransport = nodemailer.createTransport({
  service: 'Gmail',
  auth: {
    user: 'study.simpleSNS@gmail.com',
    pass: 'simple123SNS'
  }
});

router.post('/send', function(req, res, next) {
  var email = req.body.to;
  var rand = Math.floor((Math.random() * 9000 + 1000));
  var link="http://"+req.get("host")+"/email/verify?email="+email+"&num="+rand;
  var mailOptions={
      to : email,
      subject : "Please confirm your Email account",
      html : "Hello,<br> Please Click on the link to verify your email.<br><a href="+link+">Click here to verify</a>" 
  }
  smtpTransport.sendMail(mailOptions, function(error, response){
    if(error){
      console.log(error);
      res.json({result:"error",error:error});
    }else{
      console.log("Message sent: " + response.message);
      dbHelper.emailRequest(email,rand);
      res.json({result:"success"});
    }
  });
});
router.get('/verify',async function(req,res){
  var result = await dbHelper.verify(req.query.email,req.query.num);
  if(result==true){
    console.log("email is verified");
    dbHelper.verifyUpdate(req.query.email,req.query.num);
    res.json({result:"success"});
  } else
  {
      console.log("email is not verified");
      res.json({result:"failed"});
  }
});
router.post('/register',async function(req,res){
    if(req.body.email==undefined||req.body.pass==undefined||req.body.nick==undefined){
        res.json({result:"failed",error:"undefined parameter"});
    } else {
        var result = await dbHelper.emailRegister(req.body.email,req.body.pass,req.body.nick,req.body.devid);
        res.json(result);
    }
});
router.post('/login',async function(req,res){
    if(req.body.email==undefined||req.body.pass==undefined){
        res.json({result:"failed",error:"undefined parameter"});
    } else {
        var result = await dbHelper.emailLogin(req.body.email,req.body.pass,req.body.devid);
        res.json(result);
    }
});
module.exports = router;
