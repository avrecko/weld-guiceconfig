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

import weld.guiceconfig.attic.Binder;
import weld.guiceconfig.attic.Binding;
import weld.guiceconfig.attic.Module;
import weld.guiceconfig.attic.binder.AnnotatedBindingBuilder;

import java.util.Stack;

import static java.util.Arrays.asList;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 13.7.2010
 * Time: 17:31:25
 * To change this template use File | Settings | File Templates.
 */
public class RecordingBinder implements Binder {

    final Stack<Binding> bindings = new Stack<Binding>();


    public <E> AnnotatedBindingBuilder<E> bind(Class<E> clazz) {
        bindings.push(new DefaultBinding(clazz));
        return new RecordersBindingBuilder<E>(this);
    }

    public void install(Module module) {
        module.configure(this);
    }

    public Iterable<Binding> getBindings() {
        return asList(bindings.toArray(new Binding[bindings.size()]));
    }
}
