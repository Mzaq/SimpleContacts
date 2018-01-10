package com.harish.solstice.solsticecontacts.api;

import com.harish.solstice.solsticecontacts.obj.Contact;
import com.harish.solstice.solsticecontacts.util.Config;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Harish Veeramani on 1/9/2018.
 */

public interface APIService {
    @GET(Config.API_CALL)
    Observable<List<Contact>> getAllContacts();
}
