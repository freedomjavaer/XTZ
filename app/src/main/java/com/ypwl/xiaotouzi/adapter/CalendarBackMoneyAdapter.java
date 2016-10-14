package com.ypwl.xiaotouzi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.bean.CalendarBackMoneyBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.CalendarBackMoneyBidStatusChangeEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.LlcBidDetailActivity;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.FileUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * @author tengtao
 *         回款适配器
 */
public class CalendarBackMoneyAdapter extends BaseAdapter {

    private Activity mContext;
    private List<CalendarBackMoneyBean.ListEntity> mDataList;
    private String p_name, pid;
    private KProgressHUD mLoading;

    public CalendarBackMoneyAdapter(Activity context) {
        this.mContext = context;
        mLoading = KProgressHUD.create(context);
    }

    public void loadData(List<CalendarBackMoneyBean.ListEntity> dataList) {
        if (dataList != null) {
            mDataList = dataList;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public CalendarBackMoneyBean.ListEntity getItem(int position) {
        return mDataList != null ? mDataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.activity_recent_return_monry_item, null);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CalendarBackMoneyBean.ListEntity item = getItem(position);
        long return_time = 0;
        try {
            return_time = Long.parseLong(item.getReturn_time());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String timeFromInt = DateTimeUtil.formatDateTime(return_time * 1000, "yyyy-MM-dd");
        if (position == 0) {
            holder.arrmitemreturntime.setVisibility(View.VISIBLE);
        }
        if (position > 0) {
            long time = 0;
            try {
                time = Long.parseLong(getItem(position - 1).getReturn_time());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String lastTime = GlobalUtils.getDataByLong(time);
            holder.arrmitemreturntime.setVisibility(timeFromInt.equals(lastTime) ? View.GONE : View.VISIBLE);
        }
        //时间
        holder.arrmitemreturntime.setText(timeFromInt);
        holder.arrmAutoTallyTag.setVisibility(item.getIs_auto() == 1 ? View.VISIBLE : View.GONE);// 1：自动
        holder.arrmitempname.setText(item.getP_name());
        holder.arrmProjectName.setText(item.getProject_name());
        holder.arrmitemtotal.setText("￥" + item.getTotal()); //￥2402.72
        holder.arrmitemperiod.setText(item.getPeriod() + "/" + item.getPeriod_total()); // 第8期还款

        //设置logo
        boolean b = item.getP_logo() != null && item.getP_logo().length() > 0;
        holder.arrmItemCustomLogo.setVisibility(b ? View.GONE : View.VISIBLE);
        String p_name = item.getP_name();
        holder.arrmItemCustomLogo.setText(p_name.length() > 4 ? p_name.substring(0, 4) : p_name);
        if (b) {
            ImgLoadUtil.loadLogo(item.getPid(), item.getP_logo(), holder.arrmitemplogo, R.mipmap.pic_021);
        } else {
            holder.arrmitemplogo.setImageResource(R.mipmap.custom_platform_logo);
        }

        holder.arrmItemContainer.setTag(R.id.arrm_item_ll,item.getAid());
        holder.arrmItemContainer.setOnClickListener(mOnClickListener);

        holder.arrmItemContainer.setTag(R.id.arrm_item_period,position);
        holder.arrmItemContainer.setOnLongClickListener(mOnLongClickListener);

        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String aid = (String) v.getTag(R.id.arrm_item_ll);
            Intent intent = new Intent(mContext, LlcBidDetailActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,aid);
            intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"还款日历");
            mContext.startActivity(intent);
        }
    };

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int position = (int) v.getTag(R.id.arrm_item_period);
            showDialog(mContext, position);
            return false;
        }
    };

    public class ViewHolder {
        public final TextView arrmitemreturntime;//回款时间
        public final ImageView arrmitemplogo;//平台logo
        public final TextView arrmitempname;//平台名称
        public final ImageView arrmAutoTallyTag;//自动记账标记
        public final TextView arrmProjectName;//项目名称
        public final TextView arrmitemtotal;//当前总额
        public final TextView arrmitemperiod;//回款期
        public final LinearLayout arrmItemContainer;
        public final TextView arrmItemCustomLogo;//自定义平台logo

        public ViewHolder(View root) {
            arrmitemreturntime = (TextView) root.findViewById(R.id.arrm_item_return_time);
            arrmitemplogo = (ImageView) root.findViewById(R.id.arrm_item_p_logo);
            arrmitempname = (TextView) root.findViewById(R.id.arrm_item_p_name);
            arrmAutoTallyTag = (ImageView) root.findViewById(R.id.arrm_item_auto_tally_tag);
            arrmProjectName = (TextView) root.findViewById(R.id.arrm_item_project_name);
            arrmitemtotal = (TextView) root.findViewById(R.id.arrm_item_total);
            arrmitemperiod = (TextView) root.findViewById(R.id.arrm_item_period);
            arrmItemContainer = (LinearLayout) root.findViewById(R.id.arrm_item_ll);
            arrmItemCustomLogo = (TextView) root.findViewById(R.id.tv_return_money_custom_logo);
            root.setTag(this);
        }
    }

    public void showDialog(Activity activity, int position) {
        final CalendarBackMoneyBean.ListEntity entity = mDataList.get(position);
        int is_auto = mDataList.get(position).getIs_auto();
        if (is_auto == 1) {
            final String[] list = new String[]{"同步数据"};
            ListView lv = new ListView(activity);
            lv.setAdapter(new ArrayAdapter<>(activity, R.layout.simple_list_item_2, Arrays.asList(list)));
            final CustomDialog dialog = new CustomDialog.AlertBuilder(activity).setTitleText("选择操作项").setCustomContentView(lv).create();
            dialog.show();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.dismiss();
                    p_name = entity.getP_name();
                    pid = entity.getPid();
                    String url = String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, entity.getPid(), "");
                    NetHelper.get(url, new AutoTallySyncCallBack());
                }
            });
        } else {
            final String[] list = new String[]{"已回", "逾期"};
            ListView lv = new ListView(activity);
            lv.setAdapter(new ArrayAdapter<>(activity, R.layout.simple_list_item_2, Arrays.asList(list)));
            final CustomDialog dialog = new CustomDialog.AlertBuilder(activity).setTitleText("选择操作项").setCustomContentView(lv).create();
            dialog.show();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.dismiss();
                    String url = String.format(URLConstant.CHANGING_THE_STATUS_OF_PAYMENT, GlobalUtils.token, entity.getRid(), (position + 1));
                    NetHelper.get(url, new ReturnMoneyStateCallBack());
                }
            });
        }
    }

    private class AutoTallySyncCallBack extends IRequestCallback<String> {
        @Override
        public void onStart() {
            mLoading.show();
        }

        @Override
        public void onFailure(Exception e) {
            UIUtil.showToastShort("网络异常请重试");
            mLoading.dismiss();
        }

        @Override
        public void onSuccess(String jsonStr) {
            mLoading.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.getInt(Const.JSON_KEY_status);
                if (status == ServerStatus.SERVER_STATUS_OK) {
                    syncAutoTallyData(mContext.getString(R.string.auto_tally_sync_data));
                } else if (status == ServerStatus.SERVER_STATUS_AUTO_TALLY_AUTH_CODE) {//输入验证码
                    String imgdata = jsonObject.getString(Const.JSON_KEY_AUTH_CODE_imgdata);
                    if (!StringUtil.isEmptyOrNull(imgdata) && imgdata.contains("base64")) {
                        int index = imgdata.indexOf("base64");
                        showAuthCode(imgdata.substring(index + 6));
                    }
                } else {
                    String errorMsg = "";
                    try {
                        errorMsg = jsonObject.getString(Const.JSON_KEY_ret_msg);
                    } catch (Exception e) {
                    }
                    syncAutoTallyData(errorMsg.length() > 0 ? errorMsg : mContext.getString(R.string.auto_tally_sync_service_busy));//服务器繁忙
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReturnMoneyStateCallBack extends IRequestCallback<String> {
        @Override
        public void onStart() {
            mLoading.show();
        }

        @Override
        public void onFailure(Exception e) {
            mLoading.dismiss();
            UIUtil.showToastShort("网络异常请重试");
        }

        @Override
        public void onSuccess(String jsonStr) {
            mLoading.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.getInt("status");
                if (status == ServerStatus.SERVER_STATUS_OK) {
                    UIUtil.showToastShort("操作成功");
                    EventHelper.post(new CalendarBackMoneyBidStatusChangeEvent());//发送标的状态更改事件
                } else if (status == ServerStatus.SERVER_STATUS_INVALID_TOKEN) {
                    UIUtil.showToastShort("信息错误");
                } else {
                    UIUtil.showToastShort("更新失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void syncAutoTallyData(String s) {
        if (mContext.isFinishing()) {
            return;
        }
        final CustomDialog dialog = new CustomDialog.AlertBuilder(mContext)
                .setTitleText("提示")
                .setContentText(s).setContentTextGravity(Gravity.CENTER)
                .setCanceledOnTouchOutside(false)
                .create();
        dialog.show();
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
    }

    /** 显示输入验证码 */
    private void showAuthCode(String imgdata) {
        if (mContext.isFinishing()) {
            return;
        }
        View contentView = View.inflate(XtzApp.getApplication().getTopActivity(), R.layout.layout_dialog_auto_tally_auth_ocde, null);
        final EditText inputNumber = (EditText) contentView.findViewById(R.id.auth_code);
        final ImageView authPic = (ImageView) contentView.findViewById(R.id.pic_code);
        authPic.setImageBitmap(FileUtil.getImgFromBase64Stream(imgdata));
        Button submit = com.ypwl.xiaotouzi.utils.ViewHolder.findViewById(contentView, R.id.btn_submit);

        final CustomDialog dialog = new CustomDialog.AlertBuilder(mContext)
                .setTitleText("请输入验证码")
                .setCustomContentView(contentView)
                .create();
        dialog.show();

        /** 点击刷新验证码图片 */
        authPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, pid, -1);
                NetHelper.get(url, new IRequestCallback<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }

                    @Override
                    public void onSuccess(String jsonStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            int status = jsonObject.getInt(Const.JSON_KEY_status);
                            if (status == ServerStatus.SERVER_STATUS_AUTO_TALLY_AUTH_CODE) {
                                String imgdata = jsonObject.getString(Const.JSON_KEY_AUTH_CODE_imgdata);
                                if (!StringUtil.isEmptyOrNull(imgdata) && imgdata.contains("base64")) {
                                    int index = imgdata.indexOf("base64");
                                    authPic.setImageBitmap(FileUtil.getImgFromBase64Stream(imgdata.substring(index + 6)));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        /** 提交验证码 */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inputNumber.getText().toString().trim();
                if (StringUtil.isEmptyOrNull(code)) {
                    UIUtil.showToastShort("验证码不能为空");
                    return;
                }
                String url = String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, pid, code);
                NetHelper.get(url, new AutoTallySyncCallBack());
                dialog.dismiss();
            }
        });
        /**调用系统输入法*/
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputNumber.setFocusable(true);
                inputNumber.setFocusableInTouchMode(true);
                inputNumber.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) inputNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(inputNumber, 0);
            }
        }, 200);
    }
}
