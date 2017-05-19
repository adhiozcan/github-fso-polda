package id.net.iconpln.fso.polda.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import id.net.iconpln.fso.polda.model.UserProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Created by Ozcan on 10/02/2017.
 */
public class DispatcherTest {
    private FsoApiService fsoApiService;

    @Before
    public void setUp() throws Exception {
        System.out.println("Setup environment");
        fsoApiService = RestBuilder.getApiService(FsoApiService.class);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Cleaning up");
    }

    @Test
    public void testArrayListShowingPrettyInConsole() {
        ArrayList<String> al1 = new ArrayList<>();
        al1.add("Hello");
        al1.add("World");
        System.out.println("Value in an array : " + al1);
        assertNotNull(al1);

        List<String> al2 = new ArrayList<>();
        al2.add("Jimmy");
        al2.add("Fallon");
        System.out.println(al2);
        assertNotNull(al2);
    }

    @Test
    public void loginUser() throws Exception {
        fsoApiService.loginUser("polisi", "qwert").enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                assertNotNull(response);
                response.body().data.setUserId(null);
                assertNotNull(response.body().data.getUserId());
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());
            }
        });
    }

}