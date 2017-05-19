package id.net.iconpln.fso.polda.model;

/**
 * Created by Ozcan on 14/02/2017.
 */

public class SignalEvent {
    public int signalIndicator;
    public int colorId;

    public SignalEvent(int signalIndicator, int colorId) {
        this.signalIndicator = signalIndicator;
        this.colorId = colorId;
    }
}
