package org.fermat.fermat_dap_core.layer.digital_asset_transaction.issuer_redemption;

import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.issuer_redemption.developer.DeveloperBitDubai;

/**
 * Created by lnacosta - (laion.cj91@gmail.com) on 11/11/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class IssuerRedemptionPluginSubsystem extends AbstractPluginSubsystem {

    public IssuerRedemptionPluginSubsystem() {
        super(new PluginReference(Plugins.ISSUER_REDEMPTION));
    }

    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new DeveloperBitDubai());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
}
