package com.eslam.mye_commerce.admins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eslam.mye_commerce.Model.Cart;
import com.eslam.mye_commerce.R;
import com.eslam.mye_commerce.ViewHolder.CartViewHolder;
import com.eslam.mye_commerce.databinding.ActivityAdminUserProductsBinding;
import com.eslam.mye_commerce.databinding.ActivityAdminsNewOrdersBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductsActivity extends AppCompatActivity {
    private ActivityAdminUserProductsBinding binding;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
FirebaseAuth auth;
    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminUserProductsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userID = getIntent().getStringExtra("uid");

        auth=FirebaseAuth.getInstance();

        binding.productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.productsList.setLayoutManager(layoutManager);


        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userID).child("Products");
    }


    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.txtProductPrice.setText("Price " + model.getPrice() + "$");
                holder.txtProductName.setText(model.getPname());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_item, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        binding.productsList.setAdapter(adapter);
        adapter.startListening();
    }
}