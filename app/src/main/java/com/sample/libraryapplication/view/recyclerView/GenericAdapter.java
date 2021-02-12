package com.sample.libraryapplication.view.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.libraryapplication.database.entity.BaseEntity;

import java.util.List;

/**
 * @author manoj.bhadane manojbhadane777@gmail.com
 * I added couple of new methods
 */
public abstract class GenericAdapter<T extends BaseEntity, D> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<T> mArrayList;

    public GenericAdapter(Context context, List<T> arrayList) {
        this.mContext = context;
        this.mArrayList = arrayList;
    }
    public abstract int getLayoutResId();
    public abstract void onBindData(T model, int position, D dataBinding);
    public abstract void onItemClick(T model, int position);

    public void updateList(List<T> newList){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff( new DiffCallback(mArrayList, newList), false);
        mArrayList = newList;
        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }
    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback;
    private ItemTouchHelper itemTouchHelper;

    public void onLeftSwip(RecyclerView.ViewHolder viewHolder ){

    }
    public void onRightSwip(RecyclerView.ViewHolder viewHolder ){

    }
    public ItemTouchHelper getToucCallback() {
        if ( itemTouchHelper == null ) {
            simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    if (swipeDir == ItemTouchHelper.LEFT)
                        onLeftSwip(viewHolder);
                    else
                    if (swipeDir == ItemTouchHelper.RIGHT)
                        onRightSwip(viewHolder);
                }
            };

            itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        }

        return itemTouchHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutResId(), parent, false);
        RecyclerView.ViewHolder holder = new ItemViewHolder(dataBinding);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        onBindData(mArrayList.get(position), position, ((ItemViewHolder) holder).mDataBinding);

        ((ViewDataBinding) ((ItemViewHolder) holder).mDataBinding).getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(mArrayList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void addItems(List<T> arrayList) {
        mArrayList = arrayList;
        this.notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mArrayList.get(position);
    }

    public Context getContext() {
        return mContext;
    }
    public D getCustomViewHolder(RecyclerView.ViewHolder viewHolder){
        return ((ItemViewHolder) viewHolder).mDataBinding;
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        protected D mDataBinding;

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mDataBinding = (D) binding;
        }
    }

    private class DiffCallback extends DiffUtil.Callback {
        private List<T> oldList;
        private List<T> newList;

        public DiffCallback(List<T> oldList, List<T> newList ){
            this.oldList  = oldList;
            this.newList = newList;
        }
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            if ( oldList != null && newList != null )
                return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
            else
                return false;
        }

        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0;
        }
        @Override
        public int getNewListSize() {
            return newList != null ? newList.size() : 0;
        }
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition){
            if ( oldList != null && newList != null )
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            else
                return false;
        }
    }
}