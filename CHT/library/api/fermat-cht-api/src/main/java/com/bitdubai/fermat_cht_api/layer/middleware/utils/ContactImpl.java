package com.bitdubai.fermat_cht_api.layer.middleware.utils;

import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_cht_api.all_definition.enums.ContactStatus;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.Contact;

import java.util.UUID;

/**
 * Created by franklin on 08/01/16.
 */
public class ContactImpl implements Contact {
    //Documentar
    private UUID contactId;
    private String remoteName;
    private String alias;
    private PlatformComponentType remoteActorType;
    private String remoteActorPublicKey;
    private long creationDate;
    private byte[] image;
    private ContactStatus contactStatus;

    public ContactImpl(){}

    public ContactImpl(UUID contactId,
                       String remoteName,
                       String alias,
                       PlatformComponentType remoteActorType,
                       String remoteActorPublicKey,
                       long creationDate,
                       byte[] image,
                       ContactStatus contactStatus)
    {
        this.contactId            = contactId;
        this.remoteName           = remoteName;
        this.alias                = alias;
        this.remoteActorType      = remoteActorType;
        this.remoteActorPublicKey = remoteActorPublicKey;
        this.creationDate         = creationDate;
        this.image                = image;
        this.contactStatus        = contactStatus;
    }

    @Override
    public UUID getContactId() {
        return this.contactId;
    }

    @Override
    public void setContactId(UUID contactId) {
        this.contactId = contactId;
    }

    @Override
    public String getRemoteName() {
        return this.remoteName;
    }

    @Override
    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public PlatformComponentType getRemoteActorType() {
        return this.remoteActorType;
    }

    @Override
    public void setRemoteActorType(PlatformComponentType remoteActorType) {
        this.remoteActorType = remoteActorType;
    }

    @Override
    public String getRemoteActorPublicKey() {
        return this.remoteActorPublicKey;
    }

    @Override
    public void setRemoteActorPublicKey(String remoteActorPublicKey) {
        this.remoteActorPublicKey = remoteActorPublicKey;
    }

    @Override
    public long getCreationDate() {
        return this.creationDate;
    }

    @Override
    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public byte[] getProfileImage() {
        return this.image;
    }

    @Override
    public void setProfileImage(byte[] profileImage) {
        this.image = profileImage;
    }

    @Override
    public ContactStatus getContactStatus() {
        return contactStatus;
    }

    @Override
    public void setContactStatus(ContactStatus contactStatus) {
        this.contactStatus = contactStatus;
    }

    @Override
    public String toString() {
        return "ContactImpl{" +
                "contactId=" + contactId +
                ", remoteName='" + remoteName + '\'' +
                ", alias='" + alias + '\'' +
                ", remoteActorType=" + remoteActorType +
                ", remoteActorPublicKey='" + remoteActorPublicKey + '\'' +
                ", creationDate=" + creationDate + '\'' +
                ", contactStatus=" + contactStatus +
                '}';
    }
}
