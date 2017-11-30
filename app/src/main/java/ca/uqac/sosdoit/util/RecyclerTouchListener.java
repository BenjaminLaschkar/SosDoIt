package ca.uqac.sosdoit.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Ravi Tamada on 03/09/16.
 * www.androidhive.info
 */

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector detector;
    private ClickListener listener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener listener)
    {
        this.listener = listener;

        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    listener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e)
    {
        View child = view.findChildViewUnder(e.getX(), e.getY());
        if (child != null && detector.onTouchEvent(e)) {
            listener.onClick(child, view.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    public interface ClickListener
    {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
