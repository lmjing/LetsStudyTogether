package soma.net.letsstudytogether;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import soma.net.letsstudytogether.GCM.QuickstartPreferences;
import soma.net.letsstudytogether.GCM.RegistrationIntentService;
import soma.net.letsstudytogether.component.ApplicationController;
import soma.net.letsstudytogether.model.User;
import soma.net.letsstudytogether.network.NetworkService;

public class JoinActivity extends AppCompatActivity {
    EditText input_email,input_phone1,input_phone2,input_phone3,input_name;
    Button btn_join;
    private NetworkService networkService;
    User n_user = new User(); //new_user 여기에 회원가입하는 놈 정보저장해
    int year,month,day;

    //GCM
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "GCM";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token;

    private void initview(){
        input_name = (EditText)findViewById(R.id.input_name);
        input_email = (EditText)findViewById(R.id.input_email);
        input_phone1 = (EditText)findViewById(R.id.input_phone1);
        input_phone2 = (EditText)findViewById(R.id.input_phone2);
        input_phone3 = (EditText)findViewById(R.id.input_phone3);
        btn_join = (Button)findViewById(R.id.btn_join);
    }

    private void success(){
        Call<User> userCall = networkService.newUser(n_user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) { //회원가입 성공
                    User result = response.body();
                    Toast.makeText(getApplicationContext(), "환영합니다", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("UserInfo", result); //user 정보가 들어있는 객체 전달
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MyTag", "회원가입 에러내용 : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "이미 가입되어 있습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dataInsert(){

        n_user.setName(input_name.getText().toString());
        n_user.setEmail(input_email.getText().toString());
        String phone = input_phone1.getText().toString();
        phone += input_phone2.getText().toString();
        phone += input_phone3.getText().toString();
        n_user.setPhone(phone);
        n_user.setRegisterid(token);
        //미 입력 값이 있을 때
        if(n_user.getEmail()==null)
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
        else if(n_user.getName()==null)
            Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_LONG).show();
        else if(n_user.getPhone()==null)
            Toast.makeText(getApplicationContext(), "휴대폰 번호를 입력해주세요", Toast.LENGTH_LONG).show();
        else{
            if(n_user.getPhone().length() < 11){
                Toast.makeText(getApplicationContext(), "휴대폰 번호를 제대로 입력해주세요.", Toast.LENGTH_LONG).show();
            }else{
                success();
            }
        }
    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                    Log.i(TAG,"Ready");
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                    Log.i(TAG,"generating");
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    token = intent.getStringExtra("token");
                    n_user.setRegisterid(token);
                    Log.i(TAG,"complete / token :"+token);
                }

            }
        };
    }

    @Override
    protected void attachBaseContext(Context newBase) {//폰트 적용 함수
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_join);
        Typekit.getInstance()//폰트적용
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothicLight.otf"));
        initview();
        getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.titlebar_join);

        //gcm
        registBroadcastReceiver();
        getInstanceIdToken();

        ApplicationController application = ApplicationController. getInstance();
        application.buildNetworkService("52.41.56.246", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        n_user = (User)intent.getParcelableExtra("UserInfo");
        input_name.setText(n_user.getName());
        input_email.setText(n_user.getEmail());

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataInsert();
            }
        });
    }

    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
