package parrot.mc.com.memegenerator.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import parrot.mc.com.memegenerator.model.data.MemeEntity;


public class ApiManager {

    public ApiManager() {
    }

    public void getImage(Context context, final String url, final onImageResponse callback) {
        new VolleyManager(context).GET(url, new VolleyManager.IVolleyResponse() {
            @Override
            public void onSuccess(JSONObject responseJson) {
                Log.e("onSuccess",responseJson.toString());
                try {
                    if (responseJson.getBoolean("success"))
                        callback.onSuccess(jsonToMeme(responseJson.getJSONArray("result")));
                    else
                        callback.onError(VolleyManager.ErrorState.API_KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyManager.ErrorState errorState) {
                callback.onError(VolleyManager.ErrorState.NETWORK);
            }
        });

    }

    private List<MemeEntity> jsonToMeme(JSONArray responseJson){
        List<MemeEntity> memeEntities = new CopyOnWriteArrayList<>();
        for (int i = 0; i < responseJson.length(); i++) {
            try {
                JSONObject jsonObject = responseJson.getJSONObject(i);
                MemeEntity memeEntity = new MemeEntity(jsonObject.getInt("imageID"),
                        jsonObject.getString("displayName"),
                        jsonObject.getString("imageUrl"),
                        jsonObject.getInt("totalVotesScore"),
                        jsonObject.getInt("instancesCount"));
                memeEntities.add(memeEntity);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return memeEntities;
    }

    public interface onImageResponse {
        void onSuccess(List<MemeEntity> memeEntities);
        void onError(VolleyManager.ErrorState errorMessage);
    }
}
