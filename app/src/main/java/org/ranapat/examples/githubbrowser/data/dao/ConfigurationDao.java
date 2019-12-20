package org.ranapat.examples.githubbrowser.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.ranapat.examples.githubbrowser.data.entity.Configuration;

@Dao
public interface ConfigurationDao {
    @Query("UPDATE configuration SET updated_at=0")
    void resetUpdatedAt();

    @Query("SELECT * FROM configuration LIMIT 1")
    Configuration fetch();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void store(final Configuration configuration);
}
