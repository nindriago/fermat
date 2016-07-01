package com.bitdubai.fermat_tky_plugin.layer.wallet_module.fan.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_tky_api.all_definitions.enums.SongStatus;
import com.bitdubai.fermat_tky_api.all_definitions.interfaces.User;
import com.bitdubai.fermat_tky_api.layer.external_api.exceptions.CantGetAlbumException;
import com.bitdubai.fermat_tky_api.layer.external_api.exceptions.CantGetBotException;
import com.bitdubai.fermat_tky_api.layer.external_api.exceptions.CantGetSongException;
import com.bitdubai.fermat_tky_api.layer.external_api.exceptions.CantGetUserException;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.TokenlyApiManager;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.music.Album;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.music.DownloadSong;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.music.MusicUser;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.music.Song;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.swapbot.Bot;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.Fan;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.TokenlyFanIdentityManager;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantDeleteSongException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantDownloadSongException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantGetSongListException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantGetSongStatusException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantSynchronizeWithExternalAPIException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantUpdateSongDevicePathException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.exceptions.CantUpdateSongStatusException;
import com.bitdubai.fermat_tky_api.layer.song_wallet.interfaces.SongWalletTokenlyManager;
import com.bitdubai.fermat_tky_api.layer.song_wallet.interfaces.WalletSong;
import com.bitdubai.fermat_tky_api.layer.wallet_module.FanWalletPreferenceSettings;
import com.bitdubai.fermat_tky_api.layer.wallet_module.interfaces.FanWalletModule;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alexander Jimenez (alex_jimenez76@hotmail.com) on 3/16/16.
 */
public class FanWalletModuleManagerImpl implements FanWalletModule {
    private final ErrorManager errorManager;
    private final SongWalletTokenlyManager songWalletTokenlyManager;
    private final TokenlyFanIdentityManager tokenlyFanIdentityManager;
    private final TokenlyApiManager tokenlyApiManager;
    private final PluginFileSystem pluginFileSystem;
    private final UUID pluginId;

    private SettingsManager<FanWalletPreferenceSettings> settingsManager;


    public FanWalletModuleManagerImpl(ErrorManager errorManager,
                                      SongWalletTokenlyManager songWalletTokenlyManager,
                                      TokenlyFanIdentityManager tokenlyFanIdentityManager,
                                      TokenlyApiManager tokenlyApiManager,
                                      PluginFileSystem pluginFileSystem,
                                      UUID pluginId) {
        this.errorManager = errorManager;
        this.songWalletTokenlyManager = songWalletTokenlyManager;
        this.tokenlyFanIdentityManager = tokenlyFanIdentityManager;
        this.tokenlyApiManager = tokenlyApiManager;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
    }


    @Override
    public List<WalletSong> getSongsBySongStatus(SongStatus songStatus) throws CantGetSongListException {
        return songWalletTokenlyManager.getSongsBySongStatus(songStatus);
    }

    @Override
    public List<WalletSong> getAvailableSongs() throws CantGetSongListException {
        return songWalletTokenlyManager.getAvailableSongs();
    }

    @Override
    public List<WalletSong> getDeletedSongs() throws CantGetSongListException {
        return songWalletTokenlyManager.getDeletedSongs();
    }

    @Override
    public SongStatus getSongStatus(UUID songId) throws CantGetSongStatusException {
        return songWalletTokenlyManager.getSongStatus(songId);
    }

    @Override
    public void synchronizeSongs(Fan fanIdentity) throws CantSynchronizeWithExternalAPIException {
        songWalletTokenlyManager.synchronizeSongs(fanIdentity);
    }

    @Override
    public void synchronizeSongsByUser(Fan fanIdentity) throws CantSynchronizeWithExternalAPIException {
        songWalletTokenlyManager.synchronizeSongsByUser(fanIdentity);
    }

    @Override
    public void deleteSong(UUID songId) throws CantDeleteSongException, CantUpdateSongStatusException {
        songWalletTokenlyManager.deleteSong(songId);
    }

    @Override
    public void downloadSong(UUID songId, MusicUser musicUser) throws CantDownloadSongException, CantUpdateSongDevicePathException, CantUpdateSongStatusException {
        songWalletTokenlyManager.downloadSong(songId,musicUser);
    }

    @Override
    public WalletSong getSongWithBytes(UUID songId) throws CantGetSongException {
        return songWalletTokenlyManager.getSongWithBytes(songId);
    }

    @Override
    public void cancelDownload() {
        songWalletTokenlyManager.cancelDownload();
    }

    @Override
    public List<Fan> listIdentitiesFromCurrentDeviceUser() throws CantListFanIdentitiesException {
        return tokenlyFanIdentityManager.listIdentitiesFromCurrentDeviceUser();
    }

    @Override
    public Bot getBotByBotId(String botId) throws CantGetBotException {
        return tokenlyApiManager.getBotByBotId(botId);
    }

    @Override
    public Bot getBotBySwapbotUsername(String username) throws CantGetBotException {
        return tokenlyApiManager.getBotBySwapbotUsername(username);
    }

    @Override
    public Album[] getAlbums() throws CantGetAlbumException {
        return tokenlyApiManager.getAlbums();
    }

    @Override
    public DownloadSong getDownloadSongBySongId(String id) throws CantGetSongException {
        return tokenlyApiManager.getDownloadSongBySongId(id);
    }

    @Override
    public User validateTokenlyUser(String username, String userKey) throws CantGetUserException, ExecutionException, InterruptedException {
        return tokenlyApiManager.validateTokenlyUser(username,userKey);
    }

    @Override
    public Song[] getSongsByAuthenticatedUser(MusicUser musicUser) throws CantGetAlbumException {
        return tokenlyApiManager.getSongsByAuthenticatedUser(musicUser);
    }

    @Override
    public Song getSongByAuthenticatedUser(MusicUser musicUser, String tokenlySongId) throws CantGetSongException {
        return tokenlyApiManager.getSongByAuthenticatedUser(musicUser,tokenlySongId);
    }

    @Override
    public SettingsManager<FanWalletPreferenceSettings> getSettingsManager() {
        if (this.settingsManager != null)
            return this.settingsManager;

        this.settingsManager = new SettingsManager<>(
                pluginFileSystem,
                pluginId
        );

        return this.settingsManager;
    }

    @Override
    public ActiveActorIdentityInformation getSelectedActorIdentity() throws CantGetSelectedActorIdentityException, ActorIdentityNotSelectedException {
        return null;
    }

    @Override
    public void createIdentity(String name, String phrase, byte[] profile_img) throws Exception {

    }

    @Override
    public void setAppPublicKey(String publicKey) {

    }

    @Override
    public int[] getMenuNotifications() {
        return new int[0];
    }
}
