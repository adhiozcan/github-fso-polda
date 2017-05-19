package id.net.iconpln.fso.polda.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import id.net.iconpln.fso.polda.utils.L;

/**
 * Created by Ozcan on 01/03/2017.
 */

public class SocketEntity {
    /**
     * {
     * "prop":"Received",
     * "etype":"runFunction",
     * "value":[{
     * "method":"GUESTTGK",
     * "rows":1,
     * "content":[{
     * "TGK":"Anda memiliki Tunggakan sebanyak 1 lembar"
     * }]
     * }]
     * }
     */
    @SerializedName("prop")
    private String   properties;
    @SerializedName("etype")
    private String   type;
    @SerializedName("value")
    private Values[] values;

    @Override
    public String toString() {
        return "SocketEntity{" +
                "properties='" + properties + '\'' +
                ", type='" + type + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Values[] getValues() {
        return values;
    }

    public void setValues(Values[] values) {
        this.values = values;
    }

    public class Values {
        @SerializedName("method")
        private String    method;
        @SerializedName("rows")
        private String    rows;
        @SerializedName("content")
        private Content[] contents;

        @Override
        public String toString() {
            return "Values{" +
                    "contents=" + Arrays.toString(contents) +
                    ", rows='" + rows + '\'' +
                    ", method='" + method + '\'' +
                    '}';
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getRows() {
            return rows;
        }

        public void setRows(String rows) {
            this.rows = rows;
        }

        public Content[] getContents() {
            return contents;
        }

        public void setContents(Content[] contents) {
            this.contents = contents;
        }

        public class Content {
            @SerializedName("TGK")
            private String tunggakan;

            public String getTunggakan() {
                return tunggakan;
            }

            public void setTunggakan(String tunggakan) {
                this.tunggakan = tunggakan;
            }
        }
    }

    public void prettyPrintSocketEntity() {
        L.d("[ Result ] --------------------------------------------");
        L.d(toString());
        L.d(getValues()[0].getMethod());
        L.d(getValues()[0].getRows());
        L.d(getValues()[0].getContents()[0].getTunggakan());
    }
}
