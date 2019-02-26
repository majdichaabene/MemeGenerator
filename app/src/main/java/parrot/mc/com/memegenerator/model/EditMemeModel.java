package parrot.mc.com.memegenerator.model;

import android.content.Context;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import parrot.mc.com.memegenerator.R;


public class EditMemeModel {

    private List<Integer> colorList;
    private List<Typeface> fontList;
    private Iterator<Integer> colorIterator;
    private Iterator<Typeface> fontIterator;;

    public EditMemeModel(final Context context) {
        colorList = new ArrayList<Integer>() {{
            add(context.getResources().getColor(R.color.white));
            add(context.getResources().getColor(R.color.blue));
            add(context.getResources().getColor(R.color.red));
            add(context.getResources().getColor(R.color.green));
            add(context.getResources().getColor(R.color.yellow));
            add(context.getResources().getColor(R.color.black));
        }};
        fontList = new ArrayList<Typeface>() {{
            add(Typeface.createFromAsset(context.getAssets(),"font/bascula_font.ttf"));
            add(Typeface.createFromAsset(context.getAssets(),"font/betty_font.ttf"));
            add(Typeface.createFromAsset(context.getAssets(),"font/bold_font.ttf"));
            add(Typeface.createFromAsset(context.getAssets(),"font/edosz_font.ttf"));
            add(Typeface.createFromAsset(context.getAssets(),"font/hummel_font.ttf"));
            add(Typeface.createFromAsset(context.getAssets(),"font/minya_font.ttf"));
            add(Typeface.createFromAsset(context.getAssets(),"font/tristar_font.ttf"));
            add(Typeface.createFromAsset(context.getAssets(),"font/saiyan_font.ttf"));
            add(Typeface.createFromAsset(context.getAssets(),"font/pixel_font.ttf"));
        }};
        colorIterator = colorList.iterator();
        fontIterator = fontList.iterator();
    }

    public Iterator<Integer> getColorIterator() {
        return colorIterator;
    }

    public Iterator<Typeface> getFontIterator() {
        return fontIterator;
    }

    public void resetColorIterator(){
        colorIterator = colorList.listIterator();
    }

    public void resetFontIterator(){
        fontIterator = fontList.listIterator();
    }
}
