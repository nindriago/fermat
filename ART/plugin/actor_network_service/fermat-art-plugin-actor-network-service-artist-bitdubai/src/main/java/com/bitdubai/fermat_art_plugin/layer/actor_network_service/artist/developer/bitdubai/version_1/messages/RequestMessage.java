package com.bitdubai.fermat_art_plugin.layer.actor_network_service.artist.developer.bitdubai.version_1.messages;

import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.util.Base64;
import com.bitdubai.fermat_art_api.layer.actor_network_service.enums.ConnectionRequestAction;
import com.bitdubai.fermat_art_plugin.layer.actor_network_service.artist.developer.bitdubai.version_1.enums.MessageTypes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.UUID;

/**
 * The class <code>com.bitdubai.fermat_cbp_plugin.layer.actor_network_service.crypto_broker.developer.bitdubai.version_1.messages.RequestMessage</code>
 * contains the structure of a Request message for this plugin. Connection or Disconnection.
 * <p>
 * Created by Gabriel Araujo 31/03/2016.
 */
public class RequestMessage extends NetworkServiceMessage {

    private final UUID                    requestId           ;
    private final String                  senderPublicKey     ;
    private final PlatformComponentType   senderActorType     ;
    private final String                  senderAlias         ;
    private final byte[]                  senderImage         ;
    private final String                  destinationPublicKey;
    private final PlatformComponentType destinationActorType;
    private final ConnectionRequestAction requestAction       ;
    private final long                    sentTime            ;

    public RequestMessage(final UUID requestId,
                          final String senderPublicKey,
                          final PlatformComponentType senderActorType,
                          final String senderAlias,
                          final byte[] senderImage,
                          final String destinationPublicKey,
                          PlatformComponentType destinationActorType,
                          final ConnectionRequestAction requestAction,
                          final long sentTime) {

        super(MessageTypes.CONNECTION_REQUEST);

        this.requestId            = requestId           ;
        this.senderPublicKey      = senderPublicKey     ;
        this.senderActorType      = senderActorType     ;
        this.senderAlias          = senderAlias         ;
        this.senderImage          = senderImage         ;
        this.destinationPublicKey = destinationPublicKey;
        this.destinationActorType = destinationActorType;
        this.requestAction        = requestAction       ;
        this.sentTime             = sentTime            ;
    }

    private RequestMessage(JsonObject jsonObject, Gson gson) {

        super(MessageTypes.CONNECTION_REQUEST);

        this.requestId            = UUID.fromString(jsonObject.get("requestId").getAsString());
        this.senderPublicKey      = jsonObject.get("senderPublicKey").getAsString();
        this.senderActorType      = gson.fromJson(jsonObject.get("senderActorType").getAsString(), PlatformComponentType.class);
        this.senderAlias          = jsonObject.get("senderAlias").getAsString();
        this.senderImage          = Base64.decode(jsonObject.get("senderImage").getAsString(), Base64.DEFAULT);
        this.destinationPublicKey = jsonObject.get("destinationPublicKey").getAsString();
        this.destinationActorType = gson.fromJson(jsonObject.get("destionationActorType").getAsString(), PlatformComponentType.class);
        this.requestAction        = gson.fromJson(jsonObject.get("requestAction").getAsString(), ConnectionRequestAction.class);
        this.sentTime             = jsonObject.get("sentTime").getAsLong();

    }

    public static RequestMessage fromJson(String jsonString){

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
        return new RequestMessage(jsonObject, gson);
    }

    @Override
    public String toJson() {

        Gson gson = new Gson();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messageType",          getMessageType().toString());
        jsonObject.addProperty("requestId",            requestId.toString());
        jsonObject.addProperty("senderPublicKey",      senderPublicKey);
        jsonObject.addProperty("senderActorType",      senderActorType.toString());
        jsonObject.addProperty("senderAlias",          senderAlias);
        jsonObject.addProperty("senderImage",          Base64.encodeToString(senderImage, Base64.DEFAULT));
        jsonObject.addProperty("destinationPublicKey", destinationPublicKey);
        jsonObject.addProperty("destionationActorType", destinationActorType.toString());
        jsonObject.addProperty("requestAction",        requestAction.toString());
        jsonObject.addProperty("sentTime",             sentTime);
        return gson.toJson(jsonObject);

    }

    /**
     * @return an uuid representing the request id.
     */
    public final UUID getRequestId() {
        return requestId;
    }

    /**
     * @return a string representing the sender public key.
     */
    public final String getSenderPublicKey() {
        return senderPublicKey;
    }

    /**
     * @return an element of PlatformComponentType enum representing the actor type of the sender.
     */
    public final PlatformComponentType getSenderActorType() {
        return senderActorType;
    }

    /**
     * @return a string representing the alias of the crypto broker.
     */
    public final String getSenderAlias() {
        return senderAlias;
    }

    /**
     * @return an array of bytes with the image exposed by the Crypto Broker.
     */
    public final byte[] getSenderImage() {
        return senderImage;
    }

    /**
     * @return a string representing the destination public key.
     */
    public final String getDestinationPublicKey() {
        return destinationPublicKey;
    }

    public PlatformComponentType getDestinationActorType() {
        return destinationActorType;
    }

    /**
     * @return an element of ConnectionRequestAction enum indicating the action that must to be done.
     */
    public final ConnectionRequestAction getRequestAction() {
        return requestAction;
    }

    /**
     * @return the time when the action was performed.
     */
    public final long getSentTime() {
        return sentTime;
    }

    @Override
    public String toString() {
        return "RequestMessage{" +
                "requestId=" + requestId +
                ", senderPublicKey='" + senderPublicKey + '\'' +
                ", senderActorType=" + senderActorType +
                ", senderAlias='" + senderAlias + '\'' +
                ", senderImage=" + (senderImage != null) +
                ", destinationPublicKey='" + destinationPublicKey + '\'' +
                ", requestAction=" + requestAction +
                ", sentTime=" + sentTime +
                '}';
    }
}
