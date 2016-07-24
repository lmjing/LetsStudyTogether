var express = require('express');
var mysql = require('mysql');
var router =express.Router();
var gcm = require('node-gcm');
var async = require('async');

var connection = mysql.createConnection({
    user : 'user',
    password : '201301473',
    database : 'soma',
    host : 'aws-rds-mysql.czvbtapsvpjv.us-west-2.rds.amazonaws.com'
});

    

//참여
router.post('/:su1/:su2', function(req, res, next) {

    var tasks = [
   		 function(callback){
            //우선 참여자 테이블에 참여 신청자를 넣는다
            console.log("1함수 : 진입");
            connection.query('insert into Participant(study,name,userid) values (?, ?, ?);',  [req.body.study, req.body.name, req.body.userid],function (error, info) {
                  if (error == null){
                    //인원 파악
                    if(req.params.su1 == req.params.su2)
                     {
                        callback(null,req.body.study);
                     }
                     else{
                        //삽입 성공이고 아직 꽉차지 않음
                        console.log("1함수 : 가득 차지 않았다");
                        res.send("success");
                     }
                  }
            });
        },
        function(study, callback){
           connection.query('select * from Study where studyid = ? ;',study, function (error, cursor) {
	           if (cursor.length > 0) {
	                                        callback(null,cursor[0].writer, study);
	                                } else
	                                        res.status(503).json({
	                                                result : false,
	                                                reason : "Cannot find article"
	                                });
	        });
        },
        function(id, study, callback){
            //게시자의 id를 알아낸다
                          console.log("2함수 : 진입");
            connection.query('select * from User where id = ? ;',id, function (error, cursor) {
             if (cursor.length > 0) {
                          console.log("2함수 : registerid = "+cursor[0].registerid);
                          console.log("2함수 : userid = "+id);
                          console.log("2함수 : study = "+study);
                   callback(null,cursor[0].registerid, id, study);
            } else{
                          console.log("2함수 : user 못 찾음");
                res.status(503).json(error);
            }
            });
        },
        function(registerid, id, study, callback){

                          console.log("3함수 : 진입");
                          console.log("3함수 : registerid = "+registerid);
                          console.log("3함수 : userid = "+id);
                          console.log("3함수 : study = "+study);

            var server_api_key = 'AIzaSyCdgHHDKuf33oiPwWWIW3aQbG55MiPsrq0';
            var sender = new gcm.Sender(server_api_key);
            var registrationIds = [];

            //게시자에게 push를 보낸다.
            var message = new gcm.Message({
                collapseKey: 'demo',
                delayWhileIdle: true,
                timeToLive: 3,
                data: {
                    title: '스터디 도우미',
                    message: '인원이 가득찼어요! 스터디 모집을 마감하세요' ,
                    userid : id,
                    studyid : study
                }
            });

             registrationIds.push(registerid);

                    sender.send(message, registrationIds, 4, function (err, result) {
                        console.log(result);
                        res.json(result);
                    });
        }

        ];

        async.waterfall(tasks, function(err){
            if(err)
                console.log('err');
            else
                console.log('done');
            connection.end();
        });
});
//참여자 목록
router.get('/:id', function(req, res, next) {
connection.query('select * from Participant where study = ? ;',[req.params.id], function (error, cursor) {
           if (cursor.length > 0) {
                                        res.json(cursor);
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot find article"
                                });

    });
});

//불참
router.delete('/:id/:userid', function(req, res, next) {
connection.query('delete from Participant where userid = ? and study = ?;',[req.params.userid,req.params.id], function (error, info) {
           if (error == null) {
				res.send("success");
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot delete article"
                                });
        });

});

//특정 참여자 찾기
router.get('/:userid/:id', function(req, res, next) {
connection.query('select * from Participant where userid = ? and study = ?;',[req.params.userid, req.params.id], function (error, cursor) {
           if (cursor.length > 0) {
                                        res.json(cursor[0]);
                                        console.log(cursor[0].name+"정보  전송 >완료");
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot find article"
                                });
        });

});

module.exports = router;


