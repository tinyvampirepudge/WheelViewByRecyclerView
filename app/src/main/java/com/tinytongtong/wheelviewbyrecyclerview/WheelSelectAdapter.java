package com.tinytongtong.wheelviewbyrecyclerview;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

/**
 * @Description:
 * @Author wangjianzhou@qding.me
 * @Date 2019-11-05 09:14
 * @Version v6.1.0
 */
public class WheelSelectAdapter extends RecyclerView.Adapter<WheelSelectAdapter.ViewHolder> {
    private Context mContext;
    private List<String> list;

    public WheelSelectAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_wheel_project_select, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String value = list.get(position);
        // 文字小于等于一行时，文字大小为18；
        // 文字大于一行小于两行时，文字大小为14
        // 文字内容大于2行时，字体适当缩小

        int tvWidth = ScreenUtil.getScreenWidth(mContext) - 2 * ScreenUtil.dip2px(mContext, 16);

        // 先重置文字大小
        holder.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtil.dip2px(mContext, 18));
        holder.tv.setText(value);

        int lineCount = TextViewLinesUtil.getTextViewLines(holder.tv, tvWidth);
        Log.e("ProjSelectDialog", "onBindViewHolder position:" + position + ",lineCount:" + lineCount);

        // 动态设置字体大小
        if (lineCount <= 1) {
            holder.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtil.dip2px(mContext, 18));
        } else {
            holder.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtil.dip2px(mContext, 14));
            // 根据现有字体大小重新测量行数，不然留白太多
            lineCount = TextViewLinesUtil.getTextViewLines(holder.tv, tvWidth);
        }

        Log.e("ProjSelectDialog", "lineCount111:" + lineCount);
        // 当字体大小为14时，依然超过两行时，这里字体大小设置为10，具体不再设置，情况太极端。
        if (lineCount > 2) {
            holder.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtil.dip2px(mContext, 12));
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}
