package org.ranapat.examples.githubbrowser.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.ranapat.examples.githubbrowser.data.entity.ApplicationState;

@Dao
public interface ApplicationStateDao {
    @Query("SELECT * FROM application_state LIMIT 1")
    ApplicationState fetch();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long store(final ApplicationState applicationState);
}
