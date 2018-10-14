package com.silversnowsoftware.vc.di;

/**
 * Created by burak on 10/14/2018.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by janisharali on 27/01/17.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}