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

package weld.guiceconfig.maybeBug;

import com.google.inject.Module;
import weld.guiceconfig.AbstractGuiceConfigTest;

import java.util.Arrays;

/**
 * Exposes what I believe is a bug in Weld.
 *
 * @author Alen Vrecko
 */
public class MaybeBugTest extends AbstractGuiceConfigTest {
    @Override
    protected Iterable<? extends Module> getModules() {
        return Arrays.asList();
    }


    /**
     * Should not fail. Having {@link weld.guiceconfig.maybeBug.SomeUser}  implement
     * {@link weld.guiceconfig.maybeBug.SomeInterface} fails the test even if CDI bindings make clear which
     * implementation to use.
     */
    public void testPossibleBug() {
    }
}
