package com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.utils.mnemonic;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
import android.widget.EditText;

import com.bitdubai.android_fermat_ccp_wallet_bitcoin.R;
//import com.bitdubai.fermat.R;

/**
 * Created by mati on 2016.02.20..
 */
public class MnemonicSendDialog extends Dialog implements View.OnClickListener{

    private EditText edit_email;

    public MnemonicSendDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mnemonic_dialog);
        edit_email = (EditText) findViewById(R.id.edit_email);

        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_send){
            //encrypt the text with the key and send by email

        }else if (id == R.id.btn_cancel){

        }
        dismiss();
    }
}
