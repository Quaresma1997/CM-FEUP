package org.feup.acmeeletronicsshop.adapters;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import org.feup.acmeeletronicsshop.R;
import org.feup.acmeeletronicsshop.model.Product;

import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ProductViewHolder> {

    private List<Product> listProducts;

    public ProductsRecyclerAdapter(List<Product> listProducts) {
        this.listProducts = listProducts;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_recycler, parent, false);

        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.textViewName.setText(listProducts.get(position).getName());
        String price = listProducts.get(position).getPrice() + "€";
        holder.textViewPrice.setText(price);

    }

    @Override
    public int getItemCount() {
        Log.v(ProductsRecyclerAdapter.class.getSimpleName(),""+listProducts.size());
        return listProducts.size();
    }


    /**
     * ViewHolder class
     */
    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewName;
        public AppCompatTextView textViewPrice;
        public EditText editTextQuantity;
        public ImageButton btnRemoveProduct;

        public ProductViewHolder(View view) {
            super(view);
            textViewName = (AppCompatTextView) view.findViewById(R.id.textViewName);
            textViewPrice = (AppCompatTextView) view.findViewById(R.id.textViewPrice);
            editTextQuantity = (EditText) view.findViewById(R.id.editTextQuantity);
            btnRemoveProduct = (ImageButton) view.findViewById(R.id.btnRemoveProduct);
        }
    }


}