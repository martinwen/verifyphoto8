package com.verify.photo.view.view;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */

public class HeadFootAdapter extends MultTemplateAdapter {
    public SparseArrayCompat<View> headers;
    public SparseArrayCompat<View> footers;
    public static final int HEAD_TYPE=100;
    public static final int FOOT_TYPE=200;
    public HeadFootAdapter(Context context, List list) {
        super(context, list);
        headers=new SparseArrayCompat<>();
        footers=new SparseArrayCompat<>();
    }

    public HeadFootAdapter(Context context) {
        super(context);
        headers=new SparseArrayCompat<>();
        footers=new SparseArrayCompat<>();
    }
    public HeadFootAdapter(Context context, List list, int type) {
        super(context, list, type);
        headers=new SparseArrayCompat<>();
        footers=new SparseArrayCompat<>();
    }

    public HeadFootAdapter(Context context, int type) {
        super(context, type);
        headers=new SparseArrayCompat<>();
        footers=new SparseArrayCompat<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(headers.get(viewType)!=null){
          return  ViewHolder.createViewHolder(headers.get(viewType),mContext);
        }
        if(footers.get(viewType)!=null){
            return ViewHolder.createViewHolder(footers.get(viewType),mContext);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(isFootView(position)||isHeadView(position)){
            return;
        }
        super.onBindViewHolder(holder, position-headers.size());
    }

    @Override
    public int getItemCount() {
        return headers.size()+footers.size()+getRealSize();
    }
    public int getRealSize(){
        return super.getItemCount();
    }
    public boolean isHeadView(int position){
        return position<headers.size();
    }
    public boolean isFootView(int position){
        return position>headers.size()+getRealSize()-1;
    }
    public void addHeaderView(View view){
        if(headers!=null){
            headers.put(headers.size()+HEAD_TYPE,view);
        }
    }
    public void addFooterView(View view){
        if(footers!=null){
            footers.put(footers.size()+FOOT_TYPE,view);
        }
    }
    public void removeAllFooter(){
        footers.clear();
    }
    public void removeAllHeader(){
        headers.clear();
    }
    public void removeHeader(int position){
        if(headers!=null && position<headers.size()) {
            headers.remove(position + HEAD_TYPE);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(isHeadView(position)){
            return headers.keyAt(position);
        }
        if(isFootView(position)){
            return footers.keyAt(position-getRealSize()-headers.size());
        }
        return super.getItemViewType(position-headers.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(isHeadView(position)){
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    if(isFootView(position)){
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    return 1;
                }
            });
            ((GridLayoutManager) layoutManager).setSpanCount(((GridLayoutManager) layoutManager).getSpanCount());
        }
    }
    public int getHeaderCount(){
        return headers.size();
    }
    public View getFooter(){
         return footers.valueAt(0);
    }
    public int getFooterCount(){
        return  footers.size();
    }
}
