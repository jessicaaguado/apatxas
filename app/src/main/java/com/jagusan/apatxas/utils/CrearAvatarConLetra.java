package com.jagusan.apatxas.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.jagusan.apatxas.R;

public class CrearAvatarConLetra {


    private static final int NUMERO_COLORES_POSIBLES = 9;

    public static Bitmap crearAvatarConLetra(Context context, String displayName, int width, int height) {
        final Resources res = context.getResources();

        if (displayName.equals(res.getString(R.string.sin_pagar))) {
            return null;
        }

        TextPaint paint = new TextPaint();
        paint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);

        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        canvas.drawColor(seleccionarColor(displayName, res));

        Rect bounds = new Rect();
        paint.setTextSize(res.getDimensionPixelSize(R.dimen.apatxas_persona_avatar_letra_tamano));
        paint.getTextBounds(displayName.substring(0, 1).toUpperCase().toCharArray(), 0, 1, bounds);
        canvas.drawText(displayName.substring(0, 1).toUpperCase().toCharArray(), 0, 1, 0 + width / 2, 0 + height / 2 + (bounds.bottom - bounds.top) / 2, paint);

        return bitmap;
    }


    private static int seleccionarColor(String key, Resources res) {
        TypedArray coloresPosibles = res.obtainTypedArray(R.array.colores_posibles_avatar_letra);
        try {
            return coloresPosibles.getColor(Math.abs(key.hashCode()) % NUMERO_COLORES_POSIBLES, Color.LTGRAY);
        } finally {
            coloresPosibles.recycle();
        }
    }

}