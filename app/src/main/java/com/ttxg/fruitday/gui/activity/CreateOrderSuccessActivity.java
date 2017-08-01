package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.model.OrderInfo;


/**
 * 创建订单成功界面(下单成功)
 * Created by lilijun on 2016/9/23.
 */
public class CreateOrderSuccessActivity extends BaseActivity {
    private OrderInfo orderInfo;
    private TextView orderNo;
    private TextView payMoney;
    private TextView takePerson;
    private TextView telephone;
    private TextView address;
    private TextView goPayBtn;

    @Override
    protected void initView() {
        setTitleName(getResources().getString(R.string.create_order_success));
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        if (orderInfo == null) {
            showNoDataView();
            return;
        }

        setCenterView(R.layout.activity_create_order_success);
        orderNo = (TextView) findViewById(R.id.create_success_order_num);
        payMoney = (TextView) findViewById(R.id.create_success_pay_money);
        takePerson = (TextView) findViewById(R.id.create_success_take_person);
        telephone = (TextView) findViewById(R.id.create_success_telephone);
        address = (TextView) findViewById(R.id.create_success_address);
        goPayBtn = (TextView) findViewById(R.id.create_success_go_pay_btn);
        goPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 去付款
                ConfirmPayActivity.startActivity(CreateOrderSuccessActivity.this, orderInfo);
                finish();
            }
        });

        setData();
        showCenterView();
    }

    private void setData() {
        orderNo.setText(orderInfo.getSerialNumber());
        payMoney.setText(String.format(getResources().getString(R.string.format_money), orderInfo
                .getRealPayment() + ""));
        takePerson.setText(String.format(getResources().getString(R.string.take_person),
                orderInfo.getReceiveName()));
        telephone.setText(String.format(getResources().getString(R.string.format_telephone),
                orderInfo.getReceiveMobile()));
        StringBuilder builder = new StringBuilder(orderInfo.getReceiveProvince());
        builder.append(orderInfo.getReceiveCity());
        builder.append(orderInfo.getReceiveArea());
        builder.append(orderInfo.getReceiveAddress());
        address.setText(String.format(getResources().getString(R.string.format_address),
                builder.toString()));
    }

    public static void startActivity(Context context, OrderInfo orderInfo) {
        Intent intent = new Intent(context, CreateOrderSuccessActivity.class);
        intent.putExtra("orderInfo", orderInfo);
        context.startActivity(intent);
    }
}
