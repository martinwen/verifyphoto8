package com.verify.photo.view.view;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/7/25.
 */

public class ViewHolder extends RecyclerView.ViewHolder{
   private View mItemView;
    private SparseArrayCompat<View> arrays;
    public ViewHolder(View itemView) {
        super(itemView);
        this.mItemView=itemView;
        arrays=new SparseArrayCompat<>();
    }
    public static ViewHolder createViewHolder(View view, Context context){
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    public static ViewHolder createViewHolder(int id, Context context, ViewGroup parent){
        View view= LayoutInflater.from(context).inflate(id,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    public <T extends View>T getView(int id){
        View view=arrays.get(id);
        if(view==null){
            view=mItemView.findViewById(id);
            arrays.put(id,view);
        }
        return (T)view;
    }
    public <T extends View>T getView(int id, ViewGroup.LayoutParams params){
        View view=arrays.get(id);
        if(view==null){
            view=mItemView.findViewById(id);
            view.setLayoutParams(params);
            arrays.put(id,view);
        }
        return (T)view;
    }
    public View getContentView(){
        return mItemView;
    }
}
