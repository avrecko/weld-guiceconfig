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
import weld.guiceconfig.attic.binder.AnnotatedBindingBuilder;
import weld.guiceconfig.attic.binder.LinkedBindingBuilder;
import weld.guiceconfig.attic.binder.ScopedBindingBuilder;

import java.lang.annotation.Annotation;

/**
 * Part of Fluent API configuration option.
 *
 * @author Alen Vrecko
 */
public class RecordersBindingBuilder<E> implements AnnotatedBindingBuilder<E> {

    private final RecordingBinder recorder;

    public RecordersBindingBuilder(RecordingBinder recorder) {
        this.recorder = recorder;
    }

    public ScopedBindingBuilder to(Class moreConcreteType) {
        Binding poped = recorder.bindings.pop();
        recorder.bindings.push(new LinkedKeyBinding(poped.getKey(), moreConcreteType));
        return this;
    }

    public void in(Class<? extends Annotation> annotationType) {
        Binding poped = recorder.bindings.pop();
        recorder.bindings.push(poped.withScoping(annotationType));
    }

    public LinkedBindingBuilder<E> annotatedWith(Class<? extends Annotation> qualifier) {
        Binding poped = recorder.bindings.pop();
        recorder.bindings.push(poped.withAnnotation(qualifier));
        return this;
    }
}
