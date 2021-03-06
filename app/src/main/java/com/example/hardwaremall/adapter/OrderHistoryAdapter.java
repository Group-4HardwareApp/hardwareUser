package com.example.hardwaremall.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwaremall.OrderHistoryDetailsActivity;
import com.example.hardwaremall.ReOrderActivity;
import com.example.hardwaremall.bean.Order;
import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.R;

import com.example.hardwaremall.databinding.ActivityOrderHistoryItemListBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    Context context;
    ArrayList<Order> orderList;
    private OnRecyclerViewClick listener;
    int flag = 0;

    public OrderHistoryAdapter(Context context, ArrayList<Order> orderList) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityOrderHistoryItemListBinding binding = ActivityOrderHistoryItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new OrderHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        final Order order = orderList.get(position);
        holder.binding.tvOrderId.setText("" + order.getOrderId());
        holder.binding.tvOrderName.setText("" + order.getName());
        holder.binding.tvOrderAddress.setText("" + order.getDeliveryAddress());
        holder.binding.tvOrderAmount.setText("" + order.getTotalAmount());
        holder.binding.tvOrderDate.setText("" + order.getDate());
        holder.binding.tvOrderStatus.setText("" + order.getShippingStatus());
        String status = order.getShippingStatus();

        if (status.equalsIgnoreCase("Cancelled")) {
            holder.binding.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.red));
            flag = 1;
        } else
            holder.binding.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.dark_green));

        final ArrayList<OrderItems> items = (ArrayList<OrderItems>) order.getOrderItems();

        SpannableString content = new SpannableString("View more");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.binding.btnViewMore.setText(content);

        holder.binding.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, OrderHistoryDetailsActivity.class);
                in.putExtra("item", (Serializable) items);
                in.putExtra("shippingStatus",order.getShippingStatus());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(in);
            }
        });

        /*holder.binding.btnReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ReOrderActivity.class);
                in.putExtra("items", (Serializable) items);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(in);
            }
        });*/
        holder.binding.btnReorder.setVisibility(View.VISIBLE);
        holder.binding.btnReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ReOrderActivity.class);
                in.putExtra("order",order);
                context.getApplicationContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        ActivityOrderHistoryItemListBinding binding;

        public OrderHistoryViewHolder(ActivityOrderHistoryItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Order o = orderList.get(position);
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(o, position);
                    }
                }
            });
        }
    }

    public interface OnRecyclerViewClick {
        void onItemClick(Order order, int position);
    }

    public void setOnItemClick(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}
