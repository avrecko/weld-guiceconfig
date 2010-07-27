/*
 * Copyright (C) 2010 Alen Vrecko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package weld.guiceconfig;

import com.google.common.collect.ImmutableSet;
import junit.framework.Test;
import junit.framework.TestSuite;
import weld.guiceconfig.aop.InterceptorChainTest;
import weld.guiceconfig.basics.BasicTest;
import weld.guiceconfig.maybeBug.MaybeBugTest;

import java.util.Enumeration;
import java.util.Set;

/**
 * Master test suite.
 *
 * @author Alen Vrecko
 */
public class AllTests {

    private static final Set<String> SUPPRESSED_TEST_NAMES = ImmutableSet.of(
            MaybeBugTest.class.getName()
    );

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(InterceptorChainTest.class);
        suite.addTestSuite(BasicTest.class);
        suite.addTestSuite(MaybeBugTest.class);

        return removeSuppressedTests(suite, SUPPRESSED_TEST_NAMES);
    }

    public static TestSuite removeSuppressedTests(TestSuite suite, Set<String> suppressedTestNames) {
        TestSuite result = new TestSuite(suite.getName());

        for (Enumeration e = suite.tests(); e.hasMoreElements();) {
            Test test = (Test) e.nextElement();

            if (suppressedTestNames.contains(test.toString())) {
                continue;
            }

            if (test instanceof TestSuite) {
                result.addTest(removeSuppressedTests((TestSuite) test, suppressedTestNames));
            } else {
                result.addTest(test);
            }
        }

        return result;
    }


}
