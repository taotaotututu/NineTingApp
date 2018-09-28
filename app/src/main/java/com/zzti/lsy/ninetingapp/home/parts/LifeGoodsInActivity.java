package com.zzti.lsy.ninetingapp.home.parts;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.entity.LaoBao;
import com.zzti.lsy.ninetingapp.entity.LaobaoPurchased;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.PartsInfoEntity;
import com.zzti.lsy.ninetingapp.entity.PartsPurchased;
import com.zzti.lsy.ninetingapp.home.SuccessActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.StringUtil;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 日用品入库界面
 */
public class LifeGoodsInActivity extends BaseActivity {
    @BindView(R.id.et_goodsName)
    EditText etGoodsName;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_priceTitle)
    TextView tvPriceTitle;
    @BindView(R.id.rl_price)
    RelativeLayout rlPrice;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_moneyTitle)
    TextView tvMoneyTitle;
    @BindView(R.id.rl_money)
    RelativeLayout rlMoney;
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tv_operator)
    TextView tvOperator;
    private int tag;//1代表录入  2代表入库
    private LaoBao laoBao;
    private LaobaoPurchased laobaoPurchased;

    @Override
    public int getContentViewId() {
        return R.layout.activity_lifegoods_in;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }


    private void initView() {
        setTitle("日用品录入");
        etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tag = UIUtils.getInt4Intent(this, "TAG");
        laobaoPurchased = new LaobaoPurchased();
        if (tag == 2) {//入库
            tvMoneyTitle.setVisibility(View.GONE);
            rlMoney.setVisibility(View.GONE);
            tvPriceTitle.setVisibility(View.GONE);
            rlPrice.setVisibility(View.GONE);
            laoBao = (LaoBao) getIntent().getSerializableExtra("LaoBao");
            laobaoPurchased.setLbID(laoBao.getLbID());
            laobaoPurchased.setPurchasedMoney(laoBao.getPrice());
            etGoodsName.setEnabled(false);
            etGoodsName.setText(laoBao.getLbName());
        } else {//录入
            etPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!StringUtil.isNullOrEmpty(etAmount.getText().toString()) && !StringUtil.isNullOrEmpty(editable.toString())) {
                        int amount = Integer.parseInt(etAmount.getText().toString());
                        double price = Double.parseDouble(editable.toString());
                        tvTotalMoney.setText(String.valueOf(amount * price));
                    }
                }
            });
            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!StringUtil.isNullOrEmpty(etPrice.getText().toString()) && !StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
                        double price = Double.parseDouble(etPrice.getText().toString());
                        int amount = Integer.parseInt(editable.toString());
                        tvTotalMoney.setText(String.valueOf(amount * price));
                    }
                }
            });
            laoBao = new LaoBao();
        }
    }

    private void initData() {
        tvOperator.setText(SpUtils.getInstance().getString(SpUtils.USERNAME, ""));
    }

    @OnClick(R.id.btn_submit)
    public void viewClick() {
        if (StringUtil.isNullOrEmpty(etGoodsName.getText().toString())) {
            UIUtils.showT("名称不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etAmount.getText().toString())) {
            UIUtils.showT("数量不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(tvOperator.getText().toString())) {
            UIUtils.showT("经手人不能为空");
            return;
        }
        if (tag == 1) {//录入
            if (StringUtil.isNullOrEmpty(etPrice.getText().toString())) {
                UIUtils.showT("单价不能为空");
                return;
            }
            if (StringUtil.isNullOrEmpty(tvTotalMoney.getText().toString())) {
                UIUtils.showT("总金额不能为空");
                return;
            }
            laoBao.setLbName(etGoodsName.getText().toString());
            laoBao.setPrice(etPrice.getText().toString());
        }
        laobaoPurchased.setNumber(etAmount.getText().toString());
        laobaoPurchased.setUserID(spUtils.getString(SpUtils.USERID, ""));
        hideSoftInput(etAmount);
        if (UIUtils.isNetworkConnected()) {
            showDia();
            submitInputData();
        }
    }


    private void submitInputData() {
        HashMap<String, String> params = new HashMap<>();
        if (tag == 2) {//入库
            params.put("LaoBaoJosn", "");
        } else {
            params.put("LaoBaoJosn", new Gson().toJson(laoBao));
        }
        params.put("StorageJson", new Gson().toJson(laobaoPurchased));
        OkHttpManager.postFormBody(Urls.POST_LAOSTORAGE, params, tvMoneyTitle, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    if (tag == 1) {//录入
                        etGoodsName.getText().clear();
                        etPrice.getText().clear();
                        etAmount.getText().clear();
                        tvTotalMoney.setText("");
                    } else {//入库
                        finish();
                    }
                    UIUtils.showT("提交成功");
                    Intent intent = new Intent(LifeGoodsInActivity.this, SuccessActivity.class);
                    intent.putExtra("TAG", 3);
                    startActivity(intent);
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }

            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
            }
        });

    }


}