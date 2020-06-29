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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Address.AddressUse;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.MessageHeader.MessageSourceComponent;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;

/**
 *
 * @author Damian Murphy
 */
public class MedicationRequestBundleBuilder {

    private String outputDirectory = null;
    private boolean useStdOut = false;
    
    private String prescriptionsFile = null;
    private String itemsFile = null;
    
    private boolean useXml = false;
    private boolean immediateFail = true;
    
    private static final String MYASID = "uk.nhs.digital.mait.epsfhir.asid";
    private static final String MYODS = "uk.nhs.digital.mait.epsfhir.odscode";
    private static final String MYURL = "uk.nhs.digital.mait.epsfhir.url";
    private static final String OUTPUT = "uk.nhs.digital.mait.epsfhir.output";
    private static final String IMMEDIATE = "uk.nhs.digital.mait.epsfhir.immediatefail"; 
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        @SuppressWarnings("UnusedAssignment")
        MedicationRequestBundleBuilder builder = null;
        
        try {
            System.getProperties().load(new FileReader(args[0]));
        }
        catch (IOException e) {
            System.err.println("Cannot read properties " + args[0] + ":" + e.getMessage());
            return;
        }
        switch (args.length) {
            case 3:
                builder = new MedicationRequestBundleBuilder(args[1], args[2], null);
                break;
            case 4:
                builder = new MedicationRequestBundleBuilder(args[1], args[2], args[3]);
                break;
            default:
                System.err.println("Invalid arguments: Should be prescriptionsfile itemsfile [outputdirectory]");
                return;
        }
        try {
            if (builder != null) {
                builder.go();
            }
        }
        catch (Exception e) {
            System.err.println("Immediate-Fail option set and exeption thrown: ");
            e.printStackTrace();
        }
    }


    public MedicationRequestBundleBuilder(String pfile, String ifile, String out) {
        if (out != null) {
            useStdOut = (out.contentEquals("-"));
            outputDirectory = out;
        }
        prescriptionsFile = pfile;
        itemsFile = ifile;
        if ((System.getProperty(OUTPUT) != null) && (System.getProperty(OUTPUT).trim().toLowerCase().contentEquals("xml"))) {
            useXml = true;
        }
        if ((System.getProperty(IMMEDIATE) != null) && (System.getProperty(IMMEDIATE).trim().toLowerCase().contentEquals("true"))) {
            immediateFail = true;
        }        
    }
    
    public void go() 
            throws Exception
    {
        sanityCheckOutput();
        EMUdata emu = new EMUdata(prescriptionsFile, itemsFile);
        emu.load();
        
        FhirContext ctx = FhirContext.forR4();
        
        for (String pid : emu.getPrescriptionIDs()) {
            try {
                Bundle b = makeBundle(pid, emu.getPrescriptionData(pid), emu.getItems(pid));
                write(pid, b, ctx);
            }
            catch (Exception e) {
                if (immediateFail) {
                    throw e;
                }
                System.err.println(pid + " : " + e.toString());
            }
        }
    }
    
    private void write(String pid, Bundle b, FhirContext ctx)
            throws Exception
    {
        @SuppressWarnings("UnusedAssignment")
        IParser parser = null;
        @SuppressWarnings("UnusedAssignment")
        String filename = null;
        if (useXml) {
            parser = ctx.newXmlParser();
            filename = pid + ".xml";            
        } else {
            parser = ctx.newJsonParser();
            filename = pid + ".json";
        }
        String serialised = parser.encodeResourceToString(b);
        if (useStdOut) {
            System.out.println(pid);
            System.out.print(serialised);
            System.out.flush();
        } else {
            @SuppressWarnings("UnusedAssignment")
            File f = null;
            if (outputDirectory == null) {
                f = new File(filename);
            } else {
                f = new File(outputDirectory, filename);
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(serialised);
            bw.flush();
            bw.close();
        }        
    }
    
    private void addEntryToBundle(Bundle b, Resource r) {
        BundleEntryComponent hc = b.addEntry();
        hc.setResource(r);
        hc.setFullUrl("urn:uuid:" + r.getId());
    }
    
    private Bundle makeBundle(String pid, ArrayList<String> rx, ArrayList<ArrayList<String>> items)
            throws Exception
    {
        Bundle bundle = new Bundle();
        bundle.setId(UUID.randomUUID().toString().toLowerCase());
        bundle.setType(BundleType.MESSAGE);
        // Practitioner and PractitionerRole entries so we can reference them
        
        ParticipantMaker author = new ParticipantMaker();
        author.make(EMUdefinitions.AUTHORROLEPROFILE, rx);
        
        MessageHeader header = makeMessageHeader(pid, rx, items, author);
        addEntryToBundle(bundle, header);
        
        Patient patient = getPatient(rx);
        addEntryToBundle(bundle, patient);
        addEntryToBundle(bundle, author.getPractitioner());
        addEntryToBundle(bundle, author.getOrganisation());
        addEntryToBundle(bundle, author.getRole());
        // If there is a nominated pharmacy, we need an organisation reference for it
        // so we can give it to the request builder as a performer
        
        Reference nominatedPharmacy = getNominatedPharmacyReference(rx);
                
        // For each item... make a MedicationRequest and add that, too.
        
        for (ArrayList<String> item : items) {
            MedicationRequest m = makeMedicationRequest(pid, patient, rx, item, nominatedPharmacy, author);
            if (m != null) {
                addEntryToBundle(bundle, m);
                header.addFocus(new Reference("urn:uuid:" + m.getId()));
            }
        }
        header.addFocus(new Reference("urn:uuid:" + patient.getId()));
        header.addFocus(new Reference("urn:uuid:" + author.getRole().getId()));
        return bundle;
    }
        
    private MessageHeader makeMessageHeader(String pid, ArrayList<String> rx, 
            ArrayList<ArrayList<String>> items,
            ParticipantMaker a)
            throws Exception
    {
        MessageHeader header = new MessageHeader();
        header.setId(UUID.randomUUID().toString().toLowerCase());
        Coding c = new Coding();
        c.setSystem("https://fhir.nhs.uk/R4/CodeSystem/message-event");
        c.setCode("prescription-order");
        c.setDisplay("Prescription Order");
        header.setEvent(c);
        header.setSender(new Reference("urn:uuid:" + a.getRole().getId()));
        header.getSender().setDisplay(a.getPractitioner().getName().get(0).getText());
        header.setSource(makeSource());         
        return header;
    }
    
    private MessageSourceComponent makeSource() {
        MessageSourceComponent s = new MessageSourceComponent();
        Extension asid = s.addExtension();
        asid.setUrl("https://fhir.nhs.uk/R4/StructureDefinition/Extension-spineEndpoint");
        Identifier id = new Identifier();
        id.setSystem("https://fhir.nhs.uk/Id/spine-ASID");
        id.setValue(System.getProperty(MYASID));
        asid.setValue(id);
        s.setName(System.getProperty(MYODS));
        s.setEndpoint(System.getProperty(MYURL));
        return s;
    }
    
    private MedicationRequest makeMedicationRequest(String pid, 
            Patient p,
            ArrayList<String> rx, 
            ArrayList<String> item, 
            Reference n, 
            ParticipantMaker a) {
        
        MedicationRequest m = new MedicationRequest();
        m.setId(item.get(EMUdefinitions.LINEITEMID));
        doPrescriptionType(rx, m);
        doResponsiblePractitioner(a, m);
        m.addIdentifier(doIdentifier("https://fhir.nhs.uk/Id/prescription-line-id", item.get(EMUdefinitions.LINEITEMID)));
        m.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
        m.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
        m.setMedication(doMedication(item));
        m.setSubject(new Reference("urn:uuid:" + p.getId()));
        
        // This is currently uncertain. EMU doesn't seem to provide a date for the prescription itself,
        // though it does the participation times. So use that.
        m.setAuthoredOn(hl7v3ToDate(rx.get(EMUdefinitions.AUTHORPARTICIPATIONTIME)));
        m.setRequester(new Reference("urn:uuid:" + a.getPractitioner().getId()));
        m.setGroupIdentifier(makeGroupIdentifier(pid, rx));
        m.setCourseOfTherapyType(makeCourseOfTherapyType(rx));
        if (item.get(EMUdefinitions.DOSAGEINTRUCTIONS).trim().length() > 0) {
            Dosage di = m.addDosageInstruction();
            di.setText(item.get(EMUdefinitions.DOSAGEINTRUCTIONS));
        }
        m.setDispenseRequest(makeDispenseRequest(n, rx, item));
        // Don't do "substitution" partly because HAPI has a problem with it, and it is 
        // always false anyway
        return m;
    }
    
    private MedicationRequest.MedicationRequestDispenseRequestComponent makeDispenseRequest(Reference n, ArrayList<String> rx, ArrayList<String> item) {
        MedicationRequest.MedicationRequestDispenseRequestComponent m = new MedicationRequest.MedicationRequestDispenseRequestComponent();
        Extension pte = m.addExtension();
        pte.setUrl("https://fhir.nhs.uk/R4/StructureDefinition/Extension-performerType");
        Coding c = new Coding();
        c.setSystem("https://fhir.nhs.uk/R4/CodeSystem/dispensing-site-preference");
        c.setCode(rx.get(EMUdefinitions.DISPENSINGSITEPREFERENCE));
        pte.setValue(c);
        Quantity q = new Quantity();
        q.setCode(item.get(EMUdefinitions.QUANTITYCODE));
        q.setSystem("http://snomed.info/sct");
        q.setUnit(item.get(EMUdefinitions.QUANTITYTEXT));
        q.setValue(Long.parseLong(item.get(EMUdefinitions.QUANTITYCOUNT)));
        m.setQuantity(q);
        if (n != null) {
            m.setPerformer(n);
        }
        return m;
    } 

    
    private CodeableConcept makeCourseOfTherapyType(ArrayList<String> rx) {
        CodeableConcept cc = new CodeableConcept();
        Coding c = cc.addCoding();
        c.setSystem("https://fhir.nhs.uk/R4/CodeSystem/CareConnect-PrescriptionType");
        try {
            String t = rx.get(EMUdefinitions.PRESCRIPTIONTREATMENTTYPE).trim();
            if ((t.length() == 0) || t.contentEquals("0001")) {
                c.setCode("acute");
                c.setDisplay("Acute");                
            } else {
                if (t.contentEquals("0002")) {
                    c.setCode("repeat");
                    c.setDisplay("Repeat");                                    
                } else {
                    c.setCode("repeat-dispensing");
                    c.setDisplay("Repeat Dispensing");                                    
                }
            }
        }
        catch (Exception e) {
            c.setCode("acute");
            c.setDisplay("Acute");
        }
        return cc;
    }
    
    private Identifier makeGroupIdentifier(String pid, ArrayList<String> rx) {
        Identifier id = new Identifier();
        Extension ex = id.addExtension();
        ex.setUrl("https://fhir.nhs.uk/R4/StructureDefinition/Extension-PrescriptionId");
        ex.setValue(new StringType(rx.get(EMUdefinitions.PRESCRIPTIONCLINICALSTATEMENTID)));
        id.setSystem("https://fhir.nhs.uk/Id/prescription-short-form");
        id.setValue(rx.get(EMUdefinitions.PRESCRIPTIONID));
        return id;
    }
    
    private Date hl7v3ToDate(String s) {
        if ((s == null) || (s.trim().length() == 0)) {
            return new Date();
        }
        try {
            return EMUdefinitions.TIMEFORMAT.parse(s);            
        }
        catch (ParseException e) {
            return new Date();
        }
    }
    
    private CodeableConcept doMedication(ArrayList<String> item) {
        CodeableConcept cc = new CodeableConcept();
        Coding c = cc.addCoding();
        c.setSystem("http://snomed.info/sct");
        c.setCode(item.get(EMUdefinitions.SUBSTANCECODE));
        c.setDisplay(item.get(EMUdefinitions.DISPLAYNAME));
        return cc;
    }
    
    private Identifier doIdentifier(String u, String v) {
        Identifier id = new Identifier();
        id.setSystem(u);
        id.setValue(v);
        return id;
    }
    
    private void doResponsiblePractitioner(ParticipantMaker a, MedicationRequest m) {
        Extension er = m.addExtension();
        er.setUrl("https://fhir.nhs.uk/R4/StructureDefinition/Extension-DM-ResponsiblePractitioner");
        Reference r = new Reference("urn:uuid:" + a.getPractitioner().getId());
        er.setValue(r);        
    }
    
    private void doPrescriptionType(ArrayList<String> rx, MedicationRequest m) {
        Extension pt = m.addExtension();
        pt.setUrl("https://fhir.nhs.uk/R4/StructureDefinition/Extension-prescriptionType");
        Coding ptc = new Coding();
        ptc.setSystem("https://fhir.nhs.uk/R4/CodeSystem/prescription-type");
        ptc.setCode(rx.get(EMUdefinitions.PRESCRIPTIONTYPE));
        
        // TODO: We could do with a general way to resolve displays for MIM vocabularies
        if (rx.get(EMUdefinitions.PRESCRIPTIONTYPE).contentEquals("0001")) {
            ptc.setDisplay("General Practitioner Prescribing");
        }
        pt.setValue(ptc);
    }
    
    private Reference getNominatedPharmacyReference(ArrayList<String> rx) {
        
        Reference r = null;
        
        if (rx.get(EMUdefinitions.NOMINATEDPHARMACYID).length() > 0) {
            r = new Reference();
            Identifier id = new Identifier();
            id.setSystem("https://fhir.nhs.uk/Id/ods-organization-code");
            id.setValue(rx.get(EMUdefinitions.NOMINATEDPHARMACYID));
            r.setIdentifier(id);
        }
        
        return r;
    }
    
    private Patient getPatient(ArrayList<String> rx)
            throws Exception
    {
        Patient p = new Patient();
        p.setId(UUID.randomUUID().toString().toLowerCase());
        addNhsNumber(p, rx);
        addPatientName(p, rx);
        Date dob = formatDate(rx.get(EMUdefinitions.PATIENTBIRTHTIME));
        if (dob != null) {
            p.setBirthDate(dob);
        }
        Date dd = formatDate(rx.get(EMUdefinitions.PATIENTDECEASEDTIME));
        p.setGender(getGender(rx.get(EMUdefinitions.PATIENTGENDER)));
        addPatientAddress(p, rx);
        addPatientGP(p, rx);
        
        return p;
    }
    
    private void addPatientGP(Patient p, ArrayList<String> rx) {
        // Reference to "managing organisation" with the GP practice code.
        // See https://simplifier.net/ukcore/~issues/897 for why this
        // is used and not generalPractitioner.
        
        Organization org = new Organization();
        
        // Need to see how to set reference and type
        
        Identifier gp = new Identifier();
        gp.setSystem("https://fhir.nhs.uk/Id/ods-organization-code");
        gp.setValue(rx.get(EMUdefinitions.PATIENTPRIMARYCAREPROVIDESDSID));
        ArrayList<Identifier> al = new ArrayList<>();
        al.add(gp);
        org.setIdentifier(al);
        p.setManagingOrganizationTarget(org);
        p.getManagingOrganization().setReference("https://directory.spineservices.nhs.uk/STU3/Organization/" + rx.get(EMUdefinitions.PATIENTPRIMARYCAREPROVIDESDSID));
    }
    
    private AdministrativeGender getGender(String s) {
        
        try {
            int g = Integer.parseInt(s);
            switch (g) {
                case 0:
                    return AdministrativeGender.UNKNOWN;
                case 1:
                    return AdministrativeGender.MALE;
                case 2:
                    return AdministrativeGender.FEMALE;
            }
        }
        catch (NumberFormatException e) {
            return AdministrativeGender.UNKNOWN;
        }
        return AdministrativeGender.OTHER;
    }
    
    private Date formatDate(String s) 
            throws Exception
    {
        if ((s == null) || (s.trim().length() == 0)) {
            return null;
        }
        Date d = EMUdefinitions.DATEFORMAT.parse(s);
        return d;
    }
    
    private void addIfPresent(ArrayList<StringType> st, ArrayList<String> rx, int offset) {
        try {
            String s = rx.get(offset).trim();
            if (s.length() > 0) {
                st.add(new StringType(s));
            }
        }
        catch (Exception e) {}
    }
    
    private void addPatientAddress(Patient p, ArrayList<String> rx) {
        Address a = new Address();
        a.setUse(AddressUse.HOME);        
        ArrayList<StringType> st = new ArrayList<>();
        addIfPresent(st, rx, EMUdefinitions.PATIENTADDRESSLINE1);
        addIfPresent(st, rx, EMUdefinitions.PATIENTADDRESSLINE2);
        addIfPresent(st, rx, EMUdefinitions.PATIENTADDRESSLINE3);
        addIfPresent(st, rx, EMUdefinitions.PATIENTADDRESSLINE4);
        addIfPresent(st, rx, EMUdefinitions.PATIENTADDRESSLINE5);
        a.setPostalCode(rx.get(EMUdefinitions.PATIENTADDRESSPOSTCODE));
        ArrayList<Address> aa = new ArrayList<>();
        aa.add(a);
        p.setAddress(aa);
    }
    
    private void addPatientName(Patient p, ArrayList<String> rx) {
        HumanName name = new HumanName();
        name.setUse(HumanName.NameUse.OFFICIAL);
        name.setFamily(rx.get(EMUdefinitions.PATIENTSURNAME));
        StringType st = new StringType();
        st.setValue(rx.get(EMUdefinitions.PATIENTNAMETITLE));
        ArrayList<StringType> pf = new ArrayList<>();
        pf.add(st);
        name.setPrefix(pf);
        name.addGiven(rx.get(EMUdefinitions.PATIENTGIVENNAME1));
        if (rx.get(EMUdefinitions.PATIENTGIVENNAME2).length() > 0) {
            name.addGiven(rx.get(EMUdefinitions.PATIENTGIVENNAME2));
        }
        ArrayList<HumanName> hn = new ArrayList<>();
        hn.add(name);
        p.setName(hn);
    }
    
    private void addNhsNumber(Patient p, ArrayList<String> rx) {
        Identifier nhsnumber = new Identifier();
        nhsnumber.setSystem("https://fhir.nhs.uk/Id/nhs-number");
        nhsnumber.setValue(rx.get(EMUdefinitions.PATIENTID));
        Extension evs = new Extension();
        evs.setUrl("https://fhir.nhs.uk/R4/StructureDefinition/Extension-UKCore-NHSNumberVerificationStatus");
        CodeableConcept vccvs = new CodeableConcept();
        Coding c = new Coding();
        c.setSystem("https://fhir.nhs.uk/R4/CodeSystem/UKCore-NHSNumberVerificationStatus");
        c.setCode("01");
        c.setDisplay("Number present and verified");
        vccvs.addCoding(c);
        evs.setValue(vccvs);
        nhsnumber.addExtension(evs);
        ArrayList<Identifier> n = new ArrayList<>();
        n.add(nhsnumber);
        p.setIdentifier(n);        
    }
    
    private void sanityCheckOutput() 
            throws Exception
    {
        if (!useStdOut) {
            File f = new File(outputDirectory);
            if (!f.isDirectory()) {
                throw new Exception("Output " + outputDirectory + " is not a directory");
            }
            if (!f.canExecute() || !f.canWrite()) {
                throw new Exception("Output " + outputDirectory + " not writable");
            }
        }
        
    }
}
