package com.kslimweb.ipolyglot

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(MainActivityUiTest::class, MainActivityFunctionalityTest::class)
class ActivityTestSuite
