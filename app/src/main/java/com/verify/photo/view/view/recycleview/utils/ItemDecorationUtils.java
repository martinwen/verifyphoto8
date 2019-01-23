package com.verify.photo.view.view.recycleview.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;


/**
 * Created by Administrator on 2016/8/2.
 */

    public class ItemDecorationUtils extends RecyclerView.ItemDecoration {
    private int drawHeight;//分割线高度
    private Drawable mDrawable;//自定义分割线
   // private Paint mPaint;//颜色
    private String mOrientation;//方向
    private String layoutManagerMode;//模式
    private  int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final String VERTICAL="vertical";
    public static final String HORIZENTAL="horizatal";
    public static final String LM="linearlayoutlanager";
    public static final String GM="gridLayoutmanager";
    /**
     * 自定义线样式
     * @param d 传null 使用系统默认draw
     * @param orientation 传0 默认 vertical
     * @param mode 传0 默认Linearlayoutmanager
     */
    public ItemDecorationUtils(Drawable d, String orientation, String mode, Context context){
        if(d==null){
             TypedArray a = context.obtainStyledAttributes(ATTRS);
             mDrawable = a.getDrawable(0);
             a.recycle();
        }else{
            mDrawable=d;
        }
        if(TextUtils.isEmpty(orientation)){
            mOrientation=VERTICAL;
        }else{
            mOrientation=orientation;
        }
        if(TextUtils.isEmpty(mode)){
            layoutManagerMode=LM;
        }else{
            layoutManagerMode=mode;
        }
        drawHeight=mDrawable.getIntrinsicHeight();
    }

    /**
     * 默认线
     * @param height
     * @param orientation
     * @param mode
     * @param context
     */
    public ItemDecorationUtils(int height, String orientation, String mode, Context context){
    TypedArray a = context.obtainStyledAttributes(ATTRS);
    mDrawable=a.getDrawable(0);
    if(height!=0){
        drawHeight=height;
    }else{
        drawHeight=mDrawable.getIntrinsicHeight();
    }
    if(TextUtils.isEmpty(orientation)){
        mOrientation=VERTICAL;
    }else{
        mOrientation=orientation;
    }
    if(TextUtils.isEmpty(mode)){
        layoutManagerMode=LM;
    }else{
        layoutManagerMode=mode;
    }
}



    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(LM.equals(layoutManagerMode)){
            if(mOrientation.equals(VERTICAL)){
                  drawHorizontalLine(c,parent);
            }else{//H
                drawVerticalLine(c,parent);
            }
        }else{//GM
            drawVertical(c,parent);
            drawHorizontal(c,parent);
        }
    }

    /**
     * lv 画竖直线
     * @param c
     * @param parent
     */
    private void drawVerticalLine(Canvas c, RecyclerView parent) {
          int top=parent.getPaddingTop();
          int bottom=parent.getHeight()-parent.getPaddingBottom();
          int childCount=parent.getChildCount();
          for(int i=0;i<childCount;i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left,top,right,bottom);
            mDrawable.draw(c);
        }
    }

    /**
     * lv 画水平线
     * @param c
     * @param parent
     */
    private void drawHorizontalLine(Canvas c, RecyclerView parent) {
         int left = parent.getPaddingLeft();
         int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
             View child = parent.getChildAt(i);
             RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
             int top = child.getBottom() + params.bottomMargin;
             int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent)
    {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    +drawHeight;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top +drawHeight;
//            if(isLastRaw(parent,-1)){
//                return ;
//            }
//            Paint paint=new Paint();
//            paint.setColor(Color.WHITE);
//            c.drawRect(left,top,right,bottom,paint);
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent)
    {
        final int childCount = parent.getChildCount();
        GridLayoutManager gm= (GridLayoutManager) parent.getLayoutManager();
        int lastVisibleItemPosition = gm.findLastVisibleItemPosition();
        int spanCount = gm.getSpanCount();

        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + drawHeight;
//            if(isLastSignColum(parent)){
//                return;
//            }
//            Paint paint=new Paint();
//            paint.setColor(Color.GRAY);
//            c.drawRect(left,top,right,bottom,paint);
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        if(layoutManagerMode.equals(GM)){
            GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
            int count = manager.getItemCount();
            int spanCount = manager.getSpanCount();
            if(isLast&&itemPosition>=count-footcount){
                outRect.set(0,-drawHeight,0,0);
                return;
            }
//            if(isLast){
//                if((count-footcount)%spanCount!=0&&itemPosition>=count-footcount-1){
//
//                }else{
//
//                }
//            }
            if(isEndColum(parent,itemPosition)){
//                if(isLastRaw(parent,itemPosition)){
//                    outRect.set(0,0,0,0);
//                }else{
//                    outRect.set(0,0,0,drawHeight);
//                }
                outRect.set(0,0,0,drawHeight);
                return ;
            }
//            else if(isLastRaw(parent,itemPosition)){
////                outRect.set(0,0,drawHeight,0);
////                return;
//            }
            outRect.set(0,0,drawHeight,drawHeight);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(layoutManagerMode.equals(LM)){
            if(mOrientation.equals(VERTICAL)){
                outRect.set(0, 0, 0, mDrawable.getIntrinsicHeight());
            }else{//H
                outRect.set(0, 0, mDrawable.getIntrinsicWidth(),0);
            }
        }
    }

    /**
     * 最后一个不能整除的列
     * @param parent
     * @return
     */
    private boolean isLastSignColum(RecyclerView parent)
    {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        if(isLast){
            lastVisibleItemPosition=lastVisibleItemPosition-footcount;
        }
        int spanCount = layoutManager.getSpanCount();
        if(isLastRaw(parent,-1)&&(lastVisibleItemPosition+1)%spanCount!=0){
            return true;
        }
        return false;
    }
    public boolean isEndColum(RecyclerView parent, int position){
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int itemCount = layoutManager.getItemCount();
//        if(isLast){
//           if(position> itemCount-1-footcount){
//               return true;
//           }
//        }
//        int spanCount = layoutManager.getSpanCount();
//        if((position+1)%spanCount==0){
//            return true;
//        }
//        return false;
        int spanCount = layoutManager.getSpanCount();
        if((position+1)%spanCount==0){
            return true;
        }
        return false;
    }
    /**
     * 最后一行
     * @param parent
     * @param position
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int position)
    {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
//        int spanCount = layoutManager.getSpanCount();
//        if(position==-1){
//            position= layoutManager.findLastVisibleItemPosition();
//        }
//        int childCount=layoutManager.getItemCount();
//        if(isLast){
//          if((position+1)>=childCount-(position+1)%spanCount-footcount){
//            return true;
//          }
//        }
//        if((position+1)>=childCount-(position+1)%spanCount){
//            return true;
//        }
//        return false;
        int itemCount = layoutManager.getItemCount();
       return false;
    }

    boolean isLast;
    int footcount;
    public void setLastFooter(int i, boolean b) {
        this.footcount=i;
        this.isLast=b;
    }
}
