package id.net.iconpln.fso.polda.ui.lantas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;
import com.philliphsu.bottomsheetpickers.time.grid.GridTimePickerDialog;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.Calendar;

import id.net.iconpln.fso.polda.BusProvider;
import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.AppPreference;
import id.net.iconpln.fso.polda.core.auth.UserSession;
import id.net.iconpln.fso.polda.jobscheduler.JobName;
import id.net.iconpln.fso.polda.model.SignalEvent;
import id.net.iconpln.fso.polda.jobscheduler.job.ServiceManager;
import id.net.iconpln.fso.polda.listener.MapsCompleteListener;
import id.net.iconpln.fso.polda.model.Laporan;
import id.net.iconpln.fso.polda.ui.fragment.MapInputFragment;
import id.net.iconpln.fso.polda.utils.CameraUtils;
import id.net.iconpln.fso.polda.utils.ConnectivityUtils;
import id.net.iconpln.fso.polda.utils.L;
import id.net.iconpln.fso.polda.utils.LaporanSession;
import id.net.iconpln.fso.polda.utils.camera.PhotoUtils;

/**
 * Created by Ozcan createNew 24/11/2016.
 */

public class InputLaporanActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        BottomSheetTimePickerDialog.OnTimeSetListener,
        MapsCompleteListener, View.OnClickListener {

    public static final int CAMERA_REQUEST_CODE_1 = 100;
    public static final int CAMERA_REQUEST_CODE_2 = 200;
    public static final int CAMERA_REQUEST_CODE_3 = 300;
    public static final int CAMERA_REQUEST_CODE_4 = 400;
    public static final int CAMERA_REQUEST_CODE_5 = 500;
    public static final int CAMERA_REQUEST_CODE_6 = 600;
    public static final int GALLERY_REQUEST_CODE  = 200;

    private static int REQUEST_CHOOSED = 0;

    private ImageView mSignalIndicator;
    private Toolbar   mToolbar;
    private TextView  mTxtPrioritas;
    private EditText  edJudul;
    private EditText  edLokasi;
    private EditText  edKoordinat;
    private EditText  edPelaku;
    private EditText  edUraian;
    private EditText  edTanggalKejadian;
    private EditText  edWaktuKejadian;
    private ImageView imgPhoto1;
    private ImageView imgPhoto2;
    private ImageView imgPhoto3;
    private ImageView imgPhoto4;
    private ImageView imgPhoto5;
    private ImageView imgPhoto6;

    private String mDateSelected;
    private String mTimeSelected;

    private LatLng mLatLngFound;

    private BottomSheetDatePickerDialog mBottomDatePicker;
    private GridTimePickerDialog        mBottomTimePicker;

    private String mPhotoPath;
    private Uri    mFileUri;
    private Uri[] mFileUriList = new Uri[6];

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        if (ConnectivityUtils.isHaveInternetConnection())
            ServiceManager.register(this, JobName.UPDATE_SIGNAL_INDICATOR);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
        ServiceManager.unregister(this, JobName.UPDATE_SIGNAL_INDICATOR);
    }

    @Subscribe
    public void updateUISignalIndicator(SignalEvent signalEvent) {
        L.d("[Signal Info Received] Signal indicator is "
                + signalEvent.signalIndicator + " "
                + signalEvent.colorId);
        int colorResId = ContextCompat.getColor(this, signalEvent.colorId);
        mSignalIndicator.setColorFilter(colorResId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas_input_laporan);

        mSignalIndicator = (ImageView) findViewById(R.id.signal_indicator);
        mToolbar = (Toolbar) findViewById(R.id.appcompat_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Input Laporan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        Calendar now = Calendar.getInstance();
        mBottomDatePicker = BottomSheetDatePickerDialog.newInstance(
                InputLaporanActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        mBottomTimePicker = GridTimePickerDialog.newInstance(
                InputLaporanActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(InputLaporanActivity.this));

        edJudul = (EditText) findViewById(R.id.txt_judul);
        edTanggalKejadian = (EditText) findViewById(R.id.ed_date);
        edWaktuKejadian = (EditText) findViewById(R.id.ed_time);
        mTxtPrioritas = (TextView) findViewById(R.id.txt_prioritas);
        edLokasi = (EditText) findViewById(R.id.ed_lokasi);
        edKoordinat = (EditText) findViewById(R.id.ed_koordinat);
        edPelaku = (EditText) findViewById(R.id.ed_pelaku);
        edUraian = (EditText) findViewById(R.id.ed_uraian);
        imgPhoto1 = (ImageView) findViewById(R.id.photo1);
        imgPhoto2 = (ImageView) findViewById(R.id.photo2);
        imgPhoto3 = (ImageView) findViewById(R.id.photo3);
        imgPhoto4 = (ImageView) findViewById(R.id.photo4);
        imgPhoto5 = (ImageView) findViewById(R.id.photo5);
        imgPhoto6 = (ImageView) findViewById(R.id.photo6);

        findViewById(R.id.action_date).setOnClickListener(this);
        findViewById(R.id.action_time).setOnClickListener(this);
        findViewById(R.id.txt_prioritas).setOnClickListener(this);
        findViewById(R.id.action_maps).setOnClickListener(this);
        findViewById(R.id.btn_selanjutnya).setOnClickListener(this);

        if (LaporanSession.getInstance().getStatus() == false) {
            //Make new session when user creating this laporan
            LaporanSession.getInstance().createNew();
        } else {
            //Return user input data on last remaining session
            if (LaporanSession.getInstance().getLastInput() != null) {
                Laporan laporan = LaporanSession.getInstance().getLastInput();
                setRetainLaporanInputValue(laporan);
            }
        }

        //Extract currently date component
        int defaultYear  = now.get(Calendar.YEAR);
        int defaultMonth = now.get(Calendar.MONTH) + 1;
        int defaultDay   = now.get(Calendar.DAY_OF_MONTH);

        //Extract currently time component
        int defaultHour   = now.get(Calendar.HOUR_OF_DAY);
        int defaultMinute = now.get(Calendar.MINUTE);

        mDateSelected = defaultYear + "-" + defaultMonth + "-" + defaultDay;
        mTimeSelected = defaultHour + ":" + defaultMinute;

        if (String.valueOf(defaultMinute).length() == 1) {
            mTimeSelected = defaultHour + ":0" + defaultMinute;
        }

        if (String.valueOf(defaultHour).length() == 1) {
            mTimeSelected = "0" + defaultHour + ":" + defaultMinute;
        }

        edTanggalKejadian.setText(mDateSelected);
        edWaktuKejadian.setText(mTimeSelected);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_date:
                mBottomDatePicker.show(getSupportFragmentManager(), "DATE");
                break;
            case R.id.action_time:
                mBottomTimePicker.show(getSupportFragmentManager(), "TIME");
                break;
            case R.id.txt_prioritas:
                openProritasDialog();
                break;
            case R.id.action_maps:
                if (mLatLngFound != null) {
                    openMapFragment(mLatLngFound);
                } else {
                    openMapFragment(null);
                }
                break;
            case R.id.btn_selanjutnya:
                previewLaporanBeforeSubmit();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        mDateSelected = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        edTanggalKejadian.setText(mDateSelected);
    }

    @Override
    public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {
        mTimeSelected = hourOfDay + ":" + minute;
        edWaktuKejadian.setText(mTimeSelected);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE_1:
                    if (mPhotoPath != null) {
                        PhotoUtils.Common.setOptimumPict(mPhotoPath, imgPhoto1);
                        mFileUriList[0] = mFileUri;
                        imgPhoto1.setTag(mFileUri);
                    }
                    break;

                case CAMERA_REQUEST_CODE_2:
                    PhotoUtils.Common.setOptimumPict(mPhotoPath, imgPhoto2);
                    mFileUriList[1] = mFileUri;
                    imgPhoto1.setTag(mFileUri);
                    break;

                case CAMERA_REQUEST_CODE_3:
                    PhotoUtils.Common.setOptimumPict(mPhotoPath, imgPhoto3);
                    mFileUriList[2] = mFileUri;
                    imgPhoto1.setTag(mFileUri);
                    break;

                case CAMERA_REQUEST_CODE_4:
                    PhotoUtils.Common.setOptimumPict(mPhotoPath, imgPhoto4);
                    mFileUriList[3] = mFileUri;
                    imgPhoto1.setTag(mFileUri);
                    break;

                case CAMERA_REQUEST_CODE_5:
                    PhotoUtils.Common.setOptimumPict(mPhotoPath, imgPhoto5);
                    mFileUriList[4] = mFileUri;
                    imgPhoto1.setTag(mFileUri);
                    break;

                case CAMERA_REQUEST_CODE_6:
                    PhotoUtils.Common.setOptimumPict(mPhotoPath, imgPhoto6);
                    mFileUriList[5] = mFileUri;
                    imgPhoto1.setTag(mFileUri);

                    break;

                default:
                    break;
            }

            /**
             * Auto broadcast the photo into album based on configuration.
             * @link AppPreference.class
             */
            if (AppPreference.isAutoPhotoAlbum())
                PhotoUtils.Common.galleryAddPic(InputLaporanActivity.this, mPhotoPath);

            mPhotoPath = null;
            mFileUri = null;
        }
    }

    @Override
    public void onComplete(LatLng latLng, String locationAddress) {
        mLatLngFound = latLng;
        edKoordinat.setText(latLng.latitude + ", " + latLng.longitude);
        edLokasi.setText(locationAddress);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /**
         * Delete current session
         */
        LaporanSession.getInstance().finish();
    }

    private void openGallery() {
        // Pilih gambar dari gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        REQUEST_CHOOSED = GALLERY_REQUEST_CODE;
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void setRetainLaporanInputValue(Laporan laporan) {
        edJudul.setText(laporan.getJudul());
        edTanggalKejadian.setText(laporan.getTanggalKejadian());
        edWaktuKejadian.setText(laporan.getWaktuKejadian());
        mTxtPrioritas.setText(laporan.getPrioritas());
        edLokasi.setText(laporan.getLokasi());
        edKoordinat.setText(laporan.getKoordinat());
        edPelaku.setText(laporan.getPelaku());
        edUraian.setText(laporan.getUraian());
        L.d("Data in sesi --> " + laporan.toString());
    }

    public void takePicture(View viewId) {
        if (CameraUtils.canOpenCamera(InputLaporanActivity.this))
            switch (viewId.getId()) {
                case R.id.photo1:
                    //captureImage(CAMERA_REQUEST_CODE_1);
                    dispatchTakePictureIntent(CAMERA_REQUEST_CODE_1);
                    break;
                case R.id.photo2:
                    dispatchTakePictureIntent(CAMERA_REQUEST_CODE_2);
                    break;
                case R.id.photo3:
                    dispatchTakePictureIntent(CAMERA_REQUEST_CODE_3);
                    break;
                case R.id.photo4:
                    dispatchTakePictureIntent(CAMERA_REQUEST_CODE_4);
                    break;
                case R.id.photo5:
                    dispatchTakePictureIntent(CAMERA_REQUEST_CODE_5);
                    break;
                case R.id.photo6:
                    dispatchTakePictureIntent(CAMERA_REQUEST_CODE_6);
                    break;
            }
    }

    private void dispatchTakePictureIntent(int sequenceCamera) {
        REQUEST_CHOOSED = sequenceCamera;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File f = PhotoUtils.Common.setUpPhotoFile();

            if (null == f) {
                Toast.makeText(this, "Gagal mengakses camera dan storage Anda", Toast.LENGTH_SHORT).show();
                return;
            }

            mPhotoPath = f.getAbsolutePath();
            mFileUri = Uri.fromFile(f);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
            startActivityForResult(takePictureIntent, sequenceCamera);
        }
    }

    public void openMapFragment(LatLng latLng) {
        FragmentTransaction ft   = getSupportFragmentManager().beginTransaction();
        Fragment            prev = getSupportFragmentManager().findFragmentByTag("MapsFragment");
        if (prev != null) {
            ft.remove(prev);
            ft.commit();
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        MapInputFragment mapFragment = null;

        if (latLng != null) {
            Bundle bundle = new Bundle();
            bundle.putString("Latitude", String.valueOf(latLng.latitude));
            bundle.putString("Longitude", String.valueOf(latLng.longitude));
            mapFragment = MapInputFragment.getInstance(bundle, this);
        } else {
            mapFragment = MapInputFragment.getInstance(null, this);
        }

        if (null == mapFragment) {
            DialogFragment df = mapFragment;
            df.show(ft, "MapsFragment");
        } else {
            Toast.makeText(this, "Tidak dapat mengakses map, coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
        }
    }

    private void openProritasDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater            inflater      = getLayoutInflater();

        final View layout = inflater.inflate(R.layout.layout_pilih_prioritas, (ViewGroup) findViewById(R.id.layout_root));

        final AlertDialog dialog = dialogBuilder.setView(layout).create();
        dialog.show();

        TextView txtNormal  = (TextView) layout.findViewById(R.id.txt_normal);
        TextView txtPenting = (TextView) layout.findViewById(R.id.txt_penting);

        txtNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtPrioritas.setText("Normal");
                dialog.dismiss();
            }
        });

        txtPenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtPrioritas.setText("Penting");
                dialog.dismiss();
            }
        });
    }

    private boolean validasiFormInput() {
        if (edJudul.getText().length() < 1) {
            Toast.makeText(this, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mTxtPrioritas.getText().toString().equals("Pilih Prioritas")) {
            Toast.makeText(this, "Anda belum memilih prioritas laporan", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    private void previewLaporanBeforeSubmit() {
        if (!validasiFormInput()) return;
        String prioritas     = mTxtPrioritas.getText().toString();
        String judul         = edJudul.getText().toString();
        String tglKejadian   = mDateSelected;
        String waktuKejadian = mTimeSelected;
        String lokasi        = edLokasi.getText().toString();
        String koordinat     = edKoordinat.getText().toString();
        String pelaku        = edPelaku.getText().toString();
        String uraian        = edUraian.getText().toString();
        String idPelapor     = UserSession.getUser().getUserId();
        String namaPelapor   = UserSession.getUser().getNamaLengkap();

        Intent intent = new Intent(this, InputLaporanSubmitActivity.class);
        intent.putExtra("Prioritas", prioritas);
        intent.putExtra("Judul", judul);
        intent.putExtra("Tanggal", tglKejadian);
        intent.putExtra("Waktu", waktuKejadian);
        intent.putExtra("Lokasi", lokasi);
        intent.putExtra("Koordinat", koordinat);
        intent.putExtra("Pelaku", pelaku);
        intent.putExtra("Uraian", uraian);
        intent.putExtra("IdPelapor", idPelapor);
        intent.putExtra("NamaPelapor", namaPelapor);
        String images[] = new String[6];

        if (mFileUriList != null) {
            int i = 0;
            for (Uri uri : mFileUriList) {
                if (uri != null) {
                    images[i] = String.valueOf(uri);
                    L.d("Catch Images : " + images[i]);
                }
                i++;
            }
            intent.putExtra("Images", images);
        }

        L.d("Prioritas : " + prioritas);
        L.d("Judul : " + judul);
        L.d("Tanggal : " + tglKejadian);
        L.d("Lokasi : " + lokasi + " | " + koordinat);
        L.d("Pelaku : " + pelaku);
        L.d("Uraian : " + uraian);
        L.d("ID Pelapor : " + idPelapor);
        L.d("Nama Pelapor : " + namaPelapor);

        Laporan laporan = new Laporan();
        laporan.setJudul(judul);
        laporan.setPrioritas(prioritas);
        laporan.setTanggalKejadian(tglKejadian);
        laporan.setLokasi(lokasi);
        laporan.setKoordinat(koordinat);
        laporan.setPelaku(pelaku);
        laporan.setUraian(uraian);
        laporan.setPelapor(idPelapor);
        laporan.setBukti1(images[0]);
        laporan.setBukti2(images[1]);
        laporan.setBukti3(images[2]);
        laporan.setBukti4(images[3]);
        laporan.setBukti5(images[4]);
        laporan.setBukti6(images[5]);

        /**
         * Save all form input to make available in across activity
         */
        LaporanSession.getInstance().captureFormInput(laporan);
        L.d("After Capture --> " + LaporanSession.getInstance().getLastInput().toString());
        startActivity(intent);
    }


}
