package id.net.iconpln.fso.polda.ui.lantas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.net.iconpln.fso.polda.AppConfig;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.core.auth.UserSession;
import id.net.iconpln.fso.polda.model.Laporan;
import id.net.iconpln.fso.polda.model.TahapanResponse;
import id.net.iconpln.fso.polda.network.FsoApiClient;
import id.net.iconpln.fso.polda.network.FsoApiListener;
import id.net.iconpln.fso.polda.network.Param;
import id.net.iconpln.fso.polda.network.RequestServer;
import id.net.iconpln.fso.polda.network.ServiceUrl;
import id.net.iconpln.fso.polda.ui.fragment.BuktiFotoFragment;
import id.net.iconpln.fso.polda.ui.fragment.MapInfoFragment;
import id.net.iconpln.fso.polda.utils.L;

/**
 * Created by Ozcan createNew 29/11/2016.
 */

public class LaporanDetailActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private final int TAHAP_LAPOR      = 1;
    private final int TAHAP_PERJALANAN = 2;
    private final int TAHAP_PENANGANAN = 3;

    private boolean PENANGANAN_UNSUR_PIDANA = false;

    /**
     * Val 0 --> FirstBottomSheetDatePicker
     * Val 1 --> SecondBottomSheetDatePicker
     */
    private int DATEPICKER_REQUEST = 0;

    private Toolbar  mToolbar;
    private TextView txtJudul;
    private TextView txtPelapor;
    private TextView txtStatus;
    private TextView txtPrioritas;
    private TextView txtTanggal;
    private TextView txtLokasi;
    private TextView txtPelaku;
    private TextView txtUraian;
    private TextView txtKeterangan;
    private EditText edDatePenyelidikan;
    private EditText edDatePenyidikan;
    private Spinner  spinnerEskalasi;

    private View btnMaps;
    private View btnImages;
    private View btnProgress;

    private BottomSheetDatePickerDialog mFirstDatePicker;
    private BottomSheetDatePickerDialog mSecondDatePicker;

    private View panelTerimaTugas;
    private View panelDilokasi;
    private View panelKejadian;
    private View panelUnsurPidana;
    private View panelPelidikan;
    private View panelPenyidikan;
    private View panelSelesai;
    private View panelSelesaiAdaPidana;

    private Laporan laporan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_laporan_detail);
        mToolbar = (Toolbar) findViewById(R.id.appcompat_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Laporan Lapangan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (getIntent().getExtras() != null) {
            String data = getIntent().getStringExtra("Data");
            L.d("Laporan Detail", "onCreate: " + data);
            laporan = new Gson().fromJson(data, Laporan.class);
            L.d("Laporan Detail", "onCreate: " + laporan.getIdIssue());
        }

        txtJudul = (TextView) findViewById(R.id.txt_judul);
        txtPelapor = (TextView) findViewById(R.id.txt_pelapor);
        txtStatus = (TextView) findViewById(R.id.txt_status);
        txtPrioritas = (TextView) findViewById(R.id.txt_prioritas);
        txtTanggal = (TextView) findViewById(R.id.txt_tanggal_kejadian);
        txtLokasi = (TextView) findViewById(R.id.txt_lokasi);
        txtPelaku = (TextView) findViewById(R.id.txt_pelaku);
        txtUraian = (TextView) findViewById(R.id.txt_uraian);
        txtKeterangan = (TextView) findViewById(R.id.txt_keterangan);
        edDatePenyelidikan = (EditText) findViewById(R.id.ed_date_penyelidikan);
        edDatePenyidikan = (EditText) findViewById(R.id.ed_date_penyidikan);
        panelTerimaTugas = findViewById(R.id.panel_terima_tugas);
        panelDilokasi = findViewById(R.id.panel_tiba_dilokasi);
        panelKejadian = findViewById(R.id.panel_kejadian);
        panelUnsurPidana = findViewById(R.id.panel_unsur_pidana);
        panelPelidikan = findViewById(R.id.panel_pelidikan);
        panelPenyidikan = findViewById(R.id.panel_penyidikan);
        panelSelesai = findViewById(R.id.panel_tugas_selesai);
        panelSelesaiAdaPidana = findViewById(R.id.panel_selesai_dengan_penyidikan);
        btnMaps = findViewById(R.id.btn_maps);
        btnImages = findViewById(R.id.btn_image);
        btnMaps = findViewById(R.id.btn_maps);
        btnProgress = findViewById(R.id.btn_progress);

        txtJudul.setText(laporan.getJudul());
        txtPelapor.setText(UserSession.getUser().getNamaLengkap());
        txtStatus.setText(laporan.getStatus());
        txtPrioritas.setText(laporan.getPrioritas());
        txtTanggal.setText(laporan.getTanggalKejadian());
        txtLokasi.setText(laporan.getLokasi());
        txtPelaku.setText(laporan.getPelaku());
        txtUraian.setText(laporan.getUraian());

        String[] listEskalasi = {"Default", "Laka Lantas", "Laka Trouble Spot", "Binmas", "Sabhara", "Intelkam", "Polair"};
        spinnerEskalasi = (Spinner) findViewById(R.id.spinner_eskalasi);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.adapter_item_field_spinner_simple, listEskalasi);
        adapter.setDropDownViewResource(R.layout.adapter_item_field_spinner_simple);
        spinnerEskalasi.setAdapter(adapter);

        Calendar now = Calendar.getInstance();
        mFirstDatePicker = BottomSheetDatePickerDialog.newInstance(
                LaporanDetailActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        mSecondDatePicker = BottomSheetDatePickerDialog.newInstance(
                LaporanDetailActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);

                if (laporan.getKoordinat() != null) {
                    String latLngInString = laporan.getKoordinat();
                    Log.d("LaporanDetail", "onClick: " + latLngInString);
                    String[] latLngArray = latLngInString.split(",");

                    double latitude  = Double.parseDouble(latLngArray[0].trim());
                    double longitude = Double.parseDouble(latLngArray[1].trim());
                    LatLng latLng    = new LatLng(latitude, longitude);

                    MapInfoFragment mapFragment = MapInfoFragment.create(laporan.getLokasi(), latLng);
                    DialogFragment  df          = mapFragment;
                    df.show(ft, "FRAGMENT_MAPS_INFO");
                } else {
                    Toast.makeText(LaporanDetailActivity.this, "Data mLokasi tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment            prev = getSupportFragmentManager().findFragmentByTag("FRAGMENT_BUKTI");
                FragmentTransaction ft   = getSupportFragmentManager().beginTransaction();
                if (prev != null) {
                    ft.remove(prev);
                    ft.commit();
                }
                ft.addToBackStack(null);

                String[] fileImages = new String[6];
                fileImages[0] = laporan.getBukti1();
                fileImages[1] = laporan.getBukti2();
                fileImages[2] = laporan.getBukti3();
                fileImages[3] = laporan.getBukti4();
                fileImages[4] = laporan.getBukti5();
                fileImages[5] = laporan.getBukti6();

                BuktiFotoFragment buktiFotoFragment = BuktiFotoFragment.newInstance(fileImages);
                buktiFotoFragment.show(ft, "FRAGMENT_BUKTI");
            }
        });

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LaporanDetailActivity.this, TrackReportActivity.class));
            }
        });

        matchingLaporanStatus();
    }

    private void matchingLaporanStatus() {
        if (laporan.getStatus() == null) return;

        String statusLaporan = laporan.getStatus().toLowerCase().trim();
        L.d("Status Laporan --> " + statusLaporan);
        switch (statusLaporan) {
            case "lapor":
                L.d("True Cuk");
                konfirmasiLapor();
                break;
            case "penugasan regu":
                konfirmasiLapor();
                break;
            case "dalam perjalanan":
                konfirmasiJenisKejadian();
                break;
            case "dalam penanganan":
                konfirmasiJenisKejadian();
                break;
            case "dalam pelidikan":
                konfirmasiPenyidikan();
                break;
            case "dalam penyidikan":
                PENANGANAN_UNSUR_PIDANA = false;
                konfirmasiSelesai();
                break;
            case "selesai":
                findViewById(R.id.txt_message_selesai).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_versa_selesai).setVisibility(View.GONE);
                break;
            default:
                konfirmasiLapor();
                break;
        }
    }

    private void konfirmasiLapor() {
        panelTerimaTugas.setVisibility(View.VISIBLE);
        konfirmasiTerimaPenugasan();
    }

    private void konfirmasiTerimaPenugasan() {
        View btnTerimaTugas = findViewById(R.id.btn_versa_terima_tugas);
        btnTerimaTugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConfig.checkUpdateTahapanAlwaysTrue()) {
                    konfirmasiPenangananLokasi();
                    return;
                }

                updateTahapanOnline(TAHAP_PERJALANAN, new FsoApiListener<TahapanResponse>() {
                    @Override
                    public void onResponse(TahapanResponse response) {
                        konfirmasiPenangananLokasi();
                    }

                    @Override
                    public void onFailed(String message) {
                        Toast.makeText(LaporanDetailActivity.this, "Gagal update laporan saat ini", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void konfirmasiPenangananLokasi() {
        panelTerimaTugas.setVisibility(View.GONE);
        panelDilokasi.setVisibility(View.VISIBLE);

        txtStatus.setText("Dalam Perjalanan");

        if (AppConfig.checkUpdateTahapanAlwaysTrue()) {
            konfirmasiJenisKejadian();
            return;
        }

        updateTahapanOnline(TAHAP_PENANGANAN, new FsoApiListener<TahapanResponse>() {
            @Override
            public void onResponse(TahapanResponse response) {
                konfirmasiJenisKejadian();
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(LaporanDetailActivity.this, "Gagal update laporan saat ini", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void konfirmasiJenisKejadian() {
        panelDilokasi.setVisibility(View.GONE);
        panelKejadian.setVisibility(View.VISIBLE);

        txtStatus.setText("Dalam Penanganan");

        View btnJenisKejadian = findViewById(R.id.btn_versa_keadaan);
        btnJenisKejadian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConfig.checkUpdateTahapanAlwaysTrue()) {
                    int checkId = ((RadioGroup) findViewById(R.id.rg_jenis_kejadian)).getCheckedRadioButtonId();
                    if (checkId == R.id.rb_lakalantas) {
                        PENANGANAN_UNSUR_PIDANA = true;
                        konfirmasiUnsurPidana();
                    } else {
                        PENANGANAN_UNSUR_PIDANA = false;
                        konfirmasiSelesai();
                    }
                    return;
                }

                RadioGroup rgJenisKejadian = (RadioGroup) findViewById(R.id.rg_jenis_kejadian);

                int checkId = rgJenisKejadian.getCheckedRadioButtonId();
                if (checkId == R.id.rb_lakalantas) {
                    PENANGANAN_UNSUR_PIDANA = true;
                    updateTahapanOnlinePenanganan(1, new FsoApiListener<TahapanResponse>() {
                        @Override
                        public void onResponse(TahapanResponse response) {
                            konfirmasiUnsurPidana();
                        }

                        @Override
                        public void onFailed(String message) {
                            Toast.makeText(LaporanDetailActivity.this, "Gagal update laporan saat ini", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if (checkId == R.id.rb_troublespot) {
                    int jenisKejadian = 0;
                    updateTahapanOnlinePenanganan(jenisKejadian, new FsoApiListener<TahapanResponse>() {
                        @Override
                        public void onResponse(TahapanResponse response) {
                            PENANGANAN_UNSUR_PIDANA = false;
                            konfirmasiSelesai();
                        }

                        @Override
                        public void onFailed(String message) {
                            Toast.makeText(LaporanDetailActivity.this, "Gagal update laporan saat ini", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LaporanDetailActivity.this, "Pilih opsi jenis kejadian", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void konfirmasiUnsurPidana() {
        panelKejadian.setVisibility(View.GONE);
        panelUnsurPidana.setVisibility(View.VISIBLE);

        final View       optionDisposisi = findViewById(R.id.panel_option_disposisi);
        final RadioGroup rgUnsurPidana   = (RadioGroup) findViewById(R.id.rg_unsur_pidana);
        View             btnUnsurPidana  = findViewById(R.id.btn_versa_unsur_pidana);

        rgUnsurPidana.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_pidana_ya) {
                    optionDisposisi.setVisibility(View.VISIBLE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rb_pidana_tidak) {
                    optionDisposisi.setVisibility(View.GONE);
                }
            }
        });

        btnUnsurPidana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rgUnsurPidana.getCheckedRadioButtonId() == R.id.rb_pidana_ya) {
                    konfirmasiPelidikan();
                } else {
                    PENANGANAN_UNSUR_PIDANA = false;
                    konfirmasiSelesai();
                }
            }
        });
    }

    private void konfirmasiPelidikan() {
        panelUnsurPidana.setVisibility(View.GONE);
        panelPelidikan.setVisibility(View.VISIBLE);

        View btnPelidikan = findViewById(R.id.btn_versa_pelidikan_selesai);
        btnPelidikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfirmasiPenyidikan();
            }
        });
    }

    private void konfirmasiPenyidikan() {
        panelPelidikan.setVisibility(View.GONE);
        panelPenyidikan.setVisibility(View.VISIBLE);

        View btnPenyidikan = findViewById(R.id.btn_versa_penyidikan_selesai);
        btnPenyidikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfirmasiSelesai();
            }
        });
    }

    private void konfirmasiSelesai() {
        panelDilokasi.setVisibility(View.GONE);
        panelKejadian.setVisibility(View.GONE);
        panelUnsurPidana.setVisibility(View.GONE);
        panelPenyidikan.setVisibility(View.GONE);
        panelSelesai.setVisibility(View.VISIBLE);

        if (PENANGANAN_UNSUR_PIDANA) {
            panelSelesaiAdaPidana.setVisibility(View.VISIBLE);
        } else {
            panelSelesaiAdaPidana.setVisibility(View.GONE);
        }

        View btnSelesai = findViewById(R.id.btn_versa_selesai);
        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTahapanSelesai();
            }
        });
    }

    private void updateTahapanOnline(int tahapan, final FsoApiListener<TahapanResponse> updateTahapStatus) {
        Map param = new HashMap<>();
        param.put(Param.POST_TAHAP_LAPORAN_ISSUE_ID, laporan.getIdIssue());
        param.put(Param.POST_TAHAP_LAPORAN_USER_ID, UserSession.getUser().getUserId());
        param.put(Param.POST_TAHAP_LAPORAN_KETERANGAN, txtKeterangan.getText().toString());
        param.put(Param.POST_TAHAP_LAPORAN_TAHAPAN, tahapan);

        FsoApiClient client = new RequestServer(ServiceUrl.UPDATE_TAHAP_PERJALANAN, param);
        client.execute(new FsoApiListener<TahapanResponse>() {
            @Override
            public void onResponse(TahapanResponse response) {
                updateTahapStatus.onResponse(response);
                Toast.makeText(LaporanDetailActivity.this, "Status telah terupdate", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String message) {
                updateTahapStatus.onFailed(message);
                Toast.makeText(LaporanDetailActivity.this, "Gagal mengirimkan update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTahapanOnlinePenanganan(int jenisKejadian, final FsoApiListener<TahapanResponse> updateTahapStatus) {
        Map param = new HashMap();
        param.put(Param.POST_TAHAP_LAPORAN_ISSUE_ID, laporan.getIdIssue());
        param.put(Param.POST_TAHAP_LAPORAN_USER_ID, UserSession.getUser().getUserId());
        param.put(Param.POST_TAHAP_LAPORAN_JENIS_KEJADIAN, jenisKejadian);
        param.put(Param.POST_TAHAP_LAPORAN_KETERANGAN, "");

        FsoApiClient client = new RequestServer(ServiceUrl.UPDATE_TAHAP_PENANGANAN, param);
        client.execute(new FsoApiListener<TahapanResponse>() {
            @Override
            public void onResponse(TahapanResponse response) {
                updateTahapStatus.onResponse(response);
                Toast.makeText(LaporanDetailActivity.this, "Status telah terupdate", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String message) {
                updateTahapStatus.onFailed(message);
            }
        });
    }

    private void updateTahapanSelesai() {

        if (AppConfig.checkUpdateTahapanAlwaysTrue()) {
            txtStatus.setText("Selesai");
            findViewById(R.id.txt_message_selesai).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_versa_selesai).setVisibility(View.GONE);
            return;
        }

        Map param = new HashMap<>();
        param.put(Param.POST_TAHAP_LAPORAN_ISSUE_ID, laporan.getIdIssue());
        param.put(Param.POST_TAHAP_LAPORAN_USER_ID, UserSession.getUser().getUserId());
        param.put(Param.POST_TAHAP_LAPORAN_KETERANGAN, txtKeterangan.getText().toString());
        param.put(Param.POST_TAHAP_LAPORAN_TAHAPAN, "5");
        param.put(Param.POST_TAHAP_LAPORAN_SELESAI_TANGGAL_LIDIK, "2016-10-1");
        param.put(Param.POST_TAHAP_LAPORAN_SELESAI_TANGGAL_SIDIK, "2016-10-30");
        param.put(Param.POST_TAHAP_LAPORAN_SELESAI_TANGGAL_BERKAS, "2016-12-1");

        FsoApiClient client = new RequestServer(ServiceUrl.UPDATE_TAHAP_SELESAI, param);
        client.execute(new FsoApiListener<TahapanResponse>() {
            @Override
            public void onResponse(TahapanResponse response) {
                txtStatus.setText("Selesai");
                findViewById(R.id.txt_message_selesai).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_versa_selesai).setVisibility(View.GONE);
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(LaporanDetailActivity.this, "Gagal update laporan ini", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        String mDateSelected = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        switch (DATEPICKER_REQUEST) {
            case 0:
                edDatePenyelidikan.setText(mDateSelected);
                break;
            case 1:
                edDatePenyidikan.setText(mDateSelected);
                break;
        }
    }

    public void openDatePickerDialog(View view) {
        switch (view.getId()) {
            case R.id.action_date_penyelidikan:
                DATEPICKER_REQUEST = 0;
                mFirstDatePicker.show(getSupportFragmentManager(), "DATE_PENYELIDIKAN");
                break;

            case R.id.action_date_penyidikan:
                DATEPICKER_REQUEST = 1;
                mSecondDatePicker.show(getSupportFragmentManager(), "DATE_PENYIDIKAN");
                break;
        }

    }
}
