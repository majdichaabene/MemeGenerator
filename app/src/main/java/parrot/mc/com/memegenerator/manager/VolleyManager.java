package parrot.mc.com.memegenerator.manager;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyManager {

    private Context context;
    public static final int TIMEOUT_MS = 5000;

    public VolleyManager(Context context) {
        this.context = context;
    }

    public enum ErrorState {
        UNKNOWN,
        NETWORK,
        API_KEY
    }

    public void GET(String url, final IVolleyResponse callBack){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(ErrorState.UNKNOWN);
            }

        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
    public interface IVolleyResponse {
        void onSuccess(JSONObject responseJson);
        void onError(ErrorState errorState);
    }

}
