package id.net.iconpln.fso.polda.utils;

import java.util.ArrayList;
import java.util.List;

import id.net.iconpln.fso.polda.model.Laporan;
import id.net.iconpln.fso.polda.model.TrackReport;

/**
 * Created by Ozcan on 06/01/2017.
 */

public class QueryUtils {

    /**
     * Provide mocking data for laporan
     */
    public static List<Laporan> extractDummyLaporan() {
        List<Laporan> laporan    = new ArrayList<>();
        String        loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam sed ipsum tempus, dignissim neque id, ornare est.";
        for (int i = 0; i < 10; i++) {
            Laporan ilap = new Laporan();
            ilap.setIdIssue(String.valueOf(i));
            ilap.setJudul("Kasus " + (i + 1));
            ilap.setStatus("Status : Laporan");
            ilap.setTanggalKejadian("24/12/2016");
            ilap.setPrioritas("Penting");
            ilap.setLokasi("Jl. Trunojoyo, Komplek Blok-M, Jakarta Selatan");
            ilap.setPelaku("John Doe");
            ilap.setUraian(loremIpsum);
            ilap.setKoordinat("1.161790, 104.120062");
            ilap.setStatus("Lapor");
            ilap.setPelapor("Johny English");
            laporan.add(ilap);
        }
        return laporan;
    }

    public static List<TrackReport> extractDummyTrackReport() {
        TrackReport tr1 = new TrackReport();
        tr1.setStatus("Laporan");
        tr1.setTanggal("1/11/2016");
        tr1.setNamaLaporan("Kasus 1");
        tr1.setKeterangan("Laporan telah terkirim");

        TrackReport tr2 = new TrackReport();
        tr2.setStatus("Tindak Lanjut");
        tr2.setTanggal("1/11/2016");
        tr2.setNamaLaporan("Kasus 1");
        tr2.setKeterangan("Sedang ditindaklanjuti oleh petugas");

        TrackReport tr3 = new TrackReport();
        tr3.setStatus("Penugasan Regu");
        tr3.setTanggal("1/11/2016");
        tr3.setNamaLaporan("Kasus 1");
        tr3.setKeterangan("Laporan telah diteruskan ke regu");

        TrackReport tr4 = new TrackReport();
        tr4.setStatus("Perjalanan");
        tr4.setTanggal("1/11/2016");
        tr4.setNamaLaporan("Kasus 1");
        tr4.setKeterangan("Regu dalam perjalanan");

        TrackReport tr5 = new TrackReport();
        tr5.setStatus("Penanganan");
        tr5.setTanggal("1/11/2016");
        tr5.setNamaLaporan("Kasus 1");
        tr5.setKeterangan("Regu telah sampai di lokasi perjalanan");

        List<TrackReport> trackReportList = new ArrayList<>();
        trackReportList.add(tr1);
        trackReportList.add(tr2);
        trackReportList.add(tr3);
        trackReportList.add(tr4);
        trackReportList.add(tr5);
        return trackReportList;
    }
}
