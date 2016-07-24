# Let's Study Together
소프트웨어 마에스트로 첫번째 과제를 위한 android application입니다.


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
