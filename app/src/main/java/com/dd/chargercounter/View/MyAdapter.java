package com.dd.chargercounter.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dd.chargercounter.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;

    MyAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.mConstantName.setText(product.getConstantName());
        holder.mConstantDefenition.setText(product.getConstantDefenition());
        holder.mValue.setText(product.getValue());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mConstantName;
        TextView mConstantDefenition;
        TextView mValue;

        ViewHolder(View itemView) {
            super(itemView);
            this.mConstantName = itemView.findViewById(R.id.tv_constantName);
            this.mConstantDefenition = itemView.findViewById(R.id.tv_constantDefenition);
            this.mValue = itemView.findViewById(R.id.tv_value);
        }
    }
}
