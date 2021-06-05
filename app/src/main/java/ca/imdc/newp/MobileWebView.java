package ca.imdc.newp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MobileWebView extends WebView
{
    private GestureDetector mDetector;

    public MobileWebView(Context context)
    {
        super(context);
        mDetector = new GestureDetector(context, new MyGestureListener());
    }

    public MobileWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mDetector.onTouchEvent(event);
        this.performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick()
    {
        return super.performClick();
    }

    static class MyGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override public boolean onDown(MotionEvent event)
        {
            return true;
        }

        @Override public boolean onSingleTapConfirmed(MotionEvent e)
        {
            //do stuff here
            return false;
        }
    }
}
