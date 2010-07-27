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

package weld.guiceconfig.attic.internal;

import weld.guiceconfig.attic.Binding;
import weld.guiceconfig.attic.Key;

import java.lang.annotation.Annotation;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 13.7.2010
 * Time: 17:33:02
 * To change this template use File | Settings | File Templates.
 */
public class LinkedKeyBinding extends DefaultBinding {
    private final Key target;


    public LinkedKeyBinding(Key key, Class<?> target) {
        super(key);
        this.target = Key.newKey(target);
    }

    public LinkedKeyBinding(Key base, Key target, Class<? extends Annotation> scoping) {
        super(base, scoping);
        this.target = target;
    }

    public Key getTarget() {
        return target;
    }

    @Override
    public Binding withScoping(Class<? extends Annotation> scoping) {
        return new LinkedKeyBinding(getKey(), getTarget(), scoping);
    }

    @Override
    public Binding withAnnotation(Class<? extends Annotation> qualifier) {
        return new LinkedKeyBinding(Key.newKey(key.clazz, qualifier), target, scoping);
    }
}
