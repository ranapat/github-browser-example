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

    @ColumnInfo(name = "default_members_in_organization_per_page")
    public final int defaultMembersInOrganizationPerPage;

    @ColumnInfo(name = "organization")
    public final String organization;

    @ColumnInfo(name = "organization_members")
    public final String organizationMembers;

    @ColumnInfo(name = "user_info")
    public final String userInfo;

    @ColumnInfo(name = "updated_at")
    public final Date updatedAt;

    @Ignore
    public Configuration(
            final int defaultMembersInOrganizationPerPage,
            final String organization,
            final String organizationMembers,
            final String userInfo
    ) {
        this(
                1,
                defaultMembersInOrganizationPerPage,
                organization,
                organizationMembers,
                userInfo,
                new Date()
        );
    }

    public Configuration(
            final long id,
            final int defaultMembersInOrganizationPerPage,
            final String organization,
            final String organizationMembers,
            final String userInfo,
            final Date updatedAt
    ) {
        this.id = id;

        this.defaultMembersInOrganizationPerPage = defaultMembersInOrganizationPerPage;
        this.organization = organization;
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
