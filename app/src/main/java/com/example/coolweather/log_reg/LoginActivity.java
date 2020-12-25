package com.example.coolweather.log_reg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coolweather.MainActivity;
import com.example.coolweather.R;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ToastUtil toastUtil;

    private TextView mTvLogin_Register;
    private EditText mEtLogin_Username;
    private EditText mEtLogin_Password;
    private CheckBox rememberPass;
    private Button mBtLogin_Login;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        toastUtil=ToastUtil.createToastConfig();
        initView();

        boolean isRemeber=pref.getBoolean("remember_password",false);
        if(isRemeber){
            String name=pref.getString("name","");
            String password=pref.getString("password","");
            mEtLogin_Username.setText(name);
            mEtLogin_Password.setText(password);
            rememberPass.setChecked(true);
        }
    }

    /**
     * onCreae()中大的布局已经摆放好了，接下来就该把layout里的东西
     * 声明、实例化对象然后有行为的赋予其行为
     * 这样就可以把视图层View也就是layout 与 控制层 Java 结合起来了
     */
    private void initView() {
        // 初始化控件
        mEtLogin_Username = findViewById(R.id.et_login_username);
        mEtLogin_Password = findViewById(R.id.et_login_password);
        rememberPass = findViewById(R.id.remember_pass);

        mBtLogin_Login = findViewById(R.id.bt_login_login);
        mTvLogin_Register = findViewById(R.id.tv_login_register);

        // 设置点击事件监听器
        mBtLogin_Login.setOnClickListener(this);
        mTvLogin_Register.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.tv_login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.bt_login_login:
                String name = mEtLogin_Username.getText().toString().trim();
                String password = mEtLogin_Password.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    List<User> userList= DataSupport.findAll(User.class);
                    int match=0;
                    if(userList.size()>0){
                        for (User user:userList) {
                            if(name.equals(user.getName()) && !password.equals(user.getPassword())){
                                match = 1;  //密码错误
                                break;
                            }else if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                                match = 2; //登录成功
                                break;
                            }else {
                                match = 0;
                            }
                        }
                    }else {
                        match = 0;
                    }
                    if (match==2) {
                        editor = pref.edit();
                        if(rememberPass.isChecked()){
                            editor.putBoolean("remember_password",true);
                            editor.putString("name",name);
                            editor.putString("password",password);
                        }else {
                            editor.clear();
                        }
                        editor.apply();

                        toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"登录成功");
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("username",name);
                        startActivity(intent);
                        finish();//销毁此Activity
                    } else if(match==1) {
                        toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"您输入的密码不正确！");
                        mEtLogin_Password.setText("");
                    }else {
                        toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),name+"不存在！");
                    }
                }else if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
                    toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"请输入用户名！");
                } else if(!TextUtils.isEmpty(name) && TextUtils.isEmpty(password)){
                    toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"请输入密码！");
                }else {
                    toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"请输入用户名和密码！");
                }
                break;
        }
    }

}


