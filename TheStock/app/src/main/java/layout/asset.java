package layout;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.william.www.thestock.R;
import com.william.www.thestock.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Iterator;


public class asset extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String id;
    private Login.CallBackValue callBackValue;



    public asset() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment asset.
     */
    // TODO: Rename and change types and number of parameters
    public static asset newInstance(String param1, String param2) {
        asset fragment = new asset();
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
        View v = inflater.inflate(R.layout.fragment_asset, container, false);

        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url="http://47.93.227.240:8080/SSMDemo/billt/userBill.action";

        final RequestParams requestParams=new RequestParams(url);
        SharedPreferencesHelper share = new SharedPreferencesHelper();
        String id = share.getValue(getContext(),"shared","id","");
        System.out.println(id);
        requestParams.addBodyParameter("uid",id);

                //xutils网络连接执行的核心方法，相对于当前Activity类主线程操作的子线程
        x.http().post(requestParams, new Callback.CommonCallback<String>() {


            //成功返回的 类似于success:function
                    @Override
                    public void onSuccess(String result) {
                        //Toast.makeText(this,"success  :"+result,Toast.LENGTH_LONG).show();
                        try {
                            //修改用户名
                            JSONArray stockinfo= new JSONArray(result);
                            int length = stockinfo.length();
                            String []str_a = new String[10];
                            int[] textViewID = new int[] { R.id.bill1,R.id.bill2, R.id.bill3,R.id.bill4,R.id.bill5,R.id.bill6 };
                            Resources res = getResources();
                            for(int i = 0; i < length; i++){//遍历JSONArray
                                JSONObject oj = stockinfo.getJSONObject(i);
                                str_a[i] = "公司名称："+oj.getString("sname")+"    持股数："+oj.getString("balance")+"\n买入价格："+oj.getString("cost")+"           当前价格："+oj.getString("currentPri");


                                TextView textview = (TextView)getView().findViewById(textViewID[i]);
                                textview.setText(str_a[i]);
                            }
                            System.out.println(length);
                            //callBackValue.SendMessageValue(userinfo.getString("id"),userinfo.getString("name"));
                            //System.out.println(userinfo.getString("name"));


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
