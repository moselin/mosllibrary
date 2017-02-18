package com.moselin.rmlib.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @Description RecyclerView通用的adapter
 * @Author MoseLin
 * @Date 2016/7/21.
 */
public class CommonRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List datas;//数据集
    private Context context;
    private AdapterTemplate template;
    private AdapterObservable adapterObservable;

    /**
     * 构造方法，不需要响应式监听
     * @param context 上下文
     * @param datas 数据集
     * @param template item模板接口
     */
    public CommonRecycleViewAdapter(Context context, List datas, AdapterTemplate template){
       this(context,datas,template,null);
    }

    /**
     * 构造方法，需要响应式监听数据
     * @param context 上下文
     * @param datas 数据集
     * @param template item模板接口
     * @param adapterObservable 回调给RxAndroid的接口
     */
    public CommonRecycleViewAdapter(Context context, List datas, AdapterTemplate template,AdapterObservable adapterObservable){
        this.context = context;
        this.datas = datas;
        this.template = template;
        this.adapterObservable = adapterObservable;
    }
    @Override
    public int getItemViewType(int position)
    {
        return template.getItemViewType().get(datas.get(position).getClass());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        try
        {
            return template.getViewHolder().get(viewType).getConstructor(View.class).newInstance(LayoutInflater.from(context).inflate(viewType,parent,false));
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        BaseViewHolder viewHolder = ( BaseViewHolder ) holder;
        viewHolder.setAdapter(this);
        viewHolder.bindViewHolder(datas.get(position),position);
        if (adapterObservable != null)
        {
            adapterObservable.observable(viewHolder.getObservable());
        }

    }

    @Override
    public int getItemCount()
    {
        return datas==null?0:datas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public interface AdapterTemplate
    {
        /**
         *
         * @return map, class is entity class , integer is layoutId
         */
        Map<Class<?>,Integer> getItemViewType();

        /**
         *
         * @return SparseArray, class is what extents BaseViewHolder
         */
        SparseArray<Class<? extends BaseViewHolder>> getViewHolder();
    }
    public interface AdapterObservable
    {
        /**
         * Rx Observable, Integer was position
         */
        void observable(Observable<Object> observable);

    }

}
