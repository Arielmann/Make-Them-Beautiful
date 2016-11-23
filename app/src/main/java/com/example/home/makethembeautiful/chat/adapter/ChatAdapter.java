package com.example.home.makethembeautiful.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.makethembeautiful.R;
import com.example.home.makethembeautiful.chat.model.ChatItem;

import java.util.List;


/**
 * Created by home on 6/28/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<GenericViewHolder> {

    private List<ChatItem> dataSet;
    private Context context;

    public ChatAdapter(Context context, List<ChatItem> dataSet) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == R.layout.component_presented_image_right ||
                viewType == R.layout.component_presented_image_left) {
            view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ChatImageViewHolder(context, view, dataSet);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ChatTextViewHolder(view, dataSet);
        }
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.setUIDataOnView(position);
    }


    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getParentTypeLayoutValue();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
