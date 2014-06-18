package com.builtinmenlo.selfieclub.views;

import android.graphics.RectF;


        import android.content.Context;
        import android.content.res.TypedArray;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.RectF;
        import android.util.AttributeSet;

import com.builtinmenlo.selfieclub.R;
import com.builtinmenlo.selfieclub.util.SVG;
import com.builtinmenlo.selfieclub.util.SVGParser;


/**
 * Created by Mostafa Gazar on 11/2/13.
 */
public class SVGImageView extends BaseImageView {

    private int mSvgRawResourceId;

    public SVGImageView(Context context) {
        super(context);
    }

    public SVGImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructor(context, attrs);
    }

    public SVGImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        sharedConstructor(context, attrs);
    }

    private void sharedConstructor(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomShapeImageView);
        mSvgRawResourceId = a.getResourceId(R.styleable.CustomShapeImageView_svg_raw_resource, 0);
        a.recycle();
    }

    public static Bitmap getBitmap(Context context, int width, int height, int svgRawResourceId) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (svgRawResourceId > 0) {
            SVG svg = SVGParser.getSVGFromInputStream(
                    context.getResources().openRawResource(svgRawResourceId), width, height);
            canvas.drawPicture(svg.getPicture());
        } else {
            canvas.drawRect(new RectF(0.0f, 0.0f, width, height), paint);
        }

        return bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        return getBitmap(mContext, getWidth(), getHeight(), mSvgRawResourceId);
    }
}