package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ozcan createNew 23/11/2016.
 */

public class UserProfile {
    @SerializedName("userId")
    private String user_id;
    @SerializedName("userNama")
    private String nama;
    @SerializedName("namaLkp")
    private String nama_lengkap;
    @SerializedName("pangkat")
    private String jabatan;
    @SerializedName("unit")
    private String unit;
    @SerializedName("dirId")
    private String direktorat_id;
    @SerializedName("direktorat")
    private String direktorat;
    @SerializedName("roles")
    private String roles;

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaLengkap() {
        return nama_lengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.nama_lengkap = namaLengkap;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDirektoratId() {
        return direktorat_id;
    }

    public void setDirektoratId(String direktoratId) {
        this.direktorat_id = direktoratId;
    }

    public String getDirektorat() {
        return direktorat;
    }

    public void setDirektorat(String direktorat) {
        this.direktorat = direktorat;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "user_id='" + user_id + '\'' +
                ", nama='" + nama + '\'' +
                ", nama_lengkap='" + nama_lengkap + '\'' +
                ", jabatan='" + jabatan + '\'' +
                ", unit='" + unit + '\'' +
                ", direktorat_id='" + direktorat_id + '\'' +
                ", direktorat='" + direktorat + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
