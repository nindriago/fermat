/*
 * @#ComponentVersionsDetails.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */

package com.bitdubai.fermat_api.layer.dmp_middleware.wallet_publisher.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.resources_structure.enums.ScreenSize;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_factory.enums.DescriptorFactoryProjectType;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_publisher.enums.ComponentPublishedInformationStatus;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_api.layer.dmp_middleware.wallet_publisher.interfaces.InformationPublishedComponent</code> define
 * the static information about the detail version published component.
 * <p/>
 *
 * @author Ezequiel Postan (ezequiel.postan@gmail.com)
 * @author Update by Roberto Requena - (rart3001@gmail.com) on 06/08/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public interface ComponentVersionsDetails {

    /**
     * Get the id
     *
     * @return UUID
     */
    public UUID getId();

    /**
     * Get the Catalog Id
     *
     * @return UUID
     */
    public UUID getCatalogId();

    /**
     * Get the Screen Size
     *
     * @return ScreenSize
     */
    public ScreenSize getScreenSize();

    /**
     * Get the Initial Wallet Version Supported
     *
     * @return Version
     */
    public Version getInitialWalletVersion();

    /**
     * Get the Final Wallet Version Supported
     *
     * @return Version
     */
    public Version getFinalWalletVersion();

    /**
     * Get the Initial Platform Version Supported
     *
     * @return Version
     */
    public Version getInitialPlatformVersion();

    /**
     * Get the Final Platform Version Supported
     *
     * @return Version
     */
    public Version getFinalPlatformVersion();

    /**
     * Get the Version
     *
     * @return Version
     */
    public Version getVersion();

    /**
     * Get the Version Timestamp
     *
     * @return Timestamp
     */
    public Timestamp getVersionTimestamp();

    /**
     * Get the Component Id
     *
     * @return UUID
     */
    public UUID getComponentId();

    /**
     * Get the observations
     *
     * @return String
     */
    public String getObservations();

}
