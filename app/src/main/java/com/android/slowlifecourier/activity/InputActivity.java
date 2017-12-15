package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.slowlifecourier.R;

public class InputActivity extends AppCompatActivity {
    public static final String INPUTTYPE = "input_type";
    public static final int TEXT = 1;
    public static final int NUMBER = 2;
    public static final String RESULT = "result";
    public static final String HINT = "hint";
    public static final String DEFULT = "defult";
    public static final String MAXLENGHT = "maxLenght";
    public static final String MAXLINES = "maxLines";
    public static final String TITLE = "title";

    private EditText editText;
    private int input_type;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        editText = (EditText) findViewById(R.id.edittext);
        title = (TextView) findViewById(R.id.title);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 2048) {
                    done();
                    return true;
                }
                return false;
            }
        });

        editText.setImeActionLabel("完成", 2048);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Intent intent = getIntent();
        input_type = intent.getIntExtra(INPUTTYPE, 1);
        String title = intent.getStringExtra(TITLE);
        if (TextUtils.isEmpty(title)) {
            throw new IllegalArgumentException("title = null");
        }
        this.title.setText(title);
        String hint = intent.getStringExtra(HINT);
        String text = intent.getStringExtra(DEFULT);
        int maxlenght = intent.getIntExtra(MAXLENGHT, -1);
        int maxlines = intent.getIntExtra(MAXLINES, -1);
        editText.setText(text);
        editText.setHint(hint);
        if (maxlenght != -1) editText.setMaxHeight(maxlenght);
        if (maxlines != -1) editText.setMaxLines(maxlines);
        else if (input_type == NUMBER)
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void done() {
        String res = editText.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra(RESULT, res);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.done:
                done();
                break;
        }
    }

}
