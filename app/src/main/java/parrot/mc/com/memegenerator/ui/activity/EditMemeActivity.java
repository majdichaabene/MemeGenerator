package parrot.mc.com.memegenerator.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import parrot.mc.com.memegenerator.R;
import parrot.mc.com.memegenerator.manager.StorageManager;
import parrot.mc.com.memegenerator.manager.Utils;
import parrot.mc.com.memegenerator.model.EditMemeModel;

public class EditMemeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgEditMeme;
    private Button btnCancel,btnDone;
    private ImageButton btnBack,btnShare,btnSave,btnClear,btnFont,btnColor,btnIncrease,btnDecrease;
    private EditText txtComment;
    private InputMethodManager inputMethodManager;
    private RelativeLayout layEditMemeContainer;
    private FrameLayout layEditPanelContainer;
    private LinearLayout layExportPanelContainer;
    private boolean isEditTextAdded;
    private float dX, dY,fontSize;
    private EditMemeModel memeEditSettingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meme);
        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        Bundle bundle = getIntent().getExtras();

        initVar();
        initView(bundle.getString("image_url"));
        setEvent();
//        setupUI(findViewById(R.id.main_layout));
    }
    private void initVar(){
        memeEditSettingModel = new EditMemeModel(this);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        isEditTextAdded = false;
        fontSize = 18;
    }
    private void initView(String imgUrl){
        layEditMemeContainer = findViewById(R.id.edit_meme_container_lay);
        layEditPanelContainer = findViewById(R.id.edit_panel_container_lay);
        layExportPanelContainer = findViewById(R.id.export_panel_container_lay);
        imgEditMeme = findViewById(R.id.meme_edit_img);
        btnBack = findViewById(R.id.back_btn);
        btnCancel = findViewById(R.id.cancel_btn);
        btnDone = findViewById(R.id.done_btn);
        btnShare = findViewById(R.id.share_btn);
        btnClear = findViewById(R.id.clear_btn);
        btnColor = findViewById(R.id.color_btn);
        btnFont = findViewById(R.id.font_btn);
        btnSave = findViewById(R.id.save_btn);
        btnIncrease = findViewById(R.id.increase_btn);
        btnDecrease = findViewById(R.id.decrease_btn);

        Picasso.get()
                .load(imgUrl)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .into(imgEditMeme);
    }

    private void setEvent(){
        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnFont.setOnClickListener(this);
        btnColor.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        btnDecrease.setOnClickListener(this);
    }

    private void setEditTextEvent(){
        txtComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        activeEditMode(false);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        activeEditMode(false);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (!isEditTextAdded){
            generateEditText();
            setEditTextEvent();
        }
        return false;
    }

    private void generateEditText(){
        isEditTextAdded = true;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtComment = new EditText(this);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        txtComment.setBackgroundResource(R.color.transparent);
        txtComment.setLayoutParams(params);
        txtComment.requestFocus();
        txtComment.setHintTextColor(getResources().getColor(R.color.black));
        txtComment.setHint("Start Typing");
        txtComment.setTextSize(fontSize);
        txtComment.setTypeface(Typeface.createFromAsset(getAssets(),"font/pixel_font.ttf"));
        layEditMemeContainer.addView(txtComment);
        layEditPanelContainer.setVisibility(View.VISIBLE);
        activeEditMode(true);
    }


    private void activeEditMode(boolean isEditMode){
        if (isEditMode){
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            btnCancel.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
            layExportPanelContainer.setVisibility(View.GONE);
        } else {
            txtComment.clearFocus();
            inputMethodManager.hideSoftInputFromWindow(txtComment.getWindowToken(), 0);
            btnCancel.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
            layExportPanelContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.share_btn:
                layEditMemeContainer.setDrawingCacheEnabled(true);
                Bitmap bitmap1 = Bitmap.createBitmap(layEditMemeContainer.getDrawingCache());
                shareMeme(bitmap1);
                break;
            case R.id.save_btn:
                layEditMemeContainer.setDrawingCacheEnabled(true);
                Bitmap bitmap2 = Bitmap.createBitmap(layEditMemeContainer.getDrawingCache());
                StorageManager.getInstance().saveImage(bitmap2, new StorageManager.IStorageManager() {
                    @Override
                    public void onSuccess() {
                        Utils.getInstance().makeToast(getApplicationContext(),"imaged saved");
                    }

                    @Override
                    public void onError(String errorState) {
                        Utils.getInstance().makeToast(getApplicationContext(),"error while saving the image");
                    }
                });
                break;
            case R.id.clear_btn:
                activeEditMode(false);
                clearTxtComment();
                break;
            case R.id.font_btn:
                updateTxtCommentFont();
                break;
            case R.id.color_btn:
                updateTxtCommentColor();
                break;
            case R.id.done_btn:
                activeEditMode(false);
                if (txtComment.getText().toString().equals(""))
                    clearTxtComment();
                txtComment.setBackgroundResource(R.color.transparent);
                txtComment.setFocusable(false);
                break;
            case R.id.cancel_btn:
                activeEditMode(false);
                clearTxtComment();
                break;
            case R.id.increase_btn:
                updateTextSize(true);
                break;
            case R.id.decrease_btn:
                updateTextSize(false);
                break;
            default:
                break;
        }
    }

    private void updateTxtCommentColor(){
        if (memeEditSettingModel.getColorIterator().hasNext()){
            txtComment.setTextColor(memeEditSettingModel.getColorIterator().next());
        }
        else {
            memeEditSettingModel.resetColorIterator();
            txtComment.setTextColor(memeEditSettingModel.getColorIterator().next());
        }
    }

    private void updateTxtCommentFont(){
        if (memeEditSettingModel.getFontIterator().hasNext()){
            txtComment.setTypeface(memeEditSettingModel.getFontIterator().next());
        }
        else {
            memeEditSettingModel.resetFontIterator();
            txtComment.setTypeface(memeEditSettingModel.getFontIterator().next());
        }
    }

    private void updateTextSize(boolean isIncrease){
        if (isIncrease)
            fontSize ++;
        else
            fontSize --;
        txtComment.setTextSize(fontSize);
    }

    private void shareMeme(Bitmap bitmap){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/png");
        String bnpPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,null, null);
        Uri bmpUri = Uri.parse(bnpPath);
        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void clearTxtComment(){
        fontSize = 18;
        layEditMemeContainer.removeView(txtComment);
        layEditPanelContainer.setVisibility(View.GONE);
        isEditTextAdded = false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Utils.getInstance().makeToast(this,"Permission denied to save your picture");
                }
            }
        }
    }
    public void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (txtComment != null){
                        if (txtComment.getText().toString().equals("")){
                            clearTxtComment();
                        }
                    }
                    hideSoftKeyboard(EditMemeActivity.this);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
        if (isEditTextAdded)
            txtComment.setFocusable(false);
    }
}
