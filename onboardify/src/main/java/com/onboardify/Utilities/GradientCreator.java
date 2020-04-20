package com.onboardify.Utilities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GradientCreator {
    private Context context;
    GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;

    public GradientCreator(Context context) {
        context = context;
    }

    public GradientDrawable getGradient(@NonNull int[] colors, int alpha, @Nullable float corner, @Nullable int orientationInt) {

        switch (orientationInt) {
            case 1:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case 2:
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case 3:
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case 4:
                orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case 5:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
            case 6:
                orientation = GradientDrawable.Orientation.TR_BL;
                break;
            case 7:
                orientation = GradientDrawable.Orientation.BL_TR;
                break;
            case 8:
                orientation = GradientDrawable.Orientation.BR_TL;
                break;
        }
        GradientDrawable drawable;
        if (colors.length >= 2) {
            drawable = new GradientDrawable(orientation, colors);
        } else {
            drawable = new GradientDrawable(orientation, new int[]{Color.RED, Color.GREEN, Color.BLUE});
        }
        drawable.setCornerRadius(corner);
        drawable.setAlpha(alpha);
        return drawable;
    }
}
