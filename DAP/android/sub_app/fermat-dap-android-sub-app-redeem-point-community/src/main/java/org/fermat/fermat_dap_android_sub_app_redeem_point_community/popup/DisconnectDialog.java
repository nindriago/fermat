package org.fermat.fermat_dap_android_sub_app_redeem_point_community.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_dap_android_sub_app_redeem_point_community_bitdubai.R;
import org.fermat.fermat_dap_android_sub_app_redeem_point_community.models.Actor;
import org.fermat.fermat_dap_android_sub_app_redeem_point_community.sessions.AssetRedeemPointCommunitySubAppSession;
import org.fermat.fermat_dap_api.layer.dap_actor.exceptions.CantDisconnectAssetActorException;
import org.fermat.fermat_dap_api.layer.dap_identity.redeem_point.interfaces.RedeemPointIdentity;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;


/**
 * Added by Jinmy Bohorquez 11/02/2016
 */
@SuppressWarnings("FieldCanBeLocal")
public class DisconnectDialog extends FermatDialog<AssetRedeemPointCommunitySubAppSession, SubAppResourcesProviderManager> implements View.OnClickListener {

    /**
     * UI components
     */
    private FermatButton positiveBtn;
    private FermatButton negativeBtn;
    private FermatTextView mDescription;
    private FermatTextView mUsername;
    private FermatTextView mTitle;
    private CharSequence description;
    private CharSequence username;
    private CharSequence title;

    private final Actor actorRedeem;
    private final RedeemPointIdentity identity;

    public DisconnectDialog(final Activity activity,
                            final AssetRedeemPointCommunitySubAppSession intraUserSubAppSession,
                            final SubAppResourcesProviderManager subAppResources,
                            final Actor actorRedeem,
                            final RedeemPointIdentity identity) {

        super(activity, intraUserSubAppSession, subAppResources);

        this.actorRedeem = actorRedeem;
        this.identity = identity;
    }
    public DisconnectDialog(Activity a,
                            final AssetRedeemPointCommunitySubAppSession intraUserSubAppSession,
                            final SubAppResourcesProviderManager subAppResources) {
        super(a, intraUserSubAppSession, subAppResources);
        this.actorRedeem = null;
        this.identity = null;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDescription = (FermatTextView) findViewById(R.id.description);
        mUsername = (FermatTextView) findViewById(R.id.user_name);
        mTitle = (FermatTextView) findViewById(R.id.title);
        positiveBtn = (FermatButton) findViewById(R.id.positive_button);
        negativeBtn = (FermatButton) findViewById(R.id.negative_button);

        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);
        mDescription.setText(description != null ? description : "");
        mUsername.setText(username != null ? username : "");
        mTitle.setText(title != null ? title : "");

    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public void setUsername(CharSequence username) {
        this.username = username;
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dap_redeempoint_dialog_builder;
    }

    @Override
    protected int setWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.positive_button) {
            try {
                //image null
                if (actorRedeem != null) {
                    getSession().getModuleManager().disconnectToActorAssetRedeemPoint(
//                            identity.getPublicKey(),  // ACTOR INSIDE/LOCAL
                            actorRedeem // ACTOR OUTSIDE/EXTERNAL
                    );
                    Toast.makeText(getContext(), "Disconnected", Toast.LENGTH_SHORT).show();

                } else {
                    super.toastDefaultError();
                }
            } catch (CantDisconnectAssetActorException e) {
                super.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                super.toastDefaultError();
            }
            dismiss();
        } else if (i == R.id.negative_button) {
            dismiss();
        }
    }


}