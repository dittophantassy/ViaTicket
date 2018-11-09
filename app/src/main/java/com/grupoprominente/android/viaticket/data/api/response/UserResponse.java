package com.grupoprominente.android.viaticket.data.api.response;

import com.google.gson.annotations.SerializedName;
import com.grupoprominente.android.viaticket.models.User;

public class UserResponse extends GenericResponse {
    @SerializedName("Usuario")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
