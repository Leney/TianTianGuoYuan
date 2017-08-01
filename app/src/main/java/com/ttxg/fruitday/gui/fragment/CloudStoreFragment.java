package com.ttxg.fruitday.gui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ttxg.fruitday.R;
import com.ttxg.fruitday.callback.OnLocationLinstener;
import com.ttxg.fruitday.gui.activity.StoreDitailActivity;
import com.ttxg.fruitday.gui.adapter.StoreAdapter;
import com.ttxg.fruitday.gui.view.RefreshListView;
import com.ttxg.fruitday.model.SpinnerMode;
import com.ttxg.fruitday.model.Store;
import com.ttxg.fruitday.net.NetUtil;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.Util;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 云店铺Fragment
 * Created by lilijun on 2016/9/1.
 */
public class CloudStoreFragment extends BaseFragment {
    private static final String GET_CLOUD_LIST = "cloud/list";
    private View topView;
    /**
     * 位置
     */
    private TextView locationText;
    /**
     * 分类，排序，距离 下拉菜单
     */
    private Spinner classifySpinner, orderSpinner, distanceSpinner;
    private List<SpinnerMode> classifyValueList;
    private List<String> classifyValues;
    private String[] orderValues;
    private String[] distanceValues;

    private RefreshListView listView;
    private StoreAdapter adapter;
    /**
     * 云店列表数据
     */
    private List<Store> storeList;
    /**
     * 参数信息
     */
    private HashMap<String, Object> params = null;


    private ArrayAdapter classifyAdapter;

    private int curPage = 1;
    private static final int LOAD_DATA_SIZE = 20;

    /**
     * 经纬度 [0]=纬度，[1]=经度
     */
    private double[] locations;
    /**
     * 定位到的位置信息
     */
    private String locationAdress;

    /**
     * 标记是否是第一次加载数据
     */
    private boolean isFirstLoad = true;

    /**
     * 类别menu 当前显示的position
     */
    private int classifyCurPosition = 0;

    /**
     * 距离类别(order)menu 当前显示的position
     */
    private int orderCurPosition = 0;

    /**
     * 距离范围menu 当前显示的position
     */
    private int distanceCurPosition = 0;

    /**
     * 是否是新的加载页(分页重新计算)
     */
    private boolean isNewLoad = true;

    /**
     * 是否已经设置过类别的menu
     */
    private boolean isSetClassifyMenu = false;

    /**
     * 是否正在刷新
     */
    private boolean isRefreshing = false;

    @Override
    protected void initView(RelativeLayout view) {


        classifyValueList = new ArrayList<>();
        // 设置全部的SpinnerMode
        SpinnerMode spinnerMode = new SpinnerMode();
        spinnerMode.setName(getResources().getString(R.string.all));
        spinnerMode.setDiscription(spinnerMode.getName());
        spinnerMode.setId(0);
        classifyValueList.add(spinnerMode);

        classifyValues = new ArrayList<>();
        classifyValues.add(spinnerMode.getName());

        storeList = new ArrayList<>();
        params = new HashMap<>();
        locations = new double[2];

        initTopView();

        listView = new RefreshListView(getActivity());
        setCenterView(listView);

        listView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params.put("start", curPage);
                loadDataGet(GET_CLOUD_LIST, params);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StoreDitailActivity.startActivity(getActivity(), storeList.get(i - 1).getUserId(),
                        storeList.get(i - 1).getName());
            }
        });

        adapter = new StoreAdapter(storeList);
        listView.setAdapter(adapter);

        listView.setonRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                params.put("start", 1);
                params.put("st1", 1);
                params.put("st2", 2);
                params.put("st3", 3);
                params.put("sv1", classifyValueList.get(classifyCurPosition).getId());
                params.put("sv2", orderCurPosition);
                params.put("sv3", distanceCurPosition);
                loadDataGet(GET_CLOUD_LIST, params);
            }
        });

        params.put("start", curPage);
        params.put("storeType", 9);
        params.put("limit", LOAD_DATA_SIZE);
    }

    private void initTopView() {
        topView = View.inflate(getActivity(), R.layout.cloud_fragment_top_lay, null);
        locationText = (TextView) topView.findViewById(R.id.cloud_store_location_text);
        classifySpinner = (Spinner) topView.findViewById(R.id.cloud_store_spinner_classify);
        orderSpinner = (Spinner) topView.findViewById(R.id.cloud_store_spinner_order);
        distanceSpinner = (Spinner) topView.findViewById(R.id.cloud_store_spinner_distance);
        setAddView(topView);


        // 得到排序和距离的菜单值
        orderValues = getResources().getStringArray(R.array.sppiner_order_values);
        distanceValues = getResources().getStringArray(R.array.sppiner_distance_values);

        //适配器
        classifyAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lay,
                classifyValues);
        ArrayAdapter orderAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lay,
                orderValues);
        ArrayAdapter distanceAdapter = new ArrayAdapter<>(getActivity(), R.layout
                .spinner_item_lay, distanceValues);

        // 设置样式
        classifyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        classifySpinner.setAdapter(classifyAdapter);
        classifySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classifyCurPosition = i;
                if (isNewLoad) {
                    return;
                }
                isNewLoad = true;
                // 过滤条件有变动 通过过滤条件重新加载数据
                loadDataByFillter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        orderSpinner.setAdapter(orderAdapter);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderCurPosition = i;
                if (isNewLoad) {
                    return;
                }
                isNewLoad = true;
                // 过滤条件有变动 通过过滤条件重新加载数据
                loadDataByFillter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        distanceSpinner.setAdapter(distanceAdapter);
        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                distanceCurPosition = i;
                if (isNewLoad) {
                    return;
                }
                isNewLoad = true;
                // 设置距离参数
                setParamsDistance(i);
                // 过滤条件有变动 通过过滤条件重新加载数据
                loadDataByFillter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (tag.equals(GET_CLOUD_LIST)) {
            // 获取云店信息成功
            if (isRefreshing) {
                // 下拉刷新成功
                List<Store> emptyStoreList = new ArrayList<>();
                boolean isSuccess = ParseUtil.parseStoreList(resultObject, emptyStoreList,
                        null);
                if (isSuccess) {
                    curPage = 2;
                    storeList.clear();
                    storeList.addAll(emptyStoreList);
                    adapter.notifyDataSetChanged();
                    if (storeList.isEmpty()) {
                        showNoDataView();
                    } else {
                        showCenterView();
                    }
                } else {
                    Util.showToast(getActivity(), getResources().getString(R.string.refresh_fail));
                }
                listView.onRefreshComplete();
                isRefreshing = false;
            } else {
                // 不是下拉刷新成功
                if (isNewLoad) {
                    storeList.clear();
                    curPage = 1;
                    isNewLoad = false;
                }
                if (storeList.isEmpty()) {
                    // 是加载的第一页数据回来了
                    boolean isSuccess = ParseUtil.parseStoreList(resultObject, storeList,
                            classifyValueList);
                    if (isSuccess) {
                        // 解析成功
                        if (!isSetClassifyMenu) {
                            // 只设置一次类别menu
                            int size = classifyValueList.size();
                            for (int i = 0; i < size; i++) {
                                if (i == 0) {
                                    // 第一条数据不要  因为默认有个 "全部"  在初始化的时候已经设置好了
                                    continue;
                                }
                                classifyValues.add(classifyValueList.get(i).getName());
                            }
                            // 设置类别Spinner 数据
                            classifyAdapter.notifyDataSetChanged();
                            isSetClassifyMenu = true;
                        }
                        adapter.notifyDataSetChanged();
                        if (storeList.isEmpty()) {
                            showNoDataView();
                        } else {
                            showCenterView();
                        }
                    } else {
                        // 解析出现异常
                        showErrorView();
                    }
                    if (storeList.size() < LOAD_DATA_SIZE) {
                        // 证明没有下一页数据
                        listView.setNextPageViewVisible(false);
                    } else {
                        // 还有下一页数据
                        listView.setNextPageViewVisible(true);
                    }
                } else {
                    // 是加载下一页数据回来了
                    int lastSize = storeList.size();
                    boolean isSuccess = ParseUtil.parseStoreList(resultObject, storeList,
                            classifyValueList);
                    if (storeList.size() - lastSize < LOAD_DATA_SIZE) {
                        // 证明没有下一页数据了
                        listView.setNextPageViewVisible(false);
                    } else {
                        // 还有下一页数据
                        listView.setNextPageViewVisible(true);
                    }
                }
                curPage++;
            }
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (tag.equals(GET_CLOUD_LIST)) {
            if (isRefreshing) {
                isRefreshing = false;
                listView.onRefreshComplete();
                Util.showErrorMessage(getActivity(), msg, getResources().getString(R.string
                        .refresh_fail));
            } else {
                if (isNewLoad) {
                    storeList.clear();
                    curPage = 1;
                    showErrorView();
                } else {
                    if (storeList.isEmpty()) {
                        // 是加载的第一页数据失败了
                        showErrorView();
                    } else {
                        // 是加载下一页失败了
                        listView.setNextPageViewLoadFailed();
                    }
                }
            }
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_CLOUD_LIST, params);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            Util.getLocation(getActivity(), new OnLocationLinstener() {
                @Override
                public void onLocationSuccess(double[] locationDetail) {
                    locations = locationDetail;
//                    locations[0] = locationDetail[0];
//                    locations[1] = locationDetail[1];
                    DLog.i("纬度-----------》》》" + locations[0]);
                    DLog.i("经度-----------》》》" + locations[1]);
                    // 获取地址
                    getLocationAdress(locations);
                }

                @Override
                public void onLocationFailed() {
                    // 无法定位、设置默认值
                    locations[0] = 22.52;
                    locations[1] = 113.92;
                    locationAdress = getResources().getString(R.string.default_location_adress);
                    // 获取地址
                    getLocationAdress(locations);
                }

                @Override
                public void noPermisstion() {
                    // 无法定位、设置默认值
                    locations[0] = 22.52;
                    locations[1] = 113.92;
                    locationAdress = getResources().getString(R.string.default_location_adress);
                    Toast.makeText(getActivity(), "无定位权限", Toast.LENGTH_SHORT).show();
                    // 获取地址
                    getLocationAdress(locations);
                }
            });
//            double[] locations = Util.getLocation(getActivity());
//            if(locations != null){
//                this.locations[0] = locations[0];
//                this.locations[1] = locations[1];
//                DLog.i("纬度-----------》》》"+locations[0]);
//                DLog.i("经度-----------》》》"+locations[1]);
//            }else{
//                // 无法定位、设置默认值
//                this.locations[0] = 22.52;
//                this.locations[1] = 113.92;
//                this.locationAdress = getResources().getString(R.string.default_location_adress);
//            }
            isFirstLoad = false;
        }
    }

    /**
     * 获得具体的定位地址信息
     *
     * @param location 纬度和经度，[0]=纬度，[1]=经度
     * @return
     */
    private void getLocationAdress(final double[] location) {
//        StringBuilder url = new StringBuilder();
//        url.append("http://maps.google.cn/maps/api/geocode/json");
//        url.append("?latlng="+location[0]+"," + location[1]);
//        url.append("&language=zh-CN");
        String url = "http://maps.google.cn/maps/api/geocode/json?latlng=" + location[0] + "," +
                location[1] + "&language=zh-CN";
        NetUtil.requestUrl(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                locationAdress = ParseUtil.parseLocationAdress(response);
                if ("".equals(locationAdress)) {
                    // 返回状态是失败,设置默认值
                    locations[0] = 22.52;
                    locations[1] = 113.92;
                    locationAdress = getResources().getString(R.string.default_location_adress);
                }
                locationText.setText(locationAdress);
                // 加载列表数据
                loadDataByLoacation();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 解析失败、设置默认值
                locations[0] = 22.52;
                locations[1] = 113.92;
                locationAdress = getResources().getString(R.string.default_location_adress);

                locationText.setText(locationAdress);
                //加载列表数据
                loadDataByLoacation();
            }
        });
    }

    /**
     * 通过位置信息去请求数据
     */
    private void loadDataByLoacation() {
        params.put("lat", locations[0]);
        params.put("lng", locations[1]);
        try {
            String tempAdress = URLEncoder.encode(locationAdress, "utf-8");
            params.put("addr", tempAdress);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        loadDataGet(GET_CLOUD_LIST, params);
    }

    /**
     * 通过选择的过滤信息去请求数据
     */
    private void loadDataByFillter() {
        params.put("st1", 1);
        params.put("st2", 2);
        params.put("st3", 3);
        params.put("sv1", classifyValueList.get(classifyCurPosition).getId());
        params.put("sv2", orderCurPosition);
        params.put("sv3", distanceCurPosition);
        showLoadingView();
        loadDataGet(GET_CLOUD_LIST, params);
    }


    /**
     * 设置距离参数
     *
     * @param position
     */
    private void setParamsDistance(int position) {
        switch (position) {
            case 0:
                // 全部
                params.remove("beginDistance");
                params.remove("endDistance");
                params.remove("distanceStr");
                break;
            case 1:
                // 500内
                params.put("beginDistance", 0);
                params.put("endDistance", 500);
                params.put("distanceStr", "0-500");
                break;
            case 2:
                // 0.5-2km
                params.put("beginDistance", 500);
                params.put("endDistance", 2000);
                params.put("distanceStr", "500-2000");
                break;
            case 3:
                // 2-5km
                params.put("beginDistance", 2000);
                params.put("endDistance", 5000);
                params.put("distanceStr", "2000-5000");
                break;
            case 4:
                // 5-10k
                params.put("beginDistance", 5000);
                params.put("endDistance", 10000);
                params.put("distanceStr", "5000-10000");
                break;
            case 5:
                // 10km外
                params.put("beginDistance", 10000);
                params.put("endDistance", 0);
                params.put("distanceStr", "10000-0");
                break;
        }
    }

//    private void getLocation() {
//        // 获取位置管理服务
//        LocationManager locationManager;
//        String serviceName = Context.LOCATION_SERVICE;
//        locationManager = (LocationManager) getActivity().getSystemService(serviceName);
//        // 查找到服务信息
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
//        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
// PackageManager.PERMISSION_GRANTED) {
//                Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
//                updateToNewLocation(location);
//                // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
//                locationManager.requestLocationUpdates(provider, 100 * 1000, 500,
//                        locationListener);
//            }else {
//                Toast.makeText(getActivity(),"请开启权限！！",Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
//            updateToNewLocation(location);
//            // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
//            locationManager.requestLocationUpdates(provider, 100 * 1000, 500,
//                    locationListener);
//        }
//    }
//
//    private LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
////            locationText.setText("位置改变！");
//        }
//
//        @Override
//        public void onStatusChanged(String s, int i, Bundle bundle) {
////            locationText.setText("位置状态改变！");
//        }
//
//        @Override
//        public void onProviderEnabled(String s) {
//            locationText.setText("onProviderEnabled！");
//        }
//
//        @Override
//        public void onProviderDisabled(String s) {
//            locationText.setText("onProviderDisabled！");
//        }
//    };
//
//    private void updateToNewLocation(Location location) {
//        if (location != null) {
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//            DLog.i("lilijun","纬度------>>"+latitude);
//            DLog.i("lilijun","经度------>>"+longitude);
//        } else {
//            DLog.i("lilijun","无法获取地理信息");
////            locationText.setText("无法获取地理信息");
//        }
//    }
}
