package id.net.iconpln.fso.polda.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Ozcan createNew 23/11/2016.
 */

public class CommonUtils {

    public static RecyclerView.LayoutManager getVerticalLayoutManager(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

}
