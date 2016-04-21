package org.fermat.fermat_dap_android_sub_app_asset_issuer_identity.fragmentFactory;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.enums.FermatFragmentsEnumType;
import com.bitdubai.fermat_android_api.layer.definition.wallet.exceptions.FragmentNotFoundException;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_identity.fragments.CreateIssuerIdentityFragment;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_identity.fragments.IdentityListFragment;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_identity.session.IssuerIdentitySubAppSession;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;

/**
 * Created by Matias Furszyfer on 2015.19.22..
 */
public class IssuerIdentityFragmentFactory extends FermatFragmentFactory<IssuerIdentitySubAppSession, SubAppResourcesProviderManager, IssuerIdentityFragmentsEnumType> {


    @Override
    public AbstractFermatFragment getFermatFragment(IssuerIdentityFragmentsEnumType fragments) throws FragmentNotFoundException {

        if (fragments.equals(IssuerIdentityFragmentsEnumType.DAP_SUB_APP_ASSET_ISSUER_IDENTITY_MAIN_FRAGMENT))
            return IdentityListFragment.newInstance();

        if (fragments.equals(IssuerIdentityFragmentsEnumType.DAP_SUB_APP_ASSET_ISSUER_IDENTITY_CREATE_IDENTITY_FRAGMENT))
            return CreateIssuerIdentityFragment.newInstance();


        throw createFragmentNotFoundException(fragments);
    }

    @Override
    public IssuerIdentityFragmentsEnumType getFermatFragmentEnumType(String key) {
        return IssuerIdentityFragmentsEnumType.getValue(key);
    }

    private FragmentNotFoundException createFragmentNotFoundException(FermatFragmentsEnumType fragments) {
        String possibleReason, context;

        if (fragments == null) {
            possibleReason = "The parameter 'fragments' is NULL";
            context = "Null Value";
        } else {
            possibleReason = "Not found in switch block";
            context = fragments.toString();
        }

        return new FragmentNotFoundException("Fragment not found", new Exception(), context, possibleReason);
    }

}
