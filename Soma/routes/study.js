var express = require('express'); // 로그인 + 회원가입
var mysql = require('mysql');
var router =express.Router();
var async = require('async');
var connection = mysql.createConnection({
    user : 'user',
    password : '201301473',
    database : 'soma',
    host : 'aws-rds-mysql.czvbtapsvpjv.us-west-2.rds.amazonaws.com'
});

//목록주기
router.get('/', function(req, res, next) {
        req.setEncoding('utf8');
         connection.query('select * from Study where end = 0;', function (error, cursor) {
                   res.json(cursor);
        });
});
//스터디 등록
router.post('/:name', function(req, res, next) {
    var tasks = [
        function(callback){
            connection.query('insert into Study(writer,title,studytype,startdate,enddate,starttime,endtime,member,location,day,description,phone,email) values (?, ?, ?, ?, ?,?,?,?,?,?,?,?,?);',  [req.body.writer, req.body.title, req.body.studytype, req.body.startdate, req.body.enddate, req.body.starttime, req.body.endtime, req.body.member, req.body.location, req.body.day, req.body.description, req.body.phone, req.body.email], function (error, info) {
                if (error == null){
                    callback(null,req.body.writer, req.body.studytype);
                } else{
                     res.status(503).json(error);
                }
            });
        },
        function(writer, studytype, callback){
            connection.query('select * from Study where writer = ? and studytype = ? ;',[writer, studytype], function (error, cursor) {
           if (cursor.length > 0) {
                                        callback(null,writer, cursor[0].studyid);
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot find article"
                                });
            });
        },
        function(writer, studyid, callback){
           connection.query('insert into Participant(study,name,userid) values (?, ?, ?);',  [studyid, req.params.name, writer],function (error, info) {
                  if (error == null){
			res.send("success");
                  }
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
//게시판 가져오기
router.get('/:id', function(req, res, next) {
connection.query('select * from Study where studyid = ? ;',[req.params.id], function (error, cursor) {
           if (cursor.length > 0) {
                                        res.json(cursor[0]);
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot find article"
                                });
        });

});

//스터디 삭제
router.delete('/:id', function(req, res, next) {
connection.query('delete from Study where studyid = ? ;',[req.params.id], function (error, info) {
           if (error == null) {
				res.send("success");
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot delete article"
                                });
        });

});

//스터디 마감
router.put('/', function(req, res, next) {
         connection.query('update Study set end = 1 where studyid = ?;',  [req.body.studyid], function (error, info) {
            if (error == null){
			res.send("success");
            } else res.status(503).json(error);
    });
});


module.exports = router;

