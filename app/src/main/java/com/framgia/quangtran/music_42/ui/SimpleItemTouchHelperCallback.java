package com.framgia.quangtran.music_42.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ItemTouchListener mListener;

    public SimpleItemTouchHelperCallback(ItemTouchListener listener) {
        mListener = listener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        mListener.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.swipe(viewHolder.getAdapterPosition(), direction);
    }

    public interface ItemTouchListener {
        void onMove(int oldPosition, int newPosition);

        void swipe(int position, int direction);
    }
}
