package com.verify.photo.view.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.verify.photo.bean.Base_Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */

public class MultTemplateAdapter<T extends Base_Bean> extends RecyclerView.Adapter<ViewHolder> {
    public static final int TYPE_MINE_NEWS = 1;//我的新闻
    public List<T> datas;
    public Context mContext;
    public ItemTempalteManager itemManger;
    public OnItemCLickListener onItemCLickListener;
    public OnItemLongClickListener onItemLongClickListener;
    ViewHolder viewHolder;

    public MultTemplateAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.datas = list;
        itemManger = new ItemTempalteManager();
    }

    public MultTemplateAdapter(Context context) {
        this.mContext = context;
        itemManger = new ItemTempalteManager();
    }

    public MultTemplateAdapter(Context context, List<T> list, int type) {
        this.mContext = context;
        this.datas = list;
        switch (type) {
            default:
                itemManger = new ItemTempalteManager();
                break;
        }
    }

    public MultTemplateAdapter(Context context, int type) {
        this.mContext = context;
        switch (type) {
            default:
                itemManger = new ItemTempalteManager();
                break;
        }
    }

    public void addList(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.addAll(list);
    }
    public void addPositionList(int position ,T bean) {
        if (bean == null) {
            return;
        }
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.add(position,bean);
    }

    public void removeList(Object object){
        if(datas == null){
            return;
        }
        datas.remove(object);
    }
    public void removeList(int position){
        if(datas == null){
            return;
        }
        datas.remove(position);
    }

    public void addTopList(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.addAll(0, list);
    }

    public void setList(List<T> list) {
        this.datas = list;
    }

    public void clear() {
        if (datas != null) {
            datas.clear();
        }
    }

    public List<T> getList() {
        return datas;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewHolder = ViewHolder.createViewHolder(itemManger.getItemViewId(viewType), mContext, parent);
        setClickListener(viewHolder);
        setLongClickListener(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        itemManger.updateView(holder, position, datas);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        itemManger.onViewRecycled(holder);
    }

    public void setClickListener(ViewHolder holder) {

        holder.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCLickListener != null) {
                    onItemCLickListener.onItemClick(v);
                }
            }
        });
    }

    private void setLongClickListener(ViewHolder holder) {
        holder.getContentView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(v);
                }
                return false;
            }
        });
    }

    public void addItemTemplate(int type, BaseItemTempalte tempalte) {
        itemManger.addTempate(type, tempalte);
    }

    public BaseItemTempalte getItemTemplate(int type) {
        return itemManger.getItemTempalte(type);
    }

    public void addItemTemplate(BaseItemTempalte tempalte) {
        itemManger.addTempate(tempalte);
    }

    @Override
    public int getItemViewType(int position) {
        if (itemManger.isSingle()) {
            return itemManger.defaultType;
        } else {
            return itemManger.getItemViewType(datas.get(position));
        }
    }

    public void setOnItemClickListener(OnItemCLickListener lis) {
        this.onItemCLickListener = lis;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener lis) {
        this.onItemLongClickListener = lis;
    }

    public interface OnItemCLickListener {
        void onItemClick(View view);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view);
    }

    public T getItem(int position) {
        if (datas != null && datas.size() > position) {
            return datas.get(position);
        }
        return null;
    }

    public void removeAllTemplate() {
        itemManger.removeAll();
    }
}
