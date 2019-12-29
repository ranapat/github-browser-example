package org.ranapat.examples.githubbrowser.data.migration;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration_1_2 extends Migration {
    public Migration_1_2() {
        super(1, 2);
    }

    @Override
    public void migrate(final @NonNull SupportSQLiteDatabase database) {

        database.execSQL(
                "ALTER TABLE 'configuration' ADD COLUMN 'default_members_in_organization_per_page' INTEGER NOT NULL DEFAULT 0"
        );
        database.execSQL(
                "UPDATE 'configuration' set 'default_members_in_organization_per_page' = 15, 'updated_at' = 0"
        );
    }
}