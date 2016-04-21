package com.bitdubai.fermat_art_api.layer.sub_app_module.community.artist.interfaces;

import com.bitdubai.fermat_art_api.layer.sub_app_module.community.artist.exceptions.CantGetArtistSearchResult;

import java.util.List;

/**
 * Created by Alexander Jimenez (alex_jimenez76@hotmail.com) on 4/6/16.
 */
public interface ArtistCommunitySearch {
    void addAlias(String alias);

    List<ArtistCommunityInformation> getResult() throws CantGetArtistSearchResult;

}
