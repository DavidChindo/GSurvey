package com.hics.g500.Presenter.Callbacks;



import java.util.ArrayList;

/**
 * Created by david.barrera on 2/8/18.
 */

public interface GasolinerasCallback {

    void onSuccessLoadGasolineras(ArrayList<com.hics.g500.db.Gasolineras> gasolineras);

    void onErrorLoadGasolineras(String msgError);

}
