package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
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
import android.widget.TextView;

import com.william.www.thestock.R;
import com.william.www.thestock.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class chanpsw extends Fragment implements TextWatcher {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String newpsw;
    private EditText edittext1;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private String id;


    public chanpsw() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chanpsw.
     */
    // TODO: Rename and change types and number of parameters
    public static chanpsw newInstance(String param1, String param2) {
        chanpsw fragment = new chanpsw();
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
        View view = inflater.inflate(R.layout.fragment_chanpsw, container, false);


        edittext1 =(EditText)view.findViewById(R.id.newpsw);
        edittext1.addTextChangedListener(this);
        Button button1 = (Button)view.findViewById(R.id.save);
        SharedPreferencesHelper share = new SharedPreferencesHelper();
        id = share.getValue(getContext(),"shared","id","");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://47.93.227.240:8080/SSMDemo/usert/changepassword.action";

                final RequestParams requestParams=new RequestParams(url);
                requestParams.addBodyParameter("uid",id);
                requestParams.addBodyParameter("upwd",newpsw);
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    //成功返回的 类似于success:function
                    @Override
                    public void onSuccess(String result) {
                        //Toast.makeText(this,"success  :"+result,Toast.LENGTH_LONG).show();
                        dialog();
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
        return view;
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("密码修改成功，请重新登录");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.main_container,new Login());
                ft.commit();
               }
            });
        builder.create().show();
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
        newpsw = edittext1.getText().toString();
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
