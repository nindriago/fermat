package com.bitdubai.android_core.app.common.version_1.communication.client_system_broker;

import android.util.Log;

import com.bitdubai.android_core.app.common.version_1.communication.client_system_broker.exceptions.CantCreateProxyException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetModuleManagerException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.ModuleManagerNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_core.FermatSystem;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mati on 2016.03.31..
 */
public class ProxyFactory {

    private static final String TAG = "ProxyFactory";

    private Map<PluginVersionReference,ModuleManager> openModules;

    public ProxyFactory() {
        this.openModules = new HashMap<>();
    }

    public ModuleManager createModuleManagerProxy(PluginVersionReference pluginVersionReference,InvocationHandler invocationHandler) throws CantCreateProxyException {
        ModuleManager moduleManager = null;
        if(!openModules.containsKey(pluginVersionReference)) {
            try {
                Class clazz = FermatSystem.getInstance().getModuleManager2(pluginVersionReference).getClass();
                moduleManager = (ModuleManager) Proxy.newProxyInstance(
                        clazz.getClassLoader(),
                        clazz.getInterfaces(),
                        invocationHandler);
            } catch (CantGetModuleManagerException e) {
                throw new CantCreateProxyException("Cant get module manager from system", e, "factory", "");
            } catch (ModuleManagerNotFoundException e) {
                throw new CantCreateProxyException("Cant fount module manager from system", e, "factory", "");
            }
        }else{
            moduleManager = openModules.get(pluginVersionReference);
        }
        Log.i(TAG,"interfaces: ");
        for (Class<?> aClass : moduleManager.getClass().getInterfaces()) {
            Log.i(TAG,aClass.getName());
        }
        return moduleManager;
    }

    public ModuleManager disposalModuleManager(PluginVersionReference pluginVersionReference){
        return openModules.remove(pluginVersionReference);
    }


}
