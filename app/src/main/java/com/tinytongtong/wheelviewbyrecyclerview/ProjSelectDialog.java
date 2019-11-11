package com.tinytongtong.wheelviewbyrecyclerview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 重点关注页面，选择对话框
 * @Author wangjianzhou@qding.me
 * @Date 2019-11-05 12:02
 * @Version v6.1.0
 */
public class ProjSelectDialog {
    public static final String TAG = ProjSelectDialog.class.getSimpleName();

    private Context mContext;

    private List<String> list = new ArrayList<>();
    private BottomSheetDialog mBottomSheetDialog;

    private int selectedIndex = 0;
    private String selectedValue;

    private OnResultListener onResultListener;
    private RecyclerView recyclerView;
    private List<String> mOptionsItems;
    private LinearLayoutManager llm;

    public ProjSelectDialog(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
        init();
    }

    private void init() {

        if (list == null || list.isEmpty()) {
            return;
        }

        if (mBottomSheetDialog == null) {

            View view = View.inflate(mContext, R.layout.dialog_select_project_recycler_view, null);

            recyclerView = view.findViewById(R.id.recyclerView);
            llm = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(llm);

            // 弹性滑动
            LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
            linearSnapHelper.attachToRecyclerView(recyclerView);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:// 静止
                            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                            if (layoutManager instanceof LinearLayoutManager) {// 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                                int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();// 获取第一个可见view的位置
                                selectedIndex = firstItemPosition;
                                Log.e(TAG, "onReboundFinish selectedIndex:" + selectedIndex);
                            }
                            break;
                        case RecyclerView.SCROLL_STATE_DRAGGING:// 手指拖动
                            break;
                        case RecyclerView.SCROLL_STATE_SETTLING:// 惯性滑动
                            break;
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });


            TextView cancel = view.findViewById(R.id.tv_cancel);
            TextView confirm = view.findViewById(R.id.tv_confirm);

            mBottomSheetDialog = new BottomSheetDialog(mContext);
            mBottomSheetDialog.setContentView(view);

            // BottomSheetDialog 禁止下滑关闭
            View root = mBottomSheetDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (root != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(root);
                if (behavior != null) {
                    behavior.setHideable(false);
                }
            }

            // 设置取消、确定点击事件
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onResultListener != null) {
                        if (selectedIndex >= 0 && list.size() > selectedIndex) {
                            Log.e(TAG, "onResultListener selectedIndex:" + selectedIndex + ", selectedItem" + list.get(selectedIndex));
                            onResultListener.onResult(selectedIndex, list.get(selectedIndex));
                        }
                    }
                    mBottomSheetDialog.dismiss();
                }
            });
        }

        setList();
    }

    private void setList() {
        mOptionsItems = new ArrayList<>();
        mOptionsItems.add("");
        mOptionsItems.add("");
        mOptionsItems.addAll(list);
        mOptionsItems.add("");
        mOptionsItems.add("");

        WheelProjectSelectAdapter wheelProjectSelectAdapter = new WheelProjectSelectAdapter(mContext, mOptionsItems);
        recyclerView.setAdapter(wheelProjectSelectAdapter);
    }

    public void setSelectedPosition(int selectedPosition) {
        if (list == null || list.isEmpty()) {
            return;
        }

        if (mOptionsItems == null || mOptionsItems.isEmpty()) {
            return;
        }

        if (recyclerView == null || llm == null) {
            return;
        }

        // 异常数据兼容
        if (selectedPosition < 0 || selectedPosition >= list.size()) {
            selectedPosition = 0;
        }

        if (selectedPosition + 2 >= mOptionsItems.size()) {
            selectedPosition = 0;
        }

        llm.scrollToPositionWithOffset(selectedPosition, 0);

    }

    public void show() {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.show();
        }
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public interface OnResultListener {
        void onResult(int selectedIndex, String selectedItem);
    }
}
