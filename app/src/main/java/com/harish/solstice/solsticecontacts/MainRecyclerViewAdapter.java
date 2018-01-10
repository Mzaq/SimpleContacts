package com.harish.solstice.solsticecontacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.harish.solstice.solsticecontacts.obj.Contact;
import com.harish.solstice.solsticecontacts.util.CustomEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harish Veeramani on 1/9/2018.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater mLayoutInflater;
    private List<Object> mItems;
    private Context mContext;
    private CustomEventListener mCustomEventListener;

    private static final int CONTACT_VIEW_TITLE = 0;
    private static final int CONTACT_VIEW_ITEM = 1;

    public MainRecyclerViewAdapter(Context context, LayoutInflater layoutInflater, CustomEventListener eventListener) {
        this.mLayoutInflater = layoutInflater;
        this.mContext = context;
        this.mCustomEventListener = eventListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CONTACT_VIEW_TITLE:
                View viewTitle = mLayoutInflater.inflate(R.layout.list_title, parent, false);
                return new TitleViewHolder(viewTitle);
            case CONTACT_VIEW_ITEM:
                View viewItem = mLayoutInflater.inflate(R.layout.list_item_contact, parent, false);
                return new ContactViewHolder(viewItem, mContext, mCustomEventListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item = mItems.get(position);

        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder)holder).bindTitle(item.toString());
        }

        if (holder instanceof ContactViewHolder) {
            ((ContactViewHolder)holder).bindContact((Contact)item);
        }
    }

    @Override
    public int getItemCount() {
        if (mItems != null){
            return mItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof String){
            return CONTACT_VIEW_TITLE;
        } else {
            return CONTACT_VIEW_ITEM;
        }
    }

    public void addContactList(List<Object> items) {
        if (this.mItems != null && !this.mItems.isEmpty()) {
            this.mItems.clear();
        }
        this.mItems = items;
        notifyDataSetChanged();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.contact_image_view)
        ImageView contactImage;
        @BindView(R.id.contact_name_text_view)
        TextView contactName;
        @BindView(R.id.contact_favorite_image_view)
        ImageView favoriteImage;
        @BindView(R.id.contact_description_text_view)
        TextView descriptionText;

        private Context context;
        private Contact mContact;
        private CustomEventListener mCustomEventListener;

        public ContactViewHolder(View itemView, Context context, CustomEventListener eventListener) {
            super(itemView);
            this.context = context;
            this.mCustomEventListener = eventListener;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindContact(Contact contact) {
            this.mContact = contact;

            contactName.setText(contact.getName());

            Picasso.with(context)
                    .load(contact.getSmallImageURL())
                    .placeholder(R.drawable.ic_placeholder_image)
                    .into(contactImage);

            if (contact.getIsFavorite()){
                favoriteImage.setVisibility(View.VISIBLE);
            } else {
                favoriteImage.setVisibility(View.INVISIBLE);
            }

            descriptionText.setText(contact.getCompanyName());
        }

        @Override
        public void onClick(View view) {
            if (mCustomEventListener != null) {
                mCustomEventListener.onItemClicked(mContact);
            }
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text)
        TextView mTitleText;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindTitle(String title) {
            mTitleText.setText(title);
        }
    }
}
