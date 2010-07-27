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
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 22.7.2010
 * Time: 13:03:05
 * To change this template use File | Settings | File Templates.
 */
public class MaybeBugTest extends AbstractGuiceConfigTest {
    @Override
    protected Iterable<? extends Module> getModules() {
        return Arrays.asList();
    }


    public void testPossibleBug() {

    }
}
