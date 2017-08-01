package com.ttxg.fruitday.gui.fragment;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttxg.fruitday.gui.adapter.CommentAdapter;
import com.ttxg.fruitday.gui.view.DetailCommentListView;
import com.ttxg.fruitday.model.Comment;
import com.ttxg.fruitday.util.ParseUtil;
import com.ttxg.fruitday.util.log.DLog;

/**
 * 商品详情 好评Framgment
 * Created by lilijun on 2016/8/26.
 */
public class CommentFragment extends BaseFragment {
    private static final String GET_COMMENT = "goods/rateMore";
    private DetailCommentListView listView;
    private CommentAdapter adapter;
    private List<Comment> commentList;
//    private TextView noCommentsView;

    /**
     * 商品id
     */
    private int goodsId;
    /**
     * 评论类型
     */
    private int type;
    /**
     * 获取评论数据的参数
     */
    private Map<String, Object> paramMap;
    /**
     * 当前页数
     */
    private int curPage = 1;
    /**
     * 请求每页的数据条数
     */
    private int pageSize = 20;

    /**
     * 判断是否是第一次加载数据
     */
    private boolean isFirstLoadData = true;

    private int screenWidth = 0;
    private int screenHeight = 0;

    @Override
    protected void initView(RelativeLayout view) {
        commentList = new ArrayList<>();
        paramMap = new HashMap<>();

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();

//        setCenterView(R.layout.fragment_detail_comment_lay);
//        listView = (DetailCommentListView) view.findViewById(R.id.commment_listview);
        listView = new DetailCommentListView(getActivity());
        setCenterView(listView);
        adapter = new CommentAdapter(commentList);

//        noCommentsView = (TextView) view.findViewById(R.id.comment_no_data);
//        listView.setAdapter(adapter);
        listView.setOnNextPageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DLog.i("lilijun", "加载下一页数据！！！");
                loadCommentData();
            }
        });
    }

    /**
     * 设置去获取数据的类型
     *
     * @param goodsId 评论的商品id
     * @param type    所获评论的类型  1=好评，2=差评，3=差评
     */
    public void setIdAndType(int goodsId, int type) {
        this.goodsId = goodsId;
        this.type = type;
        paramMap.put("goodsId", goodsId);
        paramMap.put("rateType", type);
    }

    // 加载评论数据
    private void loadCommentData() {
        if (isFirstLoadData) {
            // 是第一次加载评论数据
            paramMap.put("start", curPage);
            paramMap.put("limit", pageSize);
            isFirstLoadData = false;
        } else {
            // 不是第一次加载评论(加载下一页)
            curPage++;
            paramMap.remove("start");
            paramMap.put("start", curPage);
        }
        loadDataGet(GET_COMMENT, paramMap);
    }

    /**
     * 外部调用的  加载数据方法  只在第一次调用的时候有效
     */
    public void loadCommentOut() {
        if (!isFirstLoadData) {
            return;
        }
        loadCommentData();
    }


    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        DLog.i("lilijun", "好评加载完成！tag---->>" + tag);
        if (tag.equals(GET_COMMENT)) {
            List<Comment> list = ParseUtil.parseCommentList(resultObject);
            if (list != null) {
                if (list.isEmpty()) {
                    // 数据为空
                    // 不显示下一页数据
                    listView.setNextPageViewVisible(false);
                    if (curPage == 1) {
                        // 没有评论数据
//                        setNoCommentData();
                        showNoDataView();
                    }
                } else {
                    // 数据不为空
                    if (list.size() < pageSize) {
                        // 没有下一页数据了(评论数据已经全部加载完)
                        // 不显示下一页数据
                        listView.setNextPageViewVisible(false);
                    } else {
                        listView.setNextPageViewVisible(true);
                    }
                    commentList.addAll(list);
                    if (curPage == 1) {
                        // 第一页，显示中间视图
                        showCenterView();
                        // 设置listView的高度为整个屏幕高度的十分之六
                        listView.getLayoutParams().height = (int)(0.6 * screenHeight);
                        listView.setAdapter(adapter);
                    }else {
                        adapter.notifyDataSetChanged();
                    }
//                    noCommentsView.setVisibility(View.GONE);
                }
            } else {
                // 加载失败
//                listView.setNextPageViewVisible(false);
//                // 没有评论数据
//                setNoCommentData();
                showErrorView();
            }

//            showCenterView();
        }
    }


    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        DLog.i("lilijun", "请求好评失败 tag--->>>" + tag);
        if (tag.equals(GET_COMMENT)) {
            if (commentList.isEmpty()) {
                // 加载失败
                showErrorView();
            } else {
                // 改变加载下一页item的视图
                // 加载下一页数据失败  将当前页的page减1
                curPage--;
                listView.setNextPageViewLoadFailed();
            }
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
        loadDataGet(GET_COMMENT, paramMap);
    }

//    /**
//     * 没有评论数据
//     */
//    private void setNoCommentData(){
//        if(getActivity() == null){
//            return;
//        }
//        listView.setVisibility(View.GONE);
//        switch (type){
//            case Comment.COMMENT_TYPE_GOOD:
//                // 好评
//                noCommentsView.setText(getResources().getString(R.string.no_good_comments));
//                break;
//            case Comment.COMMENT_TYPE_NORMAL:
//                // 中评
//                noCommentsView.setText(getResources().getString(R.string.no_normal_comments));
//                break;
//            case Comment.COMMENT_TYPE_LOW:
//                // 差评
//                noCommentsView.setText(getResources().getString(R.string.no_low_comments));
//                break;
//            default:
//                break;
//        }
//    }
}
