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

package weld.guiceconfig.attic;


import weld.guiceconfig.internal.HardDefault;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fundamental unit of configuration. Binds a <code>Class</code> and an <code>Annotation</code> together.
 *
 * @author Alen Vrecko
 */
public class Key {

    public final Class<?> clazz;
    public final Class<? extends Annotation> qualifier;
    private static final AtomicInteger count = new AtomicInteger();

    public Key(Class<?> clazz, Class<? extends Annotation> qualifier) {
        this.clazz = clazz;
        this.qualifier = qualifier;
    }

    public Key(Class<?> target) {
        this.clazz = target;
        this.qualifier = HardDefault.class;
    }

    public static <T, A extends Annotation> Key newKey(Class<T> clazz, Class<A> qualifier) {
        return new Key(clazz, qualifier);
    }


    @Override
    public String toString() {
        return "Key{" +
                "clazz=" + clazz +
                ", qualifier=" + qualifier +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        if (qualifier != null ? !qualifier.equals(key.qualifier) : key.qualifier != null) return false;
        if (clazz != null ? !clazz.equals(key.clazz) : key.clazz != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clazz != null ? clazz.hashCode() : 0;
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        return result;
    }

    public static Key newKey(Class<?> target) {
        return new Key(target);
    }
}
