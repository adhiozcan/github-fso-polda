package id.net.iconpln.fso.polda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.model.TrackReport;
import id.net.iconpln.fso.polda.utils.DateUtils;

/**
 * Created by Ozcan createNew 30/11/2016.
 */

public class TrackReportAdapter extends RecyclerView.Adapter<TrackReportAdapter.ViewHolder> {

    private List<TrackReport> trackReportList;

    public TrackReportAdapter(List<TrackReport> trackReportList) {
        this.trackReportList = trackReportList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext   = parent.getContext();
        int     itemLayout = R.layout.adapter_item_field_track_record;
        View    view       = LayoutInflater.from(mContext).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String dateFormat  = "dd/MM/yyyy";
        String dateStr     = trackReportList.get(position).getTanggal();
        Date   date        = DateUtils.stringToDate(dateStr, dateFormat);
        String dateDisplay = DateUtils.changeDateDisplay(date);

        holder.txtTanggal.setText(dateDisplay);
        holder.txtStatus.setText(trackReportList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return trackReportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTanggal;
        private TextView txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTanggal = (TextView) itemView.findViewById(R.id.txt_tanggal);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);
        }
    }
}
