package com.bitdubai.sub_app.crypto_broker_identity.util;

import android.app.Activity;
import android.util.Log;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.interfaces.CryptoBrokerIdentityInformation;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.interfaces.CryptoBrokerIdentityModuleManager;
import com.bitdubai.sub_app.crypto_broker_identity.session.CryptoBrokerIdentitySubAppSession;

import static com.bitdubai.sub_app.crypto_broker_identity.session.CryptoBrokerIdentitySubAppSession.IDENTITY_INFO;

/**
 * Created by angel on 20/1/16.
 */

public class EditIdentityWorker extends FermatWorker {
    public static final int SUCCESS = 1;
    public static final int INVALID_ENTRY_DATA = 4;

    private CryptoBrokerIdentityModuleManager moduleManager;
    private CryptoBrokerIdentityInformation identityInfo;
    private CryptoBrokerIdentityInformation identity;

    public EditIdentityWorker(Activity context, FermatSession session, CryptoBrokerIdentityInformation identity, FermatWorkerCallBack callBack) {
        super(context, callBack);

        this.identity = identity;

        if (session != null) {
            CryptoBrokerIdentitySubAppSession subAppSession = (CryptoBrokerIdentitySubAppSession) session;
            identityInfo = (CryptoBrokerIdentityInformation) subAppSession.getData(IDENTITY_INFO);
            this.moduleManager = subAppSession.getModuleManager();
        }
    }

    @Override
    protected Object doInBackground() throws Exception {


        boolean valueChanged = (identity.isPublished() != identityInfo.isPublished());

        if ( identity == null ) {
            return INVALID_ENTRY_DATA;
        } else {
            moduleManager.updateCryptoBrokerIdentity(identity);
            if (valueChanged) {
                if (identity.isPublished()) {

                    System.out.println("VLZ: Publicando");

                    moduleManager.publishIdentity(identity.getPublicKey());
                }else {

                    System.out.println("VLZ: Ocultando");

                    moduleManager.hideIdentity(identity.getPublicKey());
                }
            }
            return SUCCESS;
        }
    }
}