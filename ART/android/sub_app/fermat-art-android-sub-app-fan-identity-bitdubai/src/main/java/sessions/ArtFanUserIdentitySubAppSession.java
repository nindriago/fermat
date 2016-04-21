package sessions;

import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractFermatSession;
import com.bitdubai.fermat_api.layer.dmp_module.sub_app_manager.InstalledSubApp;
import com.bitdubai.fermat_art_api.layer.sub_app_module.identity.FanIdentityManagerModule;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 08/04/16.
 */
public class ArtFanUserIdentitySubAppSession extends
        AbstractFermatSession<
                InstalledSubApp,
                FanIdentityManagerModule,
                SubAppResourcesProviderManager> {
    public ArtFanUserIdentitySubAppSession() {
    }
}

