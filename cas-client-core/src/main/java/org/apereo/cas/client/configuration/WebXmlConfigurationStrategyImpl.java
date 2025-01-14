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
package org.apereo.cas.client.configuration;

import org.apereo.cas.client.util.CommonUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;

/**
 * Implementation of the {@link ConfigurationStrategy} that first checks the {@link javax.servlet.FilterConfig} and
 * then checks the {@link javax.servlet.ServletContext}, ultimately falling back to the <code>defaultValue</code>.
 *
 * @author Scott Battaglia
 * @since 3.4.0
 */
public final class WebXmlConfigurationStrategyImpl extends BaseConfigurationStrategy {

    private FilterConfig filterConfig;

    @Override
    public void init(final FilterConfig filterConfig, final Class<? extends Filter> clazz) {
        this.filterConfig = filterConfig;
    }

    @Override
    protected String get(final ConfigurationKey configurationKey) {
        final String value = this.filterConfig.getInitParameter(configurationKey.getName());

        if (CommonUtils.isNotBlank(value)) {
            CommonUtils.assertFalse(ConfigurationKeys.RENEW.equals(configurationKey), "Renew MUST be specified via context parameter or JNDI environment to avoid misconfiguration.");
            logger.info("Property [{}] loaded from FilterConfig.getInitParameter with value [{}]", configurationKey, value);
            return value;
        }

        final String value2 = filterConfig.getServletContext().getInitParameter(configurationKey.getName());

        if (CommonUtils.isNotBlank(value2)) {
            logger.info("Property [{}] loaded from ServletContext.getInitParameter with value [{}]", configurationKey,
                value2);
            return value2;
        }

        return null;
    }
}
