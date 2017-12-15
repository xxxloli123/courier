package com.android.slowlifecourier.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.HelpActivity;
import com.android.slowlifecourier.activity.IncomeActivity;
import com.android.slowlifecourier.activity.JobSummaryActivity;
import com.android.slowlifecourier.activity.MessageCentreActivity;
import com.android.slowlifecourier.activity.PersonalInformationActivity;
import com.android.slowlifecourier.activity.RecommendAwardWinningActivity;
import com.android.slowlifecourier.activity.SetActivity;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.util.Common;
import com.android.slowlifecourier.view.RoundDrawable;
import com.interfaceconfig.Config;
import com.sgrape.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class PersonalCenterFragment extends BaseFragment {
    @BindView(R.id.set_up)
    LinearLayout setUp;
    @BindView(R.id.messege)
    LinearLayout messege;
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.personal_information)
    RelativeLayout personalInformation;
    @BindView(R.id.my_income)
    RelativeLayout myIncome;
    @BindView(R.id.job_summary)
    RelativeLayout jobSummary;
    @BindView(R.id.invite_friends)
    RelativeLayout inviteFriends;
    //    @BindView(R.id.complaint_opinion)
//    RelativeLayout complaintOpinion;
    @BindView(R.id.help_center)
    RelativeLayout helpCenter;
    @BindView(R.id.contact_service)
    RelativeLayout contactService;
    private String headImg;

    @Override
    public void onStart() {
        super.onStart();
        Info info = ((MyApplication) getContext().getApplicationContext()).getInfo();
        name.setText(info.getNickName());
        if (!TextUtils.equals(info.getHeaderImg(), headImg)) {
            headImg = info.getHeaderImg();
            loadImg(info);
        }
    }

    @OnClick({R.id.contact_service, R.id.set_up, R.id.messege, R.id.head, R.id.personal_information, R.id.my_income,
            R.id.job_summary, R.id.invite_friends, R.id.help_center})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.set_up://设置
                intent = new Intent(getActivity(), SetActivity.class);
                startActivity(intent);
                break;
            case R.id.messege://消息
                intent = new Intent(getActivity(), MessageCentreActivity.class);
                startActivity(intent);
                break;
            case R.id.head://头像
                break;
            case R.id.personal_information://个人信息
                intent = new Intent(getActivity(), PersonalInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.my_income://我的收入
                intent = new Intent(getActivity(), IncomeActivity.class);
                startActivity(intent);
                break;
            case R.id.job_summary://工作汇总
                intent = new Intent(getActivity(), JobSummaryActivity.class);
                startActivity(intent);
                break;
            case R.id.invite_friends://邀请好友
                intent = new Intent(getActivity(), RecommendAwardWinningActivity.class);
                startActivity(intent);
                break;
//            case R.id.complaint_opinion://投诉意见
//                intent=new Intent(getActivity(), ComplaintOpinionActivity.class);
//                startActivity(intent);
//                break;
            case R.id.help_center://帮助中心
                intent = new Intent(getActivity(), HelpActivity.class);
                intent.putExtra(HelpActivity.URI,"app/appCourier/helpCenter.html");
                startActivity(intent);
                break;
            case R.id.contact_service://联系客服
                Common.phoneDialog(getActivity(), "72721515");
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }

    private void loadImg(final Info info) {
        final Request request = new Request.Builder().url(Config.HEAD + info.getHeaderImg()).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.obtainMessage(1).sendToTarget();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Bitmap img = BitmapFactory.decodeStream(response.body().byteStream());
                handler.obtainMessage(0, img).sendToTarget();
            }
        });
    }

    @Override
    protected void readInstanceState() {
        super.readInstanceState();
        headImg = getArguments().getString("headimg");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = getArguments();
        bundle.putString("headimg", headImg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getContext(), "加载头像失败", Toast.LENGTH_SHORT).show();
                case 0:
                    if (msg.obj != null) {
                        Bitmap img = (Bitmap) msg.obj;
                        head.setImageDrawable(new RoundDrawable(img));
                        ((MyApplication) getContext().getApplicationContext()).getInfo().setHeadimg(img);
                    } else
                        Toast.makeText(getContext(), "加载头像失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
