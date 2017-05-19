package id.net.iconpln.fso.polda;

import com.squareup.otto.Bus;

/**
 * Created by Ozcan on 26/01/2017.
 */

public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
    }
}
