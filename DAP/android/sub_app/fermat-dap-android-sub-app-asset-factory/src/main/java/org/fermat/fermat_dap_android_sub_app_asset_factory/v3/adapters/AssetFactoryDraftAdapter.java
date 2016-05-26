package org.fermat.fermat_dap_android_sub_app_asset_factory.v3.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.util.BitmapWorkerTask;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Resource;
import com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter;
import com.bitdubai.fermat_dap_android_sub_app_asset_factory_bitdubai.R;

import org.fermat.fermat_dap_android_sub_app_asset_factory.holders.AssetHolder;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.AssetFactorySession;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.filters.AssetFactoryDraftAdapterFilter;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.fragments.DraftAssetsHomeFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.fragments.PublishedAssetsHomeFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.holders.AssetFactoryDraftHolder;
import org.fermat.fermat_dap_api.layer.all_definition.util.DAPStandardFormats;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.AssetFactory;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter.Currency.BITCOIN;
import static com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter.Currency.SATOSHI;

/**
 * Created by Jinmy Bohorquez on 29/04/16.
 */
public class AssetFactoryDraftAdapter extends FermatAdapter<AssetFactory, AssetFactoryDraftHolder> implements
        Filterable {

    private final AssetFactorySession assetFactorySession;
    private DraftAssetsHomeFragment fragment;
    private AssetFactoryModuleManager manager;
    private  List<AssetFactory> allAssets;

    public AssetFactoryDraftAdapter(DraftAssetsHomeFragment fragment, Context context, List<AssetFactory> dataSet, AssetFactoryModuleManager manager,
                                    FermatSession appSession) {
        super(context, dataSet);

        this.fragment = fragment;
        this.manager = manager;
        this.dataSet = dataSet;
        this.assetFactorySession = (AssetFactorySession) appSession;
        this.allAssets = dataSet;
    }
    public AssetFactoryDraftAdapter( Context context, List<AssetFactory> dataSet, AssetFactoryModuleManager manager,
                                    FermatSession appSession) {
        super(context, dataSet);


        this.manager = manager;
        this.dataSet = dataSet;
        this.assetFactorySession = (AssetFactorySession) appSession;
        this.allAssets = dataSet;
    }

    @Override
    protected AssetFactoryDraftHolder createHolder(View itemView, int type) {
        return new AssetFactoryDraftHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.dap_v3_factory_draft_assets_home_fragment_item;
    }

    @Override
    protected void bindHolder(AssetFactoryDraftHolder holder, AssetFactory data, int position) {
        bind(holder, data);
    }

    @Override
    public Filter getFilter() {
        return new AssetFactoryDraftAdapterFilter(this.allAssets, this);
    }

    public void bind(AssetFactoryDraftHolder holder, final AssetFactory data) {

        double amountTotal = BitcoinConverter.convert(Double.valueOf(data.getAmount() * data.getQuantity()), SATOSHI, BITCOIN);
        double amountPerAsset = BitcoinConverter.convert(Double.valueOf(data.getAmount()), SATOSHI, BITCOIN);
        holder.draftItemQuantity.setText((data.getQuantity() == 1)? data.getQuantity()+" Asset":data.getQuantity()+" Assets");

        List<Resource> resources = data.getResources();
        if (resources != null && resources.size() > 0) {
            holder.draftAssetImage.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(resources.get(0).getResourceBinayData())));
        } else {
            holder.draftAssetImage.setImageResource(R.drawable.img_asset_image);
        }

        holder.draftItemAssetName.setText(data.getName());
        //holder.draftItemAssetValue.setText(String.format(context.getString(R.string.dapV3_home_row_asset_bitcoins), amountTotal));
        holder.draftItemAssetValue.setText(String.format(context.getString(R.string.dapV3_home_row_asset_bitcoins), amountPerAsset));
        holder.draftItemExpDate.setText((data.getExpirationDate() == null)? "No Exp Date": DAPStandardFormats.DATE_FORMAT.format(data.getExpirationDate()));

        switch (data.getState()) {
            case DRAFT:
                renderDraft(holder, data, amountTotal);
                break;
            case FINAL:
                renderFinal(holder, data, amountTotal);
                break;
            case PENDING_FINAL:
                renderPendingFinal(holder, data, amountTotal);
                break;
            default:
                holder.itemView.setVisibility(View.INVISIBLE);
                break;
        }


    }

    private void renderPendingFinal(AssetFactoryDraftHolder holder, AssetFactory data, double amount) {
                /*holder.rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.bottomLine.setVisibility(View.GONE);
                holder.state.setTextColor(ContextCompat.getColor(context, R.color.state_color_publishing));
                holder.state.setBackgroundResource(R.drawable.white);
                holder.state.setVisibility(View.VISIBLE);
                holder.name.setTextColor(ContextCompat.getColor(context, R.color.white_asset_name));
                holder.amount.setTextColor(ContextCompat.getColor(context, R.color.white_amount));
                holder.bitcoins.setTextColor(ContextCompat.getColor(context, R.color.white_bitcoins));
                holder.name.setText(data.getName() != null ? data.getName() : context.getString(R.string.app_unnamed));
                holder.state.setText(R.string.home_asset_state_publishing);
                holder.amount.setText(String.format(context.getString(R.string.home_row_asset_amount), data.getQuantity()));
                holder.bitcoins.setText(String.format(context.getString(R.string.home_row_asset_bitcoins), amount));*/
        holder.draftItemState.setText(R.string.home_asset_state_publishing);
        holder.normalAssetButtons.setVisibility(View.GONE);
        holder.draftSeparatorLine.setVisibility(View.GONE);
        holder.assetStatusImage.setImageResource(R.drawable.publishing);
    }

    private void renderFinal(AssetFactoryDraftHolder holder, AssetFactory data, double amount) {
                /*holder.rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_gray));
                holder.bottomLine.setVisibility(View.VISIBLE);
                holder.state.setTextColor(ContextCompat.getColor(context, R.color.state_color_publishing));
                holder.state.setVisibility(View.GONE);
                holder.state.setBackgroundResource(R.drawable.white);
                holder.name.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.amount.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.bitcoins.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.name.setText(data.getName() != null ? data.getName() : context.getString(R.string.app_unnamed));
                holder.state.setText(R.string.home_asset_state_published);
                holder.amount.setText(String.format(context.getString(R.string.home_row_asset_amount), data.getQuantity()));
                holder.bitcoins.setText(String.format(context.getString(R.string.home_row_asset_bitcoins), amount));*/
        holder.draftItemState.setText(R.string.home_asset_state_published);
        holder.normalAssetButtons.setVisibility(View.GONE);
        holder.draftSeparatorLine.setVisibility(View.GONE);
        holder.publishedAssetButtons.setVisibility(View.GONE);
        holder.assetStatusImage.setImageResource(R.drawable.published);
    }

    private void renderDraft(AssetFactoryDraftHolder holder, final AssetFactory data, double amount) {
        holder.normalAssetButtons.setVisibility(View.VISIBLE);
        holder.draftSeparatorLine.setVisibility(View.VISIBLE);
                /*holder.rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
                holder.bottomLine.setVisibility(View.GONE);
                holder.state.setTextColor(ContextCompat.getColor(context, R.color.state_color_editable));
                holder.state.setBackgroundResource(R.drawable.blue);
                holder.state.setVisibility(View.VISIBLE);
                holder.name.setTextColor(ContextCompat.getColor(context, R.color.blue_asset_name));
                holder.amount.setTextColor(ContextCompat.getColor(context, R.color.blue_amount));
                holder.bitcoins.setTextColor(ContextCompat.getColor(context, R.color.blue_bitcoins));
                holder.name.setText(data.getName() != null ? data.getName() : context.getString(R.string.app_unnamed));
                holder.state.setText(R.string.home_asset_state_draft);
                holder.amount.setText(String.format(context.getString(R.string.home_row_asset_amount), data.getQuantity()));
                holder.bitcoins.setText(String.format(context.getString(R.string.home_row_asset_bitcoins), amount));*/
        holder.draftItemState.setText(R.string.home_asset_state_draft);
        holder.assetStatusImage.setImageResource(R.drawable.created);

        holder.draftItemEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetFactorySession.setData("asset_factory", data);
                fragment.doEditAsset();
            }
        });
        holder.draftItemPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetFactorySession.setData("asset_factory", data);
                if(fragment.validate())
                fragment.doPublishAsset();
            }
        });
        holder.draftItemEraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetFactorySession.setData("asset_factory", data);
                fragment.doDeleteAsset();
            }
        });
    }
}