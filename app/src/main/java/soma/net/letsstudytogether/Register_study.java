package soma.net.letsstudytogether;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import soma.net.letsstudytogether.component.ApplicationController;
import soma.net.letsstudytogether.model.Region;
import soma.net.letsstudytogether.model.Study;
import soma.net.letsstudytogether.model.User;
import soma.net.letsstudytogether.network.NetworkService;

/**
 * Created by lmjin_000 on 2016-01-08.
 */

public class Register_study extends AppCompatActivity {

    private NetworkService networkService;
    private EditText editTitle, member, startdate, enddate, starttime, endtime,description;
    private Spinner type_spinner1,type_spinner2, location_spinner1, location_spinner2;
    private Button sendBtn ,mon, tue, wed, thu, fri, sat, sun;
    private User userInfo;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    int n_year, n_month, n_day,hour,minute;
    int today_year, today_month, today_day;
    int day =0;
    String type_one, location_one;

    private HashMap<String,Integer> map = new HashMap<String, Integer>();
    private Study study = new Study();

    private String[] citylist;
    private String[] gulist;

    private void initMap(){
        map.put("스터디 상위분류", R.array.studytype2);
        map.put("프로그래밍",R.array.프로그래밍);
        map.put("어학",R.array.어학);
        map.put("취업",R.array.취업);
        map.put("금융",R.array.금융);
        map.put("기타",R.array.기타);
        map.put("지역 상위분류",R.array.location2);
        map.put("강원",R.array.강원);
        map.put("경기",R.array.경기);
        map.put("경남",R.array.경남);
        map.put("경북",R.array.경북);
        map.put("광주",R.array.광주);
        map.put("대구",R.array.대구);
        map.put("대전",R.array.대전);
        map.put("부산",R.array.부산);
    }

    private void initView() {
        editTitle = (EditText) findViewById(R.id.title);
        member = (EditText) findViewById(R.id.member);
        startdate = (EditText) findViewById(R.id.startdate);
        enddate = (EditText) findViewById(R.id.enddate);
        starttime = (EditText) findViewById(R.id.starttime);
        endtime = (EditText) findViewById(R.id.endtime);
        description = (EditText) findViewById(R.id.description);
        type_spinner1 = (Spinner) findViewById(R.id.type_spinner1);
        type_spinner2 = (Spinner) findViewById(R.id.type_spinner2);
        location_spinner1 = (Spinner) findViewById(R.id.location_spinner1);
        location_spinner2 = (Spinner) findViewById(R.id.location_spinner2);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        mon = (Button)findViewById(R.id.monday);
        tue = (Button)findViewById(R.id.tuesday);
        wed = (Button)findViewById(R.id.wednesday);
        thu = (Button)findViewById(R.id.thursday);
        fri = (Button)findViewById(R.id.friday);
        sat = (Button)findViewById(R.id.saturday);
        sun = (Button)findViewById(R.id.sunday);

        mon.setOnClickListener(mClickListener);
        tue.setOnClickListener(mClickListener);
        wed.setOnClickListener(mClickListener);
        thu.setOnClickListener(mClickListener);
        fri.setOnClickListener(mClickListener);
        sat.setOnClickListener(mClickListener);
        sun.setOnClickListener(mClickListener);
        sendBtn.setOnClickListener(mClickListener);
        startdate.setOnClickListener(mClickListener2);
        enddate.setOnClickListener(mClickListener2);
        starttime.setOnClickListener(mClickListener2);
        endtime.setOnClickListener(mClickListener2);
    }

    private void register(){
        Call<String> thunderListCall = networkService.newStudy(study,userInfo.getName());
        thunderListCall.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.i("study", "스터디 등록 성공");
                    finish();
                } else {
                    int statusCode = response.code();
                    Snackbar.make(sendBtn, "같은 분야의 스터디를 2개이상 등록할 수 없습니다", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("study", "에러내용 : " + t.getMessage());
            }
        });
    }

    private void check_register(){
        study.setDescription(description.getText().toString());
        study.setTitle(editTitle.getText().toString());
        study.setDay(day);
        study.setPhone(userInfo.getPhone());
        study.setEmail(userInfo.getEmail());

        if(member.getText().toString().equals(""))
            Snackbar.make(sendBtn, "인원을 설정해주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        else
            study.setMember(Integer.parseInt(member.getText().toString()));

        if(study.getTitle()==null)
            Snackbar.make(sendBtn, "제목을 작성해주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        else if(study.getType()==null)
            Snackbar.make(sendBtn, "분야를 선택해주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        else if(study.getMember()<0||study.getMember()>50)
            Snackbar.make(sendBtn, "인원은 2 ~ 50명까지 설정 가능합니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        else if(study.getLocation()==null)
            Snackbar.make(sendBtn, "지역을 선택해주세요.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        else
            register();
    }

    private Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void onClick(View v){
            switch (v.getId()){
                case R.id.monday :day_count(1,mon);break;
                case R.id.tuesday :day_count(10,tue);break;
                case R.id.wednesday :day_count(100,wed);break;
                case R.id.thursday :day_count(1000,thu);break;
                case R.id.friday :day_count(1000,fri);break;
                case R.id.saturday :day_count(10000,sat);break;
                case R.id.sunday :day_count(100000,sun);break;
                case R.id.sendBtn :check_register();break;
                case R.id.default_activity_button: break;
            }
        }
    };

    private EditText.OnClickListener mClickListener2 = new View.OnClickListener(){
        public void onClick(View v){
            switch (v.getId()){
                case R.id.startdate :new DatePickerDialog(Register_study.this, startdateSetListener, n_year, n_month, n_day).show();break;
                case R.id.enddate :new DatePickerDialog(Register_study.this, enddateSetListener, n_year, n_month, n_day).show();break;
                case R.id.starttime :new TimePickerDialog(Register_study.this, starttimeSetListener, hour, minute,false).show();break;
                case R.id.endtime :new TimePickerDialog(Register_study.this, endtimeSetListener, hour, minute,false).show();break;
            }
        }
    };

    private void day_count(int su,Button btn){
        if(day%(su*10)/su==1){//이미 선택되어 있었다면 해제
            day-=su;
            btn.setBackgroundResource(R.drawable.register_small);
            Log.i("daybtn",Integer.toString(su)+"버튼 해제");
        }else if(day%(su*10)/su==0){//아니라면 선택
            day+=su;
            btn.setBackgroundResource(R.drawable.day_push);
            Log.i("daybtn",Integer.toString(su)+"버튼 설정");
        }
        Log.i("daybtn","총점 : "+Integer.toString(day));
    }

    private DatePickerDialog.OnDateSetListener startdateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);
            startdate.setText(msg);
            study.setStartdate(msg);
        }
    };
    private DatePickerDialog.OnDateSetListener enddateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);
            enddate.setText(msg);
            study.setEnddate(msg);
        }
    };

    private TimePickerDialog.OnTimeSetListener starttimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String msg = String.format("%d:%d", hourOfDay,minute);
            starttime.setText(msg);
            study.setStarttime(msg);
        }
    };
    private TimePickerDialog.OnTimeSetListener endtimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String msg = String.format("%d:%d", hourOfDay,minute);
            endtime.setText(msg);
            study.setEndtime(msg);
        }
    };
    private void JinitSpinner(Spinner s,String[] array){
        ArrayAdapter adapter = null;
            adapter = new ArrayAdapter( getApplicationContext(),R.layout.spin, array);

        adapter.setDropDownViewResource(R.layout.spin_dropdown);

        s.setAdapter(adapter);
    }
    private void AinitSpinner(Spinner s, int array){
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(getApplicationContext(),array,R.layout.spin);

        adapter.setDropDownViewResource(R.layout.spin_dropdown);

        s.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__study);

        Typekit.getInstance()//폰트적용
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothicLight.otf"));

        //서버연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.41.56.246", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();


        //시, 군구 목록 setting
        initcity();
        // 타이틀 바
        getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.titlebar_register);

        initMap();
        initView();

        //스터디 타입 스피너
        AinitSpinner(type_spinner1,R.array.studytype1);
        AinitSpinner(type_spinner2,R.array.studytype2);
        AinitSpinner(location_spinner2,R.array.location2);

        Intent u_intent = getIntent();
        userInfo = (User)u_intent.getParcelableExtra("UserInfo");//user 객체를 받는다.
        study.setWriter(userInfo.getId());
        study.setPhone(userInfo.getPhone());
        study.setEmail(userInfo.getEmail());

        GregorianCalendar calendar = new GregorianCalendar();
        n_year = calendar.get(Calendar.YEAR);
        n_month = calendar.get(Calendar.MONTH);
        n_day = calendar.get(Calendar.DAY_OF_MONTH);
        today_year = calendar.get(Calendar.YEAR);
        today_month = calendar.get(Calendar.MONTH);
        today_day = calendar.get(Calendar.DAY_OF_MONTH);

        type_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!type_spinner1.getSelectedItem().toString().equals("스터디 상위분류")) {
                    type_one = type_spinner1.getSelectedItem().toString();
                    AinitSpinner(type_spinner2,map.get(type_one));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AinitSpinner(type_spinner1,R.array.studytype1);
            }
        });

        type_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!type_spinner2.getSelectedItem().toString().equals("스터디 하위분류")){
                    String str = type_one+"-"+type_spinner2.getSelectedItem();
                    study.setType(str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AinitSpinner(type_spinner2,R.array.studytype2);
            }
        });

        location_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!location_spinner1.getSelectedItem().toString().equals("지역 상위분류")) {
                    location_one = location_spinner1.getSelectedItem().toString();
                    setGu(location_one);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                JinitSpinner(location_spinner1,citylist);
            }
        });

        location_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!location_spinner2.getSelectedItem().toString().equals("지역 하위분류")){
                    String str = location_one+"-"+location_spinner2.getSelectedItem();
                    study.setLocation(str);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AinitSpinner(location_spinner2,R.array.location2);
            }
        });

    }

    private void setGu(String city) {
        Call<List<Region>> guListCall = networkService.getGu(city);
        guListCall.enqueue(new Callback<List<Region>>() {

            @Override
            public void onResponse(Response<List<Region>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<Region> temp = response.body(); // 여기서 정렬

                    gulist = new String[temp.size()];
                    for(int i = 0; i < temp.size(); i++){
                        gulist[i] = temp.get(i).getGu();
                        Log.i("zzz",i+"저장"+temp.get(i).getGu());
                    }

                    JinitSpinner(location_spinner2,gulist);
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

    private void initcity() {
        final Call<List<Region>> cityListCall = networkService.getCity();
        cityListCall.enqueue(new Callback<List<Region>>() {

            @Override
            public void onResponse(Response<List<Region>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<Region> temp = response.body();

                    citylist = new String[temp.size()];
                    for(int i = 0; i < temp.size(); i++){
                        citylist[i] = temp.get(i).getCity();
                        Log.i("zzz",i+"저장"+temp.get(i).getCity());
                    }

                    JinitSpinner(location_spinner1,citylist);
                    //Log.i("zzz","one : "+citylist.get(0));
                } else {
                    int statusCode = response.code();
                    Log.i("zzz", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "스터디 목록 가져오기에 실패하였습니다.",
                        Toast.LENGTH_SHORT).show();
                Log.i("zzz", "에러내용 : " + t.getMessage());

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void attachBaseContext(Context newBase) {//폰트 적용 함수
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}