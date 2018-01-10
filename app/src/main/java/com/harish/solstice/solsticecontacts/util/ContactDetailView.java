package com.harish.solstice.solsticecontacts.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.harish.solstice.solsticecontacts.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harish Veeramani on 1/9/2018.
 */

public class ContactDetailView extends ConstraintLayout {
    @BindView(R.id.info_title_text_view)
    TextView titleText;
    @BindView(R.id.info_description_text_view)
    TextView descriptionText;
    @BindView(R.id.typeText)
    TextView typeText;

    private void init(@Nullable AttributeSet attributeSet) {
        View view = inflate(getContext(), R.layout.list_item_contact_detail, this);
        ButterKnife.bind(this, view);

        if (attributeSet != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ContactDetailView);

            String titleText = attributes.getString(R.styleable.ContactDetailView_titleText);
            String descrText = attributes.getString(R.styleable.ContactDetailView_descriptionText);
            String typeText  = attributes.getString(R.styleable.ContactDetailView_typeText);

            this.titleText.setText(titleText);
            this.descriptionText.setText(descrText);
            this.typeText.setText(typeText);

            attributes.recycle();
        }
    }

    public ContactDetailView(Context context) {
        super(context);
        init(null);
    }

    public ContactDetailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public ContactDetailView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(attributeSet);
    }

    public void setTitleText(String titleText) {
        this.titleText.setText(titleText);
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText.setText(descriptionText);
    }

    public void setTypeText(String typeText) {
        this.typeText.setText(typeText);
    }
}