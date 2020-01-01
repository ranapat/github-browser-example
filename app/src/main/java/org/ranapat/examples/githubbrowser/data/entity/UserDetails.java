package org.ranapat.examples.githubbrowser.data.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "user_details",
        primaryKeys = {"user_id"},
        indices = @Index(value = {"user_id"}),
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = CASCADE
        ))
public class UserDetails implements DataEntity {
    @NotNull
    @ColumnInfo(name = "user_id")
    public final long userId;

    @Nullable
    @ColumnInfo(name = "name")
    public final String name;

    @Nullable
    @ColumnInfo(name = "company")
    public final String company;

    @Nullable
    @ColumnInfo(name = "blog")
    public final String blog;

    @Nullable
    @ColumnInfo(name = "location")
    public final String location;

    @Nullable
    @ColumnInfo(name = "email")
    public final String email;

    @Nullable
    @ColumnInfo(name = "bio")
    public final String bio;

    @Nullable
    @ColumnInfo(name = "hireable")
    public final boolean hireable;

    @Nullable
    @ColumnInfo(name = "public_repos")
    public final int publicRepos;

    @Nullable
    @ColumnInfo(name = "followers")
    public final int followers;

    @Nullable
    @ColumnInfo(name = "public_gists")
    public final int publicGists;

    @Nullable
    @ColumnInfo(name = "remote_created_at")
    public final Date remoteCreatedAt;

    @Nullable
    @ColumnInfo(name = "remote_updated_at")
    public final Date remoteUpdatedAt;

    public UserDetails(
            final long userId,
            final String name,
            final String company,
            final String blog,
            final String location,
            final String email,
            final String bio,
            final boolean hireable,
            final int publicRepos,
            final int followers,
            final int publicGists,
            final Date remoteCreatedAt,
            final Date remoteUpdatedAt
    ) {
        this.userId = userId;
        this.name = name;
        this.company = company;
        this.blog = blog;
        this.location = location;
        this.email = email;
        this.bio = bio;
        this.hireable = hireable;
        this.publicRepos = publicRepos;
        this.followers = followers;
        this.publicGists = publicGists;
        this.remoteCreatedAt = remoteCreatedAt;
        this.remoteUpdatedAt = remoteUpdatedAt;
    }
}
