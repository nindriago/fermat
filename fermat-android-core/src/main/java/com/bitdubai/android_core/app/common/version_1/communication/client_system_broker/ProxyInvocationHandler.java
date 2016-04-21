package com.bitdubai.android_core.app.common.version_1.communication.client_system_broker;

import android.util.Log;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by mati on 2016.03.29..
 */
public class ProxyInvocationHandler<T extends ModuleManager> implements InvocationHandler {

    private static final String TAG = "ProxyHandler";

    private ClientSystemBrokerService clientSystemBrokerService;
    private String responseStr;
    private PluginVersionReference pluginVersionReference;


    public ProxyInvocationHandler(ClientSystemBrokerService clientSystemBrokerService, String responseStr, PluginVersionReference pluginVersionReference) {
        this.clientSystemBrokerService = clientSystemBrokerService;
        this.responseStr = responseStr;
        this.pluginVersionReference = pluginVersionReference;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        Log.i(TAG,"object: "+ proxy.getClass().getInterfaces());
        Log.i(TAG,"method: "+ method.getName());
        Log.i(TAG, "args: " + args);

        return clientSystemBrokerService.sendMessage(responseStr,
               proxy,
               method,
               args);

    }

    public PluginVersionReference getPluginVersionReference() {
        return pluginVersionReference;
    }
}
