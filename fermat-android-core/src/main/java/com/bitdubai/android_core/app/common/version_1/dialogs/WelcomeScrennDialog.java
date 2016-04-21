package com.bitdubai.android_core.app.common.version_1.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bitdubai.fermat.R;
import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatScreenSwapper;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.session.ReferenceWalletSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mati on 2015.11.27..
 */
public class WelcomeScrennDialog extends FermatDialog<ReferenceWalletSession,SubAppResourcesProviderManager> implements View.OnClickListener{

    private final Activity activity;
    private RadioGroup radio_group;
    private ViewPager dialog_pager;
    private DialogViewPagerAdapter abstractViewPagerAdapter;
    private List<RadioButton> radioButtons;
    private int radioSelected;
    private LinearLayout pager_container;
    private Button btc_got_it;


    /**
     * Constructor using Session and Resources
     *
     * @param activity
     * @param fermatSession parent class of walletSession and SubAppSession
     * @param resources     parent class of WalletResources and SubAppResources
     */
    public WelcomeScrennDialog(Activity activity, ReferenceWalletSession fermatSession, SubAppResourcesProviderManager resources) {
        super(activity, fermatSession, resources);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioButtons = new ArrayList<>();

        try {

            radio_group = (RadioGroup) findViewById(R.id.radio_group);
            pager_container = (LinearLayout) findViewById(R.id.pager_container);
            dialog_pager = (ViewPager) findViewById(R.id.dialog_pager);
            btc_got_it = (Button) findViewById(R.id.btc_got_it);

            List list = WelcomeDialogsData.getWelcomeDialogData();


            for (int i = 0; i < list.size(); i++) {
                RadioButton radioButton = new RadioButton(getContext());
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                radioButton.setLayoutParams(lp);
                radio_group.addView(radioButton, i, lp);
                radioButtons.add(radioButton);
            }
            radioSelected = 0;

            radio_group.check(radioButtons.get(radioSelected).getId());

            abstractViewPagerAdapter = new DialogViewPagerAdapter(getContext(), list);

            dialog_pager.setAdapter(abstractViewPagerAdapter);
            dialog_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    radio_group.check(radioButtons.get(position).getId());
                    radioSelected = position;
                }

                @Override
                public void onPageSelected(int position) {
                    radio_group.check(radioButtons.get(position).getId());
                    radioSelected = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            btc_got_it.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    protected int setLayoutId() {
        return R.layout.welcome_dialog;
    }

    @Override
    protected int setWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

//        if(id == R.id.add_fermat_user){
//            try {
////                Object[] object = new Object[2];
////                changeApp(Engine.BITCOIN_WALLET_CALL_INTRA_USER_COMMUNITY, getSession().getCommunityConnection(), object);
//                changeActivity(Activities.CCP_BITCOIN_WALLET_ADD_CONNECTION_ACTIVITY);
//                dismiss();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        else if(id == R.id.add_extra_user){
//            getSession().setData(SessionConstant.CREATE_EXTRA_USER, Boolean.TRUE);
//
//            dismiss();
//        }
    }

    private void changeActivity(Activities ccpBitcoinWalletAddConnectionActivity) {
        ((FermatScreenSwapper)activity).changeActivity(ccpBitcoinWalletAddConnectionActivity.getCode(),getSession().getAppPublicKey());
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
