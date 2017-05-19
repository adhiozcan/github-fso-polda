package id.net.iconpln.fso.polda.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.net.iconpln.fso.polda.AppConfig;
import id.net.iconpln.fso.polda.BusProvider;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.adapter.FieldReportAdapter;
import id.net.iconpln.fso.polda.core.auth.UserSession;
import id.net.iconpln.fso.polda.jobscheduler.JobName;
import id.net.iconpln.fso.polda.jobscheduler.job.ServiceManager;
import id.net.iconpln.fso.polda.listener.FetchFilterListener;
import id.net.iconpln.fso.polda.model.Laporan;
import id.net.iconpln.fso.polda.model.LaporanEvent;
import id.net.iconpln.fso.polda.model.LaporanList;
import id.net.iconpln.fso.polda.network.FsoApiClient;
import id.net.iconpln.fso.polda.network.FsoApiListener;
import id.net.iconpln.fso.polda.network.FsoWebSocket;
import id.net.iconpln.fso.polda.network.Param;
import id.net.iconpln.fso.polda.network.RequestServer;
import id.net.iconpln.fso.polda.network.ServiceUrl;
import id.net.iconpln.fso.polda.ui.lantas.InputLaporanActivity;
import id.net.iconpln.fso.polda.ui.lantas.InputLaporanSubmitActivity;
import id.net.iconpln.fso.polda.utils.CommonUtils;
import id.net.iconpln.fso.polda.utils.L;
import id.net.iconpln.fso.polda.utils.LaporanSession;
import id.net.iconpln.fso.polda.utils.QueryUtils;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private View mSearchView;
    private View mFilterView;

    private Spinner              mSpnBulan;
    private Spinner              mSpnTahun;
    private RecyclerView         mRecyclerView;
    private FieldReportAdapter   mAdapter;
    private SwipeRefreshLayout   mSwipeRefresh;
    private EditText             mEdPencarian;
    private FloatingActionButton mFab;

    private List<Laporan> mLaporanList;
    private List<Laporan> mLaporanListTemp;

    private String[] arrayBulan;
    private String[] arrayTahun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_main);
        arrayBulan = getResources().getStringArray(R.array.list_bulan);
        arrayTahun = getResources().getStringArray(R.array.list_tahun);

        /**
         * [Set default configurations] ---------------------------------------------
         */
        PreferenceManager.setDefaultValues(this, R.xml.preferences_petugas, false);

        mToolbar = (Toolbar) findViewById(R.id.appcompat_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FSO Polda Kepri");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }

        //Show notification if MainActivity is called from InputLaporanSubmitActivity
        if (getIntent().getExtras() != null) {
            String message = getIntent().getStringExtra("Message");
            L.d("Message caller -- > " + message);
            if (message.equals(InputLaporanSubmitActivity.class.getSimpleName())) {
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Laporan berhasil diupload", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
        mSpnBulan = (Spinner) findViewById(R.id.spinner_bulan);
        mSpnTahun = (Spinner) findViewById(R.id.spinner_tahun);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        //mBtnLogout = findViewById(R.id.btn_logout);
        mSearchView = findViewById(R.id.container_searchbox);
        mFilterView = findViewById(R.id.container_filter);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mEdPencarian = (EditText) findViewById(R.id.ed_pencarian);
        mEdPencarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdPencarian.setCursorVisible(true);
            }
        });

        int adapterSpinnerItemRegular = R.layout.adapter_item_field_spinner_simple;
        int adapterSpinnerItemChecked = R.layout.adapter_item_field_spinner_simple_checked;

        ArrayAdapter<String> spnBlnAdapter = new ArrayAdapter<>(this, adapterSpinnerItemChecked, arrayBulan);
        spnBlnAdapter.setDropDownViewResource(adapterSpinnerItemRegular);
        mSpnBulan.setAdapter(spnBlnAdapter);
        mSpnBulan.setSelection(0);

        ArrayAdapter<String> spnTahunAdapter = new ArrayAdapter<>(this, adapterSpinnerItemChecked, arrayTahun);
        spnTahunAdapter.setDropDownViewResource(adapterSpinnerItemRegular);
        mSpnTahun.setAdapter(spnTahunAdapter);
        mSpnTahun.setSelection(0);

        mLaporanList = new ArrayList<>();
        mLaporanListTemp = new ArrayList<>();
        mAdapter = new FieldReportAdapter(this, mLaporanList);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(CommonUtils.getVerticalLayoutManager(this));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Make new session or use existing when user creating this laporan
                 */
                LaporanSession.getInstance().createNew();
                L.d("Status Sesi --> " + LaporanSession.getInstance().getStatus());
                startActivity(new Intent(MainActivity.this, InputLaporanActivity.class));
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 * Check Developer Configuration.
                 * This configuration can be update.
                 * For more information, please refer to #AppConfig.Class
                 */
                if (AppConfig.checkMockingMode()) {
                    mSwipeRefresh.setRefreshing(false);
                    return;
                }

                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }

                String userId = UserSession.getUser().getUserId();
                latestLaporan(userId);
            }
        });

        /**
         * Check Developer Configuration.
         * This configuration can be update.
         * For more information, please refer to #AppConfig.Class
         */
        if (AppConfig.checkMockingMode()) {
            mockFieldReportWithDummyData();
            return;
        }

        String userId = UserSession.getUser().getUserId();
        //String bulan  = String.valueOf(Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MONTH) + 1);
        //String tahun  = String.valueOf(Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR));
        latestLaporan(userId);
    }

    private void mockFieldReportWithDummyData() {
        mAdapter.getLaporanList().clear();
        mAdapter.getLaporanList().addAll(QueryUtils.extractDummyLaporan());
        mAdapter.notifyDataSetChanged();
        if (mSwipeRefresh.isRefreshing()) mSwipeRefresh.setRefreshing(false);
    }

    private void latestLaporan(String userId) {
        mSwipeRefresh.setRefreshing(true);

        //Input required parameter
        Map param = new HashMap();
        param.put(Param.LIST_LAPORAN_USER_ID, userId);
        param.put(Param.LIST_LAPORAN_BULAN, "");
        param.put(Param.LIST_LAPORAN_TAHUN, "");

        FsoApiClient client = new RequestServer(ServiceUrl.FETCH_DAFTAR_LAPORAN, param);
        client.execute(new FsoApiListener<LaporanList>() {
            @Override
            public void onResponse(LaporanList response) {
                if (!response.getListLaporan().isEmpty()) {
                    boolean isEquals = mLaporanList.containsAll(response.getListLaporan()) &&
                            response.getListLaporan().containsAll(mLaporanList);
                    if (!isEquals) {
                        mLaporanList.clear();
                        mLaporanList.addAll(response.getListLaporan());
                    }
                    /**
                     * First time fetching will be request usisng current month and year as parameter.
                     * This data will be saved in mLaporanListTemp and this variable
                     * cannot be disturbed by another process.
                     */
                    if (mLaporanListTemp.size() == 0) mLaporanListTemp.addAll(mLaporanList);
                }

                //Collections.reverse(mAdapter.getLaporanList());

                mAdapter.notifyDataSetChanged();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(MainActivity.this, "Tidak dapat memuat data, coba beberapa saat lagi.", Toast.LENGTH_SHORT).show();
                mSwipeRefresh.setRefreshing(false);
                L.d(message);
            }
        });
    }

    private void fetchLaporanFilter(String userId, String bulan, String tahun, final FetchFilterListener fetchListener) {
        mSwipeRefresh.setRefreshing(true);

        //Input required parameter
        Map param = new HashMap();
        param.put(Param.LIST_LAPORAN_USER_ID, userId);
        param.put(Param.LIST_LAPORAN_BULAN, bulan);
        param.put(Param.LIST_LAPORAN_TAHUN, tahun);

        FsoApiClient client = new RequestServer(ServiceUrl.FETCH_DAFTAR_LAPORAN, param);
        client.execute(new FsoApiListener<LaporanList>() {
            @Override
            public void onResponse(LaporanList response) {
                if (!mLaporanList.equals(response.getListLaporan())) {
                    mLaporanList.clear();
                    mLaporanList.addAll(response.getListLaporan());
                }

                L.d("Berhasil memperbarui laporan");
                //Collections.reverse(mAdapter.getLaporanList());

                /**
                 * First time fetching will be request usisng current month and year as parameter.
                 * This data will be saved in mLaporanListTemp and this variable
                 * cannot be disturbed by another process.
                 */
                if (mLaporanListTemp.size() == 0) mLaporanListTemp.addAll(mLaporanList);

                mAdapter.notifyDataSetChanged();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);

                /**
                 * Fill callback carrying data with amount of data has been fetched.
                 */
                fetchListener.onComplete(mLaporanList.size());
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(MainActivity.this, "Tidak dapat memuat data, coba beberapa saat lagi.", Toast.LENGTH_SHORT).show();
                mSwipeRefresh.setRefreshing(false);
                fetchListener.onComplete(0);
                L.d(message);
            }
        });
    }

    @Subscribe
    public void updateUISubscriber(LaporanEvent updateLaporan) {
        boolean isEquals = mLaporanList.containsAll(updateLaporan.laporanList) &&
                updateLaporan.laporanList.containsAll(mLaporanList);
        if (!isEquals) {
            mLaporanList.clear();
            mLaporanListTemp.clear();
            mLaporanList.addAll(updateLaporan.laporanList);
            mLaporanListTemp.addAll(updateLaporan.laporanList);
        }

        if (mFilterView.getVisibility() == View.GONE) {
            mAdapter.notifyDataSetChanged();
            mRecyclerView.getAdapter().notifyDataSetChanged();
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        ServiceManager.register(this, JobName.UPDATE_LAPORAN);
        FsoWebSocket.init();

    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
        ServiceManager.unregister(this, JobName.UPDATE_LAPORAN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_petugas_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_filter:
                toogleFilterView();
                break;
            case R.id.app_bar_search:
                toogleSearchView();
                break;
            case R.id.app_bar_setting:
                startActivity(new Intent(MainActivity.this, PreferenceActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void refreshSwipebility() {
        if (mFilterView.getVisibility() == View.VISIBLE) {
            mSwipeRefresh.setEnabled(false);
        } else {
            mSwipeRefresh.setEnabled(true);
        }
    }

    private List<Laporan> filter(List<Laporan> models, String query) {
        final String        lowerCaseQuery    = query.toLowerCase();
        final List<Laporan> filteredModelList = new ArrayList<>();

        for (Laporan model : models) {
            final String text = model.getJudul().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void toogleFilterView() {
        TextView  foundDataStatus     = (TextView) findViewById(R.id.status_filter_data);
        ViewGroup transitionContainer = (ViewGroup) findViewById(R.id.container_transition);
        TransitionManager.beginDelayedTransition(transitionContainer);
        mFilterView.setVisibility(mFilterView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        foundDataStatus.setVisibility(View.GONE);

        refreshSwipebility();

        if (mFilterView.getVisibility() == View.GONE) {
            mAdapter.getLaporanList().clear();
            mAdapter.getLaporanList().addAll(mLaporanListTemp);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void toogleSearchView() {
        ViewGroup transitionContainer = (ViewGroup) findViewById(R.id.container_transition);
        TransitionManager.beginDelayedTransition(transitionContainer);
        mSearchView.setVisibility(mSearchView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

        if (mSearchView.getVisibility() == View.GONE) {
            mAdapter.getLaporanList().clear();
            mAdapter.getLaporanList().addAll(mLaporanListTemp);
            mAdapter.notifyDataSetChanged();
        } else {
            mEdPencarian.setText("");
        }
    }

    public void onSearchViewClicked(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        mEdPencarian.setCursorVisible(false);
        mEdPencarian.clearFocus();

        String pencarian = mEdPencarian.getText().toString();
        if (pencarian.length() == 0)
            Toast.makeText(this, "Input pencarian masih kosong", Toast.LENGTH_SHORT).show();

        List<Laporan> filteredList = filter(mLaporanListTemp, pencarian);
        mAdapter.getLaporanList().clear();
        mAdapter.getLaporanList().addAll(filteredList);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    public void onSubmitFilterClicked(View view) {
        String userId = UserSession.getUser().getUserId();
        String bulan  = String.valueOf((mSpnBulan.getSelectedItemPosition()));
        String tahun  = mSpnTahun.getSelectedItem().toString();

        if (mSpnBulan.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Filter bulan tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (mSpnTahun.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Filter tahun tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            fetchLaporanFilter(userId, bulan, tahun, new FetchFilterListener() {
                @Override
                public void onComplete(int amountData) {
                    TextView  foundDataStatus     = (TextView) findViewById(R.id.status_filter_data);
                    ViewGroup transitionContainer = (ViewGroup) findViewById(R.id.container_transition);
                    TransitionManager.beginDelayedTransition(transitionContainer);
                    foundDataStatus.setVisibility(View.VISIBLE);
                    if (amountData == 0) {
                        foundDataStatus.setText("Data kosong");
                        foundDataStatus.setBackgroundResource(R.color.button_background_red);
                    } else {
                        foundDataStatus.setText("Menemukan " + amountData + " data");
                        foundDataStatus.setBackgroundResource(R.color.button_background_green);
                    }
                }
            });
        }
    }

    private void userSignOut() {
        UserSession.clearSession();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
