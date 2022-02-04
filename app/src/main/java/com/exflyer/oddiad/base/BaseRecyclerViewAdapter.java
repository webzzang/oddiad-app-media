
package com.exflyer.oddiad.base;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder, A> extends RecyclerView.Adapter<VH> {
    protected final Context context;
    protected List<T> listItems = new ArrayList<>();
    protected A listener;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setListener(A listener) {
        this.listener = listener;
    }

    public boolean contains(T item) {
        return listItems.contains(item);
    }

    public void add(T item, int position) {
        if (item == null) {
            return;
        }

        listItems.add(position, item);
        notifyItemInserted(position);
    }

    public void add(ArrayList<T> items, int position) {
        if (items == null || items.isEmpty()) {
            return;
        }

        listItems.addAll(position, items);
        notifyItemRangeInserted(position, items.size());
    }

    public void add(T item) {
        int position = listItems.size();
        listItems.add(position, item);
        notifyItemInserted(position);
    }

    public void add(ArrayList<T> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        int position = listItems.isEmpty() ? 0 : listItems.size();
        listItems.addAll(position, items);
        notifyItemRangeInserted(position, items.size());
    }

    public T getItem(int position) {
        return listItems.get(position);
    }

    public List<T> getItems() {
        return listItems;
    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        }

        return 0;
    }

    /**
     * Requires equals() and hashcode() to be implemented in T class.
     */
    public void remove(T item) {
        int position = listItems.indexOf(item);
        if (position == -1) {
            return;
        }

        listItems.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(int position) {
        listItems.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(int position, int itemCount) {
        for (int i = position; i < itemCount; i++) {
            listItems.remove(i);
        }

        notifyItemRangeRemoved(position, itemCount);
    }

    public void replaceList(List<T> items) {
        listItems = items;
        notifyDataSetChanged();
    }

    public void replaceListWithEqualMethodForNotBliking(ArrayList<T> items) {
        int countOfBefore = listItems.size();
        int countOfAfter = items == null ? 0 : items.size();
        if (countOfBefore != countOfAfter) {
            listItems = items;
            notifyDataSetChanged();
            return;
        }

        List<Integer> invalidates = new ArrayList<>();
        for (int index = 0; index < listItems.size(); index++) {
            T before = listItems.get(index);
            T after = items.get(index);
            if (before.equals(after) == false) {
                invalidates.add(new Integer(index));
            }
            listItems.set(index, after);
        }

        for (Integer index : invalidates) {
            notifyItemChanged(index);
        }
    }

    public void clear() {
        listItems.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        if (listItems != null) {
            return listItems.isEmpty();
        }

        return true;
    }

    public void notifyDataSetChanged(T data) {
        if (data == null) {
            return;
        }

        try {
            for (int i = 0, size = listItems.size() ; i < size ; i++) {
                T t = listItems.get(i);

                if (t != null && t == data) {
                    notifyItemChanged(i);
                }

            }
        } catch (Exception e) {
        }
    }

    public void replaceListClear(List<T> items) {
        listItems.clear();
        listItems = items;
        notifyDataSetChanged();
    }

}