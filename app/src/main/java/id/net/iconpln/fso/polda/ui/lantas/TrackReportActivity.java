package id.net.iconpln.fso.polda.ui.lantas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.net.iconpln.fso.polda.AppConfig;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.adapter.TrackReportAdapter;
import id.net.iconpln.fso.polda.model.TrackReport;
import id.net.iconpln.fso.polda.model.TrackReportList;
import id.net.iconpln.fso.polda.network.FsoApiClient;
import id.net.iconpln.fso.polda.network.FsoApiListener;
import id.net.iconpln.fso.polda.network.Param;
import id.net.iconpln.fso.polda.network.RequestServer;
import id.net.iconpln.fso.polda.network.ServiceUrl;
import id.net.iconpln.fso.polda.utils.CommonUtils;
import id.net.iconpln.fso.polda.utils.L;
import id.net.iconpln.fso.polda.utils.QueryUtils;

/**
 * Created by Ozcan createNew 30/11/2016.
 */

public class TrackReportActivity extends AppCompatActivity {
    private Toolbar            toolbar;
    private RecyclerView       mRecyclerView;
    private TrackReportAdapter mAdapter;
    private List<TrackReport>  mTrackReportList;

    private TextView mTxtPrioritas;
    private TextView mTxtJudul;
    private TextView mTxtPelapor;
    private TextView mTxtStatus;

    private String prioritas;
    private String judul;
    private String pelapor;
    private String status;
    private String issueId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_track_laporan);
        toolbar = (Toolbar) findViewById(R.id.appcompat_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Perkembangan Laporan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mTxtPrioritas = (TextView) findViewById(R.id.txt_prioritas);
        mTxtJudul = (TextView) findViewById(R.id.txt_judul);
        mTxtPelapor = (TextView) findViewById(R.id.txt_pelapor);
        mTxtStatus = (TextView) findViewById(R.id.txt_status);

        if (getIntent().getExtras() != null) {
            prioritas = getIntent().getExtras().getString("Prioritas");
            judul = getIntent().getExtras().getString("Judul");
            pelapor = getIntent().getExtras().getString("Pelapor");
            status = getIntent().getExtras().getString("Status");
            issueId = getIntent().getExtras().getString("IssueId");

            mTxtPrioritas.setText(prioritas);
            mTxtJudul.setText(judul);
            mTxtPelapor.setText("dilaporkan oleh " + pelapor);
            mTxtStatus.setText("Status terakhir " + status);
        }

        mTrackReportList = new ArrayList<>();
        mAdapter = new TrackReportAdapter(mTrackReportList);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(CommonUtils.getVerticalLayoutManager(this));

        if (AppConfig.checkMockingMode()) {
            mTrackReportList.clear();
            mTrackReportList.addAll(QueryUtils.extractDummyTrackReport());
            updateUI();
        } else {
            fetchTrackReport(issueId);
        }

        broadcastIntent();
    }

    private void broadcastIntent() {
        Intent intent = new Intent();
        intent.setAction("id.net.iconpln.fso.polda.FSO_BROADCAST_INTENT");
        sendBroadcast(intent);
    }

    private void fetchTrackReport(String issueId) {
        Map param = new HashMap();
        param.put(Param.LIST_LAPORAN_ISSUE_ID, issueId);

        FsoApiClient client = new RequestServer(ServiceUrl.FETCH_DAFTAR_LAPORAN, param);
        client.execute(new FsoApiListener<TrackReportList>() {
            @Override
            public void onResponse(TrackReportList response) {
                extractTrackReportData(response);
            }

            @Override
            public void onFailed(String message) {
                L.d(message);
                Toast.makeText(TrackReportActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void extractTrackReportData(TrackReportList trackReportList) {
        if (mTrackReportList.size() > 0) mTrackReportList.clear();
        for (TrackReport repo : trackReportList.getTrackReportList()) {
            TrackReport trackReport = new TrackReport();
            trackReport.setTanggal(repo.getTanggal());
            trackReport.setStatus(repo.getStatus());
            mTrackReportList.add(trackReport);
        }
        updateUI();
    }

    private void updateUI() {
        mAdapter.notifyDataSetChanged();
    }
}
