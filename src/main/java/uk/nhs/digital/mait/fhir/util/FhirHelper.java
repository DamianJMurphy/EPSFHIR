/*
 Copyright 2020  NHS Digital <damian.murphy@nhs.net>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package uk.nhs.digital.mait.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Type;

/**
 * Set of "helper" static methods for working with HAPI FHIR resource classes.
 * 
 * @author Damian Murphy
 */
public class FhirHelper {

    public static final SimpleDateFormat HL7v3DATEFORMAT = new SimpleDateFormat("YYYYMMdd");
    public static final SimpleDateFormat HL7v3TIMEFORMAT = new SimpleDateFormat("YYYYMMddHHMMss");
    
    /**
     * Write the given bundle. Forces UTF-8. 
     * 
     * @param fn File name
     * @param od Output directory
     * @param b The Bundle
     * @param ctx FhirContext
     * @param stdout Write to standard output (System.out)
     * @param xml Write XML (write JSON if false)
     * @param pp Pretty print
     * @throws Exception If something goes wrong
     */
    @SuppressWarnings("ConvertToTryWithResources")
    public static final void write(String fn, String od, Bundle b, FhirContext ctx, boolean stdout, boolean xml, boolean pp)
            throws Exception
    {
        @SuppressWarnings("UnusedAssignment")
        IParser parser = null;
        @SuppressWarnings("UnusedAssignment")
        String filename = null;
        if (xml) {
            parser = ctx.newXmlParser();
            filename = fn + ".xml";            
        } else {
            parser = ctx.newJsonParser();
            filename = fn + ".json";
        }
        parser.setPrettyPrint(pp);
        String serialised = parser.encodeResourceToString(b);
        if (stdout) {
            OutputStreamWriter systemout = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
            systemout.write(fn);
            systemout.write(System.getProperty("line.separator"));
            systemout.write(serialised);
            systemout.flush();
        } else {
            @SuppressWarnings("UnusedAssignment")
            File f = null;
            if (od == null) {
                f = new File(filename);
            } else {
                f = new File(od, filename);
            }
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
            osw.write(serialised);
            osw.flush();
            osw.close();
        }        
    }
    
    /**
     * Make an un-decorated (i.e. no "urn:uuid:" prefix) lower-case random UUID
     * 
     * @return The new UUID
     */
    public static final String makeId() { return UUID.randomUUID().toString().toLowerCase(); }
    
    /**
     * Create a Reference for the given resource, adding a lower-case-UUID id
     * to the resource if there is not one there initially. Reference will be of
     * the form "urn:uuid:" + the resource id.
     * 
     * @param res The resource
     * @return The reference
     */
    public static final Reference makeInternalReference(Resource res) {
        if (!res.hasId()) {
            res.setId(makeId());
        }
        return new Reference("urn:uuid:" + res.getId());
    }
        
    /**
     * Creates an Identifier instance for the given URL and value.
     * 
     * @param u URL/system
     * @param v ID value
     * @return The new Identifier instance
     */
    public static final Identifier makeIdentifier(String u, String v) {
        Identifier id = new Identifier();
        id.setSystem(u);
        id.setValue(v);
        return id;
    }

    /**
     * Make an ArrayList containing a single Identifier
     * @param u
     * @param v
     * @return
     */
    public static final ArrayList<Identifier> makeIdentifierArray(String u, String v) {
        ArrayList<Identifier> a = new ArrayList<>();
        a.add(FhirHelper.makeIdentifier(u, v));
        return a;
    }
    
    /**
     * Make a new Extension
     * @param u
     * @param v
     * @return
     */
    public static final Extension makeExtension(String u, Type v) {
        return FhirHelper.makeExtension(null, u, v);
    }
    
    /**
     * Set the URL and value on the given Extension, and return it. Makes a new Extension if
     * given Extension is null.
     * @param e
     * @param u
     * @param v
     * @return The Extension passed in parameter e, or a new Extension if e was null.
     */
    public static final Extension makeExtension(Extension e, String u, Type v) {
        Extension x = (e == null) ? new Extension() : e;
        x.setUrl(u);
        x.setValue(v);
        return x;
    } 
    
    /**
     * Set the system, code and display on the given Coding, and return it. Makes a new
     * Coding if the given one is null,
     * @param cd
     * @param s
     * @param c
     * @param d
     * @return
     */
    public static final Coding makeCoding(Coding cd, String s, String c, String d) {
        Coding coding = (cd == null) ? new Coding() : cd;
        if (s != null) 
            coding.setSystem(s);
        if (c != null)
            coding.setCode(c);
        if (d != null)
            coding.setDisplay(d);
        return coding;        
    }
    
    /**
     * Make and populate a new Coding.
     * @param s System
     * @param c Code
     * @param d Display (may be null)
     * @return
     */
    public static final Coding makeCoding(String s, String c, String d) {
        return FhirHelper.makeCoding(null, s, c, d);
    }
    
    /**
     * Parses an HL7v3 date/time format string and returns it as a Date. If this
     * operation fails, the current date is returned.
     * 
     * @param s
     * @return
     */
    public static final Date hl7v3ToDate(String s) {
        if ((s == null) || (s.trim().length() == 0)) {
            return new Date();
        }
        try {
            return HL7v3TIMEFORMAT.parse(s);            
        }
        catch (ParseException e) {
            return new Date();
        }
    }
    
    /**
     * Parses the given string as YYYYMMdd and returns it
     * 
     * @param s
     * @return
     * @throws Exception If the parse fails
     */
    public static final Date makeDate(String s) 
            throws Exception
    {
        if ((s == null) || (s.trim().length() == 0)) {
            return null;
        }
        Date d = HL7v3DATEFORMAT.parse(s);
        return d;
    }
    
    /**
     * Adds the given resource instance to the Bundle, and sets the Bundle.fullUrl
     * to "urn:uuid:" plus the id of the resource.
     * 
     * @param b The Bundle
     * @param r The Resource
     */
    public static final void addEntryToBundle(Bundle b, Resource r) {
        Bundle.BundleEntryComponent hc = b.addEntry();
        hc.setResource(r);
        hc.setFullUrl("urn:uuid:" + r.getId());
    }
    
}
