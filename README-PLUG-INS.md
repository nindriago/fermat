![alt text](https://github.com/bitDubai/media-kit/blob/master/MediaKit/Fermat%20Branding/Fermat%20Logotype/Fermat_Logo_3D.png "Fermat Logo")


<br><br>

## Introduction

Plug-ins are one of the three basic components that can be added into the Fermat Framework. The two others are Add-ons, and GUI components. Each Plug-in has a well defined responsability within the system. Usually a Plug-in performs a task on one or more workflows.  

To accomplish its mission, a Plug-in may have its own database and files to persist its state. It also has an internal structure of classes designed specifically to fullfill its goals. Some of these classes implement public interfaces which in return, exposes the Plug-in public services to other Fermat components.


<br>

## Part I: Concepts

Several new concepts are introduced at a Plug-in level. In this section the most important of them are explained.

<br>
### Main Classes
----------------

There are a few classes that every Plug-in has.


#### Developer Class

Each Plug-in has a Developer Class. The reason is that there might be more than one version of the Plug-in at the same time. To deal with this, instead of the Framework instantiating the _Plugin Root_ directly, it calls the Developer Class that is somehow representing the Plug-ins developer. 

By doing so, this class is able to choose which version of the Plug-in to run, and if it detects that a data migration must be done from version 1 to version 2, it coordinates that process too.

An extra function of this class is related to the licensing Developers declaring their Plug-in. 

<br>
#### Plug-in Root Class

The Plug-in root is the starting point of a Plug-in. It is the point of contact between the Framework and the Plug-in. Its main purpose is to implement the interfaces that transforms itself into a service that can be started and stopped by the Framework, and other interfaces that are required in order to receive the references to other plug-ins so as to be able to consume their services.

<br>
### Class Groups
-------------------

#### Public Interfaces

Each Plug-in gives services to other Plug-ins. They do it through public interfaces published either on Fermat API, or on the API platform where the Plug-in belongs. Some clases of its internal structure implements these public interfaces.

<br>
#### Internal Structure

Each Plug-in has an internal class model. Usually it is a hiearchy where one of it classes is the root. Some of them implements the public interface of the Plug-in defined within this Plug-in.

<br>
#### Event Handlers

An Event Handler is a type of Class that is called by the Event Manager Framework whenever an Plug-in event is suscribed to is triggered. A single Plug-in can have as many Event Handlers as needed. Somewhere within the Internal Structure the Plug-in subscribes itself to different type of events, declaring at that point which Event Handler class must be called.

<br>
#### Exceptions

Each Plugin can raise two types of exceptions: 

a. **Internal** : These are exceptions that are thrown and handled within the same Plug-in.

b. **External** : These are exceptions that are thrown within a Plug-in and are expected to be catched by its caller. 

| NOTE | It is not allowed for an exception that is thrown in Plug-in A to go through Plug-in B unhandled and reach Plug-in C. |
| ------------- | :------------- |

<br>
#### Agents

We define Agents as objects, which at runtime will create a new execution thread. We have developed this pattern in order to standardize and simplify the handling of processes that need to run from time to time and perform certain tasks. 

<br>
### Other Elements
-------------------

#### Databases

Plug-ins may have one or more Databases for storing their data. Usually one Database is enough, but in some cases different instances of the same data model are required.

<br>
#### Files

A Plug-in may have one or more files.



<br>

## Part II: Workflow

This section will help you understand the workflow needed to be followed in order to implement a Plug-in in Fermat.

<br>
### Getting Organized
---------------------

#### Issues

It is mandatory that you create an initial set of github issues before you proceed further on the workflow. This will show the rest of topics that someone is working in this functionality and avoid conflicting work early on. It will also hook the team leader into your workflow and allow him to guide and advise you when needed.

A basic hierarchy of issues is created as a first step. The issues are linked to one another just by placing a link on the first commit.

##### Naming Convention

Where we refer to '_Plugin Name_' what we expect is the following information:

* Platform or Super Layer name - 3 characters.
* Layer name
* Plug-in name

All of them separated by " - ". 

##### Linking to a parent Issue

Issues that need to be linked to its parent must have their first line starting with "Parent: " + http link to parent issue. 

##### Tagging the Team Leader

Team leaders are tagged in the second line in order to ask them to assign the issue to you and at the same time subscribe to any issue update. This helps team leaders to follow the issue events and provide assistance or guidance if they see something wrong. The suggested format is:

"@team-leader-user-name please assign this issue to me."

<br>
#### Plug-in Issue Structure

The mandatory initial structure is the following: (note: the word ISSUE is not part of the name)

<br>
##### ISSUE: '_Plugin Name_' - Plug-In

This is the root of your issue structure and must be labeled as _SUPER ISSUE_. It is closed only when all its children and grandchildren are closed.

<br>
##### ISSUE: '_Plugin Name_' - Implementation

This is the Implementation root. It is closed whenever all implementation is done. This issue must be linked to the root of the issue structure.

<br>
##### ISSUE: '_Plugin Name_' - QA
 

This is the QA root. It is closed whenever QA tests are passed. This issue must be linked to the root of the issue structure.

It is expected to have here child issues in the form '_Plugin Name_' QA - Bug Fix n, where n is both the number and the bug name.

<br>

## Part III: How to do it

<br>
### Analysis
------------

#### Public Interfaces

##### Are there any mandatory public interfaces?

Yes, we have two of them and they're the following:

** The Manager Interface ** : This one is usually implemented by the Plug-in Root and its purpose is to allow the caller to have access to other functionality usually implemented on classes of the Internal Structure. 

** The Deals With Interface ** : This interface is intended to be implemented by the Plug-ins consuming the service of a certain Plug-in. By implementing it, they signal the Framework to deliver them a reference of the Plug-in they need to call.

##### Where do we define the public interfaces?

Public interfaces are defined at the API library of the Platform were the Plug-in belongs.

##### How many public interfaces can a Plug-in have?

There is no limit regarding the interfaces a Plug-in may have. Having said that, it is highly recommended not to expose the internal structure of the Plug-in creating a Public Interface for each of the internal classes. 

<br>
#### Database Model

##### How do we analize the Database Model of the Plug-in?

##### How do we choose between a Database or a File?

##### How much data is it allowed to keep?

##### Which are the sustainability policies?

#### Internal Structure Class Model

##### How much freedom do you have to choose a class model for the internal structure?


<br>
### Implementation
------------------

#### Developer Class

Currently there are two types of implementations for this class:

a. **Version 1** : It just instantiates the Plug-in Root and returns it to the Framework.

b. **Version 2** : It registers each version of the Plugin root into the Framework.

To be able to use **Version 2** you must verify if the Platform your Plug-in belongs to already has its own specialized core-api or instead it is started by the Framework wide Fermat-core library.

##### Code Examples

Note in the following samples that the information regarding the licensing of the Plug-in is hard-coded and always the same. This is because the licensing infraestructure is not yet in place.

**Version 1**

```bash

package com.bitdubai.fermat_dap_plugin.layer.identity.asset.user.developer.bitdubai;

import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.PluginDeveloper;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.TimeFrequency;
import com.bitdubai.fermat_api.layer.all_definition.license.PluginLicensor;
import com.bitdubai.fermat_dap_plugin.layer.identity.asset.user.developer.bitdubai.version_1.IdentityUserPluginRoot;

/**
 * Created by Nerio on 07/09/15.
 */
public class DeveloperBitDubai implements PluginDeveloper, PluginLicensor {

    Plugin plugin;


    public DeveloperBitDubai () {
        plugin = new IdentityUserPluginRoot();
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public int getAmountToPay() {
        return 100;
    }

    @Override
    public CryptoCurrency getCryptoCurrency() {
        return CryptoCurrency.BITCOIN;
    }

    @Override
    public String getAddress() {
        return "13gpMizSNvQCbJzAPyGCUnfUGqFD8ryzcv";
    }

    @Override
    public TimeFrequency getTimePeriod() {
        return TimeFrequency.MONTHLY;
    }
}


```

**Version 2**

```bash

package com.bitdubai.fermat_ccp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai;

import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPluginDeveloper;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantRegisterVersionException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantStartPluginDeveloperException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginDeveloperReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.TimeFrequency;
import com.bitdubai.fermat_api.layer.all_definition.license.PluginLicensor;
import com.bitdubai.fermat_ccp_plugin.layer.basic_wallet.bitcoin_wallet.developer.bitdubai.version_1.BitcoinWalletBasicWalletPluginRoot;

/**
 * Created by loui on 30/04/15.
 * Modified by lnacosta (laion.cj91@gmail.com) on 23/10/2015.
 */
public class DeveloperBitDubai extends AbstractPluginDeveloper implements PluginLicensor {

    public DeveloperBitDubai () {
        super(new PluginDeveloperReference(Developers.BITDUBAI));
    }

    @Override
    public void start() throws CantStartPluginDeveloperException {
        try {

            this.registerVersion(new BitcoinWalletBasicWalletPluginRoot());

        } catch (CantRegisterVersionException e) {

            throw new CantStartPluginDeveloperException(e, "", "Error registering plugin versions for the developer.");
        }
    }

    @Override
    public int getAmountToPay() {
        return 100;
    }

    @Override
    public CryptoCurrency getCryptoCurrency() {
        return CryptoCurrency.BITCOIN;
    }

    @Override
    public String getAddress() {
        return "13gpMizSNvQCbJzAPyGCUnfUGqFD8ryzcv";
    }

    @Override
    public TimeFrequency getTimePeriod() {
        return TimeFrequency.MONTHLY;
    }
}

```

<br>
#### Plug-in Root

Currently there are two types of implementations for this class:

a. **Version 1** : Get references of Components by implementing _Deal With_ interfaces.

b. **Version 2** : Get the references by asking the Framework to provide them.

To be able to use **Version 2** you must verify if the Platform your Plug-in belongs to already has its own specialized core-api or instead it is started by the Framework wide Fermat-core library.

##### Version 1

In this case this class implements many interfaces, Most of them to obtain references to other Components. Please note that intentionally interfaces are declared on alphabetical order and implemented in this order as well. Only the **Service** and **Plugin** interfaces are mandatory.

a. **DealsWithErrors** : Means that the Plug-in needs a reference to the _Error Manager_ in order to report _Unhandled Exceptions_.

b. **DealsWithEvents** : Means that the Plug-in needs a reference to the _Event Manager_ to either raise events or listen to other events Components.

c. **DealsWithPluginDatabaseSystem** : Means that the Plug-in needs a reference to the _Database Manager_ in order to have its own databases.

d. **DealsWithPluginFileSystem** : Means that the Plug-in needs a reference to the _File System_ in order to report, create, write and read files.

e. **LogManagerForDevelopers** : Means that the Plug-in agrees that its databases can be explored by _Developers_ from outside the Plug-in for debbuggin purposes.

f. **Plugin** : Declares the current component as a Plug-in and allows it to receive its identity from the Framework. This identity allows Plug-ins for instance, to have their own set of _Files_ and _Databases_ . Also to ask the Framework for references to other Components (in **Version 2**).

g. **Service** : Enables the Framework to start, pause and stop the Plug-in.

h. **Serializable** : When present, enables the Plug-in to serialize its current state.

##### Version 2

In this version the Plug-in Root class extends from AbstractPlugin which does the implementation of **Plugin** and **Service** by itself.

Regarding the rest of the interfaces of Version 1, in this case they don't need to be implemented as the procedure to obtain the needed references was changed in order to obtain the references, like in this example.


```bash

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.ANDROID, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.ANDROID, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

```

As each Plug-in is a _Service_ that can be started, paused and stopped by the Framework, if you need to do something when this is happening, you can override the services methods from AbstractPlugin.




# FROM HERE 

#### SOMEBODY SHOULD CONTINUE THE STYLE, FORMAT AND QUALITY OF THE EXPLANATION THAT COMES FROM THE TOP -- Luis Molina



<br>
#### Public Interfaces


Additional notes:

* Most of the plug-ins can be consumed. For this we'll define in the api of the platform where the plug-in belongs the public interfaces of the same.
* If any method of the interface throws an exception, enums or any other, this exception must be public too, and must be created in the api too. 
* We've a structure for this:
 * Interfaces:  Layer -> PluginName -> Interfaces
 * Exceptions:  Layer -> PluginName -> Exceptions
 * Enums:       Layer -> PluginName -> Enums
 * Events:      Layer -> PluginName -> Events
 * Annotations: Layer -> PluginName -> Annotations
 * Utils:       Layer -> PluginName -> Utils


<br>
#### Database

To implement a database on a plug-in should follow a predetermined structure design, which basically consists in the creation of three classes:
* 'Name of the plug-in' Dao. This class must contain all methods that enable plug-in classes to open the database plug-in, insert, update, query, and delete records from the database.
* 'Name of the plug-in' Constants This class must include the names of each of the tables and columns of the database plug-in. The nomenclature used must follow these rules:
 - You must use public static String objects.
 - The name of this object must be uppercase
 - You must specify what information clearly stores this column.
 - You must assign a String object before mentioning it with a brief description of the content of this column, you should use lowercase and avoid special characters and use as a word separator character '_'.
Here are some examples:
  1. Name of the database, PLUGIN_NAME_DATABASE public static final String = "plugin_database" .;
  2. The name of a table, PLUGIN_TABLE_NAME public static final String = "plugin_table";
  3. PLUGIN_TABLE_COLUMN_NAME public static final String = "any_column_name";
* 'Name of the plug-in' DatabaseFactory. This class is responsible for creating the tables in the database where the information is kept.

Additionally, if that information is required the database can be displayed through the SubApp Develop, present at runtime on the App Fermat, the implementation of the class' name plug-in'DeveloperDatabaseFactory is required. This class will be instantiated by the PluginRoot class Plug-in for achieving the aforementioned display.

To facilitate the work of creating these classes, Fermat has developed a script in Groovy, which automates the creation of these classes, following the established design, this plug-in is available at:
[Script in Groovy Documentation](https://github.com/bitDubai/fermat/blob/master/scripts/database/database_classes_generator/documentation_en.md)
 and 
 [Script Groovy](https://github.com/bitDubai/fermat/blob/master/scripts/database/database_classes_generator/script.groovy)
<br>
#### Agents

The basic purpose of an agent is to run a parallel process or simultaneous to the main run of Plug-in, usually in order to achieve this we need to create classes that extend the Thread class, and rewrite the main "run (method) " which is to be run primarily when starting a new thread or process which represent this agent.

We must consider that we should not create indiscriminate agents, we always use as there is no better alternative for carrying out the task or process to be performed, as using Agents excessively may compromise the stability and performance.

Whenever you choose this solution for the activity or task required by our plug-in, we should take into account the following points:

* The plug-in name must begin with your activity and end with the word "Agent".
Example: TemplateNetworkServiceRegistrationProcessAgent.

* The agent should be smart enough to stop automatically when contemplating your task.

* Consider and calculate the timeout (sleeping time) between each run, the process performed by the agent, since very straight executions can reduce application performance considerably.

<br>
#### Internal Structure

##### Structure Root

##### Structure Clasess

##### Agents

The basic structure of a class that represents an agent is really simple, just take into consideration the following points:

* The implementation of an agent can be performed by inheriting from the "*java.langThread*" class or implementing the "*java.Runnable*" interface.
* At the beginning of the class you must declare a constant that indicates the downtime or waiting time thread when it is going to "sleep".
* Must be followed by any other attribute or inner member of the class.
* Continue with the implementation of the "*public void run()*" method, if the logic in this method is very complex and long, this logic should be developed in a separate method and the same be called within the "*public void run()*" method body, so make the code more readable and understandable. 

Code example:

```java
  ...
    @Override
    public void run() {
        try {
            while (isRunning){
               complexImplementationMethod();
            }
            
           if (!isInterrupted()){
             sleep(WsCommunicationVpnServerManagerAgent.SLEEP_TIME);
           }
    }
  ...
```

Example code of a complete class implementation:

```java
/*
 * @#WsCommunicationVpnServerManagerAgent.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI
 */
package com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.server.developer.bitdubai.version_1.structure.vpn;


import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.PlatformComponentProfile;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.server.developer.bitdubai.version_1.structure.WsCommunicationCloudServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.server.developer.bitdubai.version_1.structure.vpn.WsCommunicationVpnServerManagerAgent</code> this
 * agent manage all the WsCommunicationVpnServer created
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 12/09/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class WsCommunicationVpnServerManagerAgent extends Thread{

    /**
     * Represent the SLEEP_TIME
     */
    private static long SLEEP_TIME = 60000;

    /**
     * Holds the vpnServersActivesCache
     */
    private List<WsCommunicationVPNServer> vpnServersActivesCache;

    /**
     * Represent the lastPortAssigned
     */
    private Integer lastPortAssigned;

    /**
     * Represent the hostIp
     */
    private String hostIp;

    /**
     * Represent the isRunning
     */
    private boolean isRunning;

    /**
     * Constructor whit parameter
     *
     * @param serverIp
     * @param cloudServerIp
     */
    public WsCommunicationVpnServerManagerAgent(String serverIp, Integer cloudServerIp){
        this.vpnServersActivesCache = new ArrayList<>();
        this.lastPortAssigned = cloudServerIp;
        this.hostIp = serverIp;
        this.isRunning = Boolean.FALSE;
    }

    /**
     * Create a new WsCommunicationVPNServer
     *
     * @param participants
     */
    public WsCommunicationVPNServer createNewWsCommunicationVPNServer(List<PlatformComponentProfile> participants, WsCommunicationCloudServer wsCommunicationCloudServer, NetworkServiceType networkServiceTypeApplicant) {

        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostIp, (lastPortAssigned+=1));
        WsCommunicationVPNServer vpnServer = new WsCommunicationVPNServer(inetSocketAddress, participants, wsCommunicationCloudServer, networkServiceTypeApplicant);
        vpnServersActivesCache.add(vpnServer);
        vpnServer.start();

        return vpnServer;
    }


    /**
     * (non-javadoc)
     * @see Thread#run()
     */
    @Override
    public void run() {

        try {

            //While is running
            while (isRunning){

                //If empty
                if (vpnServersActivesCache.isEmpty()){
                    //Auto stop
                    isRunning = Boolean.FALSE;
                    this.interrupt();
                }

                for (WsCommunicationVPNServer wsCommunicationVPNServer : vpnServersActivesCache) {

                    try {
                        /*
                         * Send the ping message to this participant
                         */
                        wsCommunicationVPNServer.sendPingMessage();

                    }catch (Exception ex){

                        //Close all connection and stop the vpn server
                        wsCommunicationVPNServer.closeAllConnections();
                        wsCommunicationVPNServer.stop();
                        vpnServersActivesCache.remove(wsCommunicationVPNServer);
                    }

                }

                if (!isInterrupted()){
                    sleep(WsCommunicationVpnServerManagerAgent.SLEEP_TIME);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * (non-javadoc)
     * @see Thread#start()
     */
    @Override
    public synchronized void start() {
        isRunning = Boolean.TRUE;
        super.start();
    }

    /**
     * Get the is running
     * @return boolean
     */
    public boolean isRunning() {
        return isRunning;
    }
}

```
  **IMPORTANT:** 
* Agents should have the ability to stop automatically when their task is completed.
* It is recommended as good practice, that before calling the method "*public static native void sleep(long millis) throws InterruptedException;*" you evaluate if the thread has not been interrupted by another process before putting it into downtime.
  
  Code example:

```java
  ...
  if (!isInterrupted()){
      sleep(WsCommunicationVpnServerManagerAgent.SLEEP_TIME);
  }
  ...
```


<br>
#### Event Handlers


<br>

## Part IV: References

<br>

##Test Packages

To perform the test unit should establish a packet structure comprising:

* A package called "test" in the "src" directory.
Within the package "test" should create a package called "java".
within the "java" package should create a package with the following structure:
```java
unit.com.bitdubai."Plugin Name"
```
It should be created within this structure a package for each class to be tested. These packages must be called with the name of the class to be tested.
* Finally within this package must create a class for each test method with the method name followed with the legend "Test"
 <br>
For example:
My method is called "myMethod" when tested should would create a class named "MyMethodTest"

<br>

##The Database Script

Within the database package should define the following classes:

**DatabaseConstants:** Within this should define constants table names, column names, and primary keys.

**DatabaseFactory:** Class to create the database and tables.

**DeveloperDatabaseFactory:** Contains methods for queries from the developer Sub-App.

**DatabaseDao:** Contains all methods for inserting, queries, update, delete data in tables of the database.
<br>
##ConstantsDatabase:

This class must be defined with the main name of the plugin followed by
legend "databaseConstants".
<br>
For example:
```java
"MyPluginDatabaseConstants"
```
Inside the class must be defined constant for the name of the database table names, column names and names of primary keys.
The name must be in upper case continued the legend that says you element being defined.
<br>
For example:
```java
public class MyPluginNameDatabaseConstants {    
/**define database name**/
	public static final String DECLARE_NAME_OF_THE_DATABASE_DATABASE_NAME ="declare_name_of_the_database";
/**define table name**/
      public static final String DECLARING_THE_TABLE_NAME_TABLE_NAME ="declaring_the_table_name";
      /**define column name**/
      static final String DECLARE_THE_COLUMN_NAME_COLUMN_NAME="declare_the_column_name";
      /**define primary key name**/
      static final String DECLARE_THE_PRIMARY_KEY_NAME_FIRST_KEY_COLUMN ="the_primary_key_name";
}
```

##DatabaseFactory:

This class is responsible for creating the database and tables.
The name of the class must be defined with the name followed by the words "DatabaseFactory" plugin.
<br>
Inside the class must implement the interface "DealsWithPluginDatabaseSystem"
where the method is implemented:
<br>
```java
@Override 
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) { 
        this.pluginDatabaseSystem = pluginDatabaseSystem; 
    }
```
Inside the class should be created createDatabase () method, where the creation of the database and the tables are well defined.
In this case the DatabaseConstants class is used to obtain the name of the database table names and column names.
<br>
The code should be formatted as follows:
<br>
```java
package   com.bitdubai.fermat_ccp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.database; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateTableException; 
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.InvalidOwnerIdException; 
import java.util.UUID; 
/** 
 *  The Class  <code>com.bitdubai.fermat_ccp_plugin.layer.identity.intra_user.developer.bitdubai.version_1.database.Intra UserIdentityDatabaseFactory</code> 
 * is responsible for creating the tables in the database where it is to keep the information. 
 * <p/> 
 * 
 * Created by Leon Acosta - () on 20/11/15. 
 * 
 * @version 1.0 
 * @since Java JDK 1.7 
 */ 
public class IntraUserIdentityDatabaseFactory implements DealsWithPluginDatabaseSystem { 
    /** 
     * DealsWithPluginDatabaseSystem Interface member variables. 
     */ 
    private PluginDatabaseSystem pluginDatabaseSystem; 
    /** 
     * Constructor with parameters to instantiate class 
     * . 
     * 
     * @param pluginDatabaseSystem DealsWithPluginDatabaseSystem 
     */ 
    public IntraUserIdentityDatabaseFactory(PluginDatabaseSystem pluginDatabaseSystem) { 
        this.pluginDatabaseSystem = pluginDatabaseSystem; 
    } 
    /** 
     * Create the database 
     * 
     * @param ownerId      the owner id 
     * @param databaseName the database name 
     * @return Database 
     * @throws CantCreateDatabaseException 
     */ 
    protected Database createDatabase(UUID ownerId, String databaseName) throws CantCreateDatabaseException { 
        Database database; 
        /** 
         * I will create the database where I am going to store the information of this wallet. 
         */ 
        try { 
            database = this.pluginDatabaseSystem.createDatabase(ownerId, databaseName); 
        } catch (CantCreateDatabaseException cantCreateDatabaseException) { 
            /** 
             * I can not handle this situation. 
             */ 
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateDatabaseException, "", "Exception not handled by the plugin, There is a problem and i cannot create the database."); 
        } 
        /** 
         * Next, I will add the needed tables. 
         */ 
        try { 
            DatabaseTableFactory table; 
            DatabaseFactory databaseFactory = database.getDatabaseFactory(); 
           /** 
            * Create Intra User table. 
            */ 
           table = databaseFactory.newTableFactory(ownerId, IntraUserIdentityDatabaseConstants.INTRA_USER_TABLE_NAME); 

            table.addColumn(IntraUserIdentityDatabaseConstants.INTRA_USER_INTRA_USER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 100, Boolean.TRUE); 
            table.addColumn(IntraUserIdentityDatabaseConstants.INTRA_USER_ALIAS_COLUMN_NAME, DatabaseDataType.STRING, 100, Boolean.FALSE); 
            table.addColumn(IntraUserIdentityDatabaseConstants.INTRA_USER_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 100, Boolean.FALSE); 

             table.addIndex(IntraUserIdentityDatabaseConstants.INTRA_USER_FIRST_KEY_COLUMN); 

            try { 
                //Create the table 
                databaseFactory.createTable(ownerId, table); 
            } catch (CantCreateTableException cantCreateTableException) { 
                throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, cantCreateTableException, "", "Exception not handled by the plugin, There is a problem and i cannot create the table."); 
            } 
} catch (InvalidOwnerIdException invalidOwnerId) { 
            /** 
             * This shouldn't happen here because I was the one who gave the owner id to the database file system, 
             * but anyway, if this happens, I can not continue. 
             */ 
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, invalidOwnerId, "", "There is a problem with the ownerId of the database."); 
        } 
        return database; 
    } 
    /** 
     * DealsWithPluginDatabaseSystem Interface implementation. 
     */ 
    @Override 
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) { 
        this.pluginDatabaseSystem = pluginDatabaseSystem; 
    } 
}
```
##Developer DatabaseFactory

Inside the class methods must be created to query the database created from the Developer SubApp
It must contain the initializeDatabase () method, which opens the database, if it exists, or it will create not exist. Also a method to query the database.
<br>
The following code illustrates how should be the format of the template:
<br>
```java
public class IntraUserIdentityDeveloperDatabaseFactory implements DealsWithPluginDatabaseSystem, DealsWithPluginIdentity { 
    /** 
     * DealsWithPluginDatabaseSystem Interface member variables. 
     */ 
    PluginDatabaseSystem pluginDatabaseSystem; 
    /** 
     * DealsWithPluginIdentity Interface member variables. 
     */ 
    UUID pluginId; 
    Database database; 
    /** 
     * Constructor 
     * 
     * @param pluginDatabaseSystem 
     * @param pluginId 
     */ 
    public IntraUserIdentityDeveloperDatabaseFactory(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) { 
        this.pluginDatabaseSystem = pluginDatabaseSystem; 
        this.pluginId = pluginId; 
    } 
    /** 
     * This method open or creates the database i'll be working with 
     * 
     * @throws CantInitializeIntraUserIdentityDatabaseException 
     */ 
    public void initializeDatabase() throws CantInitializeIntraUserIdentityDatabaseException { 
        try { 
             /* 
              * Open new database connection 
              */ 
            database = this.pluginDatabaseSystem.openDatabase(pluginId, pluginId.toString()); 
        } catch (CantOpenDatabaseException cantOpenDatabaseException) { 
             /* 
              * The database exists but cannot be open. I can not handle this situation. 
              */ 
            throw new CantInitializeIntraUserIdentityDatabaseException(cantOpenDatabaseException.getMessage()); 
        } catch (DatabaseNotFoundException e) { 
             /* 
              * The database no exist may be the first time the plugin is running on this device, 
              * We need to create the new database 
              */ 
            IntraUserIdentityDatabaseFactory intraUserIdentityDatabaseFactory = new IntraUserIdentityDatabaseFactory(pluginDatabaseSystem); 
            try { 
                  /* 
                   * We create the new database 
                   */ 
                database = intraUserIdentityDatabaseFactory.createDatabase(pluginId, pluginId.toString()); 
            } catch (CantCreateDatabaseException cantCreateDatabaseException) { 
                  /* 
                   * The database cannot be created. I can not handle this situation. 
                   */ 
                throw new CantInitializeIntraUserIdentityDatabaseException(cantCreateDatabaseException.getMessage()); 
            } 
        } 
    } 
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) { 
        /** 
         * I only have one database on my plugin. I will return its name. 
         */ 
        List<DeveloperDatabase> databases = new ArrayList<DeveloperDatabase>(); 
        databases.add(developerObjectFactory.getNewDeveloperDatabase("Intra User", this.pluginId.toString())); 
        return databases; 
    } 
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory) { 
        List<DeveloperDatabaseTable> tables = new ArrayList<DeveloperDatabaseTable>(); 
           /** 
            * Table Intra User columns. 
            */ 
           List<String> intraUserColumns = new ArrayList<String>(); 

              intraUserColumns.add(IntraUserIdentityDatabaseConstants.INTRA_USER_INTRA_USER_PUBLIC_KEY_COLUMN_NAME); 
              intraUserColumns.add(IntraUserIdentityDatabaseConstants.INTRA_USER_ALIAS_COLUMN_NAME); 
              intraUserColumns.add(IntraUserIdentityDatabaseConstants.INTRA_USER_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME); 
           /** 
            * Table Intra User addition. 
            */ 
                   DeveloperDatabaseTable intraUserTable = developerObjectFactory.getNewDeveloperDatabaseTable(IntraUserIdentityDatabaseConstants.INTRA_USER_TABLE_NAME, intraUserColumns); 
                   tables.add(intraUserTable); 


        return tables; 
    } 
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabaseTable developerDatabaseTable) { 
        /** 
         * Will get the records for the given table 
         */ 
        List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<DeveloperDatabaseTableRecord>(); 
        /** 
         * I load the passed table name from the SQLite database. 
         */ 
        DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName()); 
        try { 
            selectedTable.loadToMemory(); 
            List<DatabaseTableRecord> records = selectedTable.getRecords(); 
            for (DatabaseTableRecord row: records){ 
                List<String> developerRow = new ArrayList<String>(); 
                /** 
                 * for each row in the table list 
                 */ 
                for (DatabaseRecord field : row.getValues()){ 
                    /** 
                     * I get each row and save them into a List<String> 
                     */ 
                    developerRow.add(field.getValue()); 
                } 
                /** 
                 * I create the Developer Database record 
                 */ 
                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow)); 
            } 
            /** 
             * return the list of DeveloperRecords for the passed table. 
             */ 
        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) { 
            /** 
             * if there was an error, I will returned an empty list. 
             */ 
            database.closeDatabase(); 
            return returnedRecords; 
        } catch (Exception e){ 
            database.closeDatabase(); 
            return returnedRecords; 
        } 
        database.closeDatabase(); 
        return returnedRecords; 
    }
```
##Database Dao
within the class should define the logic for queries, update, delete, inserts, also the method initializeDatabase (), which opens the database, if present, or created if not exist. The logic depends on the logic of the plugin.
<br><br>
In the documentation of Fermat there is a tutorial, which has defined a script to create the classes mentioned, less DatabaseDao class.
<br>
The following link is the template of how these classes should be defined.
[Database Classes Generator](https://github.com/bitDubai/fermat/tree/2a20518c484322846d5727499dfff16c8ddc0bd2/fermat-documentation/scripts/database/database_classes_generator)

###Folder Structure
<br>
The Folder Structure must be comprised of a main directory that contains three letters in upper case summarizing the plugin name.
<br>
For example:
If the plugin is called Bank, the name of the main directory should be BNK.
<br>
On the main directory should create at least two directory required. One will be called "library" and the other called "plugin".
<br>
* **"library"**
<br>
Inside the "library" directory will contain the api where you may define the public interfaces that connect the components of the platform.
<br>
For this it is necessary to define a directory called "api" in creating a module package, it should be called as the plugin. The rule must be respected to define the package name consists of placing the name "Fermat" followed by the initials three letters of the legend followed by "api" plugin. They must be inserted by means of a hyphen between separate legend.
For example:
<br>
```java
"fermat-bnk-api"
```
* **"plugin"**
<br>
Inside the "plugin" directory must be defined modules that have the Plugin. These should be defined as java packages.
<br>
The name of the java package should consist of the words "Fermat" followed by three letters of identifying the Plugin followed the legend "plugin" followed with leyeda identifying the module and finally ends with the words "bitdubai".
An Example How should the packet format:
```java
"fermat-bnk-plugin-wallet-bank-money-bitdubai"
```
##The build.gradle File
Normally it is necessary to respect a defined structure, but this always depends on the libraries that are used or required.
<br>
However, there are dependencies, settings, tasks and other parameters that must be defined in the file for the correct build.gradle other operations must be respected componentes.
<br>
Dependencies, task and configurations to be defined by default:
```java
apply plugin: 'java'
apply plugin: 'jacoco'
apply plugin: 'findbugs'
apply plugin: 'checkstyle'
apply plugin: 'pmd'

sourceCompatibility = 1.7
version = '1.0'

repositories {
    mavenCentral()
}

configurations {
    cucumberRuntime {
        extendsFrom testRuntime
    }
}

dependencies {
    compile project(':fermat-api')
    compile project(':fermat-bnk-api')
    compile project(':fermat-pip-api')

    testCompile group: 'junit', name: 'junit', version: '4.11'
    testCompile group: 'org.easytesting', name: 'fest-assert-core', version: '2.0M10'
    testCompile group: 'com.googlecode.catch-exception', name: 'catch-exception', version: '1.2.0'
    testCompile 'info.cukes:cucumber-java:1.2.4'
    testCompile 'info.cukes:cucumber-junit:1.2.4'
}

pmd {
    toolVersion = '5.1.3'
}

tasks.withType (FindBugs) {
    reports {
        xml.enabled = false
        html.enabled = true
    }
}

def jacocoHtmlReport = ""

jacocoTestReport{
    reports{
        jacocoHtmlReport = "Code Coverage HTML Report: file://" + html.destination + "/index.html"
    }
}

task cucumber() {
    dependsOn assemble, compileTestJava
    doLast {
        javaexec {
            main = "cucumber.api.cli.Main"
            classpath = configurations.cucumberRuntime + sourceSets.main.output + sourceSets.test.output
            args = ['--plugin', 'pretty', '--glue', 'gradle.cucumber', 'src/test/resources']
        }
    }
}

task testCoverage(dependsOn: jacocoTestReport) << {
    println jacocoHtmlReport
}

jacocoTestReport.dependsOn clean, test
jacocoTestReport.mustRunAfter test
test.mustRunAfter clean
```
Inside file, defined libraries and parameters for testing on plugin.
These library and parameters are important because they are responsible for the test tasks and generate reports of test coverage.
<br>
Inside the document Unit Testing is detailed how these parameters must be declared in the build.gradle file
<br><br><br><br><br><br><br>





### FALTA HABLAR DE:

* Location of a Plug-in

* The build.gradle File

* Gradle Plug-ins

* Folder Structure

* Main Packages

* Test Packages

* The Database Script

