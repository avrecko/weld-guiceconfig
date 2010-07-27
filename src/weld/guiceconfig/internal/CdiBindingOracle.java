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

package weld.guiceconfig.internal;

import com.google.common.base.Objects;
import com.google.common.collect.*;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.spi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Given a set of Guice modules the oracle provides advice for {@link weld.guiceconfig.internal.Phase} to do its job.
 *
 * @author Alen Vrecko
 */
public class CdiBindingOracle {

    private static final Logger log = LoggerFactory.getLogger(CdiBindingOracle.class);
    private static final TargetKeyExtractingVisitor targetExtractor = new TargetKeyExtractingVisitor();
    private static final ScopeExtractingVisitor scopeExtractor = new ScopeExtractingVisitor();


    public final ImmutableMultimap<Class, Class<? extends Annotation>> annotations;
    public final ImmutableMap<Class, Class<? extends Annotation>> scopes;
    public final ImmutableList<InterceptorBinding> interceptors;

    CdiBindingOracle(ImmutableMultimap<Class, Class<? extends Annotation>> annotations, ImmutableMap<Class, Class<? extends Annotation>> scopes, ImmutableList<InterceptorBinding> interceptors) {
        this.annotations = annotations;
        this.scopes = scopes;
        this.interceptors = interceptors;
    }

    public static CdiBindingOracle process(List<Element> elementList) {

        final HashMultimap<Class, Class<? extends Annotation>> annotations = HashMultimap.create();
        final HashMap<Class, Class<? extends Annotation>> scopes = Maps.newHashMap();
        final HashSet<InterceptorBinding> interceptors = Sets.newHashSet();

        for (Element element : elementList) {
            element.acceptVisitor(new ElementVisitor<Void>() {

                @Override
                public <T> Void visit(Binding<T> binding) {
                    Key<T> key = binding.getKey();
                    Key targetKey = binding.acceptTargetVisitor(targetExtractor);
                    Class<? extends Annotation> scope = binding.acceptScopingVisitor(scopeExtractor);
                    processBinding(key, targetKey, scope, annotations, scopes);
                    return null;
                }

                @Override
                public Void visit(InterceptorBinding interceptorBinding) {
                    interceptors.add(interceptorBinding);
                    return null;
                }

                @Override
                public Void visit(ScopeBinding scopeBinding) {
                    log.warn("bindScope(...); not supported.");
                    return null;
                }

                @Override
                public Void visit(TypeConverterBinding typeConverterBinding) {
                    log.warn("convertToTypes(...); not supported.");
                    return null;
                }

                @Override
                public Void visit(InjectionRequest injectionRequest) {
                    log.warn("requestInjection(...); not supported.");
                    return null;
                }

                @Override
                public Void visit(StaticInjectionRequest staticInjectionRequest) {
                    log.warn("requestStaticInjection(...); not supported.");
                    return null;
                }

                @Override
                public <T> Void visit(ProviderLookup<T> tProviderLookup) {
                    log.warn("getProvider(...); not supported.");
                    return null;
                }

                @Override
                public <T> Void visit(MembersInjectorLookup<T> tMembersInjectorLookup) {
                    log.warn("getMembersInjector(...); not supported.");
                    return null;
                }

                @Override
                public Void visit(Message message) {
                    log.warn("addError(...); not supported.");
                    return null;
                }

                @Override
                public Void visit(PrivateElements privateElements) {
                    log.warn("expose(...); not supported.");
                    return null;
                }

                @Override
                public Void visit(TypeListenerBinding typeListenerBinding) {
                    log.warn("bindListener(...); not supported.");
                    return null;
                }
            });


        }

        return new CdiBindingOracle(ImmutableMultimap.copyOf(annotations), ImmutableMap.copyOf(scopes), ImmutableList.copyOf(interceptors));
    }

    private static void processBinding(Key key, Key targetKey, Class<? extends Annotation> scope, HashMultimap<Class, Class<? extends Annotation>> annotations, HashMap<Class, Class<? extends Annotation>> scopes) {
        // toInstance, toProvider is not supported
        if (targetKey == null) {
            log.warn("Binding for " + key + "contains unsupported target.");
            return;
        }

        /* here we can only map the scope as with the limitation of CDI we cannot differentiate 1 same concrete implementation with different
                  qualifier as qualifiers are put on the implementation itself.

                   e.g. in Guice this works
                   bind(Foo.class).annotatedWIth(SessionScoped.class).to(FooImpl.class).in(SessionScoped.class);
                   bind(Foo.class).annotatedWIth(RequestScoped.class).to(FooImpl.class).in(RequestScoped.class);

                   You cannot express the same thing with CDI as @SessionScoped and  @RequestScoped goes on FooImpl  */


        // in any case put the scoping annotation on the key's type
        scopes.put(key.getTypeLiteral().getRawType(), scope);
        annotations.put(key.getTypeLiteral().getRawType(), Objects.firstNonNull(key.getAnnotationType(), HardDefault.class));
        // if is a targeted binding (differentiated using Untargeted sentinel) the scope the target key's class aswell
        if (targetKey.getTypeLiteral().getRawType() != Untargeted.class) {
            scopes.put(targetKey.getTypeLiteral().getRawType(), scope);
            annotations.put(targetKey.getTypeLiteral().getRawType(), Objects.firstNonNull(key.getAnnotationType(), HardDefault.class));
        }

    }


    public static class TargetKeyExtractingVisitor implements BindingTargetVisitor<Object, Key> {

        @Override
        public Key visit(InstanceBinding<? extends Object> instanceBinding) {
            log.warn("bind(Foo.class).toInstance(...) is not supported.");
            return null;
        }

        @Override
        public Key visit(ProviderInstanceBinding<? extends Object> providerInstanceBinding) {
            log.warn("bind(Foo.class).toProvider(...) is not supported.");
            return null;
        }

        @Override
        public Key visit(ProviderKeyBinding<? extends Object> providerKeyBinding) {
            log.warn("bind(Foo.class).toProvider(...) is not supported.");
            return null;
        }

        @Override
        public Key visit(LinkedKeyBinding<? extends Object> linkedKeyBinding) {
            // linked to a key
            return linkedKeyBinding.getLinkedKey();
        }

        @Override
        public Key visit(ExposedBinding<? extends Object> exposedBinding) {
            log.warn("expose(Foo.class) is not supported or any Private Modules functionality for that matter.");
            return null;
        }

        @Override
        public Key visit(UntargettedBinding<? extends Object> untargettedBinding) {
            // not linked binding
            return Key.get(Untargeted.class);
        }

        @Override
        public Key visit(ConstructorBinding<? extends Object> constructorBinding) {
            log.warn("bind(Foo.class).toConstructor() is not supported.");
            return null;
        }

        @Override
        public Key visit(ConvertedConstantBinding<? extends Object> convertedConstantBinding) {
            log.warn("bindConstant() is not supported.");
            return null;
        }

        @Override
        public Key visit(ProviderBinding<? extends Object> providerBinding) {
            log.warn("bind(Foo.class).toProvider(...) is not supported.");
            return null;
        }

    }

    public static class ScopeExtractingVisitor implements BindingScopingVisitor<Class<? extends Annotation>> {
        @Override
        public Class<? extends Annotation> visitEagerSingleton() {
            log.warn("bind(Foo.class).asEagerSingleton(); is not guaranteed to be eager. It will be only CDI singleton.");
            return Singleton.class;
        }

        @Override
        public Class<? extends Annotation> visitScope(Scope scope) {
            log.warn("bind(Foo.class).in(Scope scope) is not supported.");
            return null;
        }

        @Override
        public Class<? extends Annotation> visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
            return scopeAnnotation;
        }

        @Override
        public Class<? extends Annotation> visitNoScoping() {
            return Unscoped.class;
        }
    }


}
