package org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.holders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.ConfirmDialog;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.BitmapWorkerTask;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_dap_android_wallet_asset_issuer_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_asset_issuer.models.DigitalAsset;
import org.fermat.fermat_dap_android_wallet_asset_issuer.v3.fragments.HomeCardFragment;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;

/**
 * Created by frank on 12/8/15.
 */
public class HomeCardViewHolder extends FermatViewHolder {
    private AssetIssuerWalletSupAppModuleManager manager;
    private FermatSession appSession;
    private Context context;

    private Resources res;
    private ImageView assetImageCard;
    private FermatTextView assetNameCardText;
    private FermatTextView assetQuantityCardText;
    private FermatTextView assetQuantityCardText2;
    private FermatTextView assetValueCardText;
    private FermatTextView assetExpDateCardText;
    private ImageButton cardAppropriateButton;
    private ImageButton cardDeliverButton;
    private ImageButton cardTransactionsButton;
    private ImageButton cardStatsButton;


    /**
     * Constructor
     *
     * @param itemView
     * @param appSession
     */
    public HomeCardViewHolder(View itemView, AssetIssuerWalletSupAppModuleManager manager, Context context, FermatSession appSession) {
        super(itemView);
        this.manager = manager;
        this.context = context;
        this.appSession = appSession;
        res = itemView.getResources();

        assetImageCard = (ImageView) itemView.findViewById(R.id.assetImageCard);
        assetNameCardText = (FermatTextView) itemView.findViewById(R.id.assetNameCardText);
        assetQuantityCardText = (FermatTextView) itemView.findViewById(R.id.assetQuantityCardText);
        assetQuantityCardText2 = (FermatTextView) itemView.findViewById(R.id.assetQuantityCardText2);
        assetValueCardText = (FermatTextView) itemView.findViewById(R.id.assetValueCardText);
        assetExpDateCardText = (FermatTextView) itemView.findViewById(R.id.assetExpDateCardText);
        cardAppropriateButton = (ImageButton) itemView.findViewById(R.id.cardAppropriateButton);
        cardDeliverButton = (ImageButton) itemView.findViewById(R.id.cardDeliverButton);
        cardTransactionsButton = (ImageButton) itemView.findViewById(R.id.cardTransactionsButton);
        cardStatsButton = (ImageButton) itemView.findViewById(R.id.cardStatsButton);
    }

    public void bind(final DigitalAsset digitalAsset, final HomeCardFragment homeCardFragment) {
        byte[] img = (digitalAsset.getImage() == null) ? new byte[0] : digitalAsset.getImage();
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(assetImageCard, res, R.drawable.img_asset_without_image, false);
        bitmapWorkerTask.execute(img);

        assetNameCardText.setText(digitalAsset.getName());

        long available = digitalAsset.getAvailableBalanceQuantity();
        long book = digitalAsset.getBookBalanceQuantity();
        assetQuantityCardText.setText(Long.toString(available));
        assetQuantityCardText2.setText(availableText(available));

        assetValueCardText.setText(digitalAsset.getFormattedAvailableBalanceBitcoin());
        assetExpDateCardText.setText(digitalAsset.getFormattedExpDate());

        cardAppropriateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAppropriateAction(digitalAsset)) {
                    new ConfirmDialog.Builder((Activity) context, appSession)
                            .setTitle(res.getString(R.string.dap_issuer_wallet_confirm_title))
                            .setMessage(res.getString(R.string.dap_issuer_wallet_v3_appropriate_confirm))
                            .setColorStyle(res.getColor(R.color.dap_issuer_wallet_v3_dialog))
                            .setYesBtnListener(new ConfirmDialog.OnClickAcceptListener() {
                                @Override
                                public void onClick() {
                                    doAppropriate(digitalAsset.getAssetPublicKey(), digitalAsset.getWalletPublicKey());
                                }
                            }).build().show();
                }
            }
        });
        cardDeliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSession.setData("asset_data", digitalAsset);
                homeCardFragment.doDeliverAction();
            }
        });
        cardTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSession.setData("asset_data", digitalAsset);
                homeCardFragment.doTransactionsAction();
            }
        });
        cardStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private boolean validateAppropriateAction(DigitalAsset digitalAsset) {
        Activity activity = (Activity) context;
        if (digitalAsset.getAvailableBalanceQuantity() == 0) {
            Toast.makeText(activity, R.string.dap_issuer_wallet_validate_no_available_assets_appropriate, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doAppropriate(final String assetPublicKey, final String walletPublicKey) {
        final Activity activity = (Activity) context;
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(res.getString(R.string.dap_issuer_wallet_wait));
        dialog.setCancelable(false);
        dialog.show();
        FermatWorker task = new FermatWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                manager.appropriateAsset(assetPublicKey, walletPublicKey);
                return true;
            }
        };
        task.setContext(activity);
        task.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                dialog.dismiss();
                if (activity != null) {
                    Toast.makeText(activity, R.string.dap_issuer_wallet_appropriation_ok, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {
                dialog.dismiss();
                if (activity != null)
                    Toast.makeText(activity, R.string.dap_issuer_wallet_exception_retry,
                            Toast.LENGTH_SHORT).show();
            }
        });
        task.execute();
    }

    private String availableText(long available) {
        return ((available == 1) ? "ASSET" : " ASSETS");
    }
}
