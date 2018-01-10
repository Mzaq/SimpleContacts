package com.harish.solstice.solsticecontacts;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harish.solstice.solsticecontacts.api.APIService;
import com.harish.solstice.solsticecontacts.obj.Contact;
import com.harish.solstice.solsticecontacts.util.Config;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Harish Veeramani on 1/9/2018.
 */

public class MainActivityViewModel extends ViewModel {
    private Retrofit mRetrofit;
    private CompositeDisposable mCompositeDisposable;
    private MutableLiveData<Boolean> mShowHideLoadingImage;
    private MutableLiveData<Boolean> mShowErrorMessage;
    private MutableLiveData<List<Contact>> mShowContacts;

    public MainActivityViewModel() {
        super();

        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Config.API_MAIN_URL)
                .build();

        mCompositeDisposable = new CompositeDisposable();
        mShowErrorMessage = new MutableLiveData<>();
        mShowHideLoadingImage = new MutableLiveData<>();
        mShowContacts = new MutableLiveData<>();
    }

    public void callAPIService(){
        APIService service = mRetrofit.create(APIService.class);

        mCompositeDisposable.add(service.getAllContacts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));

        setShowHideLoadingImage(true);
    }

    private void handleResponse(List<Contact> contactList) {
        setShowContacts(contactList);
        setShowHideLoadingImage(false);
    }

    private void handleError(Throwable error) {
        mShowErrorMessage.setValue(true);
        setShowHideLoadingImage(false);
    }

    public MutableLiveData<Boolean> getShowHideLoadingImage() {
        return mShowHideLoadingImage;
    }

    public void setShowHideLoadingImage(Boolean showHideStatus) {
        this.mShowHideLoadingImage.setValue(showHideStatus);
    }

    public MutableLiveData<Boolean> getShowErrorMessage() {
        return mShowErrorMessage;
    }

    public MutableLiveData<List<Contact>> getShowContacts() {
        return mShowContacts;
    }

    private void setShowContacts(List<Contact> contactList) {
        this.mShowContacts.setValue(contactList);
    }
}

