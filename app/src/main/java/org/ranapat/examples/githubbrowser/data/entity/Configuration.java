package org.ranapat.examples.githubbrowser.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.ranapat.examples.githubbrowser.data.tools.UpToDateChecker;

import java.util.Date;

@Entity(tableName = "configuration")
public class Configuration implements ExpirableEntity {
    @PrimaryKey
    public final long id;

    @ColumnInfo(name = "organization_members")
    public final String organizationMembers;

    @ColumnInfo(name = "user_info")
    public final String userInfo;

    @ColumnInfo(name = "updated_at")
    public final Date updatedAt;

    @Ignore
    public Configuration(
            final String organizationMembers,
            final String userInfo
    ) {
        this(
                1,
                organizationMembers,
                userInfo,
                new Date()
        );
    }

    public Configuration(
            final long id,
            final String organizationMembers,
            final String userInfo,
            final Date updatedAt
    ) {
        this.id = id;

        this.organizationMembers = organizationMembers;
        this.userInfo = userInfo;

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
