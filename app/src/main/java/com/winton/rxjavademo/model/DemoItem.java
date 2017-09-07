package com.winton.rxjavademo.model;

import android.content.Intent;


/**
 * Created by winton on 2017/8/23.
 */
public class DemoItem {
    private String name;

    private Intent intent;

    public DemoItem(String name, Intent intent) {
        this.name = name;
        this.intent = intent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
