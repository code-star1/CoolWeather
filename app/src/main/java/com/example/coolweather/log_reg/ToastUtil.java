package com.example.coolweather.log_reg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.R;

@SuppressLint("ResourceAsColor")
public class ToastUtil {

    private static ToastUtil toastUtil;

    private Toast toast;

    private ToastUtil(){
    }

    public static ToastUtil createToastConfig(){
        if (toastUtil==null) {
            toastUtil = new ToastUtil();
        }
        return toastUtil;
    }

    public void ToastShow(Context context,ViewGroup root,String msg){
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_toast,root);
        TextView text = layout.findViewById(R.id.toast_msg);
        text.setText(msg);
        text.setTextColor(R.color.white);
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}