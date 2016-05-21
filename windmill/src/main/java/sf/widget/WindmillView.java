package sf.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import sf.singlewindmillanimation.R;

/**
 * @author Saifei
 *         风车旋转动画 View
 */
public class WindmillView extends View {


   

    private int mHeight;
    private int mWidth;
    private Bitmap littleBallBitmap;

    /**
     * 风车 叶子
     */
    private Bitmap windMillItemBitmap;

    /**
     * 风车杆
     */
    private Bitmap windPoleBitmap;

    private Paint paint;

    public WindmillView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WindmillView(Context context) {
        super(context);
        mWidth = (int) dip(100);
        init(context);
    }

    public WindmillView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WindmillView);
        mWidth = (int) a.getDimension(R.styleable.WindmillView_view_width, dip(100));

        a.recycle();

        init(context);
    }

    private void init(Context context) {
        littleBallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.little_ball);

        windMillItemBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.windmill_item);
        windPoleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bottom_pole);

        recalculateBitmapScale();


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        initAnimation();
    }

    private void recalculateBitmapScale() {
        int windItemOriginWidth = windMillItemBitmap.getWidth();
        float scale = (mWidth / 2 - littleBallBitmap.getWidth() / 2) / (float) windItemOriginWidth;
        windMillItemBitmap = Bitmap.createScaledBitmap(windMillItemBitmap, mWidth / 2 - littleBallBitmap.getWidth() / 2, (int) (windMillItemBitmap.getHeight() * scale), true);
        littleBallBitmap = Bitmap.createScaledBitmap(littleBallBitmap, (int) (littleBallBitmap.getWidth() * scale), (int) (littleBallBitmap.getHeight() * scale), true);
        windPoleBitmap = Bitmap.createScaledBitmap(windPoleBitmap, (int) (windPoleBitmap.getWidth() * scale), (int) (windPoleBitmap.getHeight() * scale), true);
    }

    float sweepDegrees = 360f;
    private float currentDegrees;

    private void initAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, sweepDegrees);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentDegrees = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator.setDuration(5000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = (int) (windMillItemBitmap.getHeight() + littleBallBitmap.getHeight() + windPoleBitmap.getHeight() + dip(20));
        setMeasuredDimension(mWidth, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(littleBallBitmap, mWidth / 2 - littleBallBitmap.getWidth() / 2, mHeight / 3 - littleBallBitmap.getWidth() / 2, paint);

        canvas.save();

        for (int i = 0; i < 3; i++) {
            canvas.rotate(120, mWidth / 2, mHeight / 3);
            canvas.rotate(currentDegrees, mWidth / 2, mHeight / 3);
            canvas.drawBitmap(windMillItemBitmap, littleBallBitmap.getWidth() / 2, mHeight / 3 - littleBallBitmap.getWidth() / 2, paint);
            canvas.rotate(-currentDegrees, mWidth / 2, mHeight / 3);

        }
        canvas.restore();

        canvas.drawBitmap(windPoleBitmap, mWidth / 2 - windPoleBitmap.getWidth() / 2, mHeight / 3 + littleBallBitmap.getHeight() / 2, paint);

    }


    protected final float dip(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics());
    }
}
