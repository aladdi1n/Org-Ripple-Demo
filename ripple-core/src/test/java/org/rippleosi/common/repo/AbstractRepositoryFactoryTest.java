/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.common.repo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

/**
 */
public abstract class AbstractRepositoryFactoryTest<F extends RepositoryFactory<R>, R extends Repository> {

    private static final String SOURCE = "source ";

    private F factory;

    @Mock
    private ApplicationContext mockApplicationContext;

    protected abstract F createRepositoryFactory();
    protected abstract Class<R> getRepositoryClass();

    @Before
    public void setUp() throws Exception {
        factory = createRepositoryFactory();

        ReflectionTestUtils.setField(factory, "applicationContext", mockApplicationContext);
    }

    @Test
    public void shouldReturnDefaultImplementationWhenNoneAreConfigured() {

        initContext(0);

        R repository = factory.select(null);
        assertEquals("not configured", repository.getSource());
    }

    @Test
    public void shouldReturnSameImplementationRegardlessOfSourceWhenOnlyOneIsConfigured() {

        initContext(1);

        R nullSource = factory.select(null);
        R source1 = factory.select(SOURCE + 1);
        R source2 = factory.select(SOURCE + 2);

        String expected = SOURCE + 1;
        assertEquals(expected, nullSource.getSource());
        assertEquals(expected, source1.getSource());
        assertEquals(expected, source2.getSource());
    }

    @Test
    public void shouldReturnCorrectImplementationWhenSourceIsSpecified() {

        initContext(4);

        R source = factory.select(SOURCE + 2);

        assertEquals(SOURCE + 2, source.getSource());
    }

    @Test
    public void shouldReturnLowestPriorityImplementationWhenInvalidSourceIsSpecified() {

        initContext(2);

        R source = factory.select("invalid");

        assertEquals(SOURCE + 1, source.getSource());
    }

    private void initContext(int count) {

        Class<R> cls = getRepositoryClass();

        Map<String, R> repositoryMap = configureRepository(cls, count);
        when(mockApplicationContext.getBeansOfType(cls)).thenReturn(repositoryMap);

        ReflectionTestUtils.invokeMethod(factory, "postConstruct");
    }

    private Map<String, R> configureRepository(Class<R> cls, int count) {

        Map<String, R> repositoryMap = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            R repository = Mockito.mock(cls);
            when(repository.getSource()).thenReturn(SOURCE + i);
            when(repository.getPriority()).thenReturn(i);

            repositoryMap.put(SOURCE + i, repository);
        }

        return repositoryMap;
    }
}
