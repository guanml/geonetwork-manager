/*
 *  GeoNetwork-Manager - Simple Manager Library for GeoNetwork
 *
 *  Copyright (C) 2007-2016 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package it.geosolutions.geonetwork.op.gn2x;

import it.geosolutions.geonetwork.util.GNVersion;
import it.geosolutions.geonetwork.util.HTTPUtils;
import java.net.MalformedURLException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;


/**
 * 
 * @author ETj (etj at geo-solutions.it)
 */
public class GNInfo {
        
    private final static Logger LOGGER = Logger.getLogger(GNInfo.class);

    private final GNVersion version;

    public static final GNInfo V26 = new GNInfo(GNVersion.V26);
    public static final GNInfo V28 = new GNInfo(GNVersion.V28);

    public static GNInfo get(GNVersion v) {
        switch (v) {
            case V26:
                return V26;
            case V28:
                return V28;
            default:
                throw new IllegalStateException("Bad version requested " + v);
        }
    }

    private String getLang() {
        return version == GNVersion.V26 ? "en" : "eng";
    }

    private GNInfo(GNVersion v) {
        this.version = v;
    }

    // needs authentication
    public boolean ping(HTTPUtils connection, String serviceURL) {
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("PING");

        connection.setIgnoreResponseContentOnSuccess(true);
        String url = serviceURL + "/srv/"+getLang()+"/test.csw"; // this is an arbitrary service that should return 200 both in authenticated and anonymous mode

        try {
            connection.get(url);
        } catch (MalformedURLException ex) {
            LOGGER.error(ex.getMessage());
            return false;
        }

        if(connection.getLastHttpStatus() != HttpStatus.SC_OK) {
            if(LOGGER.isInfoEnabled())
                LOGGER.info("PING failed");
            return false;
        }

        return true;
    }
}
