/*
 * @#ComponentConnectionRespondPacketProcessor.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.tyrus.processors;

import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.PlatformComponentProfile;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmetricCryptography;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.components.PlatformComponentProfileCommunication;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatPacket;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatPacketType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.JsonAttNamesConstants;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.tyrus.WsCommunicationsTyrusCloudClientChannel;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.tyrus.vpn.WsCommunicationTyrusVPNClientManagerAgent;
import com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.util.ServerConf;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.ws.communications.cloud.client.developer.bitdubai.version_1.structure.processors.ComponentConnectionRespondTyrusPacketProcessor</code> implement
 * the logic to process the packet when a packet type <code>com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatPacketType.COMPONENT_CONNECTION_RESPOND</code> is receive by the server.
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 13/09/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ComponentConnectionRespondTyrusPacketProcessor extends FermatTyrusPacketProcessor {

    /**
     * Constructor
     *
     * @param wsCommunicationsTyrusCloudClientChannel
     */
    public ComponentConnectionRespondTyrusPacketProcessor(WsCommunicationsTyrusCloudClientChannel wsCommunicationsTyrusCloudClientChannel) {
        super(wsCommunicationsTyrusCloudClientChannel);
    }

    /**
     * (no-javadoc)
     * @see FermatTyrusPacketProcessor#processingPackage(FermatPacket)
     */
    @Override
    public void processingPackage(FermatPacket receiveFermatPacket) {

        //System.out.println(" --------------------------------------------------------------------- ");
       //System.out.println("ComponentConnectionRespondTyrusPacketProcessor - Starting processingPackage");

        /*
         * Get the message content and decrypt
         */
        String messageContentJsonStringRepresentation = AsymmetricCryptography.decryptMessagePrivateKey(receiveFermatPacket.getMessageContent(), getWsCommunicationsTyrusCloudClientChannel().getClientIdentity().getPrivateKey());


        //System.out.println("ComponentConnectionRespondTyrusPacketProcessor - messageContentJsonStringRepresentation = "+messageContentJsonStringRepresentation);

        try {

            /*
             * Construct the json object
             */
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject respond = parser.parse(messageContentJsonStringRepresentation).getAsJsonObject();

            //Get all values
            URI vpnServerUri = new URI(respond.get(JsonAttNamesConstants.VPN_URI).getAsString());
            String vpnServerIdentity = respond.get(JsonAttNamesConstants.VPN_SERVER_IDENTITY).getAsString();
            PlatformComponentProfile participantVpn = gson.fromJson(respond.get(JsonAttNamesConstants.APPLICANT_PARTICIPANT_VPN).getAsString(), PlatformComponentProfileCommunication.class);
            PlatformComponentProfile remotePlatformComponentProfile = gson.fromJson(respond.get(JsonAttNamesConstants.REMOTE_PARTICIPANT_VPN).getAsString(), PlatformComponentProfileCommunication.class);
            PlatformComponentProfile remoteNsPlatformComponentProfile = gson.fromJson(respond.get(JsonAttNamesConstants.REMOTE_PARTICIPANT_NS_VPN).getAsString(), PlatformComponentProfileCommunication.class);

            /*
             * get the Server ip and port
             */
            String ServerIp = getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getServerIp();
            Integer ServerPort = getWsCommunicationsTyrusCloudClientChannel().getWsCommunicationsTyrusCloudClientConnection().getServerPort();

            vpnServerUri = new URI(ServerConf.WS_PROTOCOL + ServerIp + ":" + ServerPort + vpnServerUri);

            System.out.println("ComponentConnectionRespondTyrusPacketProcessor - vpnServerUri to connect = "+vpnServerUri);

            /*
             * Get the  wsCommunicationVPNClientManagerAgent
             */
            WsCommunicationTyrusVPNClientManagerAgent wsCommunicationTyrusVPNClientManagerAgent = WsCommunicationTyrusVPNClientManagerAgent.getInstance();

            /*
             * Create a new VPN client
             */
            wsCommunicationTyrusVPNClientManagerAgent.createNewWsCommunicationVPNClient(vpnServerUri, vpnServerIdentity, participantVpn, remotePlatformComponentProfile, remoteNsPlatformComponentProfile, getWsCommunicationsTyrusCloudClientChannel().getEventManager());

        } catch (Exception e) {
           throw new RuntimeException(e);
        }

    }

    /**
     * (no-javadoc)
     * @see FermatTyrusPacketProcessor#getFermatPacketType()
     */
    @Override
    public FermatPacketType getFermatPacketType() {
        return FermatPacketType.COMPONENT_CONNECTION_RESPOND;
    }
}
