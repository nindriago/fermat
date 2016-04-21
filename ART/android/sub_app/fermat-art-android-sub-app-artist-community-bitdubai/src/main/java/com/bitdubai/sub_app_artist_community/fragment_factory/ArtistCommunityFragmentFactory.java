package com.bitdubai.sub_app_artist_community.fragment_factory;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.exceptions.FragmentNotFoundException;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.sub_app_artist_community.fragments.ConnectionNotificationsFragment;
import com.bitdubai.sub_app_artist_community.fragments.ConnectionOtherProfileFragment;
import com.bitdubai.sub_app_artist_community.fragments.ConnectionsFragment;
import com.bitdubai.sub_app_artist_community.fragments.ConnectionsListFragment;
import com.bitdubai.sub_app_artist_community.fragments.ConnectionsWorldFragment;
import com.bitdubai.sub_app_artist_community.sessions.ArtistSubAppSession;

/**
 * Created by Gabriel Araujo (gabe_512@hotmail.com) on 08/04/16.
 */
public class ArtistCommunityFragmentFactory extends FermatFragmentFactory<ArtistSubAppSession,SubAppResourcesProviderManager,ArtistCommunityFragmentEnumType> {
    @Override
    protected AbstractFermatFragment getFermatFragment(ArtistCommunityFragmentEnumType fragments) throws FragmentNotFoundException {
        AbstractFermatFragment currentFragment = null;
        switch (fragments) {
            case ART_ARTIST_WALLET_STORE_ALL_FRAGMENT:
                currentFragment = ConnectionsWorldFragment.newInstance();
                break;
            case ART_SUB_APP_ARTIST_COMMUNITY_CONNECTIONS:
                currentFragment = ConnectionsFragment.newInstance();
                break;
            case ART_SUB_APP_ARTIST_COMMUNITY_CONNECTION_DETAIL:
                currentFragment = null;
                break;
            case ART_SUB_APP_ARTIST_COMMUNITY_CONNECTION_NOTIFICATIONS:
                currentFragment = ConnectionNotificationsFragment.newInstance();
                break;
            case ART_SUB_APP_ARTIST_COMMUNITY_CONNECTION_OTHER_PROFILE:
                currentFragment = ConnectionOtherProfileFragment.newInstance();
                break;
            case ART_SUB_APP_ARTIST_COMMUNITY_CONNECTION_WORLD:
                currentFragment = ConnectionsWorldFragment.newInstance();
                break;
            case ART_SUB_APP_ARTIST_COMMUNITY_CONNECTION_FRIEND_LIST:
                currentFragment = ConnectionsListFragment.newInstance();
                break;
            default:
                throw new FragmentNotFoundException(
                        fragments.toString(),
                        "Switch failed");
        }
        return currentFragment;
    }

    @Override
    public ArtistCommunityFragmentEnumType getFermatFragmentEnumType(String key) {
        return ArtistCommunityFragmentEnumType.getValue(key);
    }
}
