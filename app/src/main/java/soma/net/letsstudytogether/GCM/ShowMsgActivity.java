package soma.net.letsstudytogether.GCM;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import soma.net.letsstudytogether.R;
import soma.net.letsstudytogether.component.ApplicationController;
import soma.net.letsstudytogether.model.Study;
import soma.net.letsstudytogether.model.User;
import soma.net.letsstudytogether.network.NetworkService;
import soma.net.letsstudytogether.study_show.Show_Study;

/**
 * Created by lmjin_000 on 2016-03-24.
 */
public class ShowMsgActivity extends Activity {

    private NetworkService networkService;

    private Button cancel,move;
    private User user;
    private Study study;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ApplicationController application = ApplicationController. getInstance();
        application.buildNetworkService("52.41.56.246", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String userid = intent.getStringExtra("userid");
        int studyid = intent.getIntExtra("studyid",99999);

        getUser(userid);
        getStudy(studyid);

        //투명한 액티비티 띄우는 용도
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        //다이얼로그 띄우기
        FinalDialog(title, message,userid,studyid);
    }

    private void FinalDialog(String title, String message, String userid, int studyid){
        final Dialog dialog = new Dialog(ShowMsgActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog);
        
        move = (Button) dialog.findViewById(R.id.btn_move);
        cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Show_Study.class);
                intent.putExtra("showStudy", study);
                intent.putExtra("UserInfo",user);
                startActivity(intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    //user정보 가져오기
    private void getUser(String id){
        Call<User> userCall = networkService.getUser(id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    user = response.body();
                }else{
                    Log.i("gcm", "푸시 다이얼로그 오류");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("gcm", "push 다이얼로그 에러내용 : " + t.getMessage());
            }
        });
    }

    //study정보 가져오기
    private void getStudy(int studyid) {
        Call<Study> studyCall = networkService.toMoveStudy(studyid);

        studyCall.enqueue(new Callback<Study>() {
            @Override
            public void onResponse(Response<Study> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    study = response.body();
                } else {
                    int statusCode = response.code();
                    Log.i("gcm", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("gcm", "서버 응답코드 : " + t.toString());
            }
        });
    }
}
