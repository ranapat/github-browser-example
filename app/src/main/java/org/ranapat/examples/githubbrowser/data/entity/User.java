package org.ranapat.examples.githubbrowser.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.ranapat.examples.githubbrowser.data.tools.UpToDateChecker;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "users",
        indices = @Index(value = {"organization_id"}),
        foreignKeys = @ForeignKey(
                entity = Organization.class,
                parentColumns = "id",
                childColumns = "organization_id",
                onDelete = CASCADE
        ))
public class User implements ExpirableEntity {
    @PrimaryKey
    public final long id;

    @NotNull
    @ColumnInfo(name = "organization_id")
    public final long organizationId;

    @ColumnInfo(name = "login")
    public final String login;

    @ColumnInfo(name = "avatar_url")
    public final String avatarUrl;

    @ColumnInfo(name = "type")
    public final String type;

    @ColumnInfo(name = "site_admin")
    public final boolean siteAdmin;

    @ColumnInfo(name = "updated_at")
    public final Date updatedAt;

    @Ignore
    public UserDetails details;

    @Ignore
    public UserUrls urls;

    public User(
            final long id,
            final long organizationId,
            final String login,
            final String avatarUrl,
            final String type,
            final boolean siteAdmin,
            final Date updatedAt
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.type = type;
        this.siteAdmin = siteAdmin;
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
