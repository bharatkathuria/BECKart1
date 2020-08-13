package com.example.beckart.adapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beckart.R;
import com.example.beckart.ViewModel.AddFavoriteViewModel;
import com.example.beckart.ViewModel.FromCartViewModel;
import com.example.beckart.databinding.ActivityCartBinding;
import com.example.beckart.databinding.CartListItemBinding;
import com.example.beckart.model.Product;
import com.example.beckart.utils.RequestCallback;

import java.text.DecimalFormat;
import java.util.List;

import static com.example.beckart.utils.Constant.LOCALHOST;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context mContext;

    private List<com.example.beckart.model.Product> productsInCart;

    private com.example.beckart.model.Product currentProduct;

    private com.example.beckart.ViewModel.AddFavoriteViewModel addFavoriteViewModel;
    private com.example.beckart.ViewModel.RemoveFavoriteViewModel removeFavoriteViewModel;
    private FromCartViewModel fromCartViewModel;
    private CartAdapter.CartAdapterOnClickHandler clickHandler;
    private ActivityCartBinding binding;
    /**
     * The interface that receives onClick messages.
     */
    public interface CartAdapterOnClickHandler {
        void onClick(Product product);
    }

    public CartAdapter(Context mContext, List<Product> productInCart, CartAdapterOnClickHandler clickHandler, FragmentActivity activity, ActivityCartBinding binding) {
        this.mContext = mContext;
        this.productsInCart = productInCart;
        this.clickHandler = clickHandler;
        addFavoriteViewModel = ViewModelProviders.of(activity).get(AddFavoriteViewModel.class);
        removeFavoriteViewModel = ViewModelProviders.of(activity).get(com.example.beckart.ViewModel.RemoveFavoriteViewModel.class);
        fromCartViewModel = ViewModelProviders.of(activity).get(com.example.beckart.ViewModel.FromCartViewModel.class);
        this.binding = binding;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        CartListItemBinding cartListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cart_list_item, parent, false);
        return new CartViewHolder(cartListItemBinding,binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        currentProduct = productsInCart.get(position);
        holder.binding.txtProductName.setText(currentProduct.getProductName());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedPrice = formatter.format(currentProduct.getProductPrice());
        holder.binding.txtProductPrice.setText(mContext.getResources().getString(R.string.Rs)+" "+formattedPrice);
        // Load the Product image into ImageView
        String imageUrl = LOCALHOST + currentProduct.getProductImage().replaceAll("\\\\", "/");
        Glide.with(mContext)
                .load(imageUrl)
                .into(holder.binding.imgProductImage);

        // If product is inserted
        if (currentProduct.isFavourite() == 1) {
            holder.binding.imgFavourite.setImageResource(R.drawable.ic_favorite_pink);
        }
    }

    @Override
    public int getItemCount() {
        if (productsInCart == null) {
            return 0;
        }
        return productsInCart.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Create view instances
        private final CartListItemBinding binding;
        private final ActivityCartBinding cartBinding;
        private CartViewHolder(CartListItemBinding binding,ActivityCartBinding cartBinding) {
            super(binding.getRoot());
            this.binding = binding;
            // Register a callback to be invoked when this view is clicked.
            itemView.setOnClickListener(this);
            binding.imgFavourite.setOnClickListener(this);
            binding.imgCart.setOnClickListener(this);
            binding.incrementQuantity.setOnClickListener(this);
            binding.decrementQuantity.setOnClickListener(this);
            this.cartBinding = cartBinding;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // Get position of the product
            currentProduct = productsInCart.get(position);

            switch (v.getId()) {
                case R.id.card_view:
                    // Send product through click
                    clickHandler.onClick(currentProduct);
                    break;
                case R.id.imgFavourite:
                    toggleFavourite();
                    break;
                case R.id.imgCart:
                    deleteProductsInCart();
                    break;
                case R.id.incrementQuantity:
                    incrementProductQuantity(currentProduct);
                    break;
                case R.id.decrementQuantity:
                    decrementProductQuantity(currentProduct);
                    break;

            }
        }




        private void toggleFavourite() {
            // If favorite is not bookmarked
            if (currentProduct.isFavourite() != 1) {
                binding.imgFavourite.setImageResource(R.drawable.ic_favorite_pink);
                insertFavoriteProduct(() -> {
                    currentProduct.setIsFavourite(true);
                    notifyDataSetChanged();
                });
                showSnackBar("Bookmark Added");
            } else {
                binding.imgFavourite.setImageResource(R.drawable.ic_favorite_border);
                deleteFavoriteProduct(() -> {
                    currentProduct.setIsFavourite(false);
                    notifyDataSetChanged();
                });
                showSnackBar("Bookmark Removed");
            }
        }
        private void decrementProductQuantity(Product currentProduct) {
            int q = Integer.parseInt(binding.txtQuantity.getText().toString());
            if(q>1){
                binding.txtQuantity.setText(String.valueOf(q-1));
                decrementTotal();
            }

        }

        private void incrementProductQuantity(Product currentProduct) {

            int q = Integer.parseInt(binding.txtQuantity.getText().toString());
//            if(q<currentProduct.getProductQuantity()){
            binding.txtQuantity.setText(String.valueOf(q+1));
            incrementTotal();
//            }
        }

        private void incrementTotal(){
            double total = Double.parseDouble(cartBinding.totalPrice.getText().toString());
            total+=currentProduct.getProductPrice();
            cartBinding.totalPrice.setText(String.valueOf(total));
        }
        private void decrementTotal(){
            double total = Double.parseDouble(cartBinding.totalPrice.getText().toString());
            total-=currentProduct.getProductPrice();
            cartBinding.totalPrice.setText(String.valueOf(total));
        }

        private void deleteProductsInCart() {
            deleteFromCart(() -> {
                currentProduct.setIsInCart(false);
                notifyDataSetChanged();
            });
            productsInCart.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            decrementTotal();
            notifyItemRangeChanged(getAdapterPosition(), productsInCart.size());
            showSnackBar("Removed From Cart");
        }

        private void showSnackBar(String text) {
            Snackbar.make(itemView, text, Snackbar.LENGTH_SHORT).show();
        }

        private void insertFavoriteProduct(RequestCallback callback) {
            com.example.beckart.model.Favorite favorite = new com.example.beckart.model.Favorite(com.example.beckart.storage.LoginUtils.getInstance(mContext).getUserInfo().getId(), currentProduct.getProductId());
            addFavoriteViewModel.addFavorite(favorite, callback);
        }

        private void deleteFavoriteProduct(RequestCallback callback) {
            removeFavoriteViewModel.
                    removeFavorite(com.example.beckart.storage.LoginUtils.getInstance(mContext).getUserInfo().getId(), currentProduct.getProductId(), callback);
        }

        private void deleteFromCart(RequestCallback callback) {
            fromCartViewModel.removeFromCart(com.example.beckart.storage.LoginUtils.getInstance(mContext).getUserInfo().getId(), currentProduct.getProductId(),callback);
        }
    }
}
