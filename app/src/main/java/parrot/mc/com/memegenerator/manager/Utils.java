package parrot.mc.com.memegenerator.manager;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.Toast;

public class Utils {

    private static final Utils instance = new Utils();

    public static Utils getInstance() {
        return instance;
    }

    public int dpToPx(Context context,int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void makeToast(Context context,String message){
        Toast.makeText(context, message,
                Toast.LENGTH_SHORT).show();
    }

}
