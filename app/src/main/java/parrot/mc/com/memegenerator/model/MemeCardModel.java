package parrot.mc.com.memegenerator.model;

import android.content.Context;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import parrot.mc.com.memegenerator.manager.ApiManager;
import parrot.mc.com.memegenerator.manager.ConfigManager;
import parrot.mc.com.memegenerator.manager.VolleyManager;
import parrot.mc.com.memegenerator.model.data.MemeEntity;

public class MemeCardModel {

    private Context context;
    private ApiManager apiManager;
    private List<IMemeCardModelObserver> observers = new CopyOnWriteArrayList<>();
    private int imageIndex;
    private ConfigManager configManager;

    public MemeCardModel(Context context) {
        this.context = context;
        apiManager = new ApiManager();
        configManager = new ConfigManager();
        imageIndex = 0;
    }

    public void addObserver(IMemeCardModelObserver observer){
        if (observers == null) {
            return;
        }
        observers.add(observer);
    }

    public void removeObserver(IMemeCardModelObserver observer){
        if (observers == null) {
            return;
        }
        observers.remove(observer);
    }

    protected void notifyOnSuccess(List<MemeEntity> memeEntities){
        if (observers == null) {
            return;
        }
        for (IMemeCardModelObserver observer : observers) {
            observer.onSuccess(memeEntities);
        }
    }

    protected void notifyonError(VolleyManager.ErrorState errorMessage){
        if (observers == null) {
            return;
        }
        for (IMemeCardModelObserver observer : observers) {
            observer.onError(errorMessage);
        }
    }

    public void getImageByPopular(){
        apiManager.getImage(context, configManager.getImageByPopularUrl(imageIndex, 10), new ApiManager.onImageResponse() {
            @Override
            public void onSuccess(List<MemeEntity> memeEntities) {
                notifyOnSuccess(memeEntities);
                imageIndex++;
            }

            @Override
            public void onError(VolleyManager.ErrorState errorMessage) {
                notifyonError(errorMessage);
            }
        });
    }

    public void getImageByNew(){
        apiManager.getImage(context, configManager.getImageByNewUrl(imageIndex, 10), new ApiManager.onImageResponse() {
            @Override
            public void onSuccess(List<MemeEntity> memeEntities) {
                notifyOnSuccess(memeEntities);
                imageIndex++;
            }

            @Override
            public void onError(VolleyManager.ErrorState errorMessage) {
                notifyonError(errorMessage);
            }
        });
    }

    public void getImageByTrending(){
        apiManager.getImage(context, configManager.getImageByTrendingUrl(), new ApiManager.onImageResponse() {
            @Override
            public void onSuccess(List<MemeEntity> memeEntities) {
                notifyOnSuccess(memeEntities);
            }

            @Override
            public void onError(VolleyManager.ErrorState errorMessage) {
                notifyonError(errorMessage);
            }
        });
    }

}
