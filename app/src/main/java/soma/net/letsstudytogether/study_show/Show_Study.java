package soma.net.letsstudytogether.study_show;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import soma.net.letsstudytogether.R;
import soma.net.letsstudytogether.component.ApplicationController;
import soma.net.letsstudytogether.model.Comment;
import soma.net.letsstudytogether.model.Participant;
import soma.net.letsstudytogether.model.Study;
import soma.net.letsstudytogether.model.User;
import soma.net.letsstudytogether.network.NetworkService;
import soma.net.letsstudytogether.study_show.comment.CommentAdapter;
import soma.net.letsstudytogether.study_show.participant.GridAdapter;

public class Show_Study extends AppCompatActivity {

    private Study study = new Study();//content
    private User user = new User();
    private ImageView refresh;
    private NetworkService networkService;
    private TextView commentCount, Title ,type1, type2, member, location1, location2, date, day, time, description;
    private EditText editComment;
    private LinearLayout call_part, email_part;
    private Button btn_people, btn_participate, btn_comment_send, btn_more;
    private CircularImageView profileimg;

    int cnt;
    List<Participant> personList;

    GridAdapter adapter2;
    GridView gridview;
    TextView text_cnt;

    //번개 불참/참가/삭제 버튼 관리
    int check;

    private ListView listView;

    //댓글 등록(1)/수정(2) 관리
    int comment_check=1;
    //댓글 더보기 몇번 눌렸는지
    int comment_more_check = 1;

    //댓글
    CommentAdapter adapter;

    private void showInfo(){
        //사진
        Glide.with(getApplicationContext()).load("https://graph.facebook.com/" + study.getWriter() + "/picture?type=normal").into(profileimg);

        Title.setText(study.getTitle());
        member.setText(String.valueOf(study.getMember()));
        member.setText(String.valueOf(study.getMember()));
        if(study.getStartdate()!=null&&study.getEnddate()!=null)
            date.setText(study.getStartdate().substring(0,10)+" ~ "+study.getEnddate().substring(0,10));
        if(study.getStarttime()!=null&&study.getEndtime()!=null)
            time.setText(study.getStarttime()+" ~ "+study.getEndtime());
        if(study.getDay()>0)
            day.setText(getday());
        if(study.getDescription()!=null)
            description.setText(study.getDescription());
        String type = study.getType();
        int i = type.indexOf("-");
        type1.setText(type.substring(0,i));
        type2.setText(type.substring(i+1,type.length()));
        String location = study.getLocation();
        i = location.indexOf("-");
        location1.setText(location.substring(0,i));
        location2.setText(location.substring(i+1,location.length()));
    }

    private String getday(){
        String[] daylist = {"월","화","수","목","금","토","일"};
        String result = "";
        int j=0;
        for(int i=1;i<=100000;i*=10){
            if((study.getDay()%(i*10))/i==1){
                result +=(daylist[j]+" ");
            }
            j++;
        }
        return result;
    }

    private void initView() {
        profileimg = (CircularImageView) findViewById(R.id.profileimg);
        Title = (TextView) findViewById(R.id.title);
        type1 = (TextView) findViewById(R.id.type1);
        type2 = (TextView) findViewById(R.id.type2);
        member = (TextView) findViewById(R.id.member);
        location1 = (TextView) findViewById(R.id.location1);
        location2 = (TextView) findViewById(R.id.location2);
        date = (TextView) findViewById(R.id.date);
        day = (TextView) findViewById(R.id.day);
        time = (TextView) findViewById(R.id.time);
        description = (TextView) findViewById(R.id.description);
        btn_participate = (Button) findViewById(R.id.btn_participate);
        btn_people = (Button) findViewById(R.id.btn_people);
        call_part = (LinearLayout) findViewById(R.id.call_part);
        email_part = (LinearLayout) findViewById(R.id.email_part);
        //댓글
        btn_comment_send = (Button)findViewById(R.id.button_send_message);
        editComment = (EditText)findViewById(R.id.edit_text_message);
        commentCount = (TextView)findViewById(R.id.comment_count);
        refresh = (ImageView)findViewById(R.id.comment_refresh);
        btn_more = (Button)findViewById(R.id.btn_more);
        listView = (ListView)findViewById(R.id.expandableListView);
    }


    private void showUser(){ //이 스터디에 참여한 인원을 dialog로 보여준다.
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_people, null);
        //멤버의 세부내역 입력 Dialog 생성 및 보이기
        text_cnt = (TextView) dialogView.findViewById(R.id.text_cnt);
        gridview = (GridView) dialogView.findViewById(R.id.gridView);
        adapter2 = new GridAdapter(getApplicationContext(),R.layout.participant_item, personList);

        cnt = adapter2.getCount();
        text_cnt.setText(String.valueOf(cnt));
        gridview.setAdapter(adapter2);

        AlertDialog.Builder buider = new AlertDialog.Builder(Show_Study.this);
        AlertDialog dialog = buider.create();
        buider.setView(dialogView);
        //버튼 제거
        buider.setPositiveButton(null,null);
        buider.setNegativeButton(null,null);
        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(true);
        //Dialog 보이기
        buider.show();
    }

    private void throwUserInfo(){
        try {
            if (check == 1) {//등록자가 자신의 게시글을 봤을 때. = > 삭제
                Call<String> userCall = networkService.deleteStudy(study.getStudyid());
                userCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess()) { //삭제 공
                                AlertDialog.Builder d = new AlertDialog.Builder(Show_Study.this);

                                d.setMessage("마감 하시겠습니꺄?");
                                d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LoginManager.getInstance().logOut();
                                        finish();
                                    }
                                });

                                d.setNegativeButton("아니요",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                d.show();
                        } else {
                            int statusCode = response.code();
                            Log.i("Show", "응답코드 : " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("Show", "스터디 마감 에러내용 : " + t.getMessage());
                    }
                });
            } else {
                Participant pp = new Participant();
                pp.setName(user.getName());
                pp.setStudy(study.getStudyid());
                pp.setUserid(user.getId());
                Call<String> userCall = null;   //참여하기 누를 때
                if (check == 2)//그 게시글 참석자가 자신이 신청한 스터디를 봤을 때 = > 불참
                    userCall = networkService.comeoutStudy(study.getStudyid(), user.getId());
                else if(check==3) {  //미 참석자가 스터디를 봤을 때. => 참여
                    if(personList.size() != study.getMember())
                         userCall = networkService.participateStudy(pp,study.getMember(),personList.size()+1);
                    else{
                        Snackbar.make(btn_participate, "인원이 꽉 찼습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        return;
                    }
                }

                userCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            if(check==2) {
                                btn_participate.setText("참여");
                                check=3;
                                Snackbar.make(btn_participate, "스터디 참여가 취소되었습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                            else if(check==3) {
                                check=2;
                                btn_participate.setText("불참");
                                Snackbar.make(btn_participate, "스터디에 참여되었습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }

                            //리스트 초기화
                            initParticipant();
                        } else {
                            int statusCode = response.code();
                            Log.i("Show", "응답코드 : " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("Show", "스터디 삭제 에러내용 : " + t.getMessage());
                    }
                });
            }
        }
        catch(Exception e) {//오류처리
            Log.v("erro",e.toString());
        }

    }

    private void initParticipant(){   //참여 인원 목록 가져오기(인원수도 파악)
        try {
            Call<List<Participant>> userCall = networkService.getParticipant(study.getStudyid());     //참여 인원 확인 누를 때
            userCall.enqueue(new Callback<List<Participant>>() {
                @Override
                public void onResponse(Response<List<Participant>> response, Retrofit retrofit) {
                    try {
                        if (response.isSuccess()) {
                            personList = response.body();
                        } else {
                            int statusCode = response.code();
                            Log.i("Show", "응답코드 : " + statusCode);
                        }
                    }catch(Exception e) {//오류처리
                        Log.v("Show erro1",e.toString());
                    }

                }
                @Override
                public void onFailure(Throwable t) {
                    Log.i("Show", "로그인 에러내용 : " + t.getMessage());
                }
            });
        }
        catch(Exception e) {//오류처리
            Log.v("erro",e.toString());
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {//폰트 적용 함수
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__study);

        Intent intent = getIntent();
        study = (Study) intent.getParcelableExtra("showStudy");  //번개 보기 화면이 띄워지면 그 내용을 객체로 받는다
        user = (User)intent.getParcelableExtra("UserInfo");

        ApplicationController application = ApplicationController. getInstance();
        application.buildNetworkService("52.41.56.246", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();

        initView();
        showInfo();
        initParticipant();
        //댓글
        initAdapter();
        check_check();

        try {
            Typekit.getInstance()//폰트적용
                    .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothicLight.otf"));

            if(check == 1)//등록자가 자신의 게시글을 봤을 때. = > "참여" 버튼을 "마감" 로 바꾸기
                btn_participate.setText("마감");

            gridview = (GridView) findViewById(R.id.gridView);//그리드 뷰를 위한 코드

            getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.titlebar_show);

            btn_participate.setOnClickListener(new View.OnClickListener() {//참여하기 버튼 클릭 시
                @Override
                public void onClick(View v) {
                    Log.i("Show", "참여하기 클릭");
                    throwUserInfo();
                }
            });
            btn_people.setOnClickListener(new View.OnClickListener() {//현재 참여 인원 버튼 클릭 시
                @Override
                public void onClick(View v) {
                    Log.i("Show", "adapter 0");
                    try {
                        showUser();//이 번개에 참여한 인원을 dialog로 보여준다.
                    } catch (Exception e) {//오류처리
                        Log.v("Show erro 2", e.toString());
                    }
                }
            });
            /*
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Log.i("Study", position+"게시물 선택됨");
                    } catch (Exception e) {//오류처리
                        Log.v("Main erro", e.toString());
                    }
                }
            });
            */
            //댓글 등록
            comment_check = 1;//보통엔 1 ( 등록 )
            Comment lastcomment = (Comment) adapter.getItem(adapter.getCount());
            btn_comment_send_method(lastcomment);
            //v2 댓글 클릭시
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//해당 댓글 클릭시
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Comment comment_temp = (Comment) adapter.getItem(position);

                    if (comment_temp.getId().equals(user.getId()))//내가 등록한 댓글일 경우에만
                    {
                        setCommnetSelect(comment_temp);
                        Log.i("comment", "내가 쓴 댓글");
                    } else Log.i("comment", "남의 댓글");
                }
            });
            //v2 댓글 새로고침
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowComment(1);
                    comment_more_check = 1;
                }
            });
            //v2 댓글 더보기
            btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comment_more_check++;
                    ShowComment(comment_more_check);
                }
            });
            // 이메일 보내기
            email_part.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    send_email();
                }
            });
            call_part.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    call();
                }
            });
        }catch(Exception e){
            Log.v("Show erro 3",e.toString());
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+study.getPhone()));
        startActivity(intent);
    }

    private void check_check() {
        if(user.getId().equals(study.getWriter()))//등록자일때
            check = 1;
        else {
            Call<Participant> userCall = networkService.findParticipant(user.getId(),study.getStudyid());
            userCall.enqueue(new Callback<Participant>() {
                @Override
                public void onResponse(Response<Participant> response, Retrofit retrofit) {
                    try {
                        if (response.isSuccess()) {
                            //참여자일때
                            check = 2;
                            btn_participate.setText("불참");
                        } else {
                            //참여자가 아닐때
                            check = 3;
                            btn_participate.setText("참여");
                        }
                    } catch (Exception e) {//오류처리
                        Log.v("Show erro1", e.toString());
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.i("Show", "에러내용 : " + t.getMessage());
                }
            });
        }
    }

    //댓글 전송 버튼 클릭 메소드 - 매개변수에 comment값 넣기 위해 메소드 따로 만듦
    private void btn_comment_send_method(final Comment comment) {
        try {
            btn_comment_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("comment","버튼 눌림");
                    if (comment_check == 1) {
                        newcomment();
                        comment_more_check=1;
                        ShowComment(comment_more_check);
                    } else if (comment_check == 2) {
                        editcomment(comment);
                    }
                }
            });
        }
        catch(Exception e) {//오류처리
            Log.v("erro",e.toString());
            Log.i("Show","댓글 에러 : "+e.toString());
        }
    }
    //댓글 수정
    private void editcomment(Comment comment) {
        String comment_text = editComment.getText().toString();

        Snackbar.make(btn_participate, "댓글이 수정되었습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        comment.setContents(comment_text);

        Call<String> CommentCall = networkService.editComment(comment);
        CommentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.i("comment", "댓글 수정 성공");
                } else {
                    int statusCode = response.code();
                    Log.i("comment", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("comment", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
        ShowComment(comment_more_check);
        comment_check = 1;
    }

    private void newcomment() {
        String comment_text = editComment.getText().toString();

        //Date 형식을 정해줍니다.
        long now = System.currentTimeMillis();
        Date currentTime = new Date(now);
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String mTime = mSimpleDateFormat.format(currentTime);

        Comment comment = new Comment();
        comment.setName(user.getName());
        comment.setId(user.getId());
        comment.setContents(comment_text);
        comment.setStudyid(study.getStudyid());
        comment.setWrite_time(mTime);

        Call<String> CommentCall = networkService.newComment(comment); // 0부터 시작
        CommentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.i("comment2", "댓글 등록성공");
                } else {
                    int statusCode = response.code();
                    Log.i("comment2", "응답코드 : " + statusCode);
                    Snackbar.make(btn_participate, "연속으로 등록할 수 없습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("comment", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
    }

    //댓글 클릭시 호출 함수
    private void setCommnetSelect(final Comment comment) {
        CharSequence info[] = new CharSequence[] {"댓글 수정", "댓글 삭제" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Show_Study.this);
        builder.setItems(info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        comment_check = 2;//이 경우에만 2 ( 수정 )
                        // 댓글 수정
                        editComment.setText(comment.getContents());//원래 내용 띄워주고 수정하게 하기
                        editComment.requestFocus();
                        editComment.setSelection(editComment.length());
                        btn_comment_send_method(comment);
                        break;
                    case 1:
                        // 댓글 삭제
                        AlertDialog.Builder dlg = new AlertDialog.Builder(Show_Study.this);
                        dlg.setMessage("댓글을 삭제하시겠습니까?");
                        // 탈퇴 실행
                        dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteComment(comment);
                            }
                        });
                        // no 그대로
                        dlg.setNegativeButton("아니오", null);
                        dlg.show();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteComment(Comment comment) {
        //댓글 삭제 구현하기
        Call<String> CommentCall = networkService.deleteComment(comment.getStudyid(), comment.getWrite_time());
        CommentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Snackbar.make(btn_participate, "댓글이 삭제되었습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    int statusCode = response.code();
                    Log.i("comment", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("comment", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
        comment_more_check=1;
        ShowComment(comment_more_check);
    }

    private void initAdapter() { // 초기화
        adapter = new CommentAdapter(getApplicationContext());
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //네트워크
        ApplicationController application = ApplicationController. getInstance();
        application.buildNetworkService("52.41.56.246", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();

        //댓글 띄우기
        ShowComment(1);
        Log.i("comment", "OnResume");
    }

    // 댓글 띄우기
    private void ShowComment(final int su) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        initAdapter();

        Call<List<Comment>> CommentListCall = networkService.getCommentList(study.getStudyid());
        CommentListCall.enqueue(new Callback<List<Comment>>() {

            @Override
            public void onResponse(Response<List<Comment>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<Comment> commentList = response.body(); // 여기서 정렬
                    commentCount.setText(Integer.toString(commentList.size()));
                    Log.i("commentrefresh", "등록된 댓글 갯 수 : " + commentList.size());

                    if (commentList.size() > su * 5)//등록된 댓글이 원하는 수보다 많을 경우
                    {
                        List<Comment> showcommentrList = new ArrayList<Comment>();
                        for (int i = su * 5; i > 0; i--) {
                            showcommentrList.add(commentList.get(commentList.size() - i));
                            Log.i("comment", "showcommentList에 저장 : " + commentList.get(commentList.size() - i).getContents());
                        }
                        Log.i("comment", "showcommentList에 저장된 수 : " + showcommentrList.size());
                        adapter.setItemDatas((ArrayList) showcommentrList);
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnItems(listView);
                    } else {//등록된 댓글이 원하는 수보다 작거나 같을 경우
                        adapter.setItemDatas((ArrayList) commentList);
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnItems(listView);
                    }
                    int ShowCommentCount = adapter.getCount();//화면에 나와있는 댓글 수
                    int AllCommentCount = commentList.size();

                    if (AllCommentCount > ShowCommentCount)//댓글이 더 있을 경우
                        btn_more.setVisibility(View.VISIBLE);
                    else btn_more.setVisibility(View.GONE);//댓글 더 없을 경우
                } else {
                    //리스트 목록 0

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("comment", "에러내용 : " + t.getMessage());
            }
        });

            }
        }, 100);

    }

    // 댓글 높이 측정
    public void setListViewHeightBasedOnItems(ListView listView) {

        if (adapter == null)  return;

        int numberOfItems = adapter.getCount();

        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = adapter.getView(itemPos, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() *  (numberOfItems - 1);

        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void send_email(){
        Intent it = new Intent(Intent.ACTION_SEND);
        String[] mailaddr = {study.getEmail()};

        it.setType("plaine/text");
        it.putExtra(Intent.EXTRA_EMAIL, mailaddr); // 받는사람
        it.putExtra(Intent.EXTRA_SUBJECT, "[스터디 도우미]"+study.getTitle()); // 제목
        it.putExtra(Intent.EXTRA_TEXT,"[스터디 도우미]"+study.getTitle()+"문의합니다"); // 첨부내용

        startActivity(it);
    }
}