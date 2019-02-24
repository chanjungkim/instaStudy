package org.simplesns.simplesns.activity.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.simplesns.simplesns.GlobalUser;
import org.simplesns.simplesns.R;
import org.simplesns.simplesns.activity.main.MainActivity;
import org.simplesns.simplesns.activity.sign.item.BasicResult;
import org.simplesns.simplesns.activity.sign.item.SignUpData;
import org.simplesns.simplesns.activity.sign.item.SignUpResult;
import org.simplesns.simplesns.lib.BasicCountDownTimer;
import org.simplesns.simplesns.lib.remote.RemoteService;
import org.simplesns.simplesns.lib.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstActivity extends AppCompatActivity {
    private static String TAG = FirstActivity.class.getSimpleName();
    long timeLeftInMillionSeconds = 1000 * 60 * 3;
    int backCount = 0;

    String email;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(GlobalUser.getInstance().getMyIDPreference(FirstActivity.this) != null){
            GlobalUser.getInstance().loginByMyId(FirstActivity.this, GlobalUser.getInstance().getMyId());
        }
        super.onCreate(savedInstanceState);
        initFirst();
    }

    @Override
    public void onBackPressed() {
        backCount++;

        switch (backCount) {
            case 0:
                initEmailValidView();
                break;
            case 1:
                initFirst();
                break;
            case 2:
                Toast.makeText(this, "Press back to exit.", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                super.onBackPressed();
                finish();
                break;
        }
    }

    public void initFirst() {
        setContentView(R.layout.activity_first);
        Button createBTN = findViewById(R.id.create_button_firstactivity);
        Button loginBTN = findViewById(R.id.login_button_firstactivity);

        createBTN.setOnClickListener((v) -> {
            initEmailValidView();
        });

        loginBTN.setOnClickListener((v) -> {
            initLoginView();
        });
    }

    public void initEmailValidView() {
        backCount = 0;
        setContentView(R.layout.activity_first_email_valid);
        EditText emailET = findViewById(R.id.email_edittext_first_email_valid);
        Button sendBTN = findViewById(R.id.send_button_first_email_valid);
        EditText codeET = findViewById(R.id.code_edittext_first_email_valid);
        TextView countDownTimerTV = findViewById(R.id.timecount_textview_first_email_valid);
        Button nextBTN = findViewById(R.id.next_button_first_email_valid);

        nextBTN.setClickable(false);
        countDownTimerTV.setVisibility(View.INVISIBLE);

        if (email != null) {
            emailET.setText(email);
        }

        emailET.setImeOptions(EditorInfo.IME_ACTION_DONE);
        // Why it's not working?
        emailET.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                nextBTN.performClick();
            }
            return false;
        });

        sendBTN.setOnClickListener((v) -> {
            email = emailET.getText().toString();
            try {
                sendVerificationEmail(email, nextBTN, countDownTimerTV);
//                initCreateUserView(email);
            } catch (NullPointerException e) {
                Toast.makeText(FirstActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        nextBTN.setOnClickListener((v)->{
            verifyEmailAndCode(emailET.getText().toString(), codeET.getText().toString());
        });
    }

    public void sendVerificationEmail(String to, Button nextBTN, TextView countDownTimerTV) {
        Log.d(TAG, "sendVerificationEmail()= to(email): " + email);

        // backCount 세지 않음.
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<BasicResult> call = null;
        try {
            call = remoteService.validateEmail(to);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            call.enqueue(new Callback<BasicResult>() {
                @Override
                public void onResponse(Call<BasicResult> call, Response<BasicResult> response) {
                    Log.d(TAG, "onResponse()");
                    try {
                        BasicResult validResult = response.body();
                        Log.d(TAG, validResult.toString());

                        switch (validResult.getCode()) {
                            case 100:
                                nextBTN.setClickable(true);
                                nextBTN.setBackgroundColor(getResources().getColor(R.color.link_blue));
                                countDownTimerTV.setVisibility(View.VISIBLE);
                                BasicCountDownTimer basicCountDownTimer = BasicCountDownTimer.getInstance(FirstActivity.this);
                                if (!basicCountDownTimer.isTimerRunning()) { // not running. initialize.
                                    basicCountDownTimer.setCountDownTimerFormat("Verify your email in: (", ":", ")");
                                    basicCountDownTimer.setTimeLeftInMilliseconds(60 * 1000 * 3);
                                    basicCountDownTimer.startTimer(countDownTimerTV, nextBTN);
                                } else { // running
                                    basicCountDownTimer.stopTimer();
                                    basicCountDownTimer.setTimeLeftInMilliseconds(60 * 1000 * 3);
                                    basicCountDownTimer.startTimer(countDownTimerTV, nextBTN);
                                }
                                Toast.makeText(FirstActivity.this, validResult.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(FirstActivity.this, validResult.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BasicResult> call, Throwable throwable) {
                    throwable.printStackTrace();
                    Toast.makeText(FirstActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initCreateUserView(String email) {
        Log.d(TAG, "initCreateUserView()= email: " + email);
        backCount = -1;
        setContentView(R.layout.activity_first_create);
        TextView infoTV = findViewById(R.id.info_textview_first_create_activity);
        EditText usernameET = findViewById(R.id.fullname_edittext_first_create_activity);
        EditText passwordET = findViewById(R.id.password_edittext_first_create_activity);

        Button continueBTN = findViewById(R.id.continue_button_first_create_activity);

        infoTV.setText(Html.fromHtml("Your contacts will be periodically synced and stored on instagram servers to help you and others find friends, and to help us provide a better service. To remove contacts, go to Settings and disconnect. <a href=''>Learn More</a>"));

        if (username != null) {
            usernameET.setText(username);
        }

        if (password != null) {
            passwordET.setText(password);
        }

        passwordET.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                continueBTN.performClick();
            }
            return false;
        });

        continueBTN.setOnClickListener((v) -> {
            try {
                username = usernameET.getText().toString();
                try {
                    password = passwordET.getText().toString();
                } catch (NullPointerException e) {
                    Toast.makeText(FirstActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                Toast.makeText(FirstActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }

//            tempPass();

            try {
                SignUpData signUpData = new SignUpData(email, username, password);

                RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

                Call<SignUpResult> call = remoteService.insertMember(signUpData);

                call.enqueue(new Callback<SignUpResult>() {
                    @Override
                    public void onResponse(Call<SignUpResult> call, Response<SignUpResult> response) {
                        SignUpResult signUpResult = response.body();
                        try {
                            Log.d(TAG, signUpResult.toString());
                            switch (signUpResult.code) {
                                case 100:
                                    try {
                                        GlobalUser.getInstance().login(FirstActivity.this, username, password);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(FirstActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                default:
                                    Toast.makeText(FirstActivity.this, signUpResult.code + ": " + signUpResult.message, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(FirstActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpResult> call, Throwable throwable) {
                        throwable.printStackTrace();
                        Toast.makeText(FirstActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(FirstActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void verifyEmailAndCode(String email, String code) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<BasicResult> call = remoteService.verifyEmailAndCode(email, code);
        call.enqueue(new Callback<BasicResult>() {
            @Override
            public void onResponse(Call<BasicResult> call, Response<BasicResult> response) {
                BasicResult basicResult = response.body();

                switch (basicResult.getCode()) {
                    case 100:
                        Toast.makeText(FirstActivity.this, basicResult.getMessage(), Toast.LENGTH_SHORT).show();
                        initCreateUserView(email);
                        break;
                    default:
                        Toast.makeText(FirstActivity.this, basicResult.getCode() + ": " + basicResult.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<BasicResult> call, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(FirstActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initLoginView() {
        backCount = 0;
        setContentView(R.layout.activity_login);

        EditText usernameET = findViewById(R.id.username_edittext_loginactivity);
        EditText passwordET = findViewById(R.id.password_edittext_loginactivity);
        Button loginBTN = findViewById(R.id.login_button_loginactivity);

        loginBTN.setOnClickListener((v) -> {
            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();

            if ( username != null && password != null) {
                GlobalUser.getInstance().login(FirstActivity.this, username, password);
//                tempPass();
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 임피 패스를 위한 메소드
     */
    public void tempPass() {
        // 임시 패스
        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
        GlobalUser.getInstance().setMyId(username);
        startActivity(intent);
        finish();
    }
}