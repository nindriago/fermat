package org.fermat.fermat_dap_android_sub_app_asset_factory.factory;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.enums.FermatFragmentsEnumType;

import com.bitdubai.fermat_android_api.layer.definition.wallet.exceptions.FragmentNotFoundException;
import org.fermat.fermat_dap_android_sub_app_asset_factory.fragments.AssetEditorFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.fragments.EditableAssetsFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.fragments.PublishedAssetsFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.fragments.SettingsFactoryFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.fragments.SettingsFactoryNetworkFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.fragments.SettingsFactoryNotificationFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.AssetFactorySession;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;

/**
 * AssetFactoryFragmentFactory
 *
 * @author Francisco Vasquez on 15/09/15.
 * @version 1.0
 */
public class AssetFactoryFragmentFactory extends FermatFragmentFactory<AssetFactorySession, SubAppResourcesProviderManager, AssetFactoryFragmentsEnumType> {

    @Override
    public AbstractFermatFragment getFermatFragment(AssetFactoryFragmentsEnumType fragments) throws FragmentNotFoundException {
        if (fragments.equals(AssetFactoryFragmentsEnumType.DAP_SUB_APP_ASSET_FACTORY_EDITABLE_TAB_FRAGMENT))
                return EditableAssetsFragment.newInstance();
        if (fragments.equals(AssetFactoryFragmentsEnumType.DAP_SUB_APP_ASSET_FACTORY_PUBLISHED_TAB_FRAGMENT))
                return PublishedAssetsFragment.newInstance();
        if (fragments.equals(AssetFactoryFragmentsEnumType.DAP_SUB_APP_ASSET_EDITOR_ACTIVITY))
                return AssetEditorFragment.newInstance(EditableAssetsFragment.getAssetForEdit());
        if (fragments.equals(AssetFactoryFragmentsEnumType.DAP_SUB_APP_ASSET_FACTORY_SETTINGS))
            return SettingsFactoryFragment.newInstance();
        if (fragments.equals(AssetFactoryFragmentsEnumType.DAP_SUB_APP_ASSET_FACTORY_SETTINGS_NETWORK_MAIN))
            return SettingsFactoryNetworkFragment.newInstance();
        if (fragments.equals(AssetFactoryFragmentsEnumType.DAP_SUB_APP_ASSET_FACTORY_SETTINGS_NOTIFICATIONS))
            return SettingsFactoryNotificationFragment.newInstance();
        throw createFragmentNotFoundException(fragments);
    }

    @Override
    public AssetFactoryFragmentsEnumType getFermatFragmentEnumType(String key) {
        return AssetFactoryFragmentsEnumType.getValue(key);
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
