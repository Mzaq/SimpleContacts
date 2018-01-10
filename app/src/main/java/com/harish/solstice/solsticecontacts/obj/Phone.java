package com.harish.solstice.solsticecontacts.obj;

/**
 * Created by Harish Veeramani on 1/8/2018.
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Phone implements Serializable {

    private String work;
    private String home;
    private String mobile;

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}