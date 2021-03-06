package com.zzti.lsy.ninetingapp.home.production;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseFragment;
import com.zzti.lsy.ninetingapp.entity.MsgInfo;
import com.zzti.lsy.ninetingapp.entity.NsBxEntity;
import com.zzti.lsy.ninetingapp.event.C;
import com.zzti.lsy.ninetingapp.home.adapter.HomeBxAdapter;
import com.zzti.lsy.ninetingapp.home.adapter.HomeNsAdapter;
import com.zzti.lsy.ninetingapp.home.device.DeviceDetailActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceInputActivity;
import com.zzti.lsy.ninetingapp.home.device.DeviceListActivity;
import com.zzti.lsy.ninetingapp.home.pm.MaintenanceStatisticActivity;
import com.zzti.lsy.ninetingapp.home.repair.RepairRecordActivity;
import com.zzti.lsy.ninetingapp.home.repair.RepairRequestActivity;
import com.zzti.lsy.ninetingapp.network.OkHttpManager;
import com.zzti.lsy.ninetingapp.network.Urls;
import com.zzti.lsy.ninetingapp.utils.ParseUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：anxin on 2018/8/3 16:39
 * 统计员 operator 1
 */
public class ProductManageFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recycleView_ns)
    RecyclerView mRecycleViewNs;
    @BindView(R.id.tv_lookMore_ns)
    TextView tvLookMoreNs;
    @BindView(R.id.tv_lookMore_qbx)
    TextView tvLookMoreQBx;
    @BindView(R.id.recycleView_qbx)
    RecyclerView mRecycleViewQBx;
    @BindView(R.id.tv_lookMore_sbx)
    TextView tvLookMoreSBx;
    @BindView(R.id.recycleView_sbx)
    RecyclerView mRecycleViewSBx;
    private List<NsBxEntity> homeHintEntitiesNs;
    private List<NsBxEntity> homeHintEntitiesQBx;
    private List<NsBxEntity> homeHintEntitiesSBx;
    private HomeBxAdapter homeQBxAdapter;
    private HomeBxAdapter homeSBxAdapter;
    private HomeNsAdapter homeNsAdapter;

    public static ProductManageFragment newInstance() {
        ProductManageFragment fragment = new ProductManageFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("首页");
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setEnableRefresh(true);
    }

    @Override
    protected void initData() {
        homeHintEntitiesQBx = new ArrayList<>();
        homeHintEntitiesSBx = new ArrayList<>();
        homeHintEntitiesNs = new ArrayList<>();

        LinearLayoutManager linearLayoutManagerNs = new LinearLayoutManager(getContext());
        linearLayoutManagerNs.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewNs.setLayoutManager(linearLayoutManagerNs);
        homeNsAdapter = new HomeNsAdapter(homeHintEntitiesNs);
        mRecycleViewNs.setAdapter(homeNsAdapter);
        homeNsAdapter.setOnItemClickListener(this);


        LinearLayoutManager linearLayoutManagerQBx = new LinearLayoutManager(getContext());
        linearLayoutManagerQBx.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewQBx.setLayoutManager(linearLayoutManagerQBx);
        homeQBxAdapter = new HomeBxAdapter(homeHintEntitiesQBx);
        mRecycleViewSBx.setAdapter(homeQBxAdapter);
        homeQBxAdapter.setOnItemClickListener(this);

        LinearLayoutManager linearLayoutManagerSBx = new LinearLayoutManager(getContext());
        linearLayoutManagerSBx.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleViewSBx.setLayoutManager(linearLayoutManagerSBx);
        homeSBxAdapter = new HomeBxAdapter(homeHintEntitiesSBx);
        mRecycleViewSBx.setAdapter(homeSBxAdapter);
        homeSBxAdapter.setOnItemClickListener(this);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCarExpire();
            }
        });
        if (UIUtils.isNetworkConnected()) {
            showDia();
            getCarExpire();
        }
    }

    /**
     * 获取提醒实体类
     */
    private void getCarExpire() {
        homeHintEntitiesQBx.clear();
        homeHintEntitiesSBx.clear();
        homeHintEntitiesNs.clear();
        OkHttpManager.postFormBody(Urls.POST_GETCAREXPIRE, null, mRecycleViewQBx, new OkHttpManager.OnResponse<String>() {
            @Override
            public String analyseResult(String result) {
                return result;
            }

            @Override
            public void onSuccess(String s) {
                cancelDia();
                endRefresh(smartRefreshLayout);
                MsgInfo msgInfo = ParseUtils.parseJson(s, MsgInfo.class);
                if (msgInfo.getCode() == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgInfo.getData());
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                NsBxEntity nsBxEntity = ParseUtils.parseJson(jsonArray.getString(i), NsBxEntity.class);
                                if (nsBxEntity.getTypeName().equals("年审")) {
                                    homeHintEntitiesNs.add(nsBxEntity);
                                } else if (nsBxEntity.getTypeName().equals("强险")) {
                                    homeHintEntitiesQBx.add(nsBxEntity);
                                } else if (nsBxEntity.getTypeName().equals("商业险")) {
                                    homeHintEntitiesSBx.add(nsBxEntity);
                                }
                            }
                            if (homeHintEntitiesNs.size() == 0) {
                                tvLookMoreNs.setText("暂无数据");
                                tvLookMoreNs.setEnabled(false);
                            } else {
                                tvLookMoreNs.setText("查看更多");
                                tvLookMoreNs.setEnabled(true);
                                if (homeHintEntitiesNs.size() > 5) {
                                    homeHintEntitiesNs.subList(0, 4);
                                }
                            }
                            if (homeHintEntitiesQBx.size() == 0) {
                                tvLookMoreQBx.setText("暂无数据");
                                tvLookMoreQBx.setEnabled(false);
                            } else {
                                tvLookMoreQBx.setText("查看更多");
                                tvLookMoreQBx.setEnabled(true);
                                if (homeHintEntitiesQBx.size() > 5) {
                                    homeHintEntitiesQBx.subList(0, 4);
                                }
                            }
                            if (homeHintEntitiesSBx.size() == 0) {
                                tvLookMoreSBx.setText("暂无数据");
                                tvLookMoreSBx.setEnabled(false);
                            } else {
                                tvLookMoreSBx.setText("查看更多");
                                tvLookMoreSBx.setEnabled(true);
                                if (homeHintEntitiesSBx.size() > 5) {
                                    homeHintEntitiesSBx.subList(0, 4);
                                }
                            }
                        } else {
                            tvLookMoreNs.setVisibility(View.GONE);
                            tvLookMoreQBx.setVisibility(View.GONE);
                            tvLookMoreSBx.setVisibility(View.GONE);
                            UIUtils.showT("暂无保险年审数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (msgInfo.getCode() == C.Constant.HTTP_UNAUTHORIZED) {
                    loginOut();
                } else {
                    UIUtils.showT(msgInfo.getMsg());
                }
                homeQBxAdapter.notifyDataSetChanged();
                homeSBxAdapter.notifyDataSetChanged();
                homeNsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String msg, String url) {
                super.onFailed(code, msg, url);
                cancelDia();
                endRefresh(smartRefreshLayout);
            }
        });
    }

    @OnClick({R.id.rl_menu1, R.id.rl_menu2, R.id.rl_menu3, R.id.rl_menu4, R.id.rl_menu5,
            R.id.rl_menu6, R.id.rl_menu7, R.id.rl_menu8})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_menu1://生产录入
                Intent intent1 = new Intent(mActivity, ProductInputActivity.class);
                intent1.putExtra("TAG", 0);
                startActivity(intent1);
                break;
            case R.id.rl_menu2://生产统计
                startActivity(new Intent(mActivity, ProductStatisticsActivity.class));
                break;
            case R.id.rl_menu3://生产记录
                startActivity(new Intent(mActivity, ProductRecordActivity.class));
                break;
            case R.id.rl_menu4://维修申请
                startActivity(new Intent(mActivity, RepairRequestActivity.class));
                break;
            case R.id.rl_menu5://维修统计
                startActivity(new Intent(mActivity, MaintenanceStatisticActivity.class));
                break;
            case R.id.rl_menu6://维修记录
                startActivity(new Intent(mActivity, RepairRecordActivity.class));
                break;
            case R.id.rl_menu7: //设备列表
                startActivity(new Intent(mActivity, DeviceListActivity.class));
                break;
            case R.id.rl_menu8://设备录入
                startActivity(new Intent(mActivity, DeviceInputActivity.class));
                break;


        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(mActivity, DeviceDetailActivity.class);
        if (adapter == homeQBxAdapter) {
            intent.putExtra("carNumber", homeHintEntitiesQBx.get(position).getPlateNumber());
        }
        if (adapter == homeSBxAdapter) {
            intent.putExtra("carNumber", homeHintEntitiesSBx.get(position).getPlateNumber());
        } else if (adapter == homeNsAdapter) {
            intent.putExtra("carNumber", homeHintEntitiesNs.get(position).getPlateNumber());
        }
        intent.putExtra("TAG", 1);
        startActivity(intent);
    }
}
