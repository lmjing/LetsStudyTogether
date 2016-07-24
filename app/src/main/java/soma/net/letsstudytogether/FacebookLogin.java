package soma.net.letsstudytogether;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.ShareDialog;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import soma.net.letsstudytogether.component.ApplicationController;
import soma.net.letsstudytogether.model.User;
import soma.net.letsstudytogether.network.NetworkService;

/**
 * Created by lmjin_000 on 2016-07-12.
 */
public class FacebookLogin extends Activity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private NetworkService networkService;
    User user = new User(); // 로그인할때
    public String name, id, email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.facebooklogin);

        Typekit.getInstance()//폰트적용
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunGothicLight.otf"));
        //서버연결
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.41.56.246", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        LoginButton loginButton = (LoginButton)findViewById(R.id.btn_facebook);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {//로그인이 성공되었을때 호출
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        // Application code
                                        try {
                                            id = (String) response.getJSONObject().get("id");//페이스북 아이디값
                                            name = (String) response.getJSONObject().get("name");//페이스북 이름
                                            email = (String) response.getJSONObject().get("email");//이메일

                                            user.setId(id);
                                            user.setName(name);
                                            user.setEmail(email);

                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        check_first(id);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

              @Override
              public void onCancel() {
                  Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
              }

              @Override
              public void onError(FacebookException error) {
                  Toast.makeText(getApplicationContext(), error + "", Toast.LENGTH_SHORT).show();
              }
          });
    }

    //가입되어있는지 확인
    private void check_first(String id){
        Call<User> userCall = networkService.getUser(id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) { //가입 되어있음 - 바로 메인으로 넘어감
                    user = response.body();
                    Toast.makeText(getApplicationContext(), "환영합니다", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("UserInfo", user); //user 정보가 들어있는 객체 전달
                    startActivity(intent);
                    finish();
                }else{
                    //가입되어있지 않음
                    Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                    intent.putExtra("UserInfo", user); //user 정보가 들어있는 객체 전달
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("Show", "로그인 에러내용 : " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {//폰트 적용 함수
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
