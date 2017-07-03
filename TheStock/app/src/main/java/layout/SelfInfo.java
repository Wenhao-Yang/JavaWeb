package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.william.www.thestock.R;
import com.william.www.thestock.SharedPreferencesHelper;

import org.w3c.dom.Text;

public class SelfInfo extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private FragmentManager fm;
    private FragmentTransaction ft;



    public SelfInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelfInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static SelfInfo newInstance(String param1, String param2) {
        SelfInfo fragment = new SelfInfo();
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
        View v = inflater.inflate(R.layout.fragment_self_info, container, false);
        //ButterKnife.bind(this, v);
        textView1 = (TextView) v.findViewById(R.id.xinxi);
        textView2 = (TextView) v.findViewById(R.id.asset);
        textView3 = (TextView) v.findViewById(R.id.chanpsw);
        textView4 = (TextView) v.findViewById(R.id.textView5);
        SharedPreferencesHelper share = new SharedPreferencesHelper();
        String name = share.getValue(getContext(),"shared","name","");
        System.out.println("1111111111");
        System.out.println(name);
        textView4.setText(name);
        //System.out.println(textView1.getText());
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        Fragment fragment=null;
        return v;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated methodstub
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        System.out.println("aaaaaaaaaaaaaaa");
        switch (v.getId()) {

            case R.id.xinxi:
                ft.replace(R.id.main_container,new infor());
                break;
            case R.id.asset:
                ft.replace(R.id.main_container,new asset());
                break;
            case R.id.chanpsw:
                ft.replace(R.id.main_container,new chanpsw());
                break;

            default:
                break;
        }
        // 不要忘记提交
        ft.commit();
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

    public void onClick1(View view) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
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
