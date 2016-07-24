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

router.get('/', function(req, res, next) {
        req.setEncoding('utf8');
         connection.query('select distinct city from Region order by ( case when city = "지역 상위분류" then 1 else 2 end )', function (error, cursor) {
			 res.json(cursor);
        });
});

//군구 목록 주기
router.get('/:city', function(req, res, next) {
connection.query('select gu from Region where city = ? order by( case when gu = "지역 하위분류" then 1 else 2 end );',[req.params.city], function (error, cursor) {
           if (cursor.length > 0) {
                                        res.json(cursor);
                                } else
                                        res.status(503).json({
                                                result : false,
                                                reason : "Cannot find article"
                                });
        });

});


module.exports = router;

