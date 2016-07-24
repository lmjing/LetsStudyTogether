package soma.net.letsstudytogether;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Splash extends AppCompatActivity {

    SharedPreferences setting;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

            Typekit.getInstance()//폰트적용
                    .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothicLight.otf"));

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        //현재 시간 알아내기
        String now = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));

        //설치가 처음이라면 앱 설치 시간 저장
        if(setting.getString("app_install_time",null)==null)
            editor.putString("app_install_time", now);
        //어플 접속시간 저장
        editor.putString("app_play_time",now);
        editor.commit();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),FacebookLogin.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void attachBaseContext(Context newBase) {//폰트 적용 함수
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
