package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.entities.ClientConnectionHistory;

import java.sql.Timestamp;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_ALIAS_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_COMPONENT_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_EXTRA_DATA_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_FIRST_KEY_COLUMN;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_LAST_LATITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_LAST_LONGITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_NAME_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_NETWORK_SERVICE_TYPE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.CLIENT_CONNECTION_HISTORY_TABLE_NAME;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.daos.ClientConnectionHistoryDao</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 13/11/15.
 * Updated by Leon Acosta - (laion.cj91@gmail.com) on 07/04/2016.
 *
 * @author lnacosta
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientConnectionHistoryDao extends AbstractBaseDao<ClientConnectionHistory> {

    public ClientConnectionHistoryDao(final Database dataBase) {

        super(
                dataBase,
                CLIENT_CONNECTION_HISTORY_TABLE_NAME,
                CLIENT_CONNECTION_HISTORY_FIRST_KEY_COLUMN
        );
    }

    /**
     * @param record with values from the table
     * @return KnownServerCatalogInfo setters the values from table
     */
    protected ClientConnectionHistory getEntityFromDatabaseTableRecord(final DatabaseTableRecord record) {

        try {

            return new ClientConnectionHistory(
                    record.getStringValue(CLIENT_CONNECTION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                    record.getStringValue(CLIENT_CONNECTION_HISTORY_NAME_COLUMN_NAME),
                    record.getStringValue(CLIENT_CONNECTION_HISTORY_ALIAS_COLUMN_NAME),
                    record.getStringValue(CLIENT_CONNECTION_HISTORY_COMPONENT_TYPE_COLUMN_NAME),
                    record.getStringValue(CLIENT_CONNECTION_HISTORY_NETWORK_SERVICE_TYPE_COLUMN_NAME),
                    Double.valueOf(record.getStringValue(CLIENT_CONNECTION_HISTORY_LAST_LATITUDE_COLUMN_NAME)),
                    Double.valueOf(record.getStringValue(CLIENT_CONNECTION_HISTORY_LAST_LONGITUDE_COLUMN_NAME)),
                    record.getStringValue(CLIENT_CONNECTION_HISTORY_EXTRA_DATA_COLUMN_NAME),
                    new Timestamp(record.getLongValue(CLIENT_CONNECTION_HISTORY_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME))
            );
        } catch (Exception e) {
            //this should not happen, but if it happens return null
            e.printStackTrace();
            return null;
        }
    }

    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(final ClientConnectionHistory clientConnectionHistory) {

         /*
         * Create the record to the entity
         */
        DatabaseTableRecord entityRecord = getDatabaseTable().getEmptyRecord();

        /*
         * Set the entity values
         */
        entityRecord.setStringValue(CLIENT_CONNECTION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME      , clientConnectionHistory.getIdentityPublicKey()                );
        entityRecord.setStringValue(CLIENT_CONNECTION_HISTORY_NAME_COLUMN_NAME                     , clientConnectionHistory.getName()                             );
        entityRecord.setStringValue(CLIENT_CONNECTION_HISTORY_ALIAS_COLUMN_NAME                    , clientConnectionHistory.getAlias()                            );
        entityRecord.setStringValue(CLIENT_CONNECTION_HISTORY_COMPONENT_TYPE_COLUMN_NAME           , clientConnectionHistory.getComponentType()                    );
        entityRecord.setStringValue(CLIENT_CONNECTION_HISTORY_NETWORK_SERVICE_TYPE_COLUMN_NAME     , clientConnectionHistory.getNetworkServiceType()               );
        entityRecord.setDoubleValue(CLIENT_CONNECTION_HISTORY_LAST_LATITUDE_COLUMN_NAME            , clientConnectionHistory.getLastLatitude()                     );
        entityRecord.setDoubleValue(CLIENT_CONNECTION_HISTORY_LAST_LONGITUDE_COLUMN_NAME           , clientConnectionHistory.getLastLongitude()                    );
        entityRecord.setStringValue(CLIENT_CONNECTION_HISTORY_EXTRA_DATA_COLUMN_NAME               , clientConnectionHistory.getExtraData()                        );
        entityRecord.setLongValue  (CLIENT_CONNECTION_HISTORY_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME, clientConnectionHistory.getLastConnectionTimestamp().getTime());

        /*
         * return the new table record
         */
        return entityRecord;
    }
}
