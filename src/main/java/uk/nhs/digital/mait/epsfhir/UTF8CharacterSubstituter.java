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
package uk.nhs.digital.mait.epsfhir;

import java.util.HashMap;

/**
 *
 * @author Damian Murphy
 */
public class UTF8CharacterSubstituter {

    private static final String[] ENCODINGS = {"__COPYRIGHT__", "00a9",
        "__REGISTERED__", "00ae",
        "__YEN__", "00a5",
        "__POUND__", "00a3",
        "__DEGREE__", "00b0",
        "__aGRAVE__", "00e0",
        "__eGRAVE__", "00e8",
        "__iGRAVE__", "00ec",
        "__oGRAVE__", "00f2",
        "__uGRAVE__", "00f9",
        "__AGRAVE__", "00c0",
        "__EGRAVE__", "00c8",
        "__IGRAVE__", "00cc",
        "__OGRAVE__", "00d2",
        "__UGRAVE__", "00d9",
        "__aCIRCUMFLEX__", "00e2",
        "__eCIRCUMFLEX__", "00ea",
        "__iCIRCUMFLEX__", "00ee",
        "__oCIRCUMFLEX__", "00f4",
        "__uCIRCUMFLEX__", "00fb",
        "__ACIRCUMFLEX__", "00c2",
        "__ECIRCUMFLEX__", "00ca",
        "__ICIRCUMFLEX__", "00ce",
        "__OCIRCUMFLEX__", "00d4",
        "__UCIRCUMFLEX__", "00db",
        "__LATINaDIAERESIS__", "00e4",
        "__eDIAERESIS__", "00eb",
        "__iDIAERESIS__", "00ef",
        "__oDIAERESIS__", "00f6",
        "__uDIAERESIS__", "00fc",
        "__yDIAERESIS__", "00ff",
        "__ADIAERESIS__", "00c4",
        "__EDIAERESIS__", "00cb",
        "__ODIAERESIS__", "00d6",
        "__UDIAERESIS__", "00dc",
        "__YDIAERESIS__", "0178",
        "__aTILDE__", "00e3",
        "__oTILDE__", "00f5",
        "__nTILDE__", "00f1",
        "__aRING__", "00e5",
        "__oSTROKE__", "00f8",
        "__sCARON__", "0161",
        "__ATILDE__", "00c3",
        "__OTILDE__", "00d5",
        "__NTILDE__", "00d1",
        "__ARING__", "00c5",
        "__OSTROKE__", "00d8",
        "__cCEDILLA__", "00e7",
        "__CCEDILLA__", "00c7",
        "__DIAERESIS__", "00a8",
        "__TRADEMARK__", "2122",
        "__EURO__", "20ac",
        "__CENT__", "00a2",
        "__CURRENCY__", "00a4",
        "__GAMMA__", "0194",
        "__RH_SINGLE__", "2019",
        "__LH_SINGLE__", "2018",
        "__LINE_FEED__", "000a",
        "__DASH__", "2013",
        "__CURVED_APOSTROPHE__", "055a"};
    
    private static final HashMap<String,String> utfSubstitutions = new HashMap<>();
    private static Exception bootException = null;
    
    static {
        try {
            for (int i = 0; i < ENCODINGS.length; i += 2) {
                String tag = ENCODINGS[i];
                String u = ENCODINGS[i + 1];
                String h = hexToUnicodePoint(u);
                utfSubstitutions.put(tag, h);
            }
        }
        catch (Exception e) {
            bootException = e;
        }
    }
    
    public static Exception getBootException() { return bootException; }

    public static String doSubstitutions(String in) 
            throws Exception
    {
        if (bootException != null) {
            throw bootException;
        }
        StringBuilder sb = new StringBuilder(in);
        for (String s : utfSubstitutions.keySet()) {
            @SuppressWarnings("UnusedAssignment")
            int start = 0;
            while ((start = sb.indexOf(s)) != -1) {
                sb.replace(start, start + s.length(), utfSubstitutions.get(s));
            }
        }        
        return sb.toString();
    }
    
    private static String hexToUnicodePoint(String s)
            throws Exception
    {        
        int cp[] = new int[1];
        cp[0] = Integer.parseInt(s, 16);
        return new String(cp, 0, 1);
    }
    
}
