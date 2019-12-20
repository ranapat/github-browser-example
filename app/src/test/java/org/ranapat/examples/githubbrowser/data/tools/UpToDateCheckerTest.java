package org.ranapat.examples.githubbrowser.data.tools;

import org.junit.Test;
import org.ranapat.examples.githubbrowser.Settings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UpToDateCheckerTest {

    @Test
    public void shouldHandleValues() {
        assertThat(UpToDateChecker.keepConfigurationFor, is(equalTo(Settings.keepConfigurationFor)));
        assertThat(UpToDateChecker.keepEntitiesFor, is(equalTo(Settings.keepEntitiesFor)));

        UpToDateChecker.setValues(1, 2);

        assertThat(UpToDateChecker.keepConfigurationFor, is(equalTo(1)));
        assertThat(UpToDateChecker.keepEntitiesFor, is(equalTo(2)));

        UpToDateChecker.resetValues();

        assertThat(UpToDateChecker.keepConfigurationFor, is(equalTo(Settings.keepConfigurationFor)));
        assertThat(UpToDateChecker.keepEntitiesFor, is(equalTo(Settings.keepEntitiesFor)));
    }

}