package org.ranapat.examples.githubbrowser.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "user_urls",
        primaryKeys = {"user_id"},
        indices = @Index(value = {"user_id"}),
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = CASCADE
        ))
public class UserUrls implements DataEntity {
    @NotNull
    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo(name = "url")
    public final String url;

    @ColumnInfo(name = "followers_url")
    public final String followersUrl;

    @ColumnInfo(name = "following_url")
    public final String followingUrl;

    @ColumnInfo(name = "starred_url")
    public final String starredUrl;

    @ColumnInfo(name = "subscriptions_url")
    public final String subscriptionsUrl;

    @ColumnInfo(name = "organizations_url")
    public final String organizationsUrl;

    @ColumnInfo(name = "repos_url")
    public final String reposUrl;

    @ColumnInfo(name = "events_url")
    public final String eventsUrl;

    @ColumnInfo(name = "received_events_url")
    public final String receivedEventsUrl;

    public UserUrls(
            final long userId,
            final String url,
            final String followersUrl, final String followingUrl,
            final String starredUrl,
            final String subscriptionsUrl,
            final String organizationsUrl,
            final String reposUrl,
            final String eventsUrl, final String receivedEventsUrl
    ) {
        this.userId = userId;
        this.url = url;
        this.followersUrl = followersUrl;
        this.followingUrl = followingUrl;
        this.starredUrl = starredUrl;
        this.subscriptionsUrl = subscriptionsUrl;
        this.organizationsUrl = organizationsUrl;
        this.reposUrl = reposUrl;
        this.eventsUrl = eventsUrl;
        this.receivedEventsUrl = receivedEventsUrl;
    }

}
