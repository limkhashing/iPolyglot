package com.kslimweb.ipolyglot.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

// Singleton definition
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope { }