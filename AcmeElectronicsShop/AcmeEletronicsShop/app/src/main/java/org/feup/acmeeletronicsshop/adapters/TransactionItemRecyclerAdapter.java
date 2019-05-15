package org.feup.acmeeletronicsshop.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.model.Transaction;
import org.feup.acmeeletronicsshop.model.TransactionItem;

import java.util.List;

public class TransactionItemRecyclerAdapter extends RecyclerView.Adapter<TransactionItemRecyclerAdapter.TransactionItemViewHolder> {

    private List<TransactionItem> listItems;

    public TransactionItemRecyclerAdapter(List<TransactionItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public TransactionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_item_recycler, parent, false);

        return new TransactionItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionItemViewHolder holder, int position) {
        holder.txtViewName.setText(listItems.get(position).getItemName());
        holder.txtViewQuantity.setText(listItems.get(position).getQuantity() + "");

    }

    @Override
    public int getItemCount() {
        Log.v(TransactionItemRecyclerAdapter.class.getSimpleName(), "" + listItems.size());
        return listItems.size();
    }


    /**
     * ViewHolder class
     */
    public class TransactionItemViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView txtViewName;
        private AppCompatTextView txtViewQuantity;

        private TransactionItemViewHolder(View view) {
            super(view);
            txtViewName = (AppCompatTextView) view.findViewById(R.id.txtViewName);
            txtViewQuantity = (AppCompatTextView) view.findViewById(R.id.txtViewQuantity);
        }
    }


}