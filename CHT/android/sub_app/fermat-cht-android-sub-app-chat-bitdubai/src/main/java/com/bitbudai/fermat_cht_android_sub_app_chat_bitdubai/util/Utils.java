package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.GridView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Chat List Adapter
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 09/01/16
 * @version 1.0
 *
 */

/**
 * This class contains static utility methods.
 */
public class Utils {

    public static SimpleDateFormat getDateFormat(String type) {
        return new SimpleDateFormat(type);
    }

    public static String getInitials(String firstName, String lastName) {
        if (firstName.isEmpty()) {
            return ":)";
        }
        if (lastName.isEmpty()) {
            char[] iconText = new char[2];
            firstName.getChars(0, 1, iconText, 0);
            firstName.getChars(1, 2, iconText, 1);
            return ("" + iconText[0] + iconText[1]).toUpperCase();
        } else {
            char[] iconText = new char[2];
            firstName.getChars(0, 1, iconText, 0);
            lastName.getChars(0, 1, iconText, 1);
            return ("" + iconText[0] + iconText[1]).toUpperCase();
        }
    }

    public static ShapeDrawable getShapeDrawable(int size, int color) {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(size);
        circle.setIntrinsicWidth(size);
        circle.getPaint().setColor(color);
        return circle;
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        //final Paint paintBorder = new Paint();
        // paintBorder.setColor(Color.GREEN);
        //paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
        //BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(output, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //paint.setShader(shader);
        paint.setAntiAlias(true);
        //paintBorder.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        //int circleCenter = bitmap.getWidth() / 2;
        //int borderWidth = 2;
        //canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth - 4.0f, paintBorder);
        //canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter - 4.0f, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //canvas.drawBitmap(bitmap, circleCenter + borderWidth, circleCenter + borderWidth, paint);
        bitmap.recycle();
        return output;
    }

    public static Bitmap decodeFile(Context context,int resId) {
// decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, o);
// Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = 50;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true)
        {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale++;
        }
// decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeResource(context.getResources(), resId, o2);
    }

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
        // TODO Auto-generated method stub
        int targetWidth = width;
        int targetHeight = width;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }

   /* public static void drawBackgroundForCheckedPhoto(TextView numberPhotos, FrameLayout buttonSend, Activity activity, InputMethodManager imm) {
        if (ListFoldersHolder.getCheckQuantity() > 0 && ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() > 0) {
            buttonSend.setEnabled(true);
            numberPhotos.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
            if (isTablet(activity)) {
                params.leftMargin = 50;
                verifySetBackground(numberPhotos, getShapeDrawable(35, activity.getResources().getColor(R.color.message_notify)));
            } else {
                if (getSmallestScreenSize(activity) <= 480) {
                    params.leftMargin = 10;
                } else {
                    params.leftMargin = 60;
                    if (imm != null && imm.isAcceptingText()) {
                        verifySetBackground(numberPhotos, getShapeDrawable(50, activity.getResources().getColor(R.color.message_notify)));
                    } else {
                        verifySetBackground(numberPhotos, getShapeDrawable(60, activity.getResources().getColor(R.color.message_notify)));
                    }
                }
            }
            numberPhotos.setLayoutParams(params);
            numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
        } else {
            buttonSend.setEnabled(false);
            numberPhotos.setVisibility(View.GONE);
        }
    }*/

    /*public static void setParamsForLayoutButtonsFigImages(Activity activity, LinearLayout layoutButtons, Configuration newConfig) {
        float weightParam;
        float weightParamPager;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet(activity)) {
            weightParam = 0.25f;
            weightParamPager = 2.5f;
        } else {
            weightParam = 0.15f;
            weightParamPager = 1.5f;
        }
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0, weightParam);
        LinearLayout.LayoutParams paramPager = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0, weightParamPager);
        if (activity instanceof PhotoViewPagerActivity) {
            layoutButtons.setLayoutParams(paramPager);
        } else {
            layoutButtons.setLayoutParams(param);
        }
    }*/

    public static void adjustGridViewPort(GridView gridList) {
        gridList.setNumColumns(2);
        gridList.setHorizontalSpacing(15);
    }

    public static void adjustGridViewLand(GridView gridList) {
        gridList.setNumColumns(3);
        gridList.setHorizontalSpacing(15);
    }

    /*public static void sendMessageFromGallery(Activity activity) {
        if (ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() != 0) {
            for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                if (ListFoldersHolder.getListForSending().get(i) instanceof ImagesObject) {
                    if (((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath().contains("http")) {
                        String linkImage = ((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath();
                        if (ListFoldersHolder.getListImages() == null) {
                            ListFoldersHolder.setListImages(new ArrayList<String>());
                        }
                        ListFoldersHolder.getListImages().add(linkImage);
                    } else {
                        ApiHelper.sendPhotoMessage(ListFoldersHolder.getChatID(),
                                ((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath());
                    }
                }
                if (ListFoldersHolder.getListForSending().get(i) instanceof GiphyObject) {
                    if (ListFoldersHolder.getListGif() == null) {
                        ListFoldersHolder.setListGif(new ArrayList<String>());
                    }
                    String link = ((GiphyObject) ListFoldersHolder.getListForSending().get(i)).getPath();
                    ListFoldersHolder.getListGif().add(link);
                }
            }
            activity.startService(new Intent(activity, SendGif.class));
            activity.finish();
        }
    }*/

    /*public static void changeButtonsWhenRotate(LinearLayout layoutButtons, LinearLayout layoutFind, ArrayAdapter adapter, Activity activity, GridView gridList) {
        if (isTablet(activity)) {
            ((TransparentActivity) activity).progressBarGone();
            adjustGridViewPort(gridList);
        } else {
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet(activity)) {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 2.5f);
                if (layoutFind != null) {
                    layoutFind.setLayoutParams(param);
                }
                layoutButtons.setLayoutParams(param);
                ((TransparentActivity) activity).progressBarGone();
                adjustGridViewLand(gridList);
            } else {
                LinearLayout.LayoutParams paramButtons = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 1.6f);
                if (layoutFind != null) {
                    LinearLayout.LayoutParams paramFind = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            0, 1.5f);
                    layoutFind.setLayoutParams(paramFind);
                }
                layoutButtons.setLayoutParams(paramButtons);
                ((TransparentActivity) activity).progressBarGone();
                adjustGridViewPort(gridList);
            }
        }
        adapter.clear();
        if (adapter instanceof FolderAdapter) {
            adapter.addAll(ListFoldersHolder.getList());
        }
        if (adapter instanceof GalleryAdapter) {
            adapter.addAll(ListFoldersHolder.getListFolders());
        }
    }*/

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void hideKeyboard(EditText e) {
        InputMethodManager imm = (InputMethodManager) e.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
    }

    public static int compare(long lhs, long rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    public static float getDensity(Resources res) {
        DisplayMetrics metrics = res.getDisplayMetrics();
        return metrics.density;
    }

   /* public static String getUserStatusString(TdApi.UserStatus status) {
        Context context = DataHolder.getContext();
        Locale.setDefault(Locale.US);
        String lastSeenString = "";
        switch (status.getConstructor()) {
            case TdApi.UserStatusOnline.CONSTRUCTOR:
                lastSeenString = context.getString(R.string.online);
                break;
            case TdApi.UserStatusOffline.CONSTRUCTOR:
                int ago = ((TdApi.UserStatusOffline) status).wasOnline;
                lastSeenString = context.getString(R.string.last_seen) + " " + DateUtils.getRelativeTimeSpanString((long) ago * 1000);
                break;
            case TdApi.UserStatusRecently.CONSTRUCTOR:
                lastSeenString = context.getString(R.string.ls_recently);
                break;
            case TdApi.UserStatusLastWeek.CONSTRUCTOR:
                lastSeenString = context.getString(R.string.ls_week_ago);
                break;
            case TdApi.UserStatusLastMonth.CONSTRUCTOR:
                lastSeenString = context.getString(R.string.ls_month_ago);
                break;
            default:
                break;
        }
        return lastSeenString;
    }
*/
    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double mByte = 1024.0;
        double k = size / mByte;
        double m = ((size / mByte) / mByte);
        double g = (((size / mByte) / mByte) / mByte);
        double t = ((((size / mByte) / mByte) / mByte) / mByte);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" B");
        }
        return hrSize;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
/*
    public static void gifFileCheckerAndLoader(final TdApi.File file, Activity activity, final ImageView view) {
        if (file instanceof TdApi.FileLocal) {
            TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
            Glide.with(DataHolder.getContext()).load(fileLocal.path).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
        } else if (file instanceof TdApi.FileEmpty) {
            final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
            if (DownloadFileHolder.getUpdatedFilePath(fileEmpty.id) != null) {
                Glide.with(DataHolder.getContext()).load(DownloadFileHolder.getUpdatedFilePath(fileEmpty.id)).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
            } else {
                ApiHelper.downloadFile(fileEmpty.id);
                findLoop(fileEmpty.id, view, activity, false);
            }
        }
    }

    public static void photoFileCheckerAndLoader(final TdApi.File file, final ImageView view, final Activity activity) {
        if (file instanceof TdApi.FileLocal) {
            TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
            ImageLoaderHelper.displayImageWithoutFadeIn(Const.IMAGE_LOADER_PATH_PREFIX + fileLocal.path, view);
        } else if (file instanceof TdApi.FileEmpty) {
            final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
            if (DownloadFileHolder.getUpdatedFilePath(fileEmpty.id) != null) {
                findLoop(fileEmpty.id, view, activity, false);
            } else {
                ApiHelper.downloadFile(fileEmpty.id);
                findLoop(fileEmpty.id, view, activity, false);
            }
        }
    }
*/
    /*public static void photoFileLoader(final int id, final ImageView view, final Activity activity) {
        ApiHelper.downloadFile(id);
        findLoop(id, view, activity, false);
    }*/

    /*public static void setIcon(TdApi.File file, int chatId, String firstName, String lastName, final ImageView iconImage, final TextView icon, final Activity activity) {
        if (file != null) {
            /*if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                iconImage.setVisibility(View.VISIBLE);
                TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                ImageLoaderHelper.displayImageWithoutFadeIn(Const.IMAGE_LOADER_PATH_PREFIX + fileLocal.path, iconImage);
            }
            if (file.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
                final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                if (fileEmpty.id != 0) {
                    if (DownloadFileHolder.getUpdatedFilePath(fileEmpty.id) != null) {
                        file = DownloadFileHolder.getUpdatedFile(fileEmpty.id);
                        ImageLoaderHelper.displayImageWithoutFadeIn(Const.IMAGE_LOADER_PATH_PREFIX + ((TdApi.FileLocal) file).path, iconImage);
                    } else {
                        ApiHelper.downloadFile(fileEmpty.id);
                        findLoop(fileEmpty.id, iconImage, activity, false);
                    }
                } else {
                    iconImage.setImageDrawable(null);
                    icon.setVisibility(View.VISIBLE);
                    if (chatId < 0) {
                        verifySetBackground(icon, getShapeDrawable(R.dimen.toolbar_icon_size, chatId));
                    } else {
                        verifySetBackground(icon, getShapeDrawable(R.dimen.toolbar_icon_size, -chatId));
                    }
                    icon.setText(getInitials(firstName, lastName));
                }
            }
        }
    }*/

    public static void verifySetBackground(View view, Drawable drawable) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    public static int getSmallestScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (width > height) {
            return height;
        } else {
            return width;
        }
    }

    /*public static void findLoop(final int id, final ImageView iconImage, final Activity activity, final boolean gif) {
        Runnable runnable = new Runnable() {
            public void run() {
                String path;
                for (int i = 0; i < 50; i++) {
                    path = DownloadFileHolder.getUpdatedFilePath(id);
                    if (path != null) {
                        final String finalPath = path;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (gif) {
                                    Glide.with(DataHolder.getContext()).load(finalPath).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iconImage);
                                } else {
                                    iconImage.setVisibility(View.VISIBLE);
                                    ImageLoaderHelper.displayImageWithoutFadeIn(Const.IMAGE_LOADER_PATH_PREFIX + finalPath, iconImage);
                                }
                            }
                        });
                        break;
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }*/

    public static File processImage(File tmpFile, ExifInterface originalExif) {
        int pictureOrientation = -1;
        try {
            ExifInterface exif;
            if (originalExif != null) {
                exif = originalExif;
            } else {
                exif = new ExifInterface(tmpFile.getAbsolutePath());
            }

            pictureOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            Log.e("Tag", "ExifInterface exception", e);
        }
        rotateFileImage(pictureOrientation, tmpFile.getAbsolutePath());
        return tmpFile;
    }

    private static void rotateFileImage(int orientationFlag, String filePathFrom) {
        int angle;
        switch (orientationFlag) {
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = 90;

                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = 270;
                break;
            default:
                return;
        }

        BitmapFactory.Options optionBmp = new BitmapFactory.Options();
        optionBmp.inJustDecodeBounds = false;
        optionBmp.inPreferredConfig = Bitmap.Config.ARGB_8888;
        optionBmp.inDither = false;
        optionBmp.inScaled = false;

        Bitmap originalBitmap = BitmapFactory.decodeFile(filePathFrom, optionBmp);

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePathFrom);
            Bitmap bitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
            originalBitmap.recycle();
            originalBitmap = null;

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
            bitmap = null;
        } catch (Exception e) {
            Log.e("Tag", "rotateFileImage error " + e);
        }
    }

   /* public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) DataHolder.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }*/

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
