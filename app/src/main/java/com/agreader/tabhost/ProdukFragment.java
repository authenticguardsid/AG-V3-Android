package com.agreader.tabhost;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.agreader.R;
import com.agreader.adapter.produkAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProdukFragment extends Fragment {


    public ProdukFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produk, container, false);

        GridView gridView = view.findViewById(R.id.gridview);
        gridView.setAdapter(new produkAdapter(getContext()));

        return view;
    }

}
