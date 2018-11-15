package com.hpr.ac.dev.trak.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.hpr.ac.dev.trak.R;

public class CustomImageViewforMarker extends android.support.v7.widget.AppCompatImageView {

    public CustomImageViewforMarker(Context context) {
        super(context);
    }

    public CustomImageViewforMarker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageViewforMarker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    public void setImageResourcewithColor(int color) {
        setImageResource(R.drawable.marker);


        if (color == 1) {
            DrawableCompat.setTint(this.getDrawable(), ContextCompat.getColor(getContext(), R.color.RedMarker));
        }
        if (color == 2) {
            DrawableCompat.setTint(this.getDrawable(), ContextCompat.getColor(getContext(), R.color.GreenMarker));
        }
        if (color == 3) {
            DrawableCompat.setTint(this.getDrawable(), ContextCompat.getColor(getContext(), R.color.OrangeMarker));
        }

    }
}
