package org.ranapat.examples.githubbrowser.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "application_state")
public class ApplicationState implements DataEntity {
    @PrimaryKey
    public final long id;

    @ColumnInfo(name = "current_organization")
    public String currentOrganization;

    @ColumnInfo(name = "sort_by")
    public String sortBy;

    @ColumnInfo(name = "sort_direction")
    public String sortDirection;

    @ColumnInfo(name = "limit")
    public String limit;

    @Ignore
    public ApplicationState(
            final String currentOrganization,
            final String sortBy,
            final String sortDirection,
            final String limit
    ) {
        this(
                1,
                currentOrganization,
                sortBy,
                sortDirection,
                limit
        );
    }

    public ApplicationState(
            final long id,
            final String currentOrganization,
            final String sortBy,
            final String sortDirection,
            final String limit
    ) {
        this.id = id;

        this.currentOrganization = currentOrganization;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
        this.limit = limit;
    }

    public void reset() {
        currentOrganization = null;
        sortBy = null;
        sortDirection = null;
        limit = null;
    }

    public boolean isSet() {
        return currentOrganization != null && !currentOrganization.isEmpty();
    }
}
