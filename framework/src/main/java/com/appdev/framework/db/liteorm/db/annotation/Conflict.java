package com.appdev.framework.db.liteorm.db.annotation;




import com.appdev.framework.db.liteorm.db.enums.Strategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 冲突策略
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Conflict {
    Strategy value();
}
