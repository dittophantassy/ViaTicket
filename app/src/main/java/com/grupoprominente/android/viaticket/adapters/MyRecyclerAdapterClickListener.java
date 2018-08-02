package com.grupoprominente.android.viaticket.adapters;

import android.view.View;

/**
 * Created by FCouzo on 20/7/2018.
 */

public interface MyRecyclerAdapterClickListener {
    void onItemClick(View v, int position);
    void onItemLongClick(View v, int position);

}
