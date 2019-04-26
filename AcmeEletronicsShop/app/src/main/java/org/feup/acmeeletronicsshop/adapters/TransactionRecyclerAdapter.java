package org.feup.acmeeletronicsshop.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.model.Transaction;

import java.util.List;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionViewHolder> {

    private List<Transaction> listTransactions;
    private TransactionRecyclerAdapter.OnItemClickListener mListener;

    public TransactionRecyclerAdapter(List<Transaction> listTransactions) {
        this.listTransactions = listTransactions;
    }

    public void setOnItemClickListener(TransactionRecyclerAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_recycler, parent, false);

        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.txtViewID.setText(listTransactions.get(position).getId() + "");
        holder.txtViewDate.setText((listTransactions.get(position).getDate()).toString());

    }

    @Override
    public int getItemCount() {
        Log.v(TransactionRecyclerAdapter.class.getSimpleName(), "" + listTransactions.size());
        return listTransactions.size();
    }


    /**
     * ViewHolder class
     */
    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView txtViewID;
        private AppCompatTextView txtViewDate;

        private TransactionViewHolder(View view) {
            super(view);
            txtViewID = (AppCompatTextView) view.findViewById(R.id.txtViewID);
            txtViewDate = (AppCompatTextView) view.findViewById(R.id.txtViewDate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }



    }


}