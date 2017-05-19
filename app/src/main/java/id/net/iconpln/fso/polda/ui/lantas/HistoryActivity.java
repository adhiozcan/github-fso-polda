package id.net.iconpln.fso.polda.ui.lantas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import id.net.iconpln.fso.polda.R;

/**
 * Created by Ozcan createNew 25/11/2016.
 */

public class HistoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private boolean isFormShowed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_history_laporan);

        mToolbar = (Toolbar) findViewById(R.id.appcompat_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("History Laporan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            if (isFormShowed) {
                findViewById(R.id.form_pencarian).setVisibility(View.GONE);
            } else {
                findViewById(R.id.form_pencarian).setVisibility(View.VISIBLE);
            }
            isFormShowed = !isFormShowed;
        }
        return super.onOptionsItemSelected(item);
    }
}
