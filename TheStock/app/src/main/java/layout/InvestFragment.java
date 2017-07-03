package layout;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.william.www.thestock.R;
import com.william.www.thestock.api.ConstantTest;
import com.william.www.thestock.SharedPreferencesHelper;
import com.william.www.thestock.bean.DataParse;
import com.william.www.thestock.bean.MinutesBean;
import com.william.www.thestock.mychart.MyBarChart;
import com.william.www.thestock.mychart.MyBottomMarkerView;
import com.william.www.thestock.mychart.MyLeftMarkerView;
import com.william.www.thestock.mychart.MyLineChart;
import com.william.www.thestock.mychart.MyRightMarkerView;
import com.william.www.thestock.mychart.MyXAxis;
import com.william.www.thestock.mychart.MyYAxis;
import com.william.www.thestock.rxutils.MyUtils;
import com.william.www.thestock.rxutils.VolFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class InvestFragment extends Fragment implements TextWatcher, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private LineDataSet d1, d2;
    MyXAxis xAxisLine;
    MyYAxis axisRightLine;
    MyYAxis axisLeftLine;
    BarDataSet barDataSet;

    MyLineChart lineChart;
    MyBarChart barChart;

    MyXAxis xAxisBar;
    MyYAxis axisLeftBar;
    MyYAxis axisRightBar;
    SparseArray<String> stringSparseArray;
    private DataParse mData;
    Integer sum = 0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TableLayout table;
    private TableLayout record;
    private Button bt_Refresh,bt_Buy,bt_Sell;
    private JSONArray resu;
    private EditText et_investSid,et_investSname,et_investCost,et_investNum;
    private TextView tv_CurrentPri;

    private Handler handler = new Handler();

    //private OnFragmentInteractionListener mListener;

    public InvestFragment() {
        // Required empty public constructor
    }

    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据

            String url="http://47.93.227.240:8080/SSMDemo/stockt/searchStockBySid.action";
            RequestParams requestParams=new RequestParams(url);
            //封装两个参数
            requestParams.addBodyParameter("sid",et_investSid.getText().toString());
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
                        et_investSname.setText(res.getString("sname"));
                        tv_CurrentPri.setText(res.getString("currentPrice"));
                        et_investNum.setText("");
                        et_investCost.setText("");
                        //更新图表信息
                        getOnLineData(res.getString("sid"));
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvestFragment newInstance(String param1, String param2) {
        InvestFragment fragment = new InvestFragment();
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
        View v=inflater.inflate(R.layout.fragment_invest, container, false);

        lineChart = (MyLineChart) v.findViewById(R.id.mc_investChart);
        barChart=(MyBarChart) v.findViewById(R.id.mbc_investChart);
        initChart();
        stringSparseArray = setXLabels();
        getOnLineData("600008");
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//              barChart.setHighlightValue(new Highlight(h.getXIndex(), 0));
                barChart.highlightValue(new Highlight(h.getXIndex(), 0));
                // lineChart.setHighlightValue(h);
            }
            @Override
            public void onNothingSelected() {
                barChart.highlightValue(null);
            }
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                lineChart.highlightValue(new Highlight(h.getXIndex(), 0));
                // lineChart.setHighlightValue(new Highlight(h.getXIndex(), 0));//此函数已经返回highlightBValues的变量，并且刷新，故上面方法可以注释
                //barChart.setHighlightValue(h);
            }
            @Override
            public void onNothingSelected() {
                lineChart.highlightValue(null);
            }
        });

        table = (TableLayout) v.findViewById(R.id.tl_userProperty);
        record=(TableLayout) v.findViewById(R.id.tl_userRecord);
        bt_Refresh= (Button) v.findViewById(R.id.bt_Refresh);
        et_investSid= (EditText) v.findViewById(R.id.et_investSid);
        et_investSname= (EditText) v.findViewById(R.id.et_investSname);
        et_investCost= (EditText) v.findViewById(R.id.et_investCost);
        et_investNum= (EditText) v.findViewById(R.id.et_investNum);
        tv_CurrentPri= (TextView) v.findViewById(R.id.tv_CurrentPri);
        bt_Buy= (Button) v.findViewById(R.id.bt_Buy);
        bt_Sell= (Button) v.findViewById(R.id.bt_Sell);
        bt_Buy.setOnClickListener(this);
        bt_Sell.setOnClickListener(this);
        et_investSid.addTextChangedListener(this);

        //更新表格
        updateUserMoney();
        updateUserRecord();
        record.canScrollVertically(1);
        bt_Refresh.setOnClickListener(this);
        return v;
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
        //mListener = null;
    }

    public void updateUserMoney(){

        SharedPreferencesHelper shared=new SharedPreferencesHelper();
        String uname=shared.getValue(getContext(),"shared","name","null");
        String uid=shared.getValue(getContext(),"shared","id","null");
        //System.out.println("用户名为： "+sname);

        String url="http://47.93.227.240:8080/SSMDemo/usert/userBalance.action";
        RequestParams requestParams=new RequestParams(url);
        requestParams.addBodyParameter("uid",uid);

        x.http().post(requestParams, new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                JSONArray resu= null;
                JSONObject res=null;
                try {
                    resu = new JSONArray(result);
                    res=(JSONObject) resu.get(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TableRow row=new TableRow(getContext());
                TextView money = new TextView(getContext());
                TextView valueOfStock = new TextView(getContext());
                TextView fValueOfStock = new TextView(getContext());
                TextView count = new TextView(getContext());

                TableRow.LayoutParams params=new TableRow.LayoutParams();
                params.weight=1.0f;

                row.setId(0);
                row.setGravity(Gravity.CENTER);
                int posi=Gravity.LEFT;

                try {
                    money.setText(res.getString("money"));
                    fValueOfStock.setText(res.getString("fValueOfStock"));
                    valueOfStock.setText(res.getString("valueOfStock"));
                    count.setText(res.getString("count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                money.setGravity(posi);//文本居中
                money.setTextSize((float) 14);//文本大小
                money.setLayoutParams(params);
                row.addView(money);//添加一行

                fValueOfStock.setGravity(posi);//文本居中
                fValueOfStock.setTextSize((float) 14);//文本大小
                fValueOfStock.setLayoutParams(params);
                row.addView(fValueOfStock);//添加一行

                valueOfStock.setGravity(posi);//文本居中
                valueOfStock.setTextSize((float) 14);//文本大小
                valueOfStock.setLayoutParams(params);
                row.addView(valueOfStock);//添加一行

                count.setGravity(posi);//文本居中
                count.setTextSize((float) 14);//文本大小
                count.setLayoutParams(params);
                row.addView(count);//添加一行

                table.addView(row);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void updateUserRecord(){

        SharedPreferencesHelper shared=new SharedPreferencesHelper();
        String uname=shared.getValue(getContext(),"shared","name","null");
        final String uid=shared.getValue(getContext(),"shared","id","null");
        //System.out.println("用户名为： "+sname);

        String url="http://47.93.227.240:8080/SSMDemo/billt/userBill.action";
        RequestParams requestParams=new RequestParams(url);
        requestParams.addBodyParameter("uid",uid);

        x.http().post(requestParams, new Callback.CommonCallback<String>(){
            @Override
            public void onSuccess(String result) {
                JSONArray resu= null;
                JSONObject res=null;

                try {
                    resu = new JSONArray(result);
                    setResu(resu);

                    for (int i=0;i<resu.length();i++){
                        TextView sid = new TextView(getContext());
                        TextView sname = new TextView(getContext());
                        TextView balance = new TextView(getContext());
                        TextView abalance = new TextView(getContext());
                        TextView unbalance = new TextView(getContext());
                        TextView profits = new TextView(getContext());
                        TextView cost = new TextView(getContext());
                        TextView currentPri = new TextView(getContext());
                        TextView profitsRate = new TextView(getContext());
                        TextView tv_uid = new TextView(getContext());
                        TableRow.LayoutParams params=new TableRow.LayoutParams();
                        params.weight=1.0f;

                        int posi=Gravity.CENTER;

                        sid.setGravity(posi);//文本居中
                        sid.setTextSize((float) 14);//文本大小
                        sid.setLayoutParams(params);

                        sname.setGravity(posi);//文本居中
                        sname.setTextSize((float) 14);//文本大小
                        sname.setLayoutParams(params);

                        balance.setGravity(posi);//文本居中
                        balance.setTextSize((float) 14);//文本大小
                        balance.setLayoutParams(params);

                        abalance.setGravity(posi);//文本居中
                        abalance.setTextSize((float) 14);//文本大小
                        abalance.setLayoutParams(params);

                        unbalance.setGravity(posi);//文本居中
                        unbalance.setTextSize((float) 14);//文本大小
                        unbalance.setLayoutParams(params);

                        profits.setGravity(posi);//文本居中
                        profits.setTextSize((float) 14);//文本大小
                        profits.setLayoutParams(params);

                        cost.setGravity(posi);//文本居中
                        cost.setTextSize((float) 14);//文本大小
                        cost.setLayoutParams(params);

                        currentPri.setGravity(posi);//文本居中
                        currentPri.setTextSize((float) 14);//文本大小
                        currentPri.setLayoutParams(params);

                        profitsRate.setGravity(posi);//文本居中
                        profitsRate.setTextSize((float) 14);//文本大小
                        profitsRate.setLayoutParams(params);

                        tv_uid.setGravity(posi);//文本居中
                        tv_uid.setTextSize((float) 14);//文本大小
                        tv_uid.setLayoutParams(params);

                        TableRow row=new TableRow(getContext());
                        row.setGravity(Gravity.CENTER);
                        row.setId(i+1);
                        //System.out.println("行号为：" +resu.length());
                        //System.out.println("行号：" + i);
                        //System.out.println("内容：" + resu.get(i));
                        res=(JSONObject) resu.get(i);
                        sid.setText(res.getString("sid"));
                        sname.setText(res.getString("sname"));
                        balance.setText(res.getString("balance"));
                        abalance.setText(res.getString("abalance"));
                        unbalance.setText(res.getString("unbalance"));
                        profits.setText(res.getString("profits"));
                        cost.setText(res.getString("cost"));
                        currentPri.setText(res.getString("currentPri"));
                        profitsRate.setText(res.getString("profitsRate"));
                        tv_uid.setText(uid);

                        row.addView(sid);//添加一行
                        row.addView(sname);//添加一行
                        row.addView(balance);//添加一行
                        row.addView(abalance);//添加一行
                        row.addView(unbalance);//添加一行
                        row.addView(profits);//添加一行
                        row.addView(cost);//添加一行
                        row.addView(currentPri);//添加一行
                        row.addView(profitsRate);//添加一行
                        row.addView(tv_uid);//添加一行

                        record.addView(row);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public JSONArray getResu() {
        return resu;
    }

    public void setResu(JSONArray resu) {
        this.resu = resu;
    }

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

    private void initChart() {
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.setBorderWidth(1);
        lineChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        lineChart.setDescription("");
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);

        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        barChart.setDescription("");


        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);
        //x轴
        xAxisLine = lineChart.getXAxis();
        xAxisLine.setDrawLabels(true);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxisLine.setLabelsToSkip(59);


        //左边y
        axisLeftLine = lineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setLabelCount(5, true);
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setDrawGridLines(false);
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);



        //右边y
        axisRightLine = lineChart.getAxisRight();
        axisRightLine.setLabelCount(2, true);
        axisRightLine.setDrawLabels(true);
        axisRightLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });

        axisRightLine.setStartAtZero(false);
        axisRightLine.setDrawGridLines(false);
        axisRightLine.setDrawAxisLine(false);
        //背景线
        xAxisLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine.enableGridDashedLine(10f,5f,0f);
        xAxisLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisRightLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        axisRightLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));

        //bar x y轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(true);
        xAxisBar.setDrawAxisLine(false);
        // xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));

        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        //y轴样式
        this.axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });

    }

    private void setData(DataParse mData) {
        setMarkerView(mData);
        setShowLabels(stringSparseArray);
        Log.e("###", mData.getDatas().size() + "ee");
        if (mData.getDatas().size() == 0) {
            lineChart.setNoDataText("暂无数据");
            return;
        }
        //设置y左右两轴最大最小值
        axisLeftLine.setAxisMinValue(mData.getMin());
        axisLeftLine.setAxisMaxValue(mData.getMax());
        axisRightLine.setAxisMinValue(mData.getPercentMin());
        axisRightLine.setAxisMaxValue(mData.getPercentMax());


        axisLeftBar.setAxisMaxValue(mData.getVolmax());
        /*单位*/
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        /*次方*/
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        axisLeftBar.setShowMaxAndUnit(unit);
        axisLeftBar.setDrawLabels(true);
        //axisLeftBar.setAxisMinValue(0);//即使最小是不是0，也无碍
        axisLeftBar.setShowOnlyMinMax(true);
        axisRightBar.setAxisMaxValue(mData.getVolmax());
        //   axisRightBar.setAxisMinValue(mData.getVolmin);//即使最小是不是0，也无碍
        //axisRightBar.setShowOnlyMinMax(true);

        //基准线
        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(getResources().getColor(R.color.minute_jizhun));
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLineWidth(1);
        axisRightLine.addLimitLine(ll);
        axisRightLine.setBaseValue(0);

        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        Log.e("##", Integer.toString(xVals.size()));
        for (int i = 0, j = 0; i < mData.getDatas().size(); i++, j++) {

            MinutesBean t = mData.getDatas().get(j);

            if (t == null) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }
            if (!TextUtils.isEmpty(stringSparseArray.get(i)) &&
                    stringSparseArray.get(i).contains("/")) {
                i++;
            }
            lineCJEntries.add(new Entry(mData.getDatas().get(i).cjprice, i));
            lineJJEntries.add(new Entry(mData.getDatas().get(i).avprice, i));
            barEntries.add(new BarEntry(mData.getDatas().get(i).cjnum, i));
            // dateList.add(mData.getDatas().get(i).time);
        }
        d1 = new LineDataSet(lineCJEntries, "成交价");
        d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        barDataSet = new BarDataSet(barEntries, "成交量");

        d1.setCircleRadius(0);
        d2.setCircleRadius(0);
        d1.setColor(getResources().getColor(R.color.minute_blue));
        d2.setColor(getResources().getColor(R.color.minute_yellow));
        d1.setHighLightColor(Color.WHITE);
        d2.setHighlightEnabled(false);
        d1.setDrawFilled(true);


        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighLightColor(Color.WHITE);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setColor(Color.RED);
        List<Integer> list=new ArrayList<>();
        list.add(Color.RED);
        list.add(Color.GREEN);
        barDataSet.setColors(list);
        //谁为基准
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);
        /*注老版本LineData参数可以为空，最新版本会报错，修改进入ChartData加入if判断*/
        LineData cd = new LineData(getMinutesCount(), sets);
        lineChart.setData(cd);
        BarData barData = new BarData(getMinutesCount(), barDataSet);
        barChart.setData(barData);

        setOffset();
        lineChart.invalidate();//刷新图
        barChart.invalidate();

    }

    private void getOnLineData(String code) {
           /*方便测试，加入假数据*/
        mData = new DataParse();
        System.out.println("code");
        String murl="http://47.93.227.240:8080/SSMDemo/stockt/searchTodayStock.action";

        RequestParams requestParams=new RequestParams(murl);
        requestParams.addBodyParameter("sid",code);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            //成功返回的 类似于success:function
            @Override
            public void onSuccess(String result) {
                try {
                    System.out.println("reslut:"+result);
                    //String[] temp1=result.split("\"");
                    String[] temp2=result.split(";");
                    //System.out.println("切割:"+temp1[1]);
                    System.out.println("切割2:"+temp2[0]);
                    mData.myParseMinutes(result);
                    //mData.getKLineDatas();

                    System.out.println("获取实时数据成功！");
                    setData(mData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Intent intent=new Intent(MainActivity.this,CenterActivity.class);
                //startActivity(intent);
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

    private SparseArray<String> setXLabels() {
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "09:30");
        xLabels.put(60, "10:30");
        xLabels.put(121, "11:30/13:00");
        xLabels.put(182, "14:00");
        xLabels.put(241, "15:00");
        return xLabels;
    }


    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = lineChart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = lineChart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
            //offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            // barChart.setExtraLeftOffset(offsetLeft);
            transLeft = lineLeft;

        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            lineChart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }

  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
            //offsetRight = Utils.convertPixelsToDp(lineRight);
            //barChart.setExtraRightOffset(offsetRight);
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            lineChart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        barChart.setViewPortOffsets(transLeft, 5, transRight, barBottom);
    }

    public void setShowLabels(SparseArray<String> labels) {
        xAxisLine.setXLabels(labels);
        xAxisBar.setXLabels(labels);
    }

    public String[] getMinutesCount() {
        return new String[242];
    }

    private void setMarkerView(DataParse mData) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(getActivity(), R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(getActivity(), R.layout.mymarkerview);
        MyBottomMarkerView bottomMarkerView = new MyBottomMarkerView(getActivity(), R.layout.mymarkerview);
        lineChart.setMarker(leftMarkerView, rightMarkerView,bottomMarkerView, mData);
        barChart.setMarker(leftMarkerView, rightMarkerView,bottomMarkerView, mData);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bt_Buy){
            String murl="http://47.93.227.240:8080/SSMDemo/billt/buyStock.action";

            RequestParams requestParams=new RequestParams(murl);
            SharedPreferencesHelper shared=new SharedPreferencesHelper();
            String uid=shared.getValue(getContext(),"shared","id","null");

            requestParams.addBodyParameter("uid",uid);
            requestParams.addBodyParameter("sid",et_investSid.getText().toString());
//            System.out.println("输入为："+et_investSid.getText()+"--"+et_investCost.getText()+"--"+
//                    et_investNum.getText().toString());
            requestParams.addBodyParameter("cost",et_investCost.getText().toString());
            requestParams.addBodyParameter("balance",et_investNum.getText().toString());

            x.http().post(requestParams, new Callback.CommonCallback<String>() {
                //成功返回的 类似于success:function
                @Override
                public void onSuccess(String result) {
                    if(result.equals("\"success\"")){
                        new AlertDialog.Builder(getContext())
                                       .setTitle("结果")
                                       .setMessage("购买成功")
                                       .setPositiveButton("确定", null)
                                       .show();
                        table.removeViewsInLayout(1,1);
                        updateUserMoney();

                        record.removeViewsInLayout(1,getResu().length());
                        updateUserRecord();
                        et_investNum.setText("");
                    }
                    else{
                        new AlertDialog.Builder(getContext())
                                .setTitle("结果")
                                .setMessage("购买失败")
                                .setPositiveButton("确定", null)
                                .show();
                    }

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
        else if(v.getId()==R.id.bt_Sell){
            String murl="http://47.93.227.240:8080/SSMDemo/billt/sellStock.action";

            RequestParams requestParams=new RequestParams(murl);
            SharedPreferencesHelper shared=new SharedPreferencesHelper();
            String uid=shared.getValue(getContext(),"shared","id","null");

            requestParams.addBodyParameter("uid",uid);
            requestParams.addBodyParameter("sid",et_investSid.getText().toString());
//            System.out.println("输入为："+et_investSid.getText()+"--"+et_investCost.getText()+"--"+
//                    et_investNum.getText().toString());
            requestParams.addBodyParameter("cost",et_investCost.getText().toString());
            requestParams.addBodyParameter("balance",et_investNum.getText().toString());

            x.http().post(requestParams, new Callback.CommonCallback<String>() {
                //成功返回的 类似于success:function
                @Override
                public void onSuccess(String result) {
                    System.out.println("返回结果:"+result);
                    if(result.equals("\"success\"")){
                        new AlertDialog.Builder(getContext())
                                .setTitle("结果")
                                .setMessage("卖出成功")
                                .setPositiveButton("确定", null)
                                .show();
                        table.removeViewsInLayout(1,1);
                        updateUserMoney();

                        record.removeViewsInLayout(1,getResu().length());
                        updateUserRecord();

                        et_investNum.setText("");
                    }
                    else{
                        new AlertDialog.Builder(getContext())
                                .setTitle("结果")
                                .setMessage("卖出失败")
                                .setPositiveButton("确定", null)
                                .show();
                    }

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
        else if(v.getId()==R.id.bt_Refresh){
            table.removeViewsInLayout(1,1);
            updateUserMoney();

            record.removeViewsInLayout(1,getResu().length());
            updateUserRecord();

            et_investSid.setText("");
            et_investSname.setText("");
            et_investNum.setText("");
            et_investCost.setText("");
            tv_CurrentPri.setText("");
        }
    }
}
