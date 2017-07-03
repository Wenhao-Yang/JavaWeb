package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.william.www.thestock.MainActivity;
import com.william.www.thestock.SharedPreferencesHelper;
import com.william.www.thestock.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private FragmentManager manager;
    private FragmentTransaction ft;

    private View view;
    private Button bt_login;
    private EditText username,passwd;
    private CallBackValue callBackValue;


    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance() {
        Login fragment = new Login();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, null);
        bt_login = (Button) view.findViewById(R.id.bt_login);
        username = (EditText) view.findViewById(R.id.et_username);
        passwd = (EditText) view.findViewById(R.id.et_passwd);

        manager = getFragmentManager();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示用户名和密码,获取控件输入的value
                String uname=username.getText().toString();
                String upwd=passwd.getText().toString();

                String url="http://47.93.227.240:8080/SSMDemo/usert/login.action";

                RequestParams requestParams=new RequestParams(url);

                requestParams.addBodyParameter("username",uname);
                requestParams.addBodyParameter("pwd",upwd);
                //xutils网络连接执行的核心方法，相对于当前Activity类主线程操作的子线程
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    //成功返回的 类似于success:function
                    @Override
                    public void onSuccess(String result) {
                        //Toast.makeText(this,"success  :"+result,Toast.LENGTH_LONG).show();
                        try {
                            //修改用户名
                            JSONObject userinfo=new JSONObject(result);
                            username.setText(userinfo.getString("name"));
                            callBackValue.SendMessageValue(userinfo.getString("id"),userinfo.getString("name"));
                            //System.out.println(userinfo.getString("name"));
                            SharedPreferencesHelper share = new SharedPreferencesHelper();
                            share.putValue(getContext(),"shared","name",userinfo.getString("name"));
                            share.putValue(getContext(),"shared","id",userinfo.getString("id"));
                            //跳转回主页
                            ft = manager.beginTransaction();
                            ft.replace(R.id.main_container, StockInfo.newInstance());
                            ft.addToBackStack(null);
                            ft.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        //取消方法
                    }

                    @Override
                    public void onFinished() {
                        //无论是连接ok，都最终执行该方法
                    }
                });

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        callBackValue =(CallBackValue) getActivity();

    }

    //定义一个回调接口
    public interface CallBackValue{
        public void SendMessageValue(String uid, String uname);
    }
}
