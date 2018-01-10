package com.harish.solstice.solsticecontacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.harish.solstice.solsticecontacts.obj.Contact;
import com.harish.solstice.solsticecontacts.util.Config;
import com.harish.solstice.solsticecontacts.util.ContactDetailView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Harish Veeramani on 1/9/2018.
 */

public class ContactDetail extends AppCompatActivity {
    @BindView(R.id.contactName)
    TextView contactName;
    @BindView(R.id.contactImage)
    ImageView contactImage;
    @BindView(R.id.companyName)
    TextView companyName;
    @BindView(R.id.homePhoneCIV)
    ContactDetailView homePhone;
    @BindView(R.id.mobilePhoneCIV)
    ContactDetailView mobilePhone;
    @BindView(R.id.workPhoneCIV)
    ContactDetailView workPhone;
    @BindView(R.id.addressCIV)
    ContactDetailView address;
    @BindView(R.id.birthdayCIV)
    ContactDetailView birthday;
    @BindView(R.id.emailCIV)
    ContactDetailView email;

    private Contact mContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail_view);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            mContact = (Contact) intent.getSerializableExtra(Config.CONTACT_KEY);
        }

        if (mContact != null) {
            contactName.setText(mContact.getName());
            companyName.setText(mContact.getCompanyName());
            setContactImage();
            setHomePhone();
            setMobilePhone();
            setWorkPhone();
            setAddress();
            setBirthday();
            setEmail();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.favorite_toggle);

        if (mContact.getIsFavorite()){
            menuItem.setIcon(R.drawable.ic_favorite_true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favorite_toggle) {
            markAsFavorite(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(Config.CONTACT_KEY, mContact);
        setResult(RESULT_OK, dataIntent);
        finish();
        super.onBackPressed();
    }

    private void setContactImage() {
        Picasso.with(this).load(mContact.getLargeImageURL()).placeholder(R.drawable.ic_placeholder_image)
                .into(contactImage);
    }

    private void setHomePhone() {
        if (mContact != null && mContact.getPhone().getHome() != null && !mContact.getPhone().getHome().isEmpty()){
            homePhone.setTitleText(getString(R.string.phoneText));
            homePhone.setDescriptionText(formatPhoneNumber(mContact.getPhone().getHome()));
            homePhone.setTypeText(getString(R.string.homeText));
        } else {
            homePhone.setVisibility(View.GONE);
        }
    }

    private void setMobilePhone() {
        if (mContact != null && mContact.getPhone().getMobile() != null && !mContact.getPhone().getMobile().isEmpty()){
            mobilePhone.setTitleText(getString(R.string.phoneText));
            mobilePhone.setDescriptionText(formatPhoneNumber(mContact.getPhone().getMobile()));
            mobilePhone.setTypeText(getString(R.string.mobileText));
        } else {
            mobilePhone.setVisibility(View.GONE);
        }
    }

    private void setWorkPhone() {
        if (mContact != null && mContact.getPhone().getWork() != null && !mContact.getPhone().getWork().isEmpty()){
            workPhone.setTitleText(getString(R.string.phoneText));
            workPhone.setDescriptionText(formatPhoneNumber(mContact.getPhone().getWork()));
            workPhone.setTypeText(getString(R.string.workText));
        } else {
            workPhone.setVisibility(View.GONE);
        }
    }

    private void setAddress(){
        if (mContact != null && mContact.getAddress() != null && !mContact.getAddress().toString().isEmpty()){
            address.setTitleText(getString(R.string.addressText));
            address.setDescriptionText(getString(R.string.addressToSet, mContact.getAddress().getStreet(),
                    mContact.getAddress().getCity(), mContact.getAddress().getState(),
                    mContact.getAddress().getZipCode(), mContact.getAddress().getCountry()));
        } else {
            address.setVisibility(View.GONE);
        }
    }

    private void setBirthday(){
        if (mContact != null && mContact.getBirthdate() != null && !mContact.getBirthdate().isEmpty()){
            birthday.setTitleText(getString(R.string.birthdayText));
            try {
                birthday.setDescriptionText(formatDate(mContact.getBirthdate()));
            } catch (Exception e){
                Toast.makeText(this, "Error Getting Date", Toast.LENGTH_SHORT).show();
            }

        } else {
            birthday.setVisibility(View.GONE);
        }
    }

    private void setEmail(){
        if (mContact != null && mContact.getEmailAddress() != null && !mContact.getEmailAddress().isEmpty()){
            email.setTitleText(getString(R.string.emailText));
            email.setDescriptionText(mContact.getEmailAddress());
        } else {
            email.setVisibility(View.GONE);
        }
    }

    private void markAsFavorite(MenuItem item) {
        if (mContact.getIsFavorite()) {
            mContact.setIsFavorite(false);
            item.setIcon(R.drawable.ic_favorite_false);
        } else {
            mContact.setIsFavorite(true);
            item.setIcon(R.drawable.ic_favorite_true);
        }
    }

    private static String formatDate(String dateString) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");
        Date date = dateFormat.parse(dateString);
        return outputFormat.format(date);
    }

    private static String formatPhoneNumber(String phoneString){
        StringBuilder stringBuilder = new StringBuilder(phoneString);
        stringBuilder.insert(0, '(');
        stringBuilder.insert(4, ')');

        return stringBuilder.toString();
    }
}
