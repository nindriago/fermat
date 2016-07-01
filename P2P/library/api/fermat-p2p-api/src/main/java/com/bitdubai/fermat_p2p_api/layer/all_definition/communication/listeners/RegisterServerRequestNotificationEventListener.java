/*
* @#RegisterServerRequestNotificationEventListener.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.listeners;

import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventMonitor;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.listeners.RegisterServerRequestNotificationEventListener</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 12/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class RegisterServerRequestNotificationEventListener extends BasicFermatEventListener {

    /**
     * Constructor with parameters
     *
     * @param p2pEventType
     * @param eventMonitor
     */
    public RegisterServerRequestNotificationEventListener(P2pEventType p2pEventType, FermatEventMonitor eventMonitor) {
        super(p2pEventType, eventMonitor);
    }
}
