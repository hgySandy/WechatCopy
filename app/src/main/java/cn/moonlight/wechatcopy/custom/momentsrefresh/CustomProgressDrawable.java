package cn.moonlight.wechatcopy.custom.momentsrefresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by songyifeng on 20/11/2017.
 */

public class CustomProgressDrawable extends MaterialProgressDrawable {

    private int ROTATION_FACTOR = 5*360;
    //    加载时的动画
    private Animation mAnimation;
    private View mParent;

    private float rotation;
    private Paint paint;
    private Bitmap mBitmap;

    public CustomProgressDrawable(Context context, View parent) {
        super(context, parent);
        mParent = parent;
        paint = new Paint();
        setupAnimation();
        setBackgroundColor(Color.WHITE);
    }

    public void setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
    }

    @Override
    public void setProgressRotation(float rotation) {
//        super.setProgressRotation(rotation);
        this.rotation = -rotation*ROTATION_FACTOR;
        invalidateSelf();
    }

    @Override
    public void start() {
        mParent.startAnimation(mAnimation);
    }

    private void setupAnimation(){
        mAnimation = new Animation(){
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                setProgressRotation(-interpolatedTime);
            }
        };
        mAnimation.setDuration(5000);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void draw(Canvas c) {
        Rect bound = getBounds();
        c.rotate(rotation,bound.exactCenterX(),bound.exactCenterY());
        Rect src = new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight());
        c.drawBitmap(mBitmap,src,bound,paint);
    }
}
