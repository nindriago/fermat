package com.bitdubai.fermat.dap_plugin.layer.digital_asset_transaction.asset_distribution.developer.bitdubai.version1.structure.mocks;

import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantAssetIssuerActorNotFoundException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantCreateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantGetAssetIssuerActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.exceptions.CantConnectToActorAssetRedeemPointException;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPoint;

import java.util.List;

/**
 * Created by lcampo on 07/11/15.
 */
public class MockActorAssetIssuerManager implements ActorAssetIssuerManager {
    @Override
    public ActorAssetIssuer getActorByPublicKey(String actorPublicKey) throws CantGetAssetIssuerActorsException, CantAssetIssuerActorNotFoundException {
        return new MockActorAssetIssuer();
    }

    @Override
    public void createActorAssetIssuerFactory(String assetIssuerActorPublicKey, String assetIssuerActorName, byte[] assetIssuerActorprofileImage) throws CantCreateActorAssetIssuerException {

    }

    @Override
    public ActorAssetIssuer getActorAssetIssuer() throws CantGetAssetIssuerActorsException {
        return new MockActorAssetIssuer();
    }

    @Override
    public List<ActorAssetIssuer> getAllAssetIssuerActorInTableRegistered() throws CantGetAssetIssuerActorsException {
        return null;
    }

    @Override
    public List<ActorAssetIssuer> getAllAssetIssuerActorConnected() throws CantGetAssetIssuerActorsException {
        return null;
    }

    /**
     * The method <code>sendMessage</code> Stablish Connection
     * with Requester and Lists Redeem Point associate
     *
     * @param requester
     * @param actorAssetRedeemPoints
     * @throws CantConnectToActorAssetRedeemPointException
     */
    @Override
    public void sendMessage(ActorAssetIssuer requester, List<ActorAssetRedeemPoint> actorAssetRedeemPoints) throws CantConnectToActorAssetRedeemPointException {

    }
}
