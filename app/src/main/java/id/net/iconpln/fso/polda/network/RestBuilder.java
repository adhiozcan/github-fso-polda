package id.net.iconpln.fso.polda.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ozcan createNew 24/11/2016.
 */

public class RestBuilder {
    private static RestBuilder restBuilder;
    private        Retrofit    retrofit;

    private RestBuilder() {
    }

    /**
     * Provide RestBuilder instance running with singleton method
     *
     * @return RestBuilder instance
     */
    private static RestBuilder restBuilderInstance() {
        if (restBuilder == null) {
            restBuilder = new RestBuilder();
            restBuilder.retrofit = retrofitBuilder().client(okHttpClient()).build();
        }
        return restBuilder;
    }

    /**
     * Create new Retrofit.Builder instance with certain configuration
     */
    private static Retrofit.Builder retrofitBuilder() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    /**
     * Create new OkHttpClient as client with logging functionality
     **/
    private static OkHttpClient okHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    /**
     * Get RestBuilder instance containing retrofit to get api service
     */
    public static <E> E getApiService(Class<E> serviceClass) {
        return restBuilderInstance().retrofit.create(serviceClass);
    }
}
