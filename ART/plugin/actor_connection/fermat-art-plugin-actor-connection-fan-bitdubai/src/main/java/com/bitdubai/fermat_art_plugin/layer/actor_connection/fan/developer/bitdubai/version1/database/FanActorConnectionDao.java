package com.bitdubai.fermat_art_plugin.layer.actor_connection.fan.developer.bitdubai.version1.database;

import com.bitdubai.fermat_api.layer.actor_connection.common.database_abstract_classes.ActorConnectionDao;
import com.bitdubai.fermat_api.layer.actor_connection.common.database_common_classes.ActorConnectionDatabaseConstants;
import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantGetProfileImageException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_art_api.layer.actor_connection.fan.utils.FanActorConnection;
import com.bitdubai.fermat_art_api.layer.actor_connection.fan.utils.FanLinkedActorIdentity;

import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 04/04/16.
 */
public class FanActorConnectionDao extends ActorConnectionDao<FanLinkedActorIdentity, FanActorConnection> {

    /**
     * Constructor with parameters.
     * @param pluginDatabaseSystem
     * @param pluginFileSystem
     * @param pluginId
     */
    public FanActorConnectionDao(
            final PluginDatabaseSystem pluginDatabaseSystem,
            final PluginFileSystem pluginFileSystem,
            final UUID pluginId) {
        super(
                pluginDatabaseSystem,
                pluginFileSystem,
                pluginId
        );
    }

    /**
     * This method returns an actor connection record.
     * @param record
     * @return
     * @throws InvalidParameterException
     */
    protected FanActorConnection buildActorConnectionNewRecord(
            final DatabaseTableRecord record) throws
            InvalidParameterException {

        try {
            UUID connectionId = record.getUUIDValue(
                    ActorConnectionDatabaseConstants.ACTOR_CONNECTIONS_CONNECTION_ID_COLUMN_NAME);
            String linkedIdentityPublicKey = record.getStringValue(
                    ActorConnectionDatabaseConstants.
                            ACTOR_CONNECTIONS_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
            String linkedIdentityActorTypeString = record.getStringValue(
                    ActorConnectionDatabaseConstants.
                            ACTOR_CONNECTIONS_LINKED_IDENTITY_ACTOR_TYPE_COLUMN_NAME);
            String publicKey = record.getStringValue(
                    ActorConnectionDatabaseConstants.ACTOR_CONNECTIONS_PUBLIC_KEY_COLUMN_NAME);
            String alias = record.getStringValue(
                    ActorConnectionDatabaseConstants.ACTOR_CONNECTIONS_ALIAS_COLUMN_NAME);
            String connectionStateString = record.getStringValue(
                    ActorConnectionDatabaseConstants.ACTOR_CONNECTIONS_CONNECTION_STATE_COLUMN_NAME);
            long creationTime = record.getLongValue(
                    ActorConnectionDatabaseConstants.ACTOR_CONNECTIONS_CREATION_TIME_COLUMN_NAME);
            long updateTime = record.getLongValue(
                    ActorConnectionDatabaseConstants.ACTOR_CONNECTIONS_UPDATE_TIME_COLUMN_NAME);
            ConnectionState connectionState = ConnectionState.getByCode(connectionStateString);
            Actors linkedIdentityActorType = Actors.getByCode(linkedIdentityActorTypeString);
            FanLinkedActorIdentity actorIdentity = new FanLinkedActorIdentity(
                    linkedIdentityPublicKey,
                    linkedIdentityActorType
            );
            byte[] profileImage;
            try {
                profileImage = getProfileImage(publicKey);
            } catch (FileNotFoundException e) {
                profileImage = new byte[0];
            }
            return new FanActorConnection(
                    connectionId,
                    actorIdentity,
                    publicKey,
                    alias,
                    profileImage,
                    connectionState,
                    creationTime,
                    updateTime
            );
        } catch (final CantGetProfileImageException e) {
            throw new InvalidParameterException(
                    e,
                    "",
                    "Problem trying to get the profile image."
            );
        }
    }
}
