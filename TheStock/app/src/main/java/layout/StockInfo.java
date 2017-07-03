package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.william.www.thestock.R;
import com.william.www.thestock.adapter.InfoAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StockInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StockInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockInfo extends Fragment implements View.OnClickListener {

    EditText et_sid,et_sname;
    TextView tv_NumOfSellFiv,
             tv_NumOfBuyFir,
             tv_NumOfSellFou,
             tv_NumOfBuyTwo,
             tv_NumOfSellThr,
             tv_NumOfBuyThr,
             tv_NumOfSellTwo,
             tv_NumOfBuyFou,
             tv_NumOfSellFir,
             tv_NumOfBuyFiv;
    ViewPager pager = null;
    PagerTabStrip tabStrip = null;
    ArrayList<Fragment> viewContainter = new ArrayList<Fragment>();
    ArrayList<String> titleContainer = new ArrayList<String>();
    public String TAG = "tag";


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Handler handler = new Handler();
    private InfoAdapter infoAdapter;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StockInfo() {
        // Required empty public constructor
    }

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            String url="http://47.93.227.240:8080/SSMDemo/stockt/searchStockBySid.action";
            RequestParams requestParams=new RequestParams(url);
            //封装两个参数
            requestParams.addBodyParameter("sid",et_sid.getText().toString());
            //requestParams.addBodyParameter("pwd4",upwd);

            x.http().post(requestParams, new Callback.CommonCallback<String>() {
                //成功返回的 类似于success:function
                @Override
                public void onSuccess(String result) {
                    //result=result;
                    //Intent intent=new Intent(MainActivity.this,CenterActivity.class);
                    //startActivity(intent);
                    JSONObject res= null;
                    try {
                        res = new JSONObject(result);
                        et_sname.setText(res.getString("sname"));
                        tv_NumOfSellFiv.setText(res.getString("numOfSellFiv"));
                        tv_NumOfBuyFir.setText(res.getString("numOfBuyFir"));
                        tv_NumOfSellFou.setText(res.getString("numOfSellFou"));
                        tv_NumOfBuyTwo.setText(res.getString("numOfBuySec"));
                        tv_NumOfSellThr.setText(res.getString("numOfSellThr"));
                        tv_NumOfBuyThr.setText(res.getString("numOfBuyThr"));
                        tv_NumOfSellTwo.setText(res.getString("numOfSellSec"));
                        tv_NumOfBuyFou.setText(res.getString("numOfBuyFou"));
                        tv_NumOfSellFir.setText(res.getString("numOfSellFir"));
                        tv_NumOfBuyFiv.setText(res.getString("numOfBuyFiv"));

                        //System.out.println("''''''''''''''''''''''"+res.getString("numOfSellFiv"));
                        //更新图表数据
                        System.out.println("更新图表数据！");
                        infoAdapter.UpdateFragment(1,KLineFragment.newInstance(res.getString("sid")));

                        System.out.println("fragment的大小："+viewContainter.size());
                        pager.setAdapter(infoAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(result);

                }
                //网络连接error error:function
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
    };



    // TODO: Rename and change types and number of parameters
    public static StockInfo newInstance() {
        return new StockInfo();
    }
    public static StockInfo newInstance(String param1, String param2) {
        StockInfo fragment = new StockInfo();
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
        View v = inflater.inflate(R.layout.fragment_stock_info, container, false);
        //ButterKnife.bind(this, v);
        pager = (ViewPager) v.findViewById(R.id.viewpager);
        tabStrip = (PagerTabStrip) v.findViewById(R.id.tabstrip);
        et_sid = (EditText) v.findViewById(R.id.et_sid);
        et_sname = (EditText) v.findViewById(R.id.et_sname);
        tv_NumOfSellFiv= (TextView) v.findViewById(R.id.tv_NumOfSellFiv);
        tv_NumOfBuyFir= (TextView) v.findViewById(R.id.tv_NumOfBuyFir);
        tv_NumOfSellFou= (TextView) v.findViewById(R.id.tv_NumOfSellFou);
        tv_NumOfBuyTwo= (TextView) v.findViewById(R.id.tv_NumOfBuyTwo);
        tv_NumOfSellThr= (TextView) v.findViewById(R.id.tv_NumOfSellThr);
        tv_NumOfBuyThr= (TextView) v.findViewById(R.id.tv_NumOfBuyThr);
        tv_NumOfSellTwo= (TextView) v.findViewById(R.id.tv_NumOfSellTwo);
        tv_NumOfBuyFou= (TextView) v.findViewById(R.id.tv_NumOfBuyFou);
        tv_NumOfSellFir= (TextView) v.findViewById(R.id.tv_NumOfSellFir);
        tv_NumOfBuyFiv= (TextView) v.findViewById(R.id.tv_NumOfBuyFiv);

        tv_NumOfBuyFir.setOnClickListener(this);
        et_sid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(delayRun!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 800);
            }
        });

        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置tab的背景色
        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.white_f));
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.white_e));
        tabStrip.setTextSpacing(200);

        //viewpager开始添加view
        viewContainter.add(new MinuteFragment());
        viewContainter.add(new KLineFragment());
        //页签项
        titleContainer.add("实时图");
        titleContainer.add("K 线图");
        boolean[] fragmentsUpdateFlag = { true, true};

        infoAdapter=new InfoAdapter(getFragmentManager(),viewContainter,fragmentsUpdateFlag);
        pager.setAdapter(infoAdapter);
        pager.getAdapter().notifyDataSetChanged();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        System.out.println("点击事件");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
