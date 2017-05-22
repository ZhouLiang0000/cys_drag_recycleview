package mchenys.net.csdn.blog.dragrecycleview;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mChenys on 2017/2/16.
 */
public class PopWindowUtils {

    public static PopupWindow popupWindow;

    public interface CallBack {
        void onSelect(String content);
    }
    /**
     * 初始化PopupWindow
     *  @param isClickOutsideDismiss
     * @param view
     * @param activity
     */
    private static void initPopupWindow(boolean isClickOutsideDismiss, View view, final Activity activity) {
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        if (isClickOutsideDismiss) {
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setParentViewAlpha(1.0f, activity);
                    dismissPopupWindow(activity);
                }
            });
        }
        popupWindow.setAnimationStyle(R.style.pop_bottom_in_out_anim);
        setParentViewAlpha(0.5f, activity);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public static void showSingleListViewPopupWindow(final Activity activity, String[] strs, boolean isClickOutsideDismiss, final CallBack callback) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_popwindow, null);
        ListView listView = (ListView) view.findViewById(R.id.lv_data);
        TextView cancelTv = (TextView) view.findViewById(R.id.tv_cancel);
        final List<String> datas = Arrays.asList(strs);
        listView.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1,datas){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null) {
                    String content = datas.get(position);
                    callback.onSelect(content);
                }
                dismissPopupWindow(activity);
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow(activity);
            }
        });
        initPopupWindow(isClickOutsideDismiss, view, activity);
    }



    /**
     * 设置父窗口的透明度
     *
     * @param alpha 透明度
     * @param activity
     */
    private static void setParentViewAlpha(float alpha, Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = alpha;
        activity.getWindow().setAttributes(params);
    }

    /**
     * 隐藏日期选择器的PopupWindow
     * @param activity
     */
    private static void dismissPopupWindow(Activity activity) {
        if (popupWindow != null && popupWindow.isShowing()) {
            setParentViewAlpha(1.0f, activity);
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}

