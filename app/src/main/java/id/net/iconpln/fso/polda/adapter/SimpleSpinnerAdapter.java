package id.net.iconpln.fso.polda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.net.iconpln.fso.polda.R;

/**
 * Created by Ozcan on 11/01/2017.
 */

public class SimpleSpinnerAdapter extends RecyclerView.Adapter<SimpleSpinnerAdapter.ViewHolder> {

    private List<String> spinnerList;

    public SimpleSpinnerAdapter(List<String> spinnerList) {
        this.spinnerList = spinnerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext   = parent.getContext();
        int     itemLayout = R.layout.adapter_item_field_spinner_simple;
        View    view       = LayoutInflater.from(mContext).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
