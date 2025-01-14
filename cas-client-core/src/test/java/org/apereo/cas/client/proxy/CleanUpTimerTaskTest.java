/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.cas.client.proxy;

import org.apereo.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

import junit.framework.TestCase;

import java.util.TimerTask;

/**
 * Unit test for the {@link CleanUpTimerTask}
 *
 * @author Brad Cupit (brad [at] lsu {dot} edu)
 */
public class CleanUpTimerTaskTest extends TestCase {

    public void testRun() throws Exception {
        final ProxyGrantingTicketStorageTestImpl storage = new ProxyGrantingTicketStorageTestImpl();
        new Cas20ProxyReceivingTicketValidationFilter().setProxyGrantingTicketStorage(storage);

        final TimerTask timerTask = new CleanUpTimerTask(storage);

        timerTask.run();
        assertTrue(storage.cleanUpWasCalled());
    }

    /**
     * implementation of the storage interface used only for testing
     *
     * @author Brad Cupit (brad [at] lsu {dot} edu)
     */
    private static final class ProxyGrantingTicketStorageTestImpl implements ProxyGrantingTicketStorage {
        private boolean cleanUpCalled = false;

        public boolean cleanUpWasCalled() {
            return cleanUpCalled;
        }

        @Override
        public void save(final String proxyGrantingTicketIou, final String proxyGrantingTicket) {
        }

        @Override
        public String retrieve(final String proxyGrantingTicketIou) {
            return null;
        }

        @Override
        public void cleanUp() {
            cleanUpCalled = true;
        }
    }
}
