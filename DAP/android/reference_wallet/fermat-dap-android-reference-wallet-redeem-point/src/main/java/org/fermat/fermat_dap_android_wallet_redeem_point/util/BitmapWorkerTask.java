package org.fermat.fermat_dap_android_wallet_redeem_point.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.ui.transformation.CircleTransform;
import com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<byte[], Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final Resources res;
    private byte[] data;
    private boolean isCircle=false;

        public BitmapWorkerTask(ImageView imageView, Resources res, boolean isCircle) {
            this.res = res;
            this.isCircle = isCircle;
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(byte[]... params) {
            data = params[0];
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageViewReference != null) {
                final ImageView imageView = imageViewReference.get();
                if (bitmap != null) {
                    //if (imageView != null) {
                    //imageView.setImageDrawable(ImagesUtils.getRoundedBitmap(res,bitmap));
                    imageView.setImageDrawable((isCircle) ? ImagesUtils.getRoundedBitmap(res, bitmap) : new BitmapDrawable(res,bitmap));
                    //}
                } else {
                    if(isCircle)
                    Picasso.with(imageView.getContext()).load(R.drawable.redeem_point).transform(new CircleTransform()).into(imageView);
                    else Picasso.with(imageView.getContext()).load(R.drawable.redeem_point).into(imageView);

                }
            }
        }
    }