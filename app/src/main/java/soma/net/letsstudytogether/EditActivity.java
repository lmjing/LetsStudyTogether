package soma.net.letsstudytogether;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import soma.net.letsstudytogether.component.ApplicationController;
import soma.net.letsstudytogether.model.User;
import soma.net.letsstudytogether.network.NetworkService;

public class EditActivity extends AppCompatActivity {
    EditText input_email,input_phone1,input_phone2,input_phone3,input_name;
    TextView txt_installtime, txt_playtime, txt_endtime;
    Button btn_edit;
    private NetworkService networkService;
    User n_user = new User(); //new_user 여기에 회원가입하는 놈 정보저장해
    int year,month,day;

    private void initview(){
        input_name = (EditText)findViewById(R.id.input_name);
        input_email = (EditText)findViewById(R.id.input_email);
        input_phone1 = (EditText)findViewById(R.id.input_phone1);
        input_phone2 = (EditText)findViewById(R.id.input_phone2);
        input_phone3 = (EditText)findViewById(R.id.input_phone3);
        btn_edit= (Button)findViewById(R.id.btn_edit);
        txt_endtime = (TextView)findViewById(R.id.txt_endtime);
        txt_installtime = (TextView)findViewById(R.id.txt_installtime);
        txt_playtime = (TextView)findViewById(R.id.txt_playtime);
    }

    private void success(){
        Call<String> userCall = networkService.editUser(n_user);
        userCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess()) { //수정 성공
                    finish();
                    Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MyTag", "회원수정 에러내용 : " + t.getMessage());
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

        //미 입력 값이 있을 때
        if(n_user.getEmail()==null)
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
        else if(n_user.getName()==null)
            Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_LONG).show();
        else if(n_user.getPhone()==null)
            Toast.makeText(getApplicationContext(), "휴대폰 번호를 입력해주세요", Toast.LENGTH_LONG).show();
        else if(n_user.getType()!=1&&n_user.getType()!=2)
            Toast.makeText(getApplicationContext(), "타입을 선택해주세요", Toast.LENGTH_LONG).show();
        else{
            if(n_user.getPhone().length() < 11){
                Toast.makeText(getApplicationContext(), "휴대폰 번호를 제대로 입력해주세요.", Toast.LENGTH_LONG).show();
            }else{
                success();
            }
        }
    }

    private void initUserInfo(User user){
        SharedPreferences setting = getSharedPreferences("setting", 0);
        txt_installtime.setText(setting.getString("app_install_time","0000-00-00 00:00:00"));
        txt_playtime.setText(setting.getString("app_play_time","0000-00-00 00:00:00"));
        txt_endtime.setText(setting.getString("app_end_time","0000-00-00 00:00:00"));

        input_email.setText(user.getEmail());
        input_name.setText(user.getName());
        input_phone1.setText(user.getPhone().substring(0,3));
        input_phone2.setText(user.getPhone().substring(3,7));
        input_phone3.setText(user.getPhone().substring(7,11));
    }

    @Override
    protected void attachBaseContext(Context newBase) {//폰트 적용 함수
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit);
        Typekit.getInstance()//폰트적용
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothicLight.otf"));
        initview();
        getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.titlebar_edit);


        ApplicationController application = ApplicationController. getInstance();
        application.buildNetworkService("52.41.56.246", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();

        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        n_user = (User)intent.getParcelableExtra("UserInfo");
        initUserInfo(n_user);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataInsert();
            }
        });
    }


}
