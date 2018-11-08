package com.grupoprominente.android.viaticket.data.serialization;

import android.content.Context;

import com.grupoprominente.android.viaticket.models.User;

public class UserSerializer extends ObjectSerializer {
    private final static String FILE_NAME = "User.dat";

    private static UserSerializer userSerializer;

    private UserSerializer()
    {
        if(userSerializer != null)
            throw new RuntimeException("Use getInstance() method to get the single instance of this class");
    }

    public static synchronized UserSerializer getInstance()
    {
        if(userSerializer == null)
            userSerializer = new UserSerializer();

        return userSerializer;
    }

    public void save(Context context, User user)
    {
        save(context, FILE_NAME, user);
    }

    public User load(Context context)
    {
        return (User) load(context, FILE_NAME);
    }

    public void clear(Context context)
    {
        save(context, null);
    }
}
