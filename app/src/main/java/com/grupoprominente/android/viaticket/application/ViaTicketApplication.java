package com.grupoprominente.android.viaticket.application;

import android.app.Application;

import com.orm.SugarContext;

public class ViaTicketApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        SugarContext.init(this);
    }
}
