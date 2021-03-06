package com.zcl.hxqh.liangqingmanagement.view.activity;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.zcl.hxqh.liangqingmanagement.R;
import com.zcl.hxqh.liangqingmanagement.api.HttpManager;
import com.zcl.hxqh.liangqingmanagement.api.HttpRequestHandler;
import com.zcl.hxqh.liangqingmanagement.api.JsonUtils;
import com.zcl.hxqh.liangqingmanagement.bean.Results;
import com.zcl.hxqh.liangqingmanagement.constants.Constants;
import com.zcl.hxqh.liangqingmanagement.dialog.FlippingLoadingDialog;
import com.zcl.hxqh.liangqingmanagement.model.ALNDOMAIN;
import com.zcl.hxqh.liangqingmanagement.model.N_CAR;
import com.zcl.hxqh.liangqingmanagement.model.N_TASKPLAN;
import com.zcl.hxqh.liangqingmanagement.until.AccountUtils;
import com.zcl.hxqh.liangqingmanagement.until.MessageUtils;
import com.zcl.hxqh.liangqingmanagement.until.T;
import com.zcl.hxqh.liangqingmanagement.view.widght.ShareBottomDialog;
import com.zcl.hxqh.liangqingmanagement.webserviceclient.AndroidClientService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * 货运预报单
 */
public class N_carAddActivity extends BaseActivity {
    private static String TAG = "N_carAddActivity";

    /**
     * 返回按钮
     */
    private ImageView backImageView;
    /**
     * 标题
     */
    private TextView titleTextView;

    /**
     * 提交按钮
     **/
    private Button submitBtn;


    /**界面信息**/
    /**
     * 编号
     */
    private TextView carnumText;
    private LinearLayout carLinearLayout;
    private View carView;
    /**
     * 仓储作业计划编号
     */
    private TextView plannumText;
    /**
     * 描述
     */
    private EditText descriptionText;

    /**
     * 收货/提货单位
     */
    private TextView fromstationText;
    /**
     * 省份
     */
    private EditText provinceText;
    /**
     * 品种
     */
    private TextView foodtypesText;
    /**
     * 量衡
     */
    private TextView theamountofmoneyText;
    /**
     * 车辆类型
     */
    private TextView cartypeText;

    /**
     * 计划总量
     **/
    private EditText plantotalText;

    /**
     * 作业类型
     */
    private EditText planstatusText;
    /**
     * 创建日期
     */
    private TextView enterdateText;
    /**
     * 创建人
     */
    private TextView enterbyText;
    /**
     * 状态
     */
    private TextView statusText;

    private Button carlineButton;//查看货运预报明细按钮

    private N_CAR n_car = new N_CAR();


    //扦样类型
    private String[] types = null;

    /**
     * 时间选择器
     **/
    private DatePickerDialog datePickerDialog;
    StringBuffer sb;
    private int layoutnum;


    //弹出框
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();


    private BaseAnimatorSet mBasIn;
    private BaseAnimatorSet mBasOut;


    protected FlippingLoadingDialog mLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hyyb_details);
        findViewById();
        initView();

        mBasIn = new BounceTopEnter();
        mBasOut = new SlideBottomExit();
    }

    @Override
    protected void findViewById() {
        backImageView = (ImageView) findViewById(R.id.title_back_id);
        titleTextView = (TextView) findViewById(R.id.title_name);
        submitBtn = (Button) findViewById(R.id.sbmit_id);

        carnumText = (TextView) findViewById(R.id.hyyb_carnum);
        carLinearLayout = (LinearLayout) findViewById(R.id.carnum_linearlayout_id);
        carView = (View) findViewById(R.id.carnum_view_id);
        plannumText = (TextView) findViewById(R.id.hyyb_plannum);
        descriptionText = (EditText) findViewById(R.id.hyyb_description);
        fromstationText = (TextView) findViewById(R.id.hyyb_fromstation);
        provinceText = (EditText) findViewById(R.id.hyyb_province);
        foodtypesText = (TextView) findViewById(R.id.hyyb_foodtypes);
        theamountofmoneyText = (TextView) findViewById(R.id.hyyb_theamountofmoney);
        cartypeText = (TextView) findViewById(R.id.hyyb_cartype);
        plantotalText = (EditText) findViewById(R.id.hyyb_plantotal);
        planstatusText = (EditText) findViewById(R.id.hyyb_planstatus);
        enterdateText = (TextView) findViewById(R.id.hyyb_enterdate);
        enterbyText = (TextView) findViewById(R.id.hyyb_enterby);
        statusText = (TextView) findViewById(R.id.hyyb_status);

        carlineButton = (Button) findViewById(R.id.carline_button);

        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        LayoutTransition transition = new LayoutTransition();
        container.setLayoutTransition(transition);




        setDataListener();
    }


    @Override
    protected void initView() {
        backImageView.setOnClickListener(backImageViewOnClickListener);
        titleTextView.setText(R.string.add_text);
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setOnClickListener(submitBtnBtnOnClickListener);
        carLinearLayout.setVisibility(View.GONE);
        carView.setVisibility(View.GONE);
        enterdateText.setText(getNowTime());
        enterbyText.setText(AccountUtils.getloginUserName(N_carAddActivity.this));
        plannumText.setOnClickListener(plannumTextOnClickListener);
        foodtypesText.setOnClickListener(foodtypesTextOnClickListner);
        carlineButton.setOnClickListener(carlineOnClickListener);
    }

    private View.OnClickListener backImageViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    private View.OnClickListener submitBtnBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submitNormalDialog();
        }
    };


    //仓储作业计划编号
    private View.OnClickListener plannumTextOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            //跳转至选项值界面
            Intent intent = new Intent(N_carAddActivity.this, CarPlanChooseActivity.class);
            startActivityForResult(intent, 0);
        }
    };

    //品种
    private View.OnClickListener foodtypesTextOnClickListner = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            getOptionsValue("品种", HttpManager.getALNDOMAIN(Constants.CROPSTYPE), foodtypesText);
        }
    };

    private View.OnClickListener carlineOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isOK()) {
                Intent intent = new Intent(N_carAddActivity.this, N_carlineListActivity.class);
                intent.putExtra("n_car", getN_CAR());
                startActivity(intent);
            }
        }
    };


    //获取选项值
    private void getOptionsValue(final String title, String url, final TextView textview) {
        {
            HttpManager.getData(this, url, new HttpRequestHandler<Results>() {


                @Override
                public void onSuccess(Results data) {

                }

                @Override
                public void onSuccess(Results data, int totalPages, int currentPage) {
                    ArrayList<ALNDOMAIN> item = JsonUtils.parsingALNDOMAIN(N_carAddActivity.this, data.getResultlist());
                    if (item == null || item.isEmpty()) {
                        MessageUtils.showMiddleToast(N_carAddActivity.this, getString(R.string.qiangyang_type_text));
                    } else {
                        types = new String[item.size()];
                        for (int i = 0; i < item.size(); i++) {
                            types[i] = item.get(i).getDESCRIPTION();
                        }
                        if (types != null && types.length != 0) {
                            showShareBottomDialog(title, types, textview);
                        } else {
                            MessageUtils.showMiddleToast(N_carAddActivity.this, getString(R.string.qiangyang_type_text));
                        }

                    }

                }

                @Override
                public void onFailure(String error) {

                }
            });

        }
    }
    /**
     * 显示选项框
     **/
    private void showShareBottomDialog(String title, final String[] typesitem, final TextView textview) {

        final ShareBottomDialog dialog = new ShareBottomDialog(N_carAddActivity.this, types, null);


        dialog.title(title)//
                .titleTextSize_SP(14.5f)//
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShort(N_carAddActivity.this, typesitem[position]);
                textview.setText(typesitem[position]);
                dialog.dismiss();
            }
        });
    }


    /**
     * 设置时间选择器*
     */
    private void setDataListener() {

        final Calendar objTime = Calendar.getInstance();
        int iYear = objTime.get(Calendar.YEAR);
        int iMonth = objTime.get(Calendar.MONTH);
        int iDay = objTime.get(Calendar.DAY_OF_MONTH);
        int hour = objTime.get(Calendar.HOUR_OF_DAY);

        int minute = objTime.get(Calendar.MINUTE);


        datePickerDialog = new DatePickerDialog(this, new datelistener(), iYear, iMonth, iDay);
    }


    /**
     * 日期选择器
     **/
    private class MydateListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            layoutnum = 0;
            sb = new StringBuffer();
            layoutnum = view.getId();
            datePickerDialog.show();
        }
    }


    private class datelistener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            sb = new StringBuffer();
            monthOfYear = monthOfYear + 1;
            if (dayOfMonth < 10) {
                sb.append(year % 100 + "-" + monthOfYear + "-" + "0" + dayOfMonth);
            } else {
                sb.append(year % 100 + "-" + monthOfYear + "-" + dayOfMonth);
            }

            enterdateText.setText(sb.toString());
        }
    }

    private String getNowTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    /**
     * 提交数据*
     */
    private void submitNormalDialog() {

        final NormalDialog dialog = new NormalDialog(N_carAddActivity.this);
        dialog.content("确定提交数据吗？")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {


                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        if (isOK()) {
                            getLoadingDialog("正在提交");
                            startAsyncTask();
                        }
                        dialog.dismiss();
                    }
                });
    }


    private FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(this, msg);
        return mLoadingDialog;
    }


    /**
     * 提交数据*
     */
    private void startAsyncTask() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return AndroidClientService.addAndUpdateN_CAR(N_carAddActivity.this, JsonUtils.encapsulationN_CAR(getN_CAR(),null));
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mLoadingDialog.dismiss();
                MessageUtils.showMiddleToast(N_carAddActivity.this, s);
                carnumText.setText(s);
//                if (s.equals("修改成功")) {
//                    finish();
//                }


            }
        }.execute();


    }


    /**
     * 封装数据
     **/
    private N_CAR getN_CAR() {
        n_car.setCARNUM(carnumText.getText().toString());
        n_car.setDESCRIPTION(descriptionText.getText().toString());
        n_car.setPLANNUM(plannumText.getText().toString());
        n_car.setFROMSTATION(fromstationText.getText().toString());
        n_car.setPROVINCE(provinceText.getText().toString());
        n_car.setFOODTYPES(foodtypesText.getText().toString());
        n_car.setTHEAMOUNTOFMONEY(theamountofmoneyText.getText().toString());
        n_car.setCARTYPE(cartypeText.getText().toString());
        n_car.setPLANTOTAL(plantotalText.getText().toString());
        n_car.setPLANSTATUS(planstatusText.getText().toString());
        n_car.setENTERDATE(enterdateText.getText().toString());
        n_car.setENTERBY(enterbyText.getText().toString());
        n_car.setSTATUS(statusText.getText().toString());
        return n_car;
    }

    private boolean isOK(){
        if (descriptionText.getText().toString().equals("")){
            MessageUtils.showMiddleToast(N_carAddActivity.this, "请输入描述");
            return false;
        }
        if (foodtypesText.getText().toString().equals("")){
            MessageUtils.showMiddleToast(N_carAddActivity.this, "请输入品种");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1001:
                N_TASKPLAN n_taskplan = (N_TASKPLAN) data.getSerializableExtra("n_taskplan");
                plannumText.setText(n_taskplan.getPLANNUM());
                descriptionText.setText(n_taskplan.getDESCRIPTION());
                fromstationText.setText(n_taskplan.getSTATION());
                foodtypesText.setText(n_taskplan.getTYPE());
                theamountofmoneyText.setText(n_taskplan.getTHEAMOUNTOFMONEY());
                cartypeText.setText(n_taskplan.getCARTYPE());
                break;
        }
    }
}
