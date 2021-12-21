package com.hrios_practice.android_mini_project_final_01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonAccountAdaptor extends RecyclerView.Adapter{

    protected String[] account_name;
    protected String[] account_num;

    public PersonAccountAdaptor(String[] givenAccounts, String[] givenIDs)
    {    account_name = givenAccounts;   account_num = givenIDs;    }

    class AccountViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView AccountNameTextView;
        protected TextView AccountIDTextView;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);

            AccountNameTextView = itemView.findViewById(R.id.account_name_text_view);
            AccountIDTextView = itemView.findViewById(R.id.account_ID_text_view);
        }

        public void bind(String AccountName, String AccountID)
        {
            AccountNameTextView.setText(AccountName);
            AccountIDTextView.setText(AccountID);
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
        ((AccountViewHolder) holder).bind(account_name[position], account_num[position]);

    }

    @Override
    public int getItemCount() {
        return account_name.length;
    }
}
