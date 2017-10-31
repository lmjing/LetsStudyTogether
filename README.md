# Let's Study Together
소프트웨어 마에스트로 첫번째 과제를 위한 android application입니다.

#안드로이드

## 환경 ##
안드로이드 스튜디오 사용

**라이브러리,SDK**
+ retrofit : Restful api 통신
+ facebook-sdk : facebook 로그인
+ glide : 사진
+ google-play-service : gcm
등등..

## 기능
페이스북을 통한 로그인
스터디 모집 등록
스터디 목록 조회(모집중인 스터디만)
스터디 참여/불참
스터디 등록시 설정한 인원 수 모두 채워질 경우 게시자에게 push알림
페이스북 친구 초대
어플리케이션 최초 실행, 어플리케이션 실행, 어플리케이션 종료 시간 저장
개인정보 수정

## java 파일 설명
+ **component**
  + **ApplicationController**
      서버 접속을 위해 url설정을 하고 retrofit객체를 생성합니다.

+ **GCM**
  + **QuickstartPreferences**
       GCM Demo에서 사용하는 LocalBoardcast의 액션을 정의합니다.
  + **RegistrationIntentService**
       Instance ID를 가지고 토큰을 가져오는 작업을 합니다.
  + **MyInstanceIDListenerService**
       Instance ID를 획득하기 위한 리스너를 상속받아서 토큰을 갱신하는 코드를 추가합니다.
  + **MyGcmListenerService**
       GCM으로 메시지가 도착하면 디바이스에 받은 메세지를 어떻게 사용할지에 대한 내용을 정의하는 클래스입니다.
  + **ShowMsgActivity**
       GCM으로 메시지가 도착할때 푸시 메세지를 띄워주는 화면입니다.

+ **ListView**
  + **CustomAdapter**
       메인 스터디를 위한 어댑터를 생성하고 xml에 추가한 Listview에 어댑터를 연결하여 ListView가 동작 되도록 합니다.
  + **ViewHolder**
       메인 스터디 목록에 표시될 아이템 하나의 뷰들을 정의합니다.

+ **model**
  + **Comment**
       서버로 부터 댓글 JSON 응답을 맵핑하기 위한 모델 클래스입니다.
  + **Participant**
       서버로 부터 참여자 JSON 응답을 맵핑하기 위한 모델 클래스입니다.
  + **Region**
       서버로 부터 지역 JSON 응답을 맵핑하기 위한 모델 클래스입니다.
  + **Study**
       서버로 부터 스터디 JSON 응답을 맵핑하기 위한 모델 클래스입니다.
  + **User**
       서버로 부터 사용자 JSON 응답을 맵핑하기 위한 모델 클래스입니다.
  + **UserTime**
        서버로 부터 사용자 시간 JSON 응답을 맵핑하기 위한 모델 클래스입니다.

+ **network**
  + **NetworkService**
       서버와 네트워킹을 하기 위한 서비스, rest uri를 정의한 곳입니다.

+ **study_show**
  + **comment**
    + **CommentAdapter**
       댓글 목록을 위한 어댑터를 생성하고 xml에 추가한 Listview에 어댑터를 연결하여 ListView가 동작 되도록 합니다.
    + **CommentViewHolder**
       댓글 목록에 표시될 아이템 하나의 뷰들을 정의합니다.
  + **participant**
    + **GridAdapter**
       참여자 목록을 위한 어댑터를 생성하고 xml에 추가한 Listview에 어댑터를 연결하여 ListView가 동작 되도록 합니다.
  + **Show_Study**
       특정 스터디의 정보들을 보여주고 참여자의 참여여부, 댓글 관련 처리를 하는 activity입니다.

+ **EditActivity**
       사용자의 개인 정보를 수정하는 activity입니다.
+ **FacebookLogin**
       페이스북 로그인을 수행하는 activity입니다.
+ **JoinActivity**
       페이스북 정보이외의 추가 정보를 입력 받아 사용자 가입을 처리하는 activity입니다.
+ **MainActivity**
       어플의 메인을 담당, 스터디의 목록을 표시, 네비게이션 드로어를 관리하는 activity입니다.
+ **Register_Study**
       새로운 스터디 모집글을 입력받는 activity입니다.
+ **splash**
       스플래쉬 화면을 표시하고 어플을 켤때, 최초 어플을 켰을때의 시간을 입력받는 activity입니다.
+ **ViewPagerAdapter**
       메인화면의 설명 사진 두개를 2초에 한번씩 옮겨 보여주도록 합니다.

#서버(Soma 폴더)

=====
## 서버, 환경설정 ##
AWS - EC2,RDS 사용

+ EC2 - NODE.JS, NPM, EXPRESS
+ RDS - MYSQL

## DB테이블 및 Rest API
**User**
어플 사용자의 정보

    +-------------+--------------+------+-----+-------------------+-------+
    | Field       | Type         | Null | Key | Default           | Extra |
    +-------------+--------------+------+-----+-------------------+-------+
    | id          | varchar(20)  | NO   | PRI | NULL              |       |
    | name        | varchar(45)  | NO   |     | NULL              |       |
    | email       | varchar(50)  | NO   |     | NULL              |       |
    | phone       | varchar(12)  | NO   |     | NULL              |       |
    | registerid  | varchar(300) | NO   |     | NULL              |       |
    | jointime    | datetime     | YES  |     | CURRENT_TIMESTAMP |       |
    | installtime | datetime     | YES  |     | NULL              |       |
    | playtime    | datetime     | YES  |     | NULL              |       |
    | endtime     | datetime     | YES  |     | NULL              |       |
    +-------------+--------------+------+-----+-------------------+-------+

URI path|method|설명
----|----|----
/user|POST|회원가입-새로운 USER생성
/user/:id|GET|특정 회원의 정보를 반환
/user/:id|DELETE|회원탈퇴 - 특정 회원을 삭제
/user|PUT|정보수정 - 특정 회원의 정보를 수정
/user/time|PUT|시간 저장 - 특정 회원의 가입,어플을 켜고 끈 시간 저장

**Study**
스터디 모집글의 정보

    +------------------+---------------+------+-----+----------------------------------------------+----------------+
    | Field            | Type          | Null | Key | Default                                      | Extra          |
    +------------------+---------------+------+-----+----------------------------------------------+----------------+
    | studyid          | int(11)       | NO   | UNI | NULL                                         | auto_increment |
    | writer           | varchar(20)   | NO   | PRI | NULL                                         |                |
    | title            | varchar(50)   | NO   |     | NULL                                         |                |
    | studytype        | varchar(50)   | NO   | PRI | NULL                                         |                |
    | member           | int(11)       | YES  |     | NULL                                         |                |
    | location         | varchar(100)  | NO   |     | NULL                                         |                |
    | day              | varchar(50)   | YES  |     | NULL                                         |                |
    | description      | varchar(1000) | YES  |     | 커리큘럼이 입력되지 않았습니다               |                |
    | created_datetime | datetime      | YES  |     | CURRENT_TIMESTAMP                            |                |
    | startdate        | date          | YES  |     | NULL                                         |                |
    | enddate          | date          | YES  |     | NULL                                         |                |
    | starttime        | time          | YES  |     | NULL                                         |                |
    | endtime          | time          | YES  |     | NULL                                         |                |
    | end              | int(11)       | NO   | PRI | 0                                            |                |
    | phone            | varchar(13)   | NO   |     | NULL                                         |                |
    | email            | varchar(50)   | NO   |     | NULL                                         |                |
    +------------------+---------------+------+-----+----------------------------------------------+----------------+

URI path|method|설명
----|----|----
/study/:name|POST|스터디 등록 - 새로운 스터디 생성하고 게시자를 참여자에 등록
/study|GET|모집중인 스터디(end = 0) 목록을 반환
/study/:id|GET|특정 스터디를 반환
/study|PUT|스터디 마감 - end의 값을 1로 수정
/study/:id|DELETE|스터디 삭제 - 특정 스터디를 삭제

**Comment**
댓글의 정보

    +------------+--------------+------+-----+-------------------+-------+
    | Field      | Type         | Null | Key | Default           | Extra |
    +------------+--------------+------+-----+-------------------+-------+
    | id         | varchar(20)  | NO   | MUL | NULL              |       |
    | studyid    | int(11)      | NO   | PRI | NULL              |       |
    | contents   | varchar(100) | YES  |     | NULL              |       |
    | write_time | datetime     | NO   | PRI | CURRENT_TIMESTAMP |       |
    | name       | varchar(45)  | NO   |     | NULL              |       |
    +------------+--------------+------+-----+-------------------+-------+


URI path|method|설명
----|----|----
/comment|POST|댓글 등록 - 새로운 댓글 생성
/comment/:id|GET|특정 스터디의 댓글 목록을 반환
/comment|PUT|댓글 수정 - 특정 댓글의 내용 수정
/comment/:studyid/:writetime|DELETE|댓글 삭제 - 특정 댓글을 삭제

**Participant**
특정 스터디의 참여자들의 정보

    +--------+-------------+------+-----+---------+-------+
    | Field  | Type        | Null | Key | Default | Extra |
    +--------+-------------+------+-----+---------+-------+
    | study  | int(11)     | NO   | PRI | NULL    |       |
    | name   | varchar(45) | NO   |     | NULL    |       |
    | userid | varchar(20) | NO   | PRI | NULL    |       |
    +--------+-------------+------+-----+---------+-------+

URI path|method|설명
----|----|----
/participate/:su1/:su2|POST|특정 스터디 참여 - 참여자 생성
/participate/:id|GET|특정 스터디의 참여자 목록을 반환
/participate/:userid/:id|GET|특정 스터디의 특정 참여자를 반환
/participate/:id/:userid|DELETE|특정 스터디 불참 - 참여자 삭제

**Participant**
스터디 등록시 지역 설정을 위한 지역정보

    +-------+-------------+------+-----+---------+-------+
    | Field | Type        | Null | Key | Default | Extra |
    +-------+-------------+------+-----+---------+-------+
    | city  | varchar(50) | NO   | PRI | NULL    |       |
    | gu    | varchar(50) | NO   | PRI | NULL    |       |
    +-------+-------------+------+-----+---------+-------+

URI path|method|설명
----|----|----
/region/:id|GET|도시 목록을 중복삭제 처리하여 반환
/region:city/:su2|GET|특정 도시의 군,구들의 목록을 반환

# 데이터베이스 (계정)
당시 미흡한 관리로 서버주소 등이 기재되어 있습니다. 현재 서버는 비활성화되어있는 상태입니다.

