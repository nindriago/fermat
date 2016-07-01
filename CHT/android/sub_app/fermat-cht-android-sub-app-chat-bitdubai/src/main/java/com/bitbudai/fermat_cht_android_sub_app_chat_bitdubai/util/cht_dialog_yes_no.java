package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.ContactConnection;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.ContactImpl;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatModuleManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by richardalexander on 09/03/16.
 * Updated by Jose Cardozo josejcb (josejcb89@gmail.com) on 17/03/16.
 */
public class cht_dialog_yes_no extends FermatDialog  implements View.OnClickListener {
    private final ContactConnection contactConn;
    Button btn_yes,btn_no;
    TextView txt_title,txt_body;
    private ChatManager chatManager;
    private ChatModuleManager moduleManager;
    private ErrorManager errorManager;
    private SettingsManager<com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.settings.ChatSettings> settingsManager;
    private com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSession chatSession;
    private AdapterCallbackContacts mAdapterCallback;
    int AlertType = 0;
    String body,title;
    boolean addcontact = false;
    public boolean delete_contact = false;
    public boolean delete_chat = false;
    ArrayList<String> contactname=new ArrayList<String>();
    ArrayList<Bitmap> contacticon=new ArrayList<>();
    ArrayList<UUID> contactid=new ArrayList<UUID>();
    ArrayList<String> contactalias =new ArrayList<String>();
    ArrayList<String> contactName=new ArrayList<>();
    ArrayList<String> message=new ArrayList<>();
    ArrayList<String> dateMessage=new ArrayList<>();
    ArrayList<UUID> chatId=new ArrayList<>();
    ArrayList<UUID> contactId=new ArrayList<>();
    ArrayList<String> status=new ArrayList<>();
    ArrayList<String> typeMessage=new ArrayList<>();
    ArrayList<Integer> noReadMsgs=new ArrayList<>();
    ArrayList<Bitmap> imgId=new ArrayList<>();
    public cht_dialog_yes_no(Context activity, FermatSession fermatSession, ResourceProviderManager resources, ContactConnection contactConnm, AdapterCallbackContacts mAdapterCallback) {
        super(activity, fermatSession, resources);
        this.contactConn = contactConnm;
        this.mAdapterCallback = mAdapterCallback;
    }

    public static interface AdapterCallbackContacts {
        void onMethodCallbackContacts();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            txt_title = (TextView) this.findViewById(R.id.cht_alert_txt_title);
            txt_body = (TextView) this.findViewById(R.id.cht_alert_txt_body);
            btn_yes = (Button) this.findViewById(R.id.cht_alert_btn_yes);
            btn_no = (Button) this.findViewById(R.id.cht_alert_btn_no);
            try {
                chatSession = ((com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSession) getSession());
                chatManager = chatSession.getModuleManager();
                //chatManager = moduleManager.getChatManager();
                errorManager = getSession().getErrorManager();
            } catch (Exception e) {
                if (errorManager != null)
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            }
            txt_title.setText(title);
            txt_body.setText(body);
        setUpListeners();
    }

    public void setTextBody(String txt){
        body = txt;
    }
    public void setTextTitle(String txt){
        title = txt;
    }
    public void setType(String txt){
        if(txt.equals("delete-chat")){
        AlertType = 3;
        }
        if(txt.equals("delete-contact")){
        AlertType = 2;
        }
        if(txt.equals("add-connections")){
        AlertType = 1;
        }
    }
    protected int setLayoutId() {
        return R.layout.cht_alert_dialog_yes_no;
    }
    public boolean getStatusAddContact(){ return addcontact; }

    private void setUpListeners() {
        btn_yes.setOnClickListener(this);
        btn_no.setOnClickListener(this);
    }
    public boolean getStatusDeleteContact(){
        return delete_contact;
    }
    public boolean getStatusDeleteChat(){
        return delete_chat;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cht_alert_btn_yes) {
            dismiss();
            if(AlertType == 1) {
                try {
                    //appSession.setData(ChatSession.CONNECTION_DATA, contactConn);
                    //Contact conn = chatSession.getSelectedConnection();
                    //TODO:Cardozo revisar esta logica ya no aplica, esto viene de un metodo nuevo que lo buscara del module del actor connections//chatManager.getChatUserIdentities();
                    if (true) {
                    //if (chatManager.getContactByLocalPublicKey(contactConn.getRemoteActorPublicKey()) == null) {
                        ContactImpl newContact = new ContactImpl();
                        newContact.setAlias(contactConn.getAlias());
                        newContact.setRemoteActorType(contactConn.getRemoteActorType());
                        newContact.setRemoteActorPublicKey(contactConn.getRemoteActorPublicKey());
                        newContact.setRemoteName(contactConn.getRemoteName());
                        newContact.setContactId(UUID.randomUUID());
                        newContact.setCreationDate(System.currentTimeMillis());
                        newContact.setContactStatus(contactConn.getContactStatus());
                        newContact.setProfileImage(contactConn.getProfileImage());
                        //TODO:Cardozo revisar esta logica ya no aplica, esto viene de un metodo nuevo que lo buscara del module del actor connections//chatManager.getChatUserIdentities();
                        //chatManager.saveContact(newContact);
                        addcontact = true;
                        Toast.makeText(getActivity(), "Contact added", Toast.LENGTH_SHORT).show();
                        //changeActivity(Activities.CHT_CHAT_OPEN_CONTACTLIST, appSession.getAppPublicKey());
                        //dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Contact already exist", Toast.LENGTH_SHORT).show();

                        //changeActivity(Activities.CHT_CHAT_OPEN_CONTACTLIST, appSession.getAppPublicKey());
                        //dismiss();
                    }
                    dismiss();
                    mAdapterCallback.onMethodCallbackContacts();//solution to access to update contacts. j

                //} catch (CantSaveContactException e) {
                //    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                } catch (Exception e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
                //changeActivity(Activities.CHT_CHAT_OPEN_CHATLIST, appSession.getAppPublicKey());
                }else if(AlertType == 2){
//                try {
//                    Contact con = chatSession.getSelectedContact();
//                    //TODO:Cardozo revisar esta logica ya no aplica, esto viene de un metodo nuevo que lo buscara del module del actor connections//chatManager.getChatUserIdentities();
//                    //chatManager.deleteContact(con);
//                    List<Contact> cont=  null;//chatManager.getContacts();
//                    if (cont.size() > 0) {
//                        for (int i=0;i<cont.size();i++){
//                            contactname.add(cont.get(i).getAlias());
//                            contactid.add(cont.get(i).getContactId());
//                            ByteArrayInputStream bytes = new ByteArrayInputStream(cont.get(i).getProfileImage());
//                            BitmapDrawable bmd = new BitmapDrawable(bytes);
//                            contacticon.add(bmd.getBitmap());
//                        }
//                        final ContactListAdapter adaptador =
//                                new ContactListAdapter(getActivity(), contactname, contacticon, contactid,chatManager,
//                                        moduleManager, errorManager, chatSession, getSession(), null);
//                        adaptador.refreshEvents(contactname, contacticon, contactid);
//                    }
//                //}catch(CantGetContactException e) {
//                //    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
//                }catch (Exception e){
//                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
//                }
                delete_contact = true;
            }else if(AlertType == 3){
                try {
                    dismiss();
                }catch (Exception e){
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
                delete_chat = true;
            }
        }

        if (id == R.id.cht_alert_btn_no) {
            dismiss();
        }
    }

    @Override
    protected int setWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }
}
