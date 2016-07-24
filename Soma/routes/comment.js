var express = require('express'); // 로그인 + 회원가입
var mysql = require('mysql');
var router =express.Router();
var connection = mysql.createConnection({
    user : 'user',
    password : '201301473',
    database : 'soma',
    host : 'aws-rds-mysql.czvbtapsvpjv.us-west-2.rds.amazonaws.com'
});

//댓글 등록
router.post('/', function(req, res, next) {
         connection.query('insert into Comment(id, studyid, contents, name) values (?, ?, ?, ?);',  [req.body.id, req.body.studyid, req.body.contents, req.body.name], function (error, info) {
            if (error == null){
            res.send("success");
		} else res.status(503).json(error);
    });
});
//댓글 목록 가져오기
router.get('/:id', function(req, res, next) {
connection.query('select * from Comment where studyid = ? ;',[req.params.id], function (error, cursor) {
           if (cursor.length > 0) {
                                        res.json(cursor);
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot find article"
                                });
        });

});

//댓글 삭제
router.delete('/:studyid/:writetime', function(req, res, next) {
connection.query('delete from Comment where studyid = ? and write_time = ? ;',[req.params.studyid, req.params.writetime], function (error, info) {
           if (error == null) {
            res.send("success");
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot delete article"
                                });
        });

});
//댓글  수정
router.put('/', function(req, res, next) {
         connection.query('update Comment set contents=? where write_time = ? and studyid = ?;',  [ req.body.contents, req.body.write_time, req.body.studyid], function (error, info) {
            if (error == null){
                   res.send("success");    
	} else res.status(503).json(error);
    });
});


module.exports = router;

