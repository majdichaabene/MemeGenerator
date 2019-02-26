package parrot.mc.com.memegenerator.model;

import java.util.List;

import parrot.mc.com.memegenerator.manager.ApiManager;
import parrot.mc.com.memegenerator.manager.VolleyManager;
import parrot.mc.com.memegenerator.model.data.MemeEntity;


public interface IMemeCardModelObserver {
    void onSuccess(List<MemeEntity> memeEntities);
    void onError(VolleyManager.ErrorState errorMessage);
}
