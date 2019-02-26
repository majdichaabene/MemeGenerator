package parrot.mc.com.memegenerator.manager;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageManager {

    private static final StorageManager instance = new StorageManager();

    public static StorageManager getInstance() {
        return instance;
    }

    public void saveImage(Bitmap finalBitmap,IStorageManager callBack) {

        try {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            Date mDate = new Date();
            SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");
            FileOutputStream fos = null;
            fos = new FileOutputStream(new File(root, fileName.format(mDate) + ".png"));
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            callBack.onSuccess();
        } catch (Exception e) {
            callBack.onError(e.toString());
        }
    }
    public interface IStorageManager {
        void onSuccess();
        void onError(String errorState);
    }
}
