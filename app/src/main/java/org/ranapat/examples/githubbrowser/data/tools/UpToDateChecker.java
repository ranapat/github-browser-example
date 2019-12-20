package org.ranapat.examples.githubbrowser.data.tools;

import org.ranapat.examples.githubbrowser.Settings;
import org.ranapat.examples.githubbrowser.data.entity.Configuration;
import org.ranapat.examples.githubbrowser.data.entity.ExpirableEntity;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class UpToDateChecker {
    public static int keepConfigurationFor;
    public static int keepEntitiesFor;

    static {
        keepConfigurationFor = Settings.keepConfigurationFor;
        keepEntitiesFor = Settings.keepEntitiesFor;
        keepEntitiesFor = Settings.keepEntitiesFor;
    }

    private UpToDateChecker() {}

    public static void setValues(
            final int _keepConfigurationFor,
            final int _keepEntitiesFor
    ) {
        keepConfigurationFor = _keepConfigurationFor;
        keepEntitiesFor = _keepEntitiesFor;
    }

    public static void resetValues() {
        keepConfigurationFor = Settings.keepConfigurationFor;
        keepEntitiesFor = Settings.keepEntitiesFor;
    }

    public static boolean isUpToDate(final ExpirableEntity entity) {
        if (entity instanceof Configuration) {
            return checkConfiguration(entity);
        } else {
            return checkEntities(entity);
        }
    }

    private static boolean checkConfiguration(final ExpirableEntity entity) {
        if (keepConfigurationFor < 0) {
            return true;
        }

        final Date now = new Date();
        long diff = now.getTime() - entity.getUpdatedAt().getTime();

        return keepConfigurationFor > TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }

    private static boolean checkEntities(final ExpirableEntity entity) {
        if (keepEntitiesFor < 0) {
            return true;
        }

        final Date now = new Date();
        long diff = now.getTime() - entity.getUpdatedAt().getTime();

        return keepEntitiesFor > TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }
}
