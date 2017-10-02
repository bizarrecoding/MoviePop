package com.bizarrecoding.example.moviepop.Utils;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Herik on 29/9/2017.
 */

public class GlobalFunctions {

    public static void showProgress(boolean show, View progress, View element, View error){
        progress.setVisibility( show ? View.VISIBLE : View.INVISIBLE);
        element.setVisibility( show ? View.INVISIBLE : View.VISIBLE);
        error.setVisibility(View.INVISIBLE);
    }

    public static void showError(int errorRes, View progress, View element, View errorTV) {
        element.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.INVISIBLE);
        errorTV.setVisibility(View.VISIBLE);
        ((TextView)errorTV).setText(errorRes);
    }
}
