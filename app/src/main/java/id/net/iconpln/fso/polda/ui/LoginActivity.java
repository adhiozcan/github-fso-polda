package id.net.iconpln.fso.polda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import id.net.iconpln.fso.polda.AppConfig;
import id.net.iconpln.fso.polda.AppPreference;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.core.auth.LoginManager;
import id.net.iconpln.fso.polda.core.auth.UserSession;
import id.net.iconpln.fso.polda.model.UserProfileResponse;
import id.net.iconpln.fso.polda.network.FsoApiListener;
import id.net.iconpln.fso.polda.network.Param;
import id.net.iconpln.fso.polda.ui.fragment.ScreenSlideFragment;
import id.net.iconpln.fso.polda.ui.lantas.DashboardActivity;
import id.net.iconpln.fso.polda.utils.L;

/**
 * Created by Ozcan createNew 22/11/2016.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText  edUser;
    private EditText  edPassword;
    private View      chevRight;
    private View      chevLeft;
    private ViewPager viewPager;

    private String statusDinas;

    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserSetting();
        setContentView(R.layout.activity_login);

        edUser = (EditText) findViewById(R.id.txt_user_id);
        edPassword = (EditText) findViewById(R.id.txt_user_password);

        avi = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.canScrollHorizontally(-1);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        statusDinas = "Dinas";
                        break;
                    case 1:
                        statusDinas = "Lepas Dinas";
                        break;
                    case 2:
                        statusDinas = "Cadangan";
                        break;
                    default:
                        statusDinas = "Dinas";
                }
            }
        });

        chevRight = findViewById(R.id.swipe_right);
        chevRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        chevLeft = findViewById(R.id.swipe_left);
        chevLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

    }

    private void checkUserSetting() {
        if (AppPreference.isAlwaysRemember()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    /**
     * Function for get always remember value and set as configuration in AppPreference.class
     *
     * @param view
     * @{@link id.net.iconpln.fso.polda.AppPreference}.
     */
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            AppPreference.saveAlwaysRemember(true);
        } else {
            AppPreference.saveAlwaysRemember(false);
        }
    }

    public void attempLogin(View view) {
        /**
         * Check Developer Configuration.
         * This configuration can be update.
         * For more information, please refer to #AppConfig.Class
         */
        if (AppConfig.checkByPassLogin()) {
            loginSuccessfully();
            finish();
            return;
        }

        String username = edUser.getText().toString();
        String password = edPassword.getText().toString();
        doLogin(username, password);
    }

    private void doLogin(final String username, String password) {
        /**
         * First check point, process will continue when field is not empty.
         * If username or password is not already set, then
         * show notification to ask user fill the form.
         */
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username / Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            loginSuccessfully();
            return;
        }

        avi.smoothToShow();

        Map param = new HashMap();
        param.put(Param.LOGIN_USERNAME, username);
        param.put(Param.LOGIN_PASSWORD, password);

        FsoApiListener<UserProfileResponse> loginCallback = new FsoApiListener<UserProfileResponse>() {
            @Override
            public void onResponse(UserProfileResponse response) {
                if (statusDinas == null || statusDinas.isEmpty()) statusDinas = "Dinas";
                UserSession.saveUser(response.getUserProfile());
                L.d(UserSession.getUser().toString());
                loginSuccessfully();
                avi.smoothToHide();
                Log.d("Login Message", "Login message --> success");
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(LoginActivity.this, "Username / Password tidak cocok", Toast.LENGTH_SHORT).show();
                avi.smoothToHide();
                Log.d("Login Message", "Login message --> " + message);
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            }
        };

        LoginManager loginManager = new LoginManager.Builder()
                .withParam(param)
                .provideListener(loginCallback)
                .build();

        loginManager.login();
    }

    private void loginSuccessfully() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private final int NUM_PAGES = 3;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("Fragment Position -->", "getItem: " + position);
            return ScreenSlideFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
