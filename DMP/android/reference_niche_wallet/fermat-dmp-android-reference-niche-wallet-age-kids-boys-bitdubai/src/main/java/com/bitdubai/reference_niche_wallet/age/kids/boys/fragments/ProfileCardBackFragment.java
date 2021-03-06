package com.bitdubai.reference_niche_wallet.age.kids.boys.fragments;

/**
 * Created by ciencias on 25.11.14.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitdubai.fermat_dmp.wallet_runtime.R;


public class ProfileCardBackFragment extends Fragment {

    /**
     * ProfileCardBackFragment member variables.
     */
    private static final String ARG_POSITION = "position";
    private int position;

    /**
     * Constructor.
     */

    public static ProfileCardBackFragment newInstance(int position) {
        ProfileCardBackFragment f = new ProfileCardBackFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    /**
     * Fragment Class implementation.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        view = inflater.inflate(R.layout.wallets_kids_fragment_profile_card_back, container, false); //Contains empty RelativeLayout
        return view;
    }


}