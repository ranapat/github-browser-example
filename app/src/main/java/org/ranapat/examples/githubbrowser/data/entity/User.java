package org.ranapat.examples.githubbrowser.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.ranapat.examples.githubbrowser.data.tools.UpToDateChecker;

import java.util.Date;

@Entity(tableName = "users")
public class User implements ExpirableEntity {
    @PrimaryKey(autoGenerate = true)
    public final long id;

    @ColumnInfo(name = "name")
    public final String name;

    @ColumnInfo(name = "updated_at")
    public final Date updatedAt;

    public User(
            final long id,
            final String name,
            final Date updatedAt
    ) {
        this.id = id;

        this.name = name;

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
