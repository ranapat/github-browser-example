package org.ranapat.examples.githubbrowser.data.entity;

import java.util.Date;

public interface ExpirableEntity extends DataEntity {
    Date getUpdatedAt();
    boolean isUpToDate();
}
