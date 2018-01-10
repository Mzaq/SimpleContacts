package com.harish.solstice.solsticecontacts;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.harish.solstice.solsticecontacts.obj.Contact;
import com.harish.solstice.solsticecontacts.util.CustomEventListener;
import com.harish.solstice.solsticecontacts.util.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harish Veeramani on 1/8/2018.
 */

public class MainActivity extends AppCompatActivity implements CustomEventListener {
    @BindView(R.id.contacts_recycler_view)
    RecyclerView recyclerView;

    private MainActivityViewModel mViewModel;
    private MainRecyclerViewAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<Contact> mContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAdapter = new MainRecyclerViewAdapter(this, this.getLayoutInflater(), this);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mProgressDialog = new ProgressDialog(this);
        mContactList = new ArrayList<>();

        setActionBarTitle();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mViewModel.callPhoneBookService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getShowErrorMessage().observe(this, this::serviceErrorMessage);
        mViewModel.getShowHideLoadingImage().observe(this, this::showHideProgressDialog);
        mViewModel.getShowPhoneBook().observe(this, this::showPhoneBookList);
    }

    private void showHideProgressDialog(Boolean hasToShow) {
        if (hasToShow) {
            mProgressDialog.setMessage(getString(R.string.loading_message));
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    private void showPhoneBookList(List<Contact> contactList) {
        if (contactList != null) {
            this.mContactList = contactList;
            sortPhoneBookList(contactList);
            mAdapter.addContactList(addHeadersList(contactList));
        }
    }

    private List<Object> addHeadersList(List<Contact> contactList) {
        List<Object> items = new ArrayList<>();
        boolean favoriteHeader = false, otherHeader = false;

        for (Contact contact : contactList) {
            if (contact.getIsFavorite() && !favoriteHeader) {
                items.add(getString(R.string.favorite_contacts_title));
                favoriteHeader = true;
            } else if (!contact.getIsFavorite() && !otherHeader) {
                items.add(getString(R.string.other_contacts_title));
                otherHeader = true;
            }

            items.add(contact);
        }

        return items;
    }

    private void sortPhoneBookList(List<Contact> contactList) {
        if (!contactList.isEmpty()) {
            Collections.sort(contactList, new Comparator<Contact>() {
                @Override
                public int compare(Contact o1, Contact o2) {
                    if (o2.getIsFavorite().compareTo(o1.getIsFavorite()) != 0) {
                        return o2.getIsFavorite().compareTo(o1.getIsFavorite());
                    }

                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
    }

    private void serviceErrorMessage(Boolean serviceHasError) {
        if (serviceHasError) {
            Snackbar.make(recyclerView, R.string.service_error_message, Snackbar.LENGTH_LONG);
        }
    }

    private void setActionBarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.activity_main_title);
        }
    }

    @Override
    public void onItemClicked(Contact contact) {
        Intent detailIntent = new Intent(this, ContactDetail.class);
        detailIntent.putExtra(Config.CONTACT_KEY, contact);
        startActivityForResult(detailIntent, Config.INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Config.INTENT_CODE) {
            if (data != null) {
                Contact contact = (Contact) data.getSerializableExtra(Config.CONTACT_KEY);

                for (int i = 0; i < mContactList.size(); i++) {
                    if (mContactList.get(i).getId().equals(contact.getId())) {
                        this.mContactList.set(i, contact);
                    }
                }
                showPhoneBookList(this.mContactList);
            }
        }
    }
}
