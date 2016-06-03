package org.fermat.fermat_dap_android_sub_app_asset_factory.navigation_drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_dap_android_sub_app_asset_factory_bitdubai.R;

import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.AssetFactorySession;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetIdentityAssetIssuerException;

import java.lang.ref.WeakReference;

/**
 * Created by frank on 12/9/15.
 */
public class AssetFactoryNavigationViewPainter implements NavigationViewPainter {

    private WeakReference<Context> activity;
    private final ActiveActorIdentityInformation identityAssetIssuer;
    AssetFactorySession assetFactorySession;

    public AssetFactoryNavigationViewPainter(Context activity, AssetFactorySession assetFactorySession, ActiveActorIdentityInformation identityAssetIssuer) {
        this.activity = new WeakReference<Context>(activity);
        this.identityAssetIssuer = identityAssetIssuer;
        this.assetFactorySession = assetFactorySession;
    }

    @Override
    public View addNavigationViewHeader(ActiveActorIdentityInformation identityAssetIssuer) {
        try {
            return FragmentsCommons.setUpHeaderScreen((LayoutInflater) activity.get()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE), activity.get(), assetFactorySession, identityAssetIssuer);
        } catch (CantGetIdentityAssetIssuerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FermatAdapter addNavigationViewAdapter() {
        try {
            return new AssetFactoryNavigationViewAdapter(activity.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ViewGroup addNavigationViewBodyContainer(LayoutInflater layoutInflater, ViewGroup base) {
//
// dap v3
        return (RelativeLayout) layoutInflater.inflate(R.layout.dap_v3_navigation_drawer_factory_bottom, base, true);

        //DAP V2
        //return null;
    }

    //DAP V3
    @Override
    public Bitmap addBodyBackground() {
        Bitmap drawable = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            options.inSampleSize = 5;
            drawable = BitmapFactory.decodeResource(
                    activity.get().getResources(), R.drawable.gradient_factory_navigation_drawer, options);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return drawable;
    }

    //DAP V2
    // @Override
    //public Bitmap addBodyBackground() {
    //return null;
    //}

    @Override
    public int addBodyBackgroundColor() {
        return 0;
    }

    @Override
    public RecyclerView.ItemDecoration addItemDecoration() {
        return null;
    }

    @Override
    public boolean hasBodyBackground() {

        //dap v3
        return true;

        //dap v2
        //return false;
    }

    @Override
    public boolean hasClickListener() {
        return false;
    }
}
