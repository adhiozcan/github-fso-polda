package id.net.iconpln.fso.polda.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

import id.net.iconpln.fso.polda.R;
import id.net.iconpln.fso.polda.model.Laporan;
import id.net.iconpln.fso.polda.ui.fragment.BuktiFotoFragment;
import id.net.iconpln.fso.polda.ui.fragment.MapInfoFragment;
import id.net.iconpln.fso.polda.ui.lantas.LaporanDetailActivity;
import id.net.iconpln.fso.polda.ui.lantas.TrackReportActivity;
import id.net.iconpln.fso.polda.utils.DateUtils;


/**
 * Created by Ozcan createNew 23/11/2016.
 */

public class FieldReportAdapter extends RecyclerView.Adapter<FieldReportAdapter.ViewHolder> {
    private AppCompatActivity activity;
    private List<Laporan>     laporanList;

    public FieldReportAdapter(AppCompatActivity activity, List<Laporan> laporanList) {
        this.activity = activity;
        this.laporanList = laporanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        //int     itemLayout = R.layout.adapter_item_field_report;
        int  itemLayout = R.layout.adapter_item_field_report_alt_1;
        View view       = LayoutInflater.from(mContext).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Edit circle urgensi
        /*if (laporanList.get(position).getPrioritas() == "1") {
            TextDrawable drawable = TextDrawable.builder().buildRound("P", Color.parseColor("#ffc107"));
            holder.imgUrgensi.setImageDrawable(drawable);
        } else {
            TextDrawable drawable = TextDrawable.builder().buildRound("N", Color.parseColor("#4CAF50"));
            holder.imgUrgensi.setImageDrawable(drawable);
        }*/

        String dateFormat  = "dd/MM/yyyy";
        String dateStr     = laporanList.get(position).getTanggalKejadian();
        Date   date        = DateUtils.stringToDate(dateStr, dateFormat);
        String dateDisplay = DateUtils.changeDateDisplay(date);

        holder.txtTitle.setText(laporanList.get(position).getJudul());
        holder.txtTanggal.setText(dateDisplay);
        holder.txtKeterangan.setText(laporanList.get(position).getUraian());
        holder.txtStatus.setText(laporanList.get(position).getStatus());

        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataInJson = new Gson().toJson(laporanList.get(pos));
                Intent intent     = new Intent(activity, LaporanDetailActivity.class);
                intent.putExtra("Data", dataInJson);
                activity.startActivity(intent);
            }
        });

        holder.viewItemBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment            prev = activity.getSupportFragmentManager().findFragmentByTag("FRAGMENT_BUKTI");
                FragmentTransaction ft   = activity.getSupportFragmentManager().beginTransaction();
                if (prev != null) {
                    ft.remove(prev);
                    ft.commit();
                }
                ft.addToBackStack(null);

                String[] fileImages = new String[6];
                fileImages[0] = laporanList.get(position).getBukti1();
                fileImages[1] = laporanList.get(position).getBukti2();
                fileImages[2] = laporanList.get(position).getBukti3();
                fileImages[3] = laporanList.get(position).getBukti4();
                fileImages[4] = laporanList.get(position).getBukti5();
                fileImages[5] = laporanList.get(position).getBukti6();

                BuktiFotoFragment buktiFotoFragment = BuktiFotoFragment.newInstance(fileImages);
                buktiFotoFragment.show(ft, "FRAGMENT_BUKTI");
            }
        });

        holder.viewItemLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Laporan laporan = laporanList.get(position);
                if (laporan.getKoordinat() != null) {
                    String   latLngInString = laporan.getKoordinat();
                    String[] latLngArray    = latLngInString.split(",");

                    double latitude  = Double.parseDouble(latLngArray[0].trim());
                    double longitude = Double.parseDouble(latLngArray[1].trim());
                    LatLng latLng    = new LatLng(latitude, longitude);

                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);

                    MapInfoFragment mapFragment = MapInfoFragment.create(laporan.getLokasi(), latLng);
                    DialogFragment  df          = mapFragment;
                    df.show(ft, "FRAGMENT_MAP_INFO");
                } else {
                    Toast.makeText(activity, "Data lokasi tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.viewItemLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TrackReportActivity.class);
                intent.putExtra("Prioritas", laporanList.get(position).getPrioritas());
                intent.putExtra("Judul", laporanList.get(position).getJudul());
                intent.putExtra("Pelapor", laporanList.get(position).getPelapor());
                intent.putExtra("Status", laporanList.get(position).getStatus());
                intent.putExtra("IssueId", laporanList.get(position).getIdIssue());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return laporanList.size();
    }

    public List<Laporan> getLaporanList() {
        return this.laporanList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //private ImageView imgUrgensi;
        //private TextView  txtPelapor;
        private TextView txtTitle;
        private TextView txtTanggal;
        private TextView txtKeterangan;
        private TextView txtStatus;

        private View viewItemBukti;
        private View viewItemLokasi;
        private View viewItemLihat;

        public ViewHolder(View itemView) {
            super(itemView);
            //imgUrgensi = (ImageView) itemView.findViewById(R.id.img_urgensi);
            //txtPelapor = (TextView) itemView.findViewById(R.id.txt_pelapor);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_judul);
            txtTanggal = (TextView) itemView.findViewById(R.id.txt_tanggal);
            txtKeterangan = (TextView) itemView.findViewById(R.id.txt_keterangan);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);

            viewItemBukti = itemView.findViewById(R.id.view_item_bukti);
            viewItemLokasi = itemView.findViewById(R.id.view_item_lokasi);
            viewItemLihat = itemView.findViewById(R.id.view_item_lihat);
        }
    }
}
