/*
 * @#ImageData.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_api.layer.dmp_middleware.wallet_publisher.interfaces;

import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_api.layer.dmp_middleware.wallet_publisher.interfaces.ImageData</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 11/08/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public interface ImageData {

    /**
     * Get the id
     *
     * @return UUID
     */
    public UUID getId();

    /**
     * Get the FileId
     *
     * @return UUID
     */
    public UUID getFileId();

    /**
     * Get the Component Id
     *
     * @return UUID
     */
    public UUID getComponentId();
}
