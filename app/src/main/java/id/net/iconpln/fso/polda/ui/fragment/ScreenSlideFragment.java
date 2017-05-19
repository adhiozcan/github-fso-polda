package id.net.iconpln.fso.polda.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.net.iconpln.fso.polda.R;

/**
 * Created by Ozcan createNew 24/11/2016.
 */

public class ScreenSlideFragment extends Fragment {

    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlideFragment create(int pageNumber) {
        ScreenSlideFragment fragment = new ScreenSlideFragment();
        Bundle              args     = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlideFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide, container, false);
        switch (mPageNumber) {
            case 0:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText("Dinas");
                break;
            case 1:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText("Lepas Dinas");
                break;
            case 2:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText("Cadangan");
                break;
            default:
                ((TextView) rootView.findViewById(android.R.id.text1)).setText("Dinas");
                break;
        }

        return rootView;
    }
}
