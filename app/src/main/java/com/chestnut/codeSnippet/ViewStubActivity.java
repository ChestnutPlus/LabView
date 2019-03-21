package com.chestnut.codeSnippet;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chestnut.ui.R;

public class ViewStubActivity extends AppCompatActivity {

    private EditText et1,et2;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stub);

        et1 = (EditText) findViewById(R.id.et_1);
        et2 = (EditText) findViewById(R.id.et_2);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(v -> {
            ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub);
            if (viewStub!=null) {
                viewStub.inflate();
            }
            TextView tvTitle = (TextView) findViewById(R.id.tv_title);
            tvTitle.setText("I'm inflate: " + System.currentTimeMillis());
        });

    }

    /**
     * 清除editText的焦点
     * @param views views
     */
    public void clearViewFocus(View v, View[] views) {
        if (null != views && views.length > 0) {
            for (View view : views) {
                if (v==view) {
                    view.clearFocus();
                    return;
                }
            }
        }
    }

    /**
     * 是否焦点在Et上
     *
     * @param v   焦点所在View
     * @param views 输入框
     * @return true代表焦点在edit上
     */
    public boolean isFocusEditText(View v, View[] views) {
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            for (View view : views) {
                if (et.getId() == view.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断触摸点，是否在过滤控件上
     * @param views views
     * @param ev ev
     * @return true / false
     */
    public boolean isTouchView(View[] views, MotionEvent ev) {
        if (views == null || views.length == 0) {
            return false;
        }
        int[] location = new int[2];
        for (View view : views) {
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getX() > x && ev.getX() < (x + view.getWidth())
                    && ev.getY() > y && ev.getY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //1. 判断是否是点击了，过滤的控件
            if (isTouchView(filterViewByIds(), ev)) {
                return super.dispatchTouchEvent(ev);
            }
            //2. 如果 et 为空，则返回
            View[] views = hideSoftByEditViewIds();
            if (views == null || views.length == 0) {
                return super.dispatchTouchEvent(ev);
            }
            //3. 判断是否是是再次点击了 et 控件
            if (isTouchView(views, ev)) {
                return super.dispatchTouchEvent(ev);
            }
            //4. 获取到当前焦点的View，然后，判断是否 view 是否是 et 中的一个，
            //      若是，就清除焦点，关掉软盘
            View v = getCurrentFocus();
            if (isFocusEditText(v, views)) {
                clearViewFocus(v, views);
                closeKeyboard(this, v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void closeKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }

    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return id 数组
     */
    public View[] hideSoftByEditViewIds() {
        return new View[] { et1, et2 };
    }

    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return id 数组
     */
    public View[] filterViewByIds() {
        return new View[] { et1, et2, btn};
    }
}
