// IServerBrokerService.aidl
package com.bitdubai.android_core.app.common.version_1.communication.server_system_broker.aidl;

// Declare any non-default types here with import statements
import com.bitdubai.android_core.app.common.version_1.communication.structure.AIDLObject;
import com.bitdubai.android_core.app.common.version_1.communication.server_system_broker.structure.FermatModuleObjectWrapper;

interface IServerBrokerService {


    FermatModuleObjectWrapper invoqueModuleMethod(
            in String platformCode,
            in String layerCode,
            in String pluginsCode,
            in String developerCode,
            in String version,
            in String method,
            in FermatModuleObjectWrapper[] parameters
            );


    FermatModuleObjectWrapper invoqueModuleMethod2(
                in String platformCode,
                in String layerCode,
                in String pluginsCode,
                in String developerCode,
                in String version,
                in String method,
                in FermatModuleObjectWrapper[] parameters
                );


     boolean isFermatSystemRunning();
}
