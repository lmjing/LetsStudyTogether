package soma.net.letsstudytogether.component;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import soma.net.letsstudytogether.network.NetworkService;

public class ApplicationController extends Application {

    // TODO: 2. ApplicationController의 instance 선언 및 getter 생성
    private static ApplicationController instance ;
    public static ApplicationController getInstance() { return instance ; }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this; //어플리케이션이 처음 실행될 때 인스턴스를 생성합니다.
    }

    // TODO: 3. Activity에서 사용할 NetworkService 선언 및 getter 생성
    private NetworkService networkService;
    public NetworkService getNetworkService() { return networkService; }

    private String baseUrl; //이번 세미나에서 baseUrl은 서버파트원들의 ip와 port에 따라 다릅니다.

    public void buildNetworkService(String ip, int port) {
        synchronized (ApplicationController.class) {
            if (networkService == null) {
                baseUrl = String.format("http://%s:%d", ip, port);
                Gson gson = new GsonBuilder()
                        .create();

                GsonConverterFactory factory = GsonConverterFactory.create(gson); //서버에서 json 형식으로 데이터를 보내고 이를 파싱해서 받아오기 위해서 사용합니다.

                // TODO: 4. Retrofit.Builder()를 이용해 Retrofit 객체를 생성한 후 이를 이용해 networkService 정의
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(factory)
                        .build();
                networkService = retrofit.create(NetworkService.class);
            }
        }
    }
}
