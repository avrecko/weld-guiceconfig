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
 * Date: 14.7.2010
 * Time: 16:13:12
 * To change this template use File | Settings | File Templates.
 */
public class DefaultBinding implements Binding {

    protected final Key key;
    protected final Class<? extends Annotation> scoping;

    public DefaultBinding(Class<?> clazz) {
        key = Key.newKey(clazz);
        this.scoping = null;
    }

    public DefaultBinding(Class<?> clazz, Class<? extends Annotation> scoping) {
        key = Key.newKey(clazz);
        this.scoping = scoping;
    }

    public DefaultBinding(Key key, Class<? extends Annotation> scoping) {
        this.key = key;
        this.scoping = scoping;
    }

    public DefaultBinding(Key key) {
        this.key = key;
        this.scoping = null;
    }


    public Key getKey() {
        return key;
    }

    public Binding withAnnotation(Class<? extends Annotation> qualifier) {
        return new DefaultBinding(Key.newKey(key.clazz, qualifier), scoping);
    }

    public Binding withScoping(Class<? extends Annotation> scoping) {
        return new DefaultBinding(key, scoping);
    }

    public Class<? extends Annotation> getScoping() {
        return scoping;
    }

    @Override
    public String toString() {
        return "DefaultBinding{" +
                "key=" + key +
                ", scoping=" + scoping +
                '}';
    }
}
