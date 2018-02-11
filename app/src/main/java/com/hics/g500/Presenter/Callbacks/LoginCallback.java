package com.hics.g500.Presenter.Callbacks;

import com.hics.g500.Network.Request.LoginRequest;
import com.hics.g500.Network.Response.LoginResponse;

/**
 * Created by david.barrera on 2/8/18.
 */

public interface LoginCallback {

    void onSuccessLogin(LoginResponse loginResponse);

    void onErrorLogin(String msgError);
}
