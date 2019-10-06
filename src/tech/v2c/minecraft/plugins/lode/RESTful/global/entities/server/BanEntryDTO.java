package tech.v2c.minecraft.plugins.lode.RESTful.global.entities.server;

import java.util.Date;

public class BanEntryDTO {
    private String name;
    private String reason;
    private Date creationDate;
    private Date expirationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expires) {
        this.expirationDate = expires;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
