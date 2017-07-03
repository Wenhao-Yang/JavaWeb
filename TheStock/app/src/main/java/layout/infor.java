package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.william.www.thestock.R;
import com.william.www.thestock.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class infor extends Fragment implements TextWatcher {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText editText0;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private String name="";
    private String age="";
    private String sex="";
    private String sign="";
    private String work="";
    private FragmentManager fm;
    private FragmentTransaction ft;


    public infor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment infor.
     */
    // TODO: Rename and change types and number of parameters
    public static infor newInstance(String param1, String param2) {
        infor fragment = new infor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_infor, container, false);
        editText0 =(EditText)view.findViewById(R.id.name);
        editText1 =(EditText)view.findViewById(R.id.age);
        editText2 =(EditText)view.findViewById(R.id.sex);
        editText3 =(EditText)view.findViewById(R.id.sign);
        editText4 =(EditText)view.findViewById(R.id.work);
        SharedPreferencesHelper share = new SharedPreferencesHelper();
        name = share.getValue(getContext(),"shared","name","");
        getinfo(name);

        //System.out.println(name+"   "+sex+"   "+work+"   "+sign);

        //System.out.println("111");

        //editText1.setText(name);
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        editText4.addTextChangedListener(this);
        Button button1 = (Button)view.findViewById(R.id.save);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(name+"   "+sex+"   "+work+"   "+sign);
                changeinfo(name,age,sex,work,sign);
                dialog();
            }
        });
        return view;
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("信息修改成功");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.main_container,new infor());
                ft.commit();
            }
        });
        builder.create().show();
    }
    private void delay(int ms){
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void getinfo(String name1)
    {
        String url="http://47.93.227.240:8080/SSMDemo/usert/getinfo.action";

        final RequestParams requestParams=new RequestParams(url);
        requestParams.addBodyParameter("uname",name1);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            //成功返回的 类似于success:function
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject userinfo=new JSONObject(result);
                    //System.out.println(userinfo);
                    name = userinfo.getString("name");
                    age = userinfo.getString("age");
                    sex = userinfo.getString("gender");
                    sign = userinfo.getString("sign");
                    work = userinfo.getString("work");
                    //System.out.println(name+"   "+sex+"   "+work+"   "+sign);
                    editText0.setText(userinfo.getString("name"));
                    editText1.setText(userinfo.getString("age"));
                    editText2.setText(userinfo.getString("gender"));
                    editText3.setText(userinfo.getString("sign"));
                    editText4.setText(userinfo.getString("work"));


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
    public void changeinfo(String name1,String age,String sex1,String work1,String sign1)
    {
        String url="http://47.93.227.240:8080/SSMDemo/usert/changeinfo.action";

        final RequestParams requestParams=new RequestParams(url);
        requestParams.addBodyParameter("uname",name1);
        requestParams.addBodyParameter("age",age);
        requestParams.addBodyParameter("sex",sex1);
        requestParams.addBodyParameter("work",work1);
        requestParams.addBodyParameter("sign",sign1);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            //成功返回的 类似于success:function
            @Override
            public void onSuccess(String result) {
                //Toast.makeText(this,"success  :"+result,Toast.LENGTH_LONG).show();
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        //System.out.println(editText1.getText().toString())
        age = editText1.getText().toString();
        sex = editText2.getText().toString();
        sign = editText3.getText().toString();
        work = editText4.getText().toString();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
