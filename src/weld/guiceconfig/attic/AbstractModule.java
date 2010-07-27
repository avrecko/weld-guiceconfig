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

import weld.guiceconfig.attic.binder.AnnotatedBindingBuilder;


/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 13.7.2010
 * Time: 15:10:47
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractModule implements Module {

    private Binder binder;


    public void configure(Binder binder) {
        this.binder = binder;
        configure();
    }

    public abstract void configure();

    protected <E> AnnotatedBindingBuilder<E> bind(Class<E> clazz) {
        return binder.bind(clazz);
    }

    protected void install(Module module) {
        binder.install(module);
    }
}
