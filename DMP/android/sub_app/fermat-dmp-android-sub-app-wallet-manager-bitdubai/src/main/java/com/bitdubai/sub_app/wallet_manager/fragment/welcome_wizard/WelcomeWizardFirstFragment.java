package com.bitdubai.sub_app.wallet_manager.fragment.welcome_wizard;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.WizardPageListener;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_dmp.wallet_manager.R;

import java.util.Map;

/**
 * Created by mati on 2016.04.13..
 */
public class WelcomeWizardFirstFragment extends AbstractFermatFragment implements WizardPageListener{



    public static AbstractFermatFragment newInstance(){
        return new WelcomeWizardFirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_first,container,false);
        FermatTextView txt_version = (FermatTextView) view.findViewById(R.id.txt_version);
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        txt_version.setText(version);
        return view;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public void savePage() {

    }

    @Override
    public void onWizardFinish(Map<String, Object> data) {

    }

    @Override
    public void onActivated(Map<String, Object> data) {

    }

    @Override
    public CharSequence getTitle() {
        return null;
    }
}
