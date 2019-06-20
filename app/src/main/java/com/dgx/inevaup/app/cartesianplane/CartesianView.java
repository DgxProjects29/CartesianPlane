package com.dgx.inevaup.app.cartesianplane;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class CartesianView extends View {

    // Variables for scroll and zooming
    private float mPosX;
    private float mPosY;
    private float mLastTouchX;
    private float mLastTouchY;
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1f;

    //Paints
    Paint paintLineXY,paintGrid,paintText,paintTextR,paintLine,paintInterception;

    //Variables for the grid
    int ScFactor;
    int w2 = 768;
    int ScaleMultiplier = 1;

    float TextPlusX1 = 0;
    float TextPlusY1 = 0;

    int BottomBound,TopBound,LeftBound,RightBound;

    // width and height of the screen
    int w;
    int h;

    Typeface FontSpecial;
    CartesianLogic logic;
    Fraccion nu1,nu2,nu3,nu4,nu5,nu6;
    float XInterception,YInterception;
    //int kdivider = 1000;
    //String kletter = "k";

    public CartesianView(Context context) {
        super(context);
        init(context);
    }

    public CartesianView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CartesianView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CartesianView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context c){
        mScaleDetector = new ScaleGestureDetector(c, new ScaleListener());
        logic = new CartesianLogic();

        FontSpecial = Typeface.createFromAsset(c.getAssets(),  "fonts/tahoma regular font.ttf");

        paintGrid  = new Paint();
        paintLineXY = new Paint();
        paintText = new Paint();
        paintTextR = new Paint();
        paintLine = new Paint();
        paintInterception = new Paint();

        paintText.setTypeface(FontSpecial);
        paintText.setColor(Color.rgb(156,156,156));
        paintTextR.setTypeface(FontSpecial);
        paintTextR.setColor(Color.rgb(156,156,156));
        paintTextR.setTextAlign(Paint.Align.RIGHT);

        paintLineXY.setColor(Color.BLACK);//Color.rgb(86,86,86)
        paintLineXY.setAntiAlias(true);
        paintGrid.setColor(Color.rgb(191,191,191));
        paintGrid.setAntiAlias(true);

        paintLine.setColor(Color.rgb(255,82,82));
        paintGrid.setAntiAlias(true);
        paintInterception.setColor(Color.rgb(97,97,97));
        paintInterception.setAntiAlias(true);

        // Here width and height are 0, we have to wait until onDraw Method be activated and call GridAttributes;
    }


    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        ConditionsForDrawing(canvas);

        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor, (float) (canvas.getClipBounds().left + canvas.getClipBounds().right) / 2, (float) (canvas.getClipBounds().top + canvas.getClipBounds().bottom) / 2);

        DrawGrid(canvas);
        DrawLines(canvas);


        canvas.restore();

    }

    private void ConditionsForDrawing(Canvas canvas){

        // According to the size of the screen we put a specific values to ensure that ScFactor is an int
        if(w>=1536){
            w2 = 1536;
        }else if(w>=1344){
            w2 = 1344;
        }else if(w>=1152){
            w2 = 1152;
        }else if(w>=960){
            w2 = 960;
        }else if(w>=768){
            w2 = 768;
        }else if(w>=576){
            w2 = 576;
        }else{
            w2 = 576;
        }

        // According to the zoom of the screen, the measure between each line will change
        if(mScaleFactor>1.3){
            ScFactor = (w2*28)/768;
            ScaleMultiplier = 1;
        }else if(mScaleFactor>0.75){
            ScFactor = (w2*56)/768;
            ScaleMultiplier = 2;
        }else if(mScaleFactor>0.4){
            ScFactor = (w2*140)/768;
            ScaleMultiplier = 5;
        }else{
            ScFactor = (w2*280)/768;
            ScaleMultiplier = 10;
        }
        GridAttributes();
    }

    private void GridAttributes(){

        paintLineXY.setStrokeWidth((float)w*(2.2f/mScaleFactor)/768);
        paintGrid.setStrokeWidth((float)w*(1f/mScaleFactor)/768);
        paintText.setTextSize((float)w*(17f/mScaleFactor)/768);
        paintTextR.setTextSize((float)w*(17f/mScaleFactor)/768);
        paintLine.setStrokeWidth((float)w*(3.7f/mScaleFactor)/768);
        TextPlusX1 = w*(8/mScaleFactor)/768;
        TextPlusY1 = w*(-8/mScaleFactor)/768;
    }

    private void DrawGrid(Canvas canvas){

        LeftBound = canvas.getClipBounds().left; // max
        RightBound = canvas.getClipBounds().right; // min
        TopBound = canvas.getClipBounds().top; //max
        BottomBound = canvas.getClipBounds().bottom; //min

        //Get the number of the point for drawing the first vertical and horizontal line of the grid
        int StNumberX = logic.GetFirstTextNumber(LeftBound,RightBound,ScFactor);
        int StNumberY = logic.GetFirstTextNumber(TopBound,BottomBound,ScFactor);

        //Draw the horizontal text and vertical grid lines
        for(int i = StNumberX;i<RightBound;i+=ScFactor){

            if(i == StNumberX){
                canvas.drawLine(0,TopBound,0,BottomBound,paintLineXY); // Y axis
                //canvas.drawRect(0,TopBound,20,BottomBound,paintRect);
            }
            canvas.drawLine(i,TopBound,i,BottomBound,paintGrid);


            if((i/ScFactor)*ScaleMultiplier!=0) {

                if(Math.abs(((i/ScFactor)*ScaleMultiplier))>=10000 && ScaleMultiplier>=10){
                    if (TopBound < 0 && BottomBound > 0) {
                        canvas.drawText(String.valueOf((float)((i/(ScFactor))*ScaleMultiplier)/1000f)+"k", i+TextPlusX1, TextPlusY1, paintText);
                    } else if (TopBound > 0 && BottomBound > 0) {
                        canvas.drawText(String.valueOf((float)((i/(ScFactor))*ScaleMultiplier)/1000f)+"k", i+TextPlusX1, (float) TopBound + (float)(w*(25f/mScaleFactor))/768, paintText);
                    } else if (TopBound < 0 && BottomBound < 0) {
                        canvas.drawText(String.valueOf((float)((i/(ScFactor))*ScaleMultiplier)/1000f)+"k", i+TextPlusX1, (float) BottomBound+TextPlusY1, paintText);
                    }
                }else{
                    if (TopBound < 0 && BottomBound > 0) {
                        canvas.drawText(String.valueOf((i / ScFactor) * ScaleMultiplier), i+TextPlusX1, TextPlusY1, paintText);
                    } else if (TopBound > 0 && BottomBound > 0) {
                        canvas.drawText(String.valueOf((i / ScFactor) * ScaleMultiplier), i+TextPlusX1, (float) TopBound + (float)(w*(25f/mScaleFactor))/768, paintText);
                    } else if (TopBound < 0 && BottomBound < 0) {
                        canvas.drawText(String.valueOf((i / ScFactor) * ScaleMultiplier), i+TextPlusX1, (float) BottomBound+TextPlusY1, paintText);
                    }
                }

            }
        }

        //Draw the vertical text and horizontal grid lines
        for(int i = StNumberY;i<BottomBound;i+=ScFactor){

            if(i == StNumberY){
                canvas.drawLine(LeftBound,0,RightBound,0,paintLineXY); // X axis
                //canvas.drawRect(LeftBound,-20,RightBound,0,paintRect);
            }

            canvas.drawLine(LeftBound,i,RightBound,i,paintGrid);

            if((i/ScFactor)*ScaleMultiplier!=0){

                if(Math.abs(((i/ScFactor)*ScaleMultiplier))>=10000 && ScaleMultiplier>=10){
                    if(LeftBound<0 && RightBound>0){
                        canvas.drawText(String.valueOf((float)((i/(ScFactor*-1))*ScaleMultiplier)/1000f)+"k",TextPlusX1,i+TextPlusY1,paintText);
                    }else if(LeftBound>0 && RightBound>0){
                        canvas.drawText(String.valueOf((float)((i/(ScFactor*-1))*ScaleMultiplier)/1000f)+"k",(float)LeftBound+TextPlusX1,i+TextPlusY1,paintText);
                    }else if(LeftBound<0 && RightBound<0){
                        canvas.drawText(String.valueOf((float)((i/(ScFactor*-1))*ScaleMultiplier)/1000f)+"k",(float)RightBound-TextPlusX1,i,paintTextR);
                    }
                }else{
                    if(LeftBound<0 && RightBound>0){
                        canvas.drawText(String.valueOf((i/(ScFactor*-1))*ScaleMultiplier),TextPlusX1,i+TextPlusY1,paintText);
                    }else if(LeftBound>0 && RightBound>0){
                        canvas.drawText(String.valueOf((i/(ScFactor*-1))*ScaleMultiplier),(float)LeftBound+TextPlusX1,i+TextPlusY1,paintText);
                    }else if(LeftBound<0 && RightBound<0){
                        canvas.drawText(String.valueOf((i/(ScFactor*-1))*ScaleMultiplier),(float)RightBound-TextPlusX1,i,paintTextR);
                    }
                }

            }

        }
    }

    public void DrawLines(Canvas canvas){

        int ScFactorLine = (w2*28)/768;
        int ScaleMultiplierLine = 1;

        //Get the coordinates of the line according to the equation and the maximum and minimum
        //coordinates which the cartesian plane provide us
        float[] MaxValues = logic.getMaxandMinPoints(ScaleMultiplierLine,ScFactorLine,1,nu1,nu2,nu3,
                (float)RightBound/ScFactorLine,(float)TopBound/ScFactorLine);
        float[] MinValues = logic.getMaxandMinPoints(ScaleMultiplierLine,ScFactorLine,2,nu1,nu2,nu3,
                (float)LeftBound/ScFactorLine,(float)BottomBound/ScFactorLine);

        paintLine.setColor(Color.rgb(255,82,82));//red
        canvas.drawLine(MaxValues[0],MaxValues[1],MinValues[0],MinValues[1],paintLine);

        float[] MaxValues2 = logic.getMaxandMinPoints(ScaleMultiplierLine,ScFactorLine,1,nu4,nu5,nu6,
                (float)RightBound/ScFactorLine,(float)TopBound/ScFactorLine);
        float[] MinValues2 = logic.getMaxandMinPoints(ScaleMultiplierLine,ScFactorLine,2,nu4,nu5,nu6,
                (float)LeftBound/ScFactorLine,(float)BottomBound/ScFactorLine);

        paintLine.setColor(Color.rgb(26,82,118));
        canvas.drawLine(MaxValues2[0],MaxValues2[1],MinValues2[0],MinValues2[1],paintLine);

        // if the equation is parallel it will not have point of intersection
        if(!logic.isparallel(nu1,nu2,nu3,nu4,nu5,nu6)) {
            float[] points = logic.Solve2x2(nu1, nu2, nu3, nu4, nu5, nu6);
            XInterception = points[0];
            YInterception = points[1] * -1;
            canvas.drawCircle((points[0] * ScFactorLine), (points[1] * ScFactorLine * -1), (float) w * (8f / mScaleFactor) / 768, paintInterception);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = (ev.getX()/mScaleFactor);
                final float y = (ev.getY()/mScaleFactor);

                // Log.d("TAGTOUCH","You touched on:"+x+"  - "+y+"  -->"+(LeftBound+x)/((w*28)/768)+"  ,  "+((TopBound+y)/((w*28)/768))*-1);

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = (ev.getX(pointerIndex)/mScaleFactor);
                final float y = (ev.getY(pointerIndex)/mScaleFactor);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;


                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex)/mScaleFactor;
                    mLastTouchY = ev.getY(newPointerIndex)/mScaleFactor;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large. 0.8-3.4, 1 escala 0.7-1.3, 1.3-2.5
            //0.2-0.37
            mScaleFactor = Math.max(0.2f, Math.min(mScaleFactor,2.9f));
            Log.d("TAGB"," "+mScaleFactor+"  zoom 1?"+"    "+(float)((-2.55*mScaleFactor)+7.895));
            invalidate();
            return true;
        }
    }



    public void setHeightAndWidth(int w, int h){
        this.w = w;
        this.h = h;
    }

    public void setEquation(Fraccion nu1,Fraccion nu2,Fraccion nu3, Fraccion nu4,Fraccion nu5,Fraccion nu6){
        this.nu1 = nu1;
        this.nu2 = nu2;
        this.nu3 = nu3;
        this.nu4 = nu4;
        this.nu5 = nu5;
        this.nu6 = nu6;
    }

    public void centerPlane(){
        if(!logic.isparallel(nu1,nu2,nu3,nu4,nu5,nu6)) {
            mScaleFactor = 1f;
            mPosX = ((XInterception * -1) * (float) ((w2 * 28) / 768)) + (float) (w / 2); //- = x positives y su += a x negatives
            mPosY = ((YInterception * -1) * (float) ((w2 * 28) / 768)) + (float) (h / 2); //- = x negatives y su += a x positives
        }else{
            mScaleFactor = 1f;
            mPosX = (float) (w / 2);
            mPosY = (float) (h / 2);
        }
        invalidate();
    }

    public void center0(){
        mScaleFactor = 1f;
        mPosX = (float) (w / 2);
        mPosY = (float) (h / 2);
        invalidate();
    }
}

