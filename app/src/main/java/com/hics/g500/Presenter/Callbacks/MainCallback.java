package com.hics.g500.Presenter.Callbacks;

/**
 * Created by david.barrera on 2/18/18.
 */

public interface MainCallback {

    void onSuccessLogOut(boolean isLogout);

    void onErrorLogOut(String msgError);
}
