package id.net.iconpln.fso.polda.ui.lantas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import id.net.iconpln.fso.polda.AppConfig;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.model.BasicResponse;
import id.net.iconpln.fso.polda.model.DataInputResponse;
import id.net.iconpln.fso.polda.network.FsoApiClient;
import id.net.iconpln.fso.polda.network.FsoApiListener;
import id.net.iconpln.fso.polda.network.Param;
import id.net.iconpln.fso.polda.network.RequestServer;
import id.net.iconpln.fso.polda.network.ServiceUrl;
import id.net.iconpln.fso.polda.ui.MainActivity;
import id.net.iconpln.fso.polda.utils.L;
import id.net.iconpln.fso.polda.utils.LaporanSession;
import id.net.iconpln.fso.polda.utils.camera.PhotoUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Ozcan createNew 29/11/2016.
 */

public class InputLaporanSubmitActivity extends AppCompatActivity {
    private ImageView mSignalIndicator;
    private Toolbar   mToolbar;

    private TextView mTxtPrioritas;
    private TextView mTxtJudul;
    private TextView mTxtTanggal;
    private TextView mTxtLokasi;
    private TextView mTxtKoordinat;
    private TextView mTxtPelaku;
    private TextView mTxtUraian;
    private TextView mTxtPelapor;
    private TextView mTxtImages;
    private View     btnKirim;
    AVLoadingIndicatorView avi;

    String mPrioritas;

    String   mJudul;
    String   mTglKejadian;
    String   mWaktuKejadian;
    String   mLokasi;
    String   mKoordinat;
    String   mPelaku;
    String   mUraian;
    String   mPelaporId;
    String   mPelaporNama;
    String[] mImages;
    int      mImagesSum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_input_laporan_submit);

        mSignalIndicator = (ImageView) findViewById(R.id.signal_indicator);
        mToolbar = (Toolbar) findViewById(R.id.appcompat_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Summary");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (getIntent().getExtras() != null) {
            mPrioritas = getIntent().getStringExtra("Prioritas");
            mJudul = getIntent().getStringExtra("Judul");
            mTglKejadian = getIntent().getStringExtra("Tanggal");
            mWaktuKejadian = getIntent().getStringExtra("Waktu");
            mLokasi = getIntent().getStringExtra("Lokasi");
            mKoordinat = getIntent().getStringExtra("Koordinat");
            mPelaku = getIntent().getStringExtra("Pelaku");
            mUraian = getIntent().getStringExtra("Uraian");
            mPelaporId = getIntent().getStringExtra("IdPelapor");
            mPelaporNama = getIntent().getStringExtra("NamaPelapor");
            mImages = getIntent().getStringArrayExtra("Images");

            if (mImages != null) {
                for (String image : mImages) {
                    System.out.println("Images : " + image);
                }
            }
        }

        avi = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);
        mTxtPrioritas = (TextView) findViewById(R.id.txt_prioritas);
        mTxtJudul = (TextView) findViewById(R.id.txt_judul);
        mTxtTanggal = (TextView) findViewById(R.id.txt_tanggal_kejadian);
        mTxtLokasi = (TextView) findViewById(R.id.txt_lokasi);
        mTxtKoordinat = (TextView) findViewById(R.id.txt_koordinat);
        mTxtPelaku = (TextView) findViewById(R.id.txt_pelaku);
        mTxtUraian = (TextView) findViewById(R.id.txt_uraian);
        mTxtPelapor = (TextView) findViewById(R.id.txt_pelapor);
        mTxtImages = (TextView) findViewById(R.id.txt_images);
        btnKirim = findViewById(R.id.btn_kirim_laporan);

        mTxtPrioritas.setText(mPrioritas);
        mTxtJudul.setText(mJudul);
        mTxtTanggal.setText(mTglKejadian + " " + mWaktuKejadian);
        mTxtLokasi.setText(mLokasi);
        mTxtKoordinat.setText(mKoordinat);
        mTxtPelaku.setText(mPelaku);
        mTxtUraian.setText(mUraian);
        mTxtPelapor.setText("dilaporkan oleh " + mPelaporNama);

        if (mImages != null) {
            String fileListName = "";
            mImagesSum = 0;
            for (int i = 0; i < mImages.length; i++) {
                if (mImages[i] != null) {
                    fileListName = fileListName + Uri.parse(mImages[i]).getLastPathSegment() + "\n";
                    mTxtImages.setText(fileListName);
                    mImagesSum++;
                }
            }
        }

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToServer();
            }
        });
    }

    private void sendToServer() {
        /**
         * -----------------------------------------------------------------------------------
         * Check Developer Configuration.
         * This configuration can be update.
         * For more information, please refer to #AppConfig.Class
         */
        if (AppConfig.checkUploadPhotoOnly()) {
            testUploadPhoto("2");
            return;
        }
        // -----------------------------------------------------------------------------------

        if (mPrioritas == "Penting") {
            mPrioritas = "1";
        } else {
            mPrioritas = "0";
        }
        Map newLaporan = new HashMap();
        newLaporan.put(Param.POST_LAPORAN_PRIORITAS, mPrioritas);
        newLaporan.put(Param.POST_LAPORAN_JUDUL, mJudul);
        newLaporan.put(Param.POST_LAPORAN_TANGGAL_KEJADIAN, mTglKejadian);
        newLaporan.put(Param.POST_LAPORAN_WAKTU_KEJADIAN, mWaktuKejadian);
        newLaporan.put(Param.POST_LAPORAN_LOKASI, mLokasi);
        newLaporan.put(Param.POST_LAPORAN_KOORDINAT, mKoordinat);
        newLaporan.put(Param.POST_LAPORAN_PELAKU, mPelaku);
        newLaporan.put(Param.POST_LAPORAN_URAIAN, mUraian);
        newLaporan.put(Param.POST_LAPORAN_PELAPOR, mPelaporId);

        FsoApiClient fsoApiClient = new RequestServer(ServiceUrl.INPUT_LAPORAN, newLaporan);
        fsoApiClient.execute(new FsoApiListener<DataInputResponse>() {
            @Override
            public void onResponse(DataInputResponse response) {
                String _id = response.getResponse().getIssueId();
                testUploadPhoto(_id);
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(InputLaporanSubmitActivity.this, "Terjadi gangguan, coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testUploadPhoto(String issueId) {
        for (int i = 0; i < mImagesSum; i++) {
            Uri                fileImage = Uri.parse(mImages[i]);
            RequestBody        id        = RequestBody.create(MediaType.parse("multipart/form-data"), issueId);
            RequestBody        fileName  = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage.getLastPathSegment());
            RequestBody        imageCol  = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(i + 1));
            MultipartBody.Part image     = PhotoUtils.Upload.prepareImageForUpload(this, fileImage);

            Map newImage = new HashMap();
            newImage.put(Param.POST_LAPORAN_IMAGE_LAPORAN_ID, id);
            newImage.put(Param.POST_LAPORAN_NAMA_PHOTO, fileName);
            newImage.put(Param.POST_LAPORAN_IMAGE_PHOTO_SN, imageCol);
            newImage.put(Param.POST_LAPORAN_FILE_PHOTO, image);

            FsoApiClient client = new RequestServer(ServiceUrl.INPUT_LAPORAN_BUKTI, newImage);
            client.execute(new FsoApiListener<BasicResponse>() {
                @Override
                public void onResponse(BasicResponse response) {
                    /**
                     * Make finish laporan session and clean all form captured
                     */
                    LaporanSession.getInstance().cleanFormInput();
                    LaporanSession.getInstance().finish();

                    finish();

                    Intent intent = new Intent(InputLaporanSubmitActivity.this, MainActivity.class);
                    intent.putExtra("Message", InputLaporanSubmitActivity.class.getSimpleName());
                    startActivity(intent);

                    Toast.makeText(InputLaporanSubmitActivity.this, "Laporan berhasil terkirim", Toast.LENGTH_SHORT).show();
                    L.d("Input Laporan Berhasil");
                }

                @Override
                public void onFailed(String message) {
                    Toast.makeText(InputLaporanSubmitActivity.this, "Laporan tidak terkirim. Error code [Fail 405]", Toast.LENGTH_SHORT).show();
                    L.d("Failure ---> " + message);
                }
            });
        }
    }
}
