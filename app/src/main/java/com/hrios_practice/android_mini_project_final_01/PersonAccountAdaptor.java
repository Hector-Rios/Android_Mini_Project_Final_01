package com.hrios_practice.android_mini_project_final_01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import okhttp3.Request;

public class PersonAccountAdaptor extends RecyclerView.Adapter{

    protected String[] account_name;
    protected String[] account_img_links;
    protected User[] account_users;

    public PersonAccountAdaptor(String[] givenAccounts, String[] givenImgLinks)
    {    account_name = givenAccounts;   account_img_links = givenImgLinks; }

    class AccountViewHolder extends RecyclerView.ViewHolder
        {
            protected TextView AccountNameTextView;
            protected ImageView AccountProfileImgView;

            public AccountViewHolder(@NonNull View itemView) {
                super(itemView);

                AccountNameTextView = itemView.findViewById(R.id.account_name_text_view);
                AccountProfileImgView = itemView.findViewById(R.id.account_img_view);

            }

            public void bind(String AccountName, String ImageLink)
            {
                AccountNameTextView.setText(AccountName);
                AccountProfileImgView.setTag(AccountName);

                Picasso.get().load(ImageLink).into(AccountProfileImgView);
            }
        }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_adaptor_item_view, parent, false);

        AccountViewHolder myView = new AccountViewHolder(view);

        return myView;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AccountViewHolder) holder).bind(account_name[position], account_img_links[position]);
    }

    @Override
    public int getItemCount() {
        return account_name.length;
    }
}
