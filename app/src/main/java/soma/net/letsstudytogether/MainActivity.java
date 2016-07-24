package soma.net.letsstudytogether;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import soma.net.letsstudytogether.ListView.CustomAdapter;
import soma.net.letsstudytogether.component.ApplicationController;
import soma.net.letsstudytogether.model.Study;
import soma.net.letsstudytogether.model.User;
import soma.net.letsstudytogether.model.UserTime;
import soma.net.letsstudytogether.network.NetworkService;
import soma.net.letsstudytogether.study_show.Show_Study;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Handler handler = new Handler();
    ListView listView;
    CustomAdapter adapter;
    FloatingActionButton fab;
    ViewPager viewPager;
    Thread thread;
    ViewPagerAdapter imagadpter;


    private NetworkService networkService;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //네트워크 연결
        ApplicationController application = ApplicationController. getInstance();
        application.buildNetworkService("52.41.56.246", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();
        //페이스북
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        //User정보 받아오기
        Intent intent = getIntent();
        user = (User)intent.getParcelableExtra("UserInfo");

        //툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("스터디 목록");

        //네비게이션 드로어
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //이미지 뷰페이저
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        imagadpter = new ViewPagerAdapter(this);
        viewPager.setAdapter(imagadpter);

        autoviewpager();

        //리스트 뷰
        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(getApplicationContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Log.i("Study", position+"게시물 선택됨");
                    move_to_study(position);
                } catch (Exception e) {//오류처리
                    Log.v("Main erro", e.toString());
                }
            }
        });


        //플로팅 버튼
        fab = (FloatingActionButton) findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), Register_study.class);
                intent.putExtra("UserInfo", user); //user 정보가 들어있는 객체 전달
                startActivity(intent);
            }
        });

        //마이페이지
        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.userName_mypage);
        userName.setText(user.getName());

                            CircularImageView circularImageView = (CircularImageView) header.findViewById(R.id.myPicture);
                            Glide.with(getApplicationContext()).load("https://graph.facebook.com/" + user.getId() + "/picture?type=large").into(circularImageView);
                            circularImageView.setBorderColor(Color.parseColor("#FFFFFF"));
                            circularImageView.setBorderWidth(4);
                            circularImageView.addShadow();

        Typekit.getInstance()//폰트적용
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothicLight.otf"));


    }

    private void autoviewpager() {
        handler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                int cur = viewPager.getCurrentItem();
                if(cur==0){
                    viewPager.setCurrentItem(cur+1);
                }if(cur==1) {
                    viewPager.setCurrentItem(cur-1);
                }
            }
        };

        thread = new Thread(){
            public void run() {
                super.run();
                while(true){
                    try {
                        Thread.sleep(4000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    private void move_to_study(int position) {
        final Study itemData_temp = (Study) adapter.getItem(position);
        Log.i("Study", "position:" + position);
        Call<Study> studyCall = networkService.toMoveStudy(itemData_temp.getStudyid());

        studyCall.enqueue(new Callback<Study>() {
            @Override
            public void onResponse(Response<Study> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Study study = response.body();
                    Intent intent = new Intent(getApplicationContext(), Show_Study.class);
                    intent.putExtra("showStudy", study);
                    intent.putExtra("UserInfo",user);
                    startActivity(intent);
                } else {
                    int statusCode = response.code();
                    Log.i("Study", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("Study", "서버 응답코드 : " + t.toString());
                Snackbar.make(fab, "특정 스터디를 가져오는데 실패했습니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        // 개인정보 수정
        if (id == R.id.nav_edit) {
            Log.i("Main", "네비드로워: 개인정보 수정");
            intent = new Intent(getApplicationContext(), EditActivity.class);
            intent.putExtra("UserInfo", user);
            startActivity(intent);
        }
        // 탈퇴
        else if (id == R.id.nav_delete) {
            Log.i("Main", "네비드로워: 탈퇴");
            deleteDialog();
        }
        // 로그아웃
        else if (id == R.id.nav_logout) {
            Log.i("Main", "네비드로워: 로그아웃");
            signout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //탈퇴 확인
    private void deleteDialog() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setMessage("정말 탈퇴하시겠습니까?");
        // 탈퇴 실행
        dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder dlg2 = new AlertDialog.Builder(MainActivity.this);
                dlg2.setMessage("이용해주셔서 감사합니다.\n페이스북 연동의 경우 \n어플 내에서 연동 해제를\n도와드리기 어렵습니다.\n" +
                        "\t페이스북 계정에서 '설정 > 앱\n>올래?Olleh!>연동 해제'를\n클릭하여 해제를 하시면\n연동이 해제 됩니다.");
                dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteInfo();
                    }
                });
                dlg2.show();
            }
        });
        // no 그대로
        dlg.setNegativeButton("아니오", null);
        dlg.show();
    }

    // 회원탈퇴
    private void deleteInfo() {
        Call<String> userCall = networkService.getDelete(user.getId());
        userCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(getApplicationContext(), FacebookLogin.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.i("MyTag", "탈퇴 실패");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("Show", "로그인 에러내용 : " + t.getMessage());
            }
        });
    }

    //로그아웃
    public void signout() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();

        Intent intent2 = new Intent(getApplicationContext(), FacebookLogin.class);
        startActivity(intent2);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //리스트뷰 목록가져오기
        Call<List<Study>> thunderListCall = networkService.getStudyList();
        thunderListCall.enqueue(new Callback<List<Study>>() {

            @Override
            public void onResponse(Response<List<Study>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<Study> thunderList = response.body(); // 여기서 정렬

                    //adapter.sort(thunderList);
                    adapter.setItemDatas((ArrayList)thunderList);
                    adapter.notifyDataSetChanged();
                } else {
                    int statusCode = response.code();
                    Log.i("MyTag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "스터디 목록 가져오기에 실패하였습니다.",
                        Toast.LENGTH_SHORT).show();
                Log.i("MyTag", "에러내용 : " + t.getMessage());

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);

            d.setMessage("정말 종료 하시겠습니꺄?");
            d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LoginManager.getInstance().logOut();
                    setUserTime();
                }
            });

            d.setNegativeButton("아니요",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            d.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //시간 db에 입력
    private void setUserTime(){
        SharedPreferences setting = getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = setting.edit();

        //현재 시간 알아내기
        String now = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        editor.putString("app_end_time",now);
        editor.commit();

        UserTime userTime = new UserTime();
        userTime.setId(user.getId());
        userTime.setEndtime(setting.getString("app_end_time",null));
        userTime.setInstalltime(setting.getString("app_install_time",null));
        userTime.setPlaytime(setting.getString("app_play_time",null));

        Call<String> userCall = networkService.editUserTime(userTime);
        userCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) { //시간 등록 성공
                    MainActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MyTag", "시간 등록 에러내용 : " + t.getMessage());
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {//폰트 적용 함수
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
