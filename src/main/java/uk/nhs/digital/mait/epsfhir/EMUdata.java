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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Damian Murphy
 */
class EMUdata {

    private String parent = null;
    private String items = null;
    
    private final HashMap<String,ArrayList<String>> prescriptions = new HashMap<>();
    private final HashMap<String,ArrayList<ArrayList<String>>> requests = new HashMap<>();
    
    EMUdata(String pfile, String ifile) {
        parent = pfile;
        items = ifile;
    }
    
    void load()
            throws Exception
    {
        loadPrescriptions();
        loadItems();
        check();
    }
    
    Set<String> getPrescriptionIDs() {
        return prescriptions.keySet();
    }
    
    ArrayList<String> getPrescriptionData(String id) { return prescriptions.get(id); }

    ArrayList<ArrayList<String>> getItems(String id) { return requests.get(id); }
    
    private void check()
            throws Exception
    {
        // TODO: Content sanity-check
    }
    
    private void loadPrescriptions()
            throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(parent));
        @SuppressWarnings("UnusedAssignment")
        String line = null;
        while ((line = br.readLine()) != null) {
            ArrayList<String> pline = readLine(line);
            prescriptions.put(pline.get(EMUdefinitions.ID), pline);
        }
    }
    
    private void loadItems()
            throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(items));
        @SuppressWarnings("UnusedAssignment")
        String line = null;
        while ((line = br.readLine()) != null) {
            ArrayList<String> iline = readLine(line);
            ArrayList<ArrayList<String>> itemset = requests.get(iline.get(EMUdefinitions.ID));
            if (itemset == null) {
                itemset = new ArrayList<>();
                requests.put(iline.get(EMUdefinitions.ID), itemset);
            } 
            itemset.add(iline);
        }        
    }
    
    private ArrayList<String> readLine(String line) 
            throws Exception
    {
        ArrayList<String> list = new ArrayList<>();
        String utfLine = UTF8CharacterSubstituter.doSubstitutions(line);
        String[] fields = utfLine.split("\t");
        list.addAll(Arrays.asList(fields));
        return list;
    }
}
