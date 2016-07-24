var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/:registerID', function(req, res, next) {

var gcm = require('node-gcm');
var fs = require('fs');

var message = new gcm.Message({
    collapseKey: 'demo',
    delayWhileIdle: true,
    timeToLive: 3,
    data: {
        title: '스터디 모집을 마감하세요',
        message: '설정하신 인원만큼 사람들이 모였습니다. 스터디 모집을 마감하세요!' ,
        userid : '1022065411234557',
        studyid : '2'
    }
});

var server_api_key = 'AIzaSyCdgHHDKuf33oiPwWWIW3aQbG55MiPsrq0';
var sender = new gcm.Sender(server_api_key);
var registrationIds = [];

var token = req.params.registerID;

registrationIds.push(token);

sender.send(message, registrationIds, 4, function (err, result) {
    console.log(result);
        res.send("success");
});

});

module.exports = router;

