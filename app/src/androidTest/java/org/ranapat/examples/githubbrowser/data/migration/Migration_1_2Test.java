package org.ranapat.examples.githubbrowser.data.migration;

import android.database.Cursor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ranapat.examples.githubbrowser.data.ApplicationDatabase;

import java.io.IOException;

import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class Migration_1_2Test {
    private static final String TEST_DB = "testApplication.db";

    @Rule
    public final MigrationTestHelper helper;

    public Migration_1_2Test() {
        helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                ApplicationDatabase.class.getCanonicalName(),
                new FrameworkSQLiteOpenHelperFactory());
    }

    @Test
    public void shallMigrate() throws IOException {
        final SupportSQLiteDatabase db1 = helper.createDatabase(TEST_DB, 1);

        db1.execSQL(
                "INSERT INTO 'configuration'('id', 'organization_members', 'user_info', 'updated_at') VALUES (1, 'organization_members', 'user_info', 1)"
        );

        final Cursor cursor1 = db1.query("select count(id) from 'configuration'");
        assertThat(true, is(equalTo(cursor1.moveToNext())));
        assertThat(1, is(equalTo(cursor1.getInt(0))));

        db1.close();

        final SupportSQLiteDatabase db2 = helper.runMigrationsAndValidate(TEST_DB, 2, true, new Migration_1_2());

        final Cursor cursor2 = db2.query("select count(id) from 'configuration'");
        assertThat(true, is(equalTo(cursor2.moveToNext())));
        assertThat(1, is(equalTo(cursor2.getInt(0))));

        final Cursor cursor3 = db2.query("select updated_at from 'configuration'");
        assertThat(true, is(equalTo(cursor3.moveToNext())));
        assertThat(0, is(equalTo(cursor3.getInt(0))));

        final Cursor cursor4 = db2.query("select default_members_in_organization_per_page from 'configuration'");
        assertThat(true, is(equalTo(cursor4.moveToNext())));
        assertThat(15, is(equalTo(cursor4.getInt(0))));

        db2.close();
    }
}