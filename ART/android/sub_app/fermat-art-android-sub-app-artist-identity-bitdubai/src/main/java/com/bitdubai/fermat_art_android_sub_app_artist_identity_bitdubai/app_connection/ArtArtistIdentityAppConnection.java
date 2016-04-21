package com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.app_connection;



import android.content.Context;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.engine.FooterViewPainter;
import com.bitdubai.fermat_android_api.engine.HeaderViewPainter;
import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.AppConnections;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.factory.ArtistIdentityFragmentFactory;
import com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.session.ArtistIdentitySubAppSession;

/**
 * Created by Juan Sulbaran sulbaranja@gmail.com on 17/03/16.
 */
public class ArtArtistIdentityAppConnection extends AppConnections {

    public ArtArtistIdentityAppConnection(Context activity) {


        super(activity);
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new ArtistIdentityFragmentFactory();
    }

    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        return null;
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return null;
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return null;
    }

    @Override
    public PluginVersionReference getPluginVersionReference() {
        return  new PluginVersionReference(
                Platforms.ART_PLATFORM,
                Layers.SUB_APP_MODULE,
                Plugins.ART_ARTIST_SUB_APP_MODULE,
                Developers.BITDUBAI,
                new Version()
        );
    }

    @Override
    protected AbstractFermatSession getSession() {
        return new ArtistIdentitySubAppSession();
    }

    //-----------------------------------------------------------------------------------------------------
    //not final

}
