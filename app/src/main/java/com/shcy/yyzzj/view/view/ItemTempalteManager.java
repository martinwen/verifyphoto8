package com.shcy.yyzzj.view.view;

import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import com.shcy.yyzzj.bean.Base_Bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */

public class ItemTempalteManager<T extends Base_Bean> {
    public int defaultType = 1000;
    public SparseArrayCompat<BaseItemTempalte> arrays = new SparseArrayCompat<BaseItemTempalte>();

    public void addTempate(int type, BaseItemTempalte tempalte) {
        if (tempalte != null) {
            arrays.put(type, tempalte);
        }
    }

    public void addTempate(BaseItemTempalte tempalte) {
        addTempate(defaultType, tempalte);
    }

    public boolean isSingle() {
        if (arrays.size() == 1 && arrays.keyAt(0) == defaultType) {
            return true;
        }
        return false;
    }

    public int getItemViewType(Base_Bean bean) {
        if (isSingle()) {
            return defaultType;
        }
        BaseItemTempalte tempalte = arrays.get(bean.getItemViewType());
        if (tempalte == null) {
            Log.e("zmm", bean.getItemViewType() + "");
            return -1;
        }
        return bean.getItemViewType();
    }

    public BaseItemTempalte getItemTempalte(int key) {
        return arrays.get(key);
    }

    public int getItemViewId(int type) {
        BaseItemTempalte baseItemTempalte = arrays.get(type);
        if (baseItemTempalte == null) {
            Log.e("zmm", type + "");
        }
        return baseItemTempalte.getViewId();
    }

    public void updateView(ViewHolder holder, int position, List<T> list) {
        if (isSingle()) {
            BaseItemTempalte baseItemTempalte = arrays.get(defaultType);
            baseItemTempalte.convert(holder, position, list);
            return;
        }
        BaseItemTempalte baseItemTempalte = arrays.get(holder.getItemViewType());
        if (baseItemTempalte == null) {
            throw new RuntimeException("multiple templates need bean cantains type");
        }
        baseItemTempalte.convert(holder, position, list);
    }

    public void removeAll() {
        arrays.clear();
    }

    public void onViewRecycled(ViewHolder holder) {
        BaseItemTempalte baseItemTempalte = arrays.get(holder.getItemViewType());
        if (baseItemTempalte != null) {
            baseItemTempalte.viewRecycled();
        }
    }

}
