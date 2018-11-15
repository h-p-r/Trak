package com.hpr.ac.dev.trak.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hpr.ac.dev.trak.R;

public class CustomSearchView extends RelativeLayout {

    private EditText searchEditText;
    private boolean isClearButtonVisible;
    private ImageView clearButtonImage, micButtonImage;


    public CustomSearchView(Context context) {
        super(context);
        initView(context, null);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        View root = inflate(context, R.layout.custom_searchview, null);
        searchEditText = root.findViewById(R.id.et_searchview);
        clearButtonImage = root.findViewById(R.id.iv_clear_text);
        micButtonImage = root.findViewById(R.id.iv_mic);

        clearButtonImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.getText().clear();
                hideClearButton();
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    hideClearButton();
                else if (!isClearButtonVisible)
                    showClearButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addView(root);
    }

    private void showClearButton() {
        clearButtonImage.setVisibility(VISIBLE);
        micButtonImage.setVisibility(GONE);
        ObjectAnimator slideInAnimation = ObjectAnimator.ofFloat(clearButtonImage, "translationX", 100f, 0f);
        slideInAnimation.setDuration(100);
        slideInAnimation.start();
        isClearButtonVisible = true;

    }

    private void hideClearButton() {
        ObjectAnimator slideInAnimation = ObjectAnimator.ofFloat(clearButtonImage, "translationX", 0f, 100f);
        slideInAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                clearButtonImage.setVisibility(View.GONE);
                micButtonImage.setVisibility(View.VISIBLE);
                isClearButtonVisible = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        slideInAnimation.setDuration(100);
        slideInAnimation.start();
    }

}