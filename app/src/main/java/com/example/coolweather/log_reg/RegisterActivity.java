package com.example.coolweather.log_reg;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coolweather.R;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ToastUtil toastUtil;
    private String realCode;
    private ImageView mIvRegister_Back;
    private EditText mEtRegister_Username;
    private EditText mEtRegister_Password1;
    private EditText mEtRegister_Password2;
    private EditText mEtRegister_Phonecodes;
    private ImageView mIvRegister_Showcode;
    private Button mBtRegister_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toastUtil=ToastUtil.createToastConfig();

        initView();

        //将验证码用图片的形式显示出来
        mIvRegister_Showcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    private void initView(){
        mIvRegister_Back = findViewById(R.id.iv_register_back);
        mEtRegister_Username = findViewById(R.id.et_register_username);
        mEtRegister_Password1 = findViewById(R.id.et_register_password1);
        mEtRegister_Password2 = findViewById(R.id.et_register_password2);
        mEtRegister_Phonecodes = findViewById(R.id.et_register_phoneCodes);
        mIvRegister_Showcode = findViewById(R.id.iv_register_showCode);
        mBtRegister_Register = findViewById(R.id.bt_register_register);

        /**
         * 注册页面能点击的就三个地方
         * top处返回箭头、刷新验证码图片、注册按钮
         */
        mIvRegister_Back.setOnClickListener(this);
        mIvRegister_Showcode.setOnClickListener(this);
        mBtRegister_Register.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_register_back: //返回登录页面
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.iv_register_showCode:    //改变随机验证码的生成
                mIvRegister_Showcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.bt_register_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegister_Username.getText().toString().trim();
                String password1 = mEtRegister_Password1.getText().toString().trim();
                String password2 = mEtRegister_Password2.getText().toString().trim();
                String phoneCode = mEtRegister_Phonecodes.getText().toString().toLowerCase();
                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1) && password1.equals(password2)
                        && !TextUtils.isEmpty(phoneCode) ) {
                    if (phoneCode.equals(realCode)) {
                        List<User> userList= DataSupport.findAll(User.class);
                        boolean match = false;
                        for (User user:userList){
                            if(username.equals(user.getName())){
                                match=true;
                                break;
                            }else if(username.equals(user.getName())&&password1.equals(user.getPassword())){
                                match=false;
                            }
                        }
                        if(match){
                            toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),username+"已存在!");
                        }else {
                            new User(username,password1).save();  //将用户名和密码加入到数据库中
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"注册成功");
                        }
                    } else {
                        toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"验证码错误,注册失败");
                        mEtRegister_Phonecodes.setText("");
                    }
                }else if (TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)
                        && !TextUtils.isEmpty(phoneCode)){
                    toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"用户名不能为空哦!");
                }else if(!password1.equals(password2) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(phoneCode)){
                    toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"两次密码不一样!");
                    mEtRegister_Password2.setText("");
                    mEtRegister_Phonecodes.setText("");
                }else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)
                        && TextUtils.isEmpty(phoneCode) ){
                    toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"请输入验证码!");
                }else {
                    toastUtil.ToastShow(getApplicationContext(), (ViewGroup) findViewById(R.id.toast),"请输入注册信息!");
                }
                break;
        }
    }
}

