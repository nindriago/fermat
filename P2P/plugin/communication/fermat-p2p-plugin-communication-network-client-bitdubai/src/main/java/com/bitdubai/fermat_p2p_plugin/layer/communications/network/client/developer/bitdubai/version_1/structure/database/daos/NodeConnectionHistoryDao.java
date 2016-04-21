package com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.daos;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.entities.NodeConnectionHistory;

import java.sql.Timestamp;

import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.NODE_CONNECTION_HISTORY_DEFAULT_PORT_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.NODE_CONNECTION_HISTORY_FIRST_KEY_COLUMN;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.NODE_CONNECTION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.NODE_CONNECTION_HISTORY_IP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.NODE_CONNECTION_HISTORY_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.NODE_CONNECTION_HISTORY_LATITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.NODE_CONNECTION_HISTORY_LONGITUDE_COLUMN_NAME;
import static com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.NetworkClientP2PDatabaseConstants.NODE_CONNECTION_HISTORY_TABLE_NAME;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.client.developer.bitdubai.version_1.structure.database.daos.NodeConnectionHistoryDao</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 13/11/15.
 * Updated by Leon Acosta - (laion.cj91@gmail.com) on 07/04/2016.
 *
 * @author lnacosta
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NodeConnectionHistoryDao extends AbstractBaseDao<NodeConnectionHistory>{

    public NodeConnectionHistoryDao(final Database dataBase) {

        super(
                dataBase                                ,
                NODE_CONNECTION_HISTORY_TABLE_NAME      ,
                NODE_CONNECTION_HISTORY_FIRST_KEY_COLUMN
        );
    }

    /**
     * @param record with values from the table
     *
     * @return NodeConnectionHistory setters the values from table
     */
    protected NodeConnectionHistory getEntityFromDatabaseTableRecord(final DatabaseTableRecord record) {

        try {

            NodeConnectionHistory NodeConnectionHistory = new NodeConnectionHistory(
                    record.getStringValue(NODE_CONNECTION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                    record.getStringValue(NODE_CONNECTION_HISTORY_IP_COLUMN_NAME),
                    record.getIntegerValue(NODE_CONNECTION_HISTORY_DEFAULT_PORT_COLUMN_NAME),
                    Double.valueOf(record.getStringValue(NODE_CONNECTION_HISTORY_LATITUDE_COLUMN_NAME)),
                    Double.valueOf(record.getStringValue(NODE_CONNECTION_HISTORY_LONGITUDE_COLUMN_NAME)),
                    new Timestamp(record.getLongValue(NODE_CONNECTION_HISTORY_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME))
            );

            return NodeConnectionHistory;

        } catch (Exception e) {
            //this should not happen, but if it happens return null
            e.printStackTrace();
            return null;
        }



    }

    protected DatabaseTableRecord getDatabaseTableRecordFromEntity(final NodeConnectionHistory nodeConnectionHistory) {

        /*
         * Create the record to the entity
         */
        DatabaseTableRecord entityRecord = getDatabaseTable().getEmptyRecord();

        /*
         * Set the entity values
         */
        entityRecord.setStringValue (NODE_CONNECTION_HISTORY_IDENTITY_PUBLIC_KEY_COLUMN_NAME      , nodeConnectionHistory.getIdentityPublicKey()                );
        entityRecord.setStringValue (NODE_CONNECTION_HISTORY_IP_COLUMN_NAME                       , nodeConnectionHistory.getIp()                               );
        entityRecord.setIntegerValue(NODE_CONNECTION_HISTORY_DEFAULT_PORT_COLUMN_NAME             , nodeConnectionHistory.getDefaultPort()                      );
        entityRecord.setDoubleValue (NODE_CONNECTION_HISTORY_LATITUDE_COLUMN_NAME                 , nodeConnectionHistory.getLatitude()                         );
        entityRecord.setDoubleValue (NODE_CONNECTION_HISTORY_LONGITUDE_COLUMN_NAME                , nodeConnectionHistory.getLongitude()                        );
        entityRecord.setLongValue   (NODE_CONNECTION_HISTORY_LAST_CONNECTION_TIMESTAMP_COLUMN_NAME, nodeConnectionHistory.getLastConnectionTimestamp().getTime());

        /*
         * return the new table record
         */
        return entityRecord;

    }


}
