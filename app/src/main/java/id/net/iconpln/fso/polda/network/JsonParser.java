package id.net.iconpln.fso.polda.network;

import retrofit2.Response;

/**
 * Created by Ozcan on 16/02/2017.
 */

interface JsonParser<E> {
    E parse(Response<E> response);
}
