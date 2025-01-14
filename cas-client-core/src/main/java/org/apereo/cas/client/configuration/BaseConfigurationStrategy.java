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
import org.apereo.cas.client.util.ReflectUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class to provide most of the boiler-plate code (i.e. checking for proper values, returning defaults, etc.
 *
 * @author Scott Battaglia
 * @since 3.4.0
 */
public abstract class BaseConfigurationStrategy implements ConfigurationStrategy {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final boolean getBoolean(final ConfigurationKey<Boolean> configurationKey) {
        return getValue(configurationKey, new Parser<Boolean>() {
            @Override
            public Boolean parse(final String value) {
                return CommonUtils.toBoolean(value);
            }
        });
    }

    @Override
    public final String getString(final ConfigurationKey<String> configurationKey) {
        return getValue(configurationKey, new Parser<String>() {
            @Override
            public String parse(final String value) {
                return value;
            }
        });
    }

    @Override
    public final long getLong(final ConfigurationKey<Long> configurationKey) {
        return getValue(configurationKey, new Parser<Long>() {
            @Override
            public Long parse(final String value) {
                return CommonUtils.toLong(value, configurationKey.getDefaultValue());
            }
        });
    }

    @Override
    public final int getInt(final ConfigurationKey<Integer> configurationKey) {
        return getValue(configurationKey, new Parser<Integer>() {
            @Override
            public Integer parse(final String value) {
                return CommonUtils.toInt(value, configurationKey.getDefaultValue());
            }
        });
    }

    @Override
    public <T> Class<? extends T> getClass(final ConfigurationKey<Class<? extends T>> configurationKey) {
        return getValue(configurationKey, new Parser<Class<? extends T>>() {
            @Override
            public Class<? extends T> parse(final String value) {
                try {
                    return ReflectUtils.loadClass(value);
                } catch (final IllegalArgumentException e) {
                    return configurationKey.getDefaultValue();
                }
            }
        });
    }

    /**
     * Retrieve the String value for this key.  Returns null if there is no value.
     *
     * @param configurationKey the key to retrieve.  MUST NOT BE NULL.
     * @return the String if its found, null otherwise.
     */
    protected abstract String get(ConfigurationKey configurationKey);

    private interface Parser<T> {

        T parse(String value);
    }

    private <T> T getValue(final ConfigurationKey<T> configurationKey, final Parser<T> parser) {
        final String value = getWithCheck(configurationKey);

        if (CommonUtils.isBlank(value)) {
            logger.trace("No value found for property {}, returning default {}", configurationKey.getName(), configurationKey.getDefaultValue());
            return configurationKey.getDefaultValue();
        } else {
            logger.trace("Loaded property {} with value {}", configurationKey.getName(), configurationKey.getDefaultValue());
        }

        return parser.parse(value);
    }

    private String getWithCheck(final ConfigurationKey configurationKey) {
        CommonUtils.assertNotNull(configurationKey, "configurationKey cannot be null");

        return get(configurationKey);
    }
}
