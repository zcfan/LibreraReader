package com.foobnix.tts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.foobnix.pdf.info.R;

public class StealthMode {

    public static Bitmap getPlaceholderCover(Context context) {
        int size = (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, size, size, paint);
        return bitmap;
    }

    public static String getReadingLabel(Context context) {
        return context.getString(R.string.stealth_reading);
    }

    public static String getBookLabel(Context context) {
        return context.getString(R.string.stealth_book);
    }

    public static String getRecentBookShortLabel(Context context) {
        return context.getString(R.string.reading_out_loud);
    }

    public static String getRecentBookLongLabel(Context context) {
        return context.getString(R.string.reading_out_loud);
    }
}
