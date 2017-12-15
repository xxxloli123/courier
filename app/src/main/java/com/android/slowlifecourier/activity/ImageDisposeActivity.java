package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.view.BitmapView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDisposeActivity extends AppCompatActivity {

    @BindView(R.id.bitmapView)
    BitmapView bitmapView;
    @BindView(R.id.edit_title)
    TextView title;
    public static final String TITLE = "title";
    public static final String RESULT_IMG = "img";
    public static final String BITMAPPATH = "bitmap_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dispose);
        Bundle intent = getIntent().getExtras();
        String titleTxt = intent.getString(TITLE);
        if (!TextUtils.isEmpty(titleTxt)) title.setText(titleTxt);
        ButterKnife.bind(this);
        String path = intent.getString(BITMAPPATH);
        Bitmap img = BitmapFactory.decodeFile(path);
        bitmapView.setBitmap(img);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.done:
                Intent intent = new Intent();
                intent.putExtra(RESULT_IMG, bitmapView.getDisposedBitmap());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

}
