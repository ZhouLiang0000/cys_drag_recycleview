package mchenys.net.csdn.blog.dragrecycleview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by mChenys on 2017/2/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = "MyAdapter";
    private List<String> mData;
    private Context mContext;

    public MyAdapter(Context ctx, List<String> list) {
        this.mContext = ctx;
        this.mData = list;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyAdapter.MyViewHolder holder, int position) {
        if (position == mData.size()) {
            holder.mInfoTv.setBackgroundResource(R.drawable.add);
            holder.mInfoTv.setText("");
            holder.mInfoTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                    b.setTitle("添加");
                    final EditText editText = new EditText(mContext);
                    b.setView(editText);
                    b.setNegativeButton("取消", null);
                    b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mData.add(editText.getText().toString().trim());
                            notifyItemInserted(mData.size() - 1);
                            dialog.dismiss();
                        }
                    });
                    b.show();
                }
            });
        } else {
            holder.mInfoTv.setBackgroundResource(0);
            holder.mInfoTv.setText(mData.get(position));
            holder.mInfoTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopWindowUtils.showSingleListViewPopupWindow((Activity) mContext, new String[]{"删除", "编辑"}, true, new PopWindowUtils.CallBack() {
                        @Override
                        public void onSelect(String content) {
                            if ("删除".equals(content)) {
                                mData.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            } else if ("编辑".equals(content)) {
                                AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                                b.setTitle("编辑");
                                final EditText editText = new EditText(mContext);
                                editText.setText(mData.get(holder.getAdapterPosition()));
                                editText.setSelection(mData.get(holder.getAdapterPosition()).length());
                                b.setView(editText);
                                b.setNegativeButton("取消", null);
                                b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mData.set(holder.getAdapterPosition(), editText.getText().toString().trim());
                                        notifyItemChanged(holder.getAdapterPosition());
                                        dialog.dismiss();
                                    }
                                });
                                b.show();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //注意:这里最少有一个,因为有多了一个添加按钮
        return null == mData ? 1 : mData.size() + 1;
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < mData.size() && toPosition < mData.size()) {
            //交换数据位置
            Collections.swap(mData, fromPosition, toPosition);
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
        }
        //移动过程中移除view的放大效果
        onItemClear(source);
    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {
        int position = source.getAdapterPosition();
        mData.remove(position); //移除数据
        notifyItemRemoved(position);//刷新数据移除
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder viewHolder) {
        //当拖拽选中时放大选中的view
        viewHolder.itemView.setScaleX(1.2f);
        viewHolder.itemView.setScaleY(1.2f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {
        //拖拽结束后恢复view的状态
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mInfoTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mInfoTv = (TextView) itemView.findViewById(R.id.tv_info);
        }
    }

}
