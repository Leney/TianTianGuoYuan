package com.ttxg.fruitday.gui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.model.OrderComment;
import com.ttxg.fruitday.model.OrderDetailInfo;
import com.ttxg.fruitday.model.OrderGoodsInfo;
import com.ttxg.fruitday.model.OrderListInfo;
import com.ttxg.fruitday.model.UserInfo;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 交易成功之后 评价订单商品的界面
 * Created by lilijun on 2016/10/10.
 */
public class CommentOrderGoodsActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 提交评论接口
     */
    private final String SUBMIT_COMMENT_LIST = "order/saveRate";
    private LinearLayout itemLay;
    private ArrayList<OrderListInfo> intentOrderListInfos;
    private OrderDetailInfo intentOrderDetailInfo;

    private List<OrderComment> orderCommentList = new ArrayList<>();

    private ProgressDialog progressDialog;

    /**
     * 评论成功的resultCode
     */
    public static final int COMMENT_SUCCESS_RESULT_CODE = 100015;

    @Override
    protected void initView() {
        intentOrderListInfos = (ArrayList<OrderListInfo>) getIntent().getSerializableExtra
                ("order_list_info_array_list");
        intentOrderDetailInfo = (OrderDetailInfo) getIntent().getSerializableExtra
                ("orderDetailInfo");
        setTitleName(getResources().getString(R.string.comment));
        setCenterView(R.layout.activity_comment_order_goods);
        itemLay = (LinearLayout) findViewById(R.id.comment_order_item_lay);
        TextView submitBtn = (TextView) findViewById(R.id.comment_order_submit_btn);
        submitBtn.setOnClickListener(this);

        setItemView();
        showCenterView();
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, SUBMIT_COMMENT_LIST)) {
            // 提交评论成功
//            EventBus.getDefault().post(Constants.NEED_REFRESH_ANY_NUM);
            // 将待评论的订单数量减去1
            UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
            if (userInfo != null) {
                userInfo.setUnCommentNum(userInfo.getUnCommentNum() - 1);
            }
            EventBus.getDefault().post(Constants.REFRESH_USER_INFO_VIEW);

            progressDialog.dismiss();
            Util.showToast(CommentOrderGoodsActivity.this, getResources().getString(R.string
                    .comment_success));
            Intent intent = new Intent();
            if(intentOrderListInfos != null){
                DLog.i("resultData");
                intent.putExtra("orderId",intentOrderListInfos.get(0).getId());
            }else {
                if(intentOrderDetailInfo != null){
                    intent.putExtra("orderId",intentOrderDetailInfo.getId());
                }
            }
            setResult(COMMENT_SUCCESS_RESULT_CODE,intent);
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, SUBMIT_COMMENT_LIST)) {
            // 提交评论失败
            progressDialog.dismiss();
            Util.showErrorMessage(CommentOrderGoodsActivity.this, msg, getResources().getString(R
                    .string.comment_failed));
        }
    }

    private void setItemView() {
        if (intentOrderListInfos != null) {
            // 是从列表跳转过来的
            int length = intentOrderListInfos.size();
            for (int i = 0; i < length; i++) {
                OrderListInfo orderListInfo = intentOrderListInfos.get(i);
                OrderComment orderComment = new OrderComment();
                orderComment.setGoodsId(orderListInfo.getGoodsId());
                orderComment.setCommentContent("");
                orderComment.setOrderNo(orderListInfo.getOrderNo());
                // 默认好评
                orderComment.setRateType(1);
                // 默认5星
                orderComment.setStar(5);
                orderCommentList.add(orderComment);


                View view = View.inflate(CommentOrderGoodsActivity.this, R.layout
                        .comment_order_goods_item, null);
                SimpleDraweeView icon = (SimpleDraweeView) view.findViewById(R.id
                        .comment_order_goods_adapter_goods_icon);
                TextView name = (TextView) view.findViewById(R.id
                        .comment_order_goods_adapter_goods_name);
                TextView price = (TextView) view.findViewById(R.id
                        .comment_order_goods_adapter_goods_price);
                final RadioGroup commentGroup = (RadioGroup) view.findViewById(R.id
                        .comment_order_goods_adapter_comment_group);
                commentGroup.setTag(i);
                commentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.comment_order_goods_adapter_comment_well:
                                // 好评
                                orderCommentList.get((Integer) commentGroup.getTag()).setRateType
                                        (1);
                                break;
                            case R.id.comment_order_goods_adapter_comment_normal:
                                // 中评
                                orderCommentList.get((Integer) commentGroup.getTag()).setRateType
                                        (2);
                                break;
                            case R.id.comment_order_goods_adapter_comment_low:
                                // 差评
                                orderCommentList.get((Integer) commentGroup.getTag()).setRateType
                                        (3);
                                break;
                        }
                    }
                });
                RatingBar ratingBar = (RatingBar) view.findViewById(R.id
                        .comment_order_goods_adapter_comment_star);
                final EditText commentInput = (EditText) view.findViewById(R.id
                        .comment_order_goods_adapter_comment_input);
                ratingBar.setTag(i);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        orderCommentList.get((Integer) ratingBar.getTag()).setStar((int) v);
                    }
                });

                commentInput.setTag(i);
                commentInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int
                            i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        orderCommentList.get((Integer) commentInput.getTag()).setCommentContent
                                (editable.toString());
                    }
                });

                icon.setImageURI(orderListInfo.getGoodsIcon());
                name.setText(orderListInfo.getGoodsName());
                price.setText(String.format(getResources().getString(R.string
                        .format_money), orderListInfo.getGoodsPrice()));
                itemLay.addView(view);
            }
        } else if (intentOrderDetailInfo != null) {
            // 是从订单详情跳转过来的
            int length = intentOrderDetailInfo.getGoodsList().size();
            for (int i = 0; i < length; i++) {
                OrderGoodsInfo orderGoodsInfo = intentOrderDetailInfo.getGoodsList().get(i);
                OrderComment orderComment = new OrderComment();
                orderComment.setGoodsId(orderGoodsInfo.getId());
                orderComment.setCommentContent("");
                orderComment.setOrderNo(intentOrderDetailInfo.getOrderNo());
                // 默认好评
                orderComment.setRateType(1);
                // 默认5星
                orderComment.setStar(5);
                orderCommentList.add(orderComment);

                View view = View.inflate(CommentOrderGoodsActivity.this, R.layout
                        .comment_order_goods_item, null);
                SimpleDraweeView icon = (SimpleDraweeView) view.findViewById(R.id
                        .comment_order_goods_adapter_goods_icon);
                TextView name = (TextView) view.findViewById(R.id
                        .comment_order_goods_adapter_goods_name);
                TextView price = (TextView) view.findViewById(R.id
                        .comment_order_goods_adapter_goods_price);
                final RadioGroup commentGroup = (RadioGroup) view.findViewById(R.id
                        .comment_order_goods_adapter_comment_group);
                commentGroup.setTag(i);
                commentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.comment_order_goods_adapter_comment_well:
                                // 好评
                                orderCommentList.get((Integer) commentGroup.getTag()).setRateType
                                        (1);
                                break;
                            case R.id.comment_order_goods_adapter_comment_normal:
                                // 中评
                                orderCommentList.get((Integer) commentGroup.getTag()).setRateType
                                        (2);
                                break;
                            case R.id.comment_order_goods_adapter_comment_low:
                                // 差评
                                orderCommentList.get((Integer) commentGroup.getTag()).setRateType
                                        (3);
                                break;
                        }
                    }
                });
                RatingBar ratingBar = (RatingBar) view.findViewById(R.id
                        .comment_order_goods_adapter_comment_star);
                ratingBar.setTag(i);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        orderCommentList.get((Integer) ratingBar.getTag()).setStar((int) v);
                    }
                });
                final EditText commentInput = (EditText) view.findViewById(R.id
                        .comment_order_goods_adapter_comment_input);
                commentInput.setTag(i);
                commentInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int
                            i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        orderCommentList.get((Integer) commentInput.getTag()).setCommentContent
                                (editable.toString());
                    }
                });

                icon.setImageURI(orderGoodsInfo.getIcon());
                name.setText(orderGoodsInfo.getName());
                price.setText(String.format(getResources().getString(R.string
                        .format_money), orderGoodsInfo.getPrice()));
                itemLay.addView(view);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_order_submit_btn:
                // 确认提交 按钮
                submitComment();
                break;
        }
    }


    private void submitComment() {
//        JSONObject resultObject = new JSONObject();
        JSONArray resultArray = new JSONArray();
        for (OrderComment comment : orderCommentList) {
            JSONObject itemObject = new JSONObject();
            try {
                itemObject.put("goodsId", comment.getGoodsId());
                itemObject.put("rateType", comment.getRateType());
                itemObject.put("orderNo", comment.getOrderNo());
                itemObject.put("context", comment.getCommentContent());
                itemObject.put("goodsScore", comment.getStar());
                resultArray.put(itemObject);
//                resultObject.put("rates",resultArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, String> params = new HashMap<>();
        params.put("rates", resultArray.toString());

        progressDialog = Util.showLoadingDialog(CommentOrderGoodsActivity.this, progressDialog);

        loadDataPost(SUBMIT_COMMENT_LIST, params);

        DLog.i("组合出来的最终评论数据---resultArray.toString()--->>>" + resultArray.toString());
    }


    public static void startActivity(Activity context, ArrayList<OrderListInfo> orderListInfos) {
        Intent intent = new Intent(context, CommentOrderGoodsActivity.class);
        intent.putExtra("order_list_info_array_list", orderListInfos);
        context.startActivityForResult(intent,10015);
    }

    public static void startActivity(Activity context, OrderDetailInfo orderDetailInfo) {
        Intent intent = new Intent(context, CommentOrderGoodsActivity.class);
        intent.putExtra("orderDetailInfo", orderDetailInfo);
        context.startActivityForResult(intent,10015);
    }
}
