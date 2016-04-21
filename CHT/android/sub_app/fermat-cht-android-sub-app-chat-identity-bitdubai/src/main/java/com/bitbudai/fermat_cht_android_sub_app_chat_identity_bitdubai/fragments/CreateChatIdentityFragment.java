package com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.sessions.ChatIdentitySession;
import com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.util.CommonLogger;
import com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.util.CreateChatIdentityExecutor;
import static com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.util.CreateChatIdentityExecutor.SUCCESS;

import com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.util.EditIdentityExecutor;
import static com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.util.MenuConstants.MENU_ADD_ACTION;
import static com.bitbudai.fermat_cht_android_sub_app_chat_identity_bitdubai.util.MenuConstants.MENU_HELP_ACTION;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.transformation.CircleTransform;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_cht_android_sub_app_chat_identity_bitdubai.R;

import com.bitdubai.fermat_cht_api.all_definition.exceptions.CHTException;
import com.bitdubai.fermat_cht_api.layer.identity.exceptions.CantGetChatIdentityException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.identity.ChatIdentityModuleManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.identity.ChatIdentityPreferenceSettings;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.fermat_cht_plugin.layer.sub_app_module.chat.identity.bitdubai.version_1.ChatIdentitySubAppModulePluginRoot;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;


/**
 * FERMAT-ORG
 * Developed by Lozadaa on 04/04/16.
 */
public class CreateChatIdentityFragment extends AbstractFermatFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOAD_IMAGE = 2;
    private Bitmap cryptoBrokerBitmap;
    private static final int CONTEXT_MENU_CAMERA = 1;
    private static final int CONTEXT_MENU_GALLERY = 2;
    ChatIdentityModuleManager moduleManager;
    private EditText mBrokerName;
    private ImageView mBrokerImage;
    ErrorManager errorManager;
    private boolean actualizable;
    private boolean contextMenuInUse = false;
    TextView textViewChtTitle;
    byte[] fanImageByteArray;
    ChatIdentitySession Session;
    Context context;

    SettingsManager<ChatIdentityPreferenceSettings> settingsManager;
    public static CreateChatIdentityFragment newInstance() {
        return new CreateChatIdentityFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
             Session = (ChatIdentitySession) appSession;
            moduleManager =  Session.getModuleManager();
             errorManager = Session.getErrorManager();
            setHasOptionsMenu(false);
          settingsManager = Session.getModuleManager().getSettingsManager();

        }catch (Exception e){
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_cht_identity_create, container, false);
        initViews(rootLayout);
        return rootLayout;
    }
    private byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    /**
     * Inicializa las vistas de este Fragment
     * @param layout el layout de este Fragment que contiene las vistas
     */
    private void initViews(View layout) {
        actualizable = true;
        mBrokerName = (EditText) layout.findViewById(R.id.aliasEdit);
        final Button botonG = (Button) layout.findViewById(R.id.cht_button);
        mBrokerImage = (ImageView) layout.findViewById(R.id.cht_image);
        textViewChtTitle = (TextView) layout.findViewById(R.id.textViewChtTitle);
        Bitmap bitmap = null;
        try {
            if(ExistIdentity() == false) {
                botonG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actualizable = false;
                        createNewIdentityInBackDevice("onClick");
                    }
                });
                setUpDialog();
                mBrokerName.requestFocus();
                mBrokerName.performClick();
                mBrokerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                });


                registerForContextMenu(mBrokerImage);
                mBrokerImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_select_cam_or_gallery);
                        dialog.findViewById(R.id.img_cam).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dispatchTakePictureIntent();
                            }
                        });
                        dialog.findViewById(R.id.img_gallery).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadImageFromGallery();
                            }
                        });
                        dialog.show();
                        botonG.setVisibility(View.VISIBLE);
                        CommonLogger.debug("Chat identity", "Entrando en chatImg.setOnClickListener");

                    }
                });

                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }else{
                bitmap = BitmapFactory.decodeByteArray(moduleManager.getIdentityChatUser().getImage(), 0, moduleManager.getIdentityChatUser().getImage().length);
                 bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                mBrokerImage.setImageDrawable(ImagesUtils.getRoundedBitmap(getResources(), bitmap));
                textViewChtTitle.setText(moduleManager.getIdentityChatUser().getAlias().toString());
                mBrokerName.setText(moduleManager.getIdentityChatUser().getAlias().toString());
                mBrokerName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.buttonedit, 0);
                mBrokerName.performClick();
                botonG.setVisibility(View.GONE);
                mBrokerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        botonG.setVisibility(View.VISIBLE);
                    }
                });
                mBrokerName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        botonG.setVisibility(View.VISIBLE);
                    }
                });
                botonG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actualizable = false;
                        try {
                            updateIdentityInBackDevice("onClick");
                        } catch (CantGetChatIdentityException e) {
                            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));

                        }
                    }
                });
                registerForContextMenu(mBrokerImage);
                mBrokerImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_select_cam_or_gallery);
                        dialog.findViewById(R.id.img_cam).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dispatchTakePictureIntent();
                            }
                        });
                        dialog.findViewById(R.id.img_gallery).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadImageFromGallery();
                            }
                        });
                        dialog.show();
                        botonG.setVisibility(View.VISIBLE);
                        CommonLogger.debug("Chat identity", "Entrando en chatImg.setOnClickListener");

                    }
                });
            }
        } catch (CHTException e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));

        }
    }





    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        try {
            if(ExistIdentity() == false) {
                menu.add(0, MENU_HELP_ACTION, 0, "help").setIcon(R.drawable.ic_menu_help_cht)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            }else {
                menu.add(0, MENU_ADD_ACTION, 0, "help").setIcon(R.drawable.ic_action_add)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            }
        } catch (CHTException e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));

        }


    }

    public void setUpDialog(){
        PresentationDialog pd = new PresentationDialog.Builder(getActivity(), appSession)
                .setSubTitle(R.string.cht_chat_identity_subtitle)
                .setBody(R.string.cht_chat_identity_body)
                .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                .setIconRes(R.drawable.chat_subapp)
                .setBannerRes(R.drawable.banner_identity_chat)
                .setTextFooter(R.string.cht_chat_footer).build();
        pd.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

           switch (id){
               case MENU_HELP_ACTION:
                setUpDialog();
                break;

               case MENU_ADD_ACTION:
                   //TODO: AÑADIR!!!


                   break;
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! recovering from system error",
                    LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    cryptoBrokerBitmap = (Bitmap) extras.get("data");
                    break;
                case REQUEST_LOAD_IMAGE:
                    Uri selectedImage = data.getData();
                    try {
                        if (isAttached) {
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            cryptoBrokerBitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage);
                            cryptoBrokerBitmap = Bitmap.createScaledBitmap(cryptoBrokerBitmap, mBrokerImage.getWidth(), mBrokerImage.getHeight(), true);
                            Picasso.with(getActivity()).load(selectedImage).transform(new CircleTransform()).into(mBrokerImage);
                        }
                    } catch (Exception e) {
                        errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));

                        Toast.makeText(getActivity().getApplicationContext(), "Error cargando la imagen", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void updateIdentityInBackDevice(String donde) throws CantGetChatIdentityException {
        String brokerNameText = mBrokerName.getText().toString();
        if (brokerNameText.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter a name or alias", Toast.LENGTH_LONG).show();
        } else {
            byte[] imgInBytes = ImagesUtils.toByteArray(cryptoBrokerBitmap);
            EditIdentityExecutor executor = null;
            try {
                executor = new EditIdentityExecutor(Session, moduleManager.getIdentityChatUser().getPublicKey(), brokerNameText, moduleManager.getIdentityChatUser().getImage());

                int resultKey = executor.execute();
                switch (resultKey) {
                    case SUCCESS:
                        if (donde.equalsIgnoreCase("onClick")) {
                            textViewChtTitle.setText(mBrokerName.getText());
                            Toast.makeText(getActivity(), "Chat Identity Update.", Toast.LENGTH_LONG).show();
                            changeActivity(Activities.CHT_CHAT_CREATE_IDENTITY, appSession.getAppPublicKey());
                        }
                        break;
                }
            } catch (CHTException e) {
                errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));

            }
        }
    }

    private void createNewIdentityInBackDevice(String donde) {
        String brokerNameText = mBrokerName.getText().toString();
        if (brokerNameText.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter a name or alias", Toast.LENGTH_LONG).show();
        } else {
            if (cryptoBrokerBitmap == null) {
                Toast.makeText(getActivity(), "You must enter an image", Toast.LENGTH_LONG).show();
            } else {
                byte[] imgInBytes = ImagesUtils.toByteArray(cryptoBrokerBitmap);
                CreateChatIdentityExecutor executor = null;
                try {
                    executor = new CreateChatIdentityExecutor(Session,brokerNameText, imgInBytes);

                int resultKey = executor.execute();
                switch (resultKey) {
                    case SUCCESS:
                        if (donde.equalsIgnoreCase("onClick")) {
                            textViewChtTitle.setText(mBrokerName.getText());
                            Toast.makeText(getActivity(), "Chat Identity Created.", Toast.LENGTH_LONG).show();
                            changeActivity(Activities.CHT_CHAT_CREATE_IDENTITY, appSession.getAppPublicKey());
                        }
                        break;
                }      } catch (CantGetChatIdentityException e) {
                    errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));

                }
            }
        }
    }

    public boolean ExistIdentity() throws CHTException {
            //TODO:Richard que pasa si esto es null porque no hay idenitdad creada te va a tirar null pointer exception?
            //TODO: Fix by Richard
        try {
            if (!moduleManager.getIdentityChatUser().getAlias().isEmpty()) {
                Log.i("CHT EXIST IDENTITY", "TRUE");
                return true;
            }
        }
        catch(Exception e){
            return false;
        }
        Log.i("CHT EXIST IDENTITY", "FALSE");
        return false;
    }

    private void dispatchTakePictureIntent() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void loadImageFromGallery() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent loadImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(loadImageIntent, REQUEST_LOAD_IMAGE);
    }
}