package org.ranapat.examples.githubbrowser.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.ranapat.examples.githubbrowser.data.tools.UpToDateChecker;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "organizations")
public class Organization implements ExpirableEntity, Serializable {
    @PrimaryKey
    public final long id;

    @ColumnInfo(name = "login")
    public final String login;

    @ColumnInfo(name = "updated_at")
    public final Date updatedAt;

    public Organization(
            final long id,
            final String login,
            final Date updatedAt
    ) {
        this.id = id;
        this.login = login;
        this.updatedAt = updatedAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean isUpToDate() {
        return UpToDateChecker.isUpToDate(this);
    }

}
