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
/*

 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.

 */

package org.apereo.cas.client.util;

import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Misagh Moayyed
 */
public class URIBuilderTests {

    @Test
    public void allPartsUsed() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .setCustomQuery("hello=world")
            .setFragment("foo");
        assertEquals("http://apache.org/shindig?hello=world#foo", builder.toString());
    }

    @Test
    public void noSchemeUsed() {
        final URIBuilder builder = new URIBuilder()
            .setHost("apache.org")
            .setPath("/shindig")
            .setCustomQuery("hello=world")
            .setFragment("foo");
        assertEquals("//apache.org/shindig?hello=world#foo", builder.toString());
    }

    @Test
    public void noAuthorityUsed() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setPath("/shindig")
            .setCustomQuery("hello=world")
            .setFragment("foo");
        assertEquals("http:/shindig?hello=world#foo", builder.toString());
    }

    @Test
    public void noPathUsed() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setCustomQuery("hello=world")
            .setFragment("foo");
        assertEquals("http://apache.org?hello=world#foo", builder.toString());
    }

    @Test
    public void noQueryUsed() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .setFragment("foo");
        assertEquals("http://apache.org/shindig#foo", builder.toString());
    }

    @Test
    public void noFragmentUsed() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .setCustomQuery("hello=world");
        assertEquals("http://apache.org/shindig?hello=world", builder.toString());
    }

    @Test
    public void hostRelativePaths() {
        final URIBuilder builder = new URIBuilder()
            .setPath("/shindig")
            .setCustomQuery("hello=world")
            .setFragment("foo");
        assertEquals("/shindig?hello=world#foo", builder.toString());
    }

    @Test
    public void relativePaths() {
        final URIBuilder builder = new URIBuilder()
            .setPath("foo")
            .setCustomQuery("hello=world")
            .setFragment("foo");
        assertEquals("foo?hello=world#foo", builder.toString());
    }

    @Test
    public void noPathNoHostNoAuthority() {
        final URIBuilder builder = new URIBuilder()
            .setCustomQuery("hello=world")
            .setFragment("foo");
        assertEquals("?hello=world#foo", builder.toString());
    }

    @Test
    public void justSchemeAndAuthority() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org");
        assertEquals("http://apache.org", builder.toString());
    }

    @Test
    public void justPath() {
        final URIBuilder builder = new URIBuilder()
            .setPath("/shindig");
        assertEquals("/shindig", builder.toString());
    }

    @Test
    public void justAuthorityAndPath() {
        final URIBuilder builder = new URIBuilder()
            .setHost("apache.org")
            .setPath("/shindig");
        assertEquals("//apache.org/shindig", builder.toString());
    }

    @Test
    public void justQuery() {
        final URIBuilder builder = new URIBuilder()
            .setCustomQuery("hello=world");
        assertEquals("?hello=world", builder.toString());
    }

    @Test
    public void justFragment() {
        final URIBuilder builder = new URIBuilder()
            .setFragment("foo");
        assertEquals("#foo", builder.toString());
    }

    @Test
    public void addSingleQueryParameter() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .addParameter("hello", "world")
            .setFragment("foo");
        assertEquals("http://apache.org/shindig?hello=world#foo", builder.toString());
    }

    @Test
    public void addTwoQueryParameters() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .addParameter("hello", "world")
            .addParameter("foo", "bar")
            .setFragment("foo");
        assertEquals("http://apache.org/shindig?hello=world&foo=bar#foo", builder.toString());
    }

    @Test
    public void iterableQueryParameters() {
        final List<URIBuilder.BasicNameValuePair> list = new ArrayList<URIBuilder.BasicNameValuePair>();
        list.add(new URIBuilder.BasicNameValuePair("hello", "world"));
        list.add(new URIBuilder.BasicNameValuePair("hello", "monde"));
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .addParameters(list)
            .setFragment("foo");
        assertEquals("http://apache.org/shindig?hello=world&hello=monde#foo", builder.toString());
    }

    @Test
    public void removeQueryParameter() {
        final URIBuilder uri = new URIBuilder("http://www.example.com/foo?bar=baz&quux=baz");
        uri.removeQuery();
        assertEquals("http://www.example.com/foo", uri.toString());
    }

    @Test
    public void addIdenticalParameters() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .addParameter("hello", "world")
            .addParameter("hello", "goodbye")
            .setFragment("foo");
        assertEquals("http://apache.org/shindig?hello=world&hello=goodbye#foo", builder.toString());
    }

    @Test
    public void queryStringIsUnescaped() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .setCustomQuery("hello+world=world%26bar");
        assertEquals("world&bar", builder.build().getQuery().split("=")[1]);
    }

    @Test
    public void queryParamsAreEscaped() {
        final URIBuilder builder = new URIBuilder(true)
            .setScheme("http")
            .setHost("apache.org")
            .setEncodedPath("/shindig")
            .addParameter("hello world", "foo&bar")
            .setFragment("foo");
        assertEquals("http://apache.org/shindig?hello+world=foo%26bar#foo", builder.toString());
        assertEquals("hello+world=foo&bar", builder.build().getQuery());
    }

    @Test
    public void addSingleFragmentParameter() {
        final URIBuilder builder = new URIBuilder()
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .setFragment("hello=world")
            .setCustomQuery("foo");
        assertEquals("http://apache.org/shindig?foo#hello=world", builder.toString());
    }

    @Test
    public void fragmentStringIsUnescaped() {
        final URIBuilder builder = new URIBuilder(true)
            .setScheme("http")
            .setHost("apache.org")
            .setPath("/shindig")
            .setEncodedFragment("hello+world=world%26bar");

        assertEquals("world&bar", builder.build().getFragment().split("=")[1]);
    }

    @Test
    public void parse() {
        final URIBuilder builder = new URIBuilder()
            .digestURI(URI.create("http://apache.org/shindig?foo=bar%26baz&foo=three%3Dbaz#blah"));

        assertEquals("http", builder.getScheme());
        assertEquals("apache.org", builder.getHost());
        assertEquals("/shindig", builder.getPath());

        final List<URIBuilder.BasicNameValuePair> list = builder.getQueryParams();
        for (final URIBuilder.BasicNameValuePair pair : list) {
            assertEquals("foo", pair.getName());
            assertTrue(pair.getValue().equals("three=baz") || pair.getValue().equals("bar&baz"));
        }
        assertEquals(list.size(), 2);
        assertEquals("blah", builder.getFragment());
    }

    @Test
    public void constructFromUriAndBack() {
        final URI uri = URI.create("http://apache.org/foo/bar?foo=bar&a=b&c=d&y=z&foo=zoo#foo");
        final URIBuilder builder = new URIBuilder(uri);

        assertEquals(uri, builder.build());
    }

    @Test
    public void constructFromUriAndModify() {
        final URI uri = URI.create("http://apache.org/foo/bar?foo=bar#foo");
        final URIBuilder builder = new URIBuilder(uri);

        builder.setHost("example.org");
        builder.addParameter("bar", "foo");

        assertEquals("http://example.org/foo/bar?foo=bar&bar=foo#foo", builder.toString());
    }

    @Test
    public void equalsAndHashCodeOk() {
        final URIBuilder uri = new URIBuilder().digestURI(URI.create("http://example.org/foo/bar/baz?blah=blah#boo"));
        final URIBuilder uri2 = new URIBuilder(URI.create("http://example.org/foo/bar/baz?blah=blah#boo"));

        assertEquals(uri, uri2);
        assertEquals(uri2, uri);

        assertEquals(uri, uri);

        assertNotNull(uri);
        assertNotSame(uri, "http://example.org/foo/bar/baz?blah=blah#boo");
        assertNotSame(uri, URI.create("http://example.org/foo/bar/baz?blah=blah#boo"));
        assertEquals(uri.hashCode(), uri2.hashCode());
    }


}
