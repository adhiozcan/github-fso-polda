package id.net.iconpln.fso.polda.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.utils.L;
import id.net.iconpln.fso.polda.utils.camera.PhotoUtils;

/**
 * Created by Ozcan createNew 29/11/2016.
 */

public class BuktiFotoFragment extends DialogFragment implements View.OnClickListener {

    private View mButtonClosed;

    private ImageView bukti1;
    private ImageView bukti2;
    private ImageView bukti3;
    private ImageView bukti4;
    private ImageView bukti5;
    private ImageView bukti6;

    private String[] fileBukti;

    public static BuktiFotoFragment newInstance(String[] fileImages) {

        Bundle args = new Bundle();
        args.putString("Bukti_1", fileImages[0]);
        args.putString("Bukti_2", fileImages[1]);
        args.putString("Bukti_3", fileImages[2]);
        args.putString("Bukti_4", fileImages[3]);
        args.putString("Bukti_5", fileImages[4]);
        args.putString("Bukti_6", fileImages[5]);

        BuktiFotoFragment fragment = new BuktiFotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bukti_foto, container, false);
        if (fileBukti == null) {
            fileBukti = new String[6];
        }
        fileBukti[0] = getArguments().getString("Bukti_1");
        fileBukti[1] = getArguments().getString("Bukti_2");
        fileBukti[2] = getArguments().getString("Bukti_3");
        fileBukti[3] = getArguments().getString("Bukti_4");
        fileBukti[4] = getArguments().getString("Bukti_5");
        fileBukti[5] = getArguments().getString("Bukti_6");

        for (String image : fileBukti) {
            L.d("Image : " + image);
        }

        mButtonClosed = view.findViewById(R.id.closed_dialog);
        bukti1 = (ImageView) view.findViewById(R.id.photo1);
        bukti2 = (ImageView) view.findViewById(R.id.photo2);
        bukti3 = (ImageView) view.findViewById(R.id.photo3);
        bukti4 = (ImageView) view.findViewById(R.id.photo4);
        bukti5 = (ImageView) view.findViewById(R.id.photo5);
        bukti6 = (ImageView) view.findViewById(R.id.photo6);

        mButtonClosed.setOnClickListener(this);
        bukti1.setOnClickListener(this);
        bukti2.setOnClickListener(this);
        bukti3.setOnClickListener(this);
        bukti4.setOnClickListener(this);
        bukti5.setOnClickListener(this);
        bukti6.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllPhotos();
    }

    private void getAllPhotos() {
        PhotoUtils.Download.getAndSet(getActivity(), fileBukti[0], bukti1);
        PhotoUtils.Download.getAndSet(getActivity(), fileBukti[1], bukti2);
        PhotoUtils.Download.getAndSet(getActivity(), fileBukti[2], bukti3);
        PhotoUtils.Download.getAndSet(getActivity(), fileBukti[3], bukti4);
        PhotoUtils.Download.getAndSet(getActivity(), fileBukti[4], bukti5);
        PhotoUtils.Download.getAndSet(getActivity(), fileBukti[5], bukti6);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo1:
                PhotoUtils.Common.showImage((AppCompatActivity) getActivity(), fileBukti[0]);
                break;
            case R.id.photo2:
                PhotoUtils.Common.showImage((AppCompatActivity) getActivity(), fileBukti[1]);
                break;
            case R.id.photo3:
                PhotoUtils.Common.showImage((AppCompatActivity) getActivity(), fileBukti[2]);
                break;
            case R.id.photo4:
                PhotoUtils.Common.showImage((AppCompatActivity) getActivity(), fileBukti[3]);
                break;
            case R.id.photo5:
                PhotoUtils.Common.showImage((AppCompatActivity) getActivity(), fileBukti[4]);
                break;
            case R.id.photo6:
                PhotoUtils.Common.showImage((AppCompatActivity) getActivity(), fileBukti[5]);
                break;
            default:
                closedDialog();
                break;
        }
    }

    private void closedDialog() {
        dismiss();
    }
}
