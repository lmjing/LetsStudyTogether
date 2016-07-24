var express = require('express'); // 로그인 + 회원가입
var mysql = require('mysql');
var router =express.Router();
var connection = mysql.createConnection({
    user : 'user',
    password : '201301473',
    database : 'soma',
    host : 'aws-rds-mysql.czvbtapsvpjv.us-west-2.rds.amazonaws.com'
});

//회원가입
router.post('/', function(req, res, next) {
	 connection.query('insert into User(id, name, email, phone, registerid) values (?, ?, ?, ?, ?);',  [req.body.id, req.body.name, req.body.email, req.body.phone, req.body.registerid], function (error, info) {
          		console.log("register id : "+req.body.registerid);

			console.log("email : "+req.body.email);
		  if (error == null){
                    connection.query('select * from User where id=?;', [req.body.id], function (error, cursor) {
                    if (cursor.length > 0) {
                            res.json(cursor[0]);
                    } else
                            res.status(503).json({
                                     result : false,
                                    reason : "Cannot post article" });
                    });
            } else res.status(503).json(error);
    });
});

//회원 정보 가져오기
router.get('/:id', function(req, res, next) {
connection.query('select * from User where id = ? ;',[req.params.id], function (error, cursor) {
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

//회원 탈퇴
router.delete('/:id', function(req, res, next) {
connection.query('delete from User where id = ? ;',[req.params.id], function (error, info) {
           if (error == null) {
					res.send("success");
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot delete article"
                                });
        });

});

//회원 정보 수정
router.put('/', function(req, res, next) {
         connection.query('update User set name=?, email=?, phone=? where id = ?;',  [ req.body.name, req.body.email, req.body.phone,req.body.id], function (error, info) {
            if (error == null){
            		res.send("success");
		} else res.status(503).json(error);
    });
});

//회원시간 업데이트
router.put('/time', function(req, res, next) {
         connection.query('update User set installtime=?, playtime=?, endtime=? where id = ?;',  [ req.body.installtime, req.body.playtime, req.body.endtime ,req.body.id], function (error, info) {
            if (error == null){
                    res.send("success");
            } else res.status(503).json(error);
    });
});


module.exports = router;

