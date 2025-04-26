package com.demo.app.domain.repository;

import java.time.LocalDateTime;


/**
 * The interface Common repo.
 * Just for Test.
 */
public interface ICommonRepo {
    /**
     * Now local date time.
     *
     * @return the local date time
     */
    LocalDateTime now();

    /**
     * Timezone string.
     *
     * @return the string
     */
    String timezone();
}
