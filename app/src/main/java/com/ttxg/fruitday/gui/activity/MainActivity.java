package com.ttxg.fruitday.gui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ttxg.fruitday.R;
import com.ttxg.fruitday.gui.fragment.TabClassifyFragment;
import com.ttxg.fruitday.gui.fragment.TabHomeFragment;
import com.ttxg.fruitday.gui.fragment.TabShoppingCartFragment;
import com.ttxg.fruitday.gui.fragment.TabUserCenterFragment;
import com.ttxg.fruitday.manager.UserInfoManager;
import com.ttxg.fruitday.net.NetUtil;
import com.ttxg.fruitday.util.Constants;
import com.ttxg.fruitday.util.PreferencesUtils;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.download.DownloadManagerUtil;
import com.ttxg.fruitday.util.log.DLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private FragmentManager fragmentManager = null;

    /**
     * 当前所在tab的标识值
     */
    private int curTab = 0;

    private RadioGroup radioGroup;

    /**
     * 4个tab的fragment
     */
    private Fragment[] fragments = new Fragment[4];

    private long mExitTime = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 检查更新版本
            checkUpdateVersion();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.main_tab_radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        fragments[0] = new TabHomeFragment();
        fragments[1] = new TabClassifyFragment();
//        fragments[2] = new TabStoresFragment();
        fragments[2] = new TabShoppingCartFragment();
        fragments[3] = new TabUserCenterFragment();

        // 设置进入应用时 默认跳转的tab
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_tab_fragment_lay, fragments[0]);
        transaction.commitAllowingStateLoss();
        curTab = 0;

        if (checkUpdatePreferencesConfig()) {
            handler.sendEmptyMessageDelayed(1, 3000);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.main_tab_1:
                if (curTab != 0) {
                    changePage(fragments[0]);
                    curTab = 0;
                }
                break;
            case R.id.main_tab_2:
                if (curTab != 1) {
                    changePage(fragments[1]);
                    curTab = 1;
                }
                break;
            case R.id.main_tab_3:
                if (UserInfoManager.getInstance().isValidUserInfo()) {
                    // 用户有登录
                    changePage(fragments[2]);
                    curTab = 2;
                } else {
                    // 没有用户登录
                    LoginActivity.startActivity(MainActivity.this);
                    changeTabBySth();
                    return;
                }
                break;
            case R.id.main_tab_4:
                if (curTab != 3) {
                    if (UserInfoManager.getInstance().isValidUserInfo()) {
                        // 有用户登陆
                        changePage(fragments[3]);
                        curTab = 3;
                    } else {
                        // 没有用户登陆
                        LoginActivity.startActivity(MainActivity.this);
                        changeTabBySth();
                        return;
                    }
                }
                break;
        }
        radioGroup.check(checkedId);
    }

    private void changePage(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragments[curTab]);
        if (!fragment.isAdded()) {
            // 如果没有被添加过
            transaction.add(R.id.main_tab_fragment_lay, fragment);
        } else if (fragment.isHidden()) {
            transaction.show(fragment);
        }
        transaction.commitAllowingStateLoss();
    }


    /**
     * 还原RadioGroup Tab切换
     */
    private void changeTabBySth() {
        switch (curTab) {
            case 0:
                radioGroup.check(R.id.main_tab_1);
                break;
            case 1:
                radioGroup.check(R.id.main_tab_2);
                break;
            case 2:
                radioGroup.check(R.id.main_tab_3);
                break;
            case 3:
                radioGroup.check(R.id.main_tab_4);
        }
    }

    /**
     * 切换到指定tab
     *
     * @param position
     */
    public void changeTab(int position) {
        curTab = position;
        changeTabBySth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int length = fragments.length;
        for (int i = 0; i < length; i++) {
            fragments[i].onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this,
                    getResources().getString(R.string.again_sure_exit),
                    Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
//            NetManager.getInstance().cancleRequests(NetManager.TAG);
            finish();
            System.exit(0);
        }
    }


    /**
     * 检查更新配置信息
     */
    private boolean checkUpdatePreferencesConfig() {
        int inTimes = PreferencesUtils.getInt(Constants.COME_IN_TIEMS, 0);
        inTimes++;
        PreferencesUtils.putInt(Constants.COME_IN_TIEMS, inTimes);
        // 是否检测到过有版本更新
        boolean isHasUpdated = PreferencesUtils.getBoolean(Constants.HAS_VERSION_UPDATE);
        if (isHasUpdated) {
            // 有检测到过
            // 从检测到有版本更新后、用户进入app的次数
            if (inTimes >= Constants.INGRORN_REMIND_TIME) {
                // 如果自从上次检测到有版本更新并已经到了忽略检测版本更新的次数了，则需再次去检测版本更新
                return true;
            }
        } else {
            // 没有检测到过、上次是最新版本
            return true;
        }
        return false;
    }


    /**
     * 检查版本更新
     */
    private void checkUpdateVersion() {
        int versionCode = Util.getVersionCode(MainActivity.this);
        DLog.i("lilijun", "versionCode----->>>" + versionCode);
        Map<String, Object> params = new HashMap<>();
        params.put("versionCode", versionCode);
        params.put("appId", Constants.YB_APP_ID);
        //1=app，2=pad
        params.put("deviceType", 1);
        NetUtil.requestStringGet("version/checkUpdate", params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DLog.i("lilijun", "检查更新版本成功！！！");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    DLog.i("lilijun", "检查更新版本成功！！！code---->>>" + code);
                    if (code == 0) {
                        // 设置检测到过版本有更新
                        PreferencesUtils.putBoolean(Constants.HAS_VERSION_UPDATE, true);
                        // 有版本更新
                        JSONObject mapObject = jsonObject.getJSONObject("map");
                        if (jsonObject.has("newVersion")) {
                            JSONObject newVersionObject = mapObject.getJSONObject("newVersion");
                            // 应用名称
                            String title = newVersionObject.getString("appName");
                            // 版本名称
                            String versionName = newVersionObject.getString("version");
                            // 新版特性
                            String describle = newVersionObject.getString("properties");
                            // 更新包下载地址
                            String downloadUrl = newVersionObject.getString("downloadUrl");
                            // 组合消息
                            String message = title + getResources().getString(R.string
                                    .update_version) + versionName + "\n\n" + describle;
                            showUpdateVersionDialog(title, message, downloadUrl);
                        }
                    } else {
                        // 设置没有检测到过版本有更新
                        PreferencesUtils.putBoolean(Constants.HAS_VERSION_UPDATE, false);
                        DLog.i("lilijun", "检查更新版本成功！！！但当前没有版本更新！");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DLog.i("lilijun", "检查更新版本失败！！！error.toString()---->>>" + error.toString());
            }
        });
    }

    private void showUpdateVersionDialog(String title, String message, final String downloadUrl) {
        Util.showAlertDialog(MainActivity.this, title, message, getResources().getString(R.string
                .update_now), getResources().getString(R.string.update_later), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 立即更新
                        // 设置没有检测到过版本有更新(这样新版本安装之后或者下载完成用户没有安装时，用户下次进入还是会去检测版本更新信息)
                        PreferencesUtils.putBoolean(Constants.HAS_VERSION_UPDATE, false);
                        DownloadManagerUtil.getInstance().startDownload(downloadUrl);
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 暂不更新
                // 设置已经忽略更新信息的次数为0
                PreferencesUtils.putInt(Constants.COME_IN_TIEMS, 0);
                dialogInterface.dismiss();
            }
        });
    }

    public static void startActivity(Context context){
        // 跳转到主页
        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
