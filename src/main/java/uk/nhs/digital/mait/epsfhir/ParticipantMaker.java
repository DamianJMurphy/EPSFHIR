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

import java.util.ArrayList;
import java.util.UUID;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;

/**
 *
 * @author Damian Murphy
 */
class ParticipantMaker {

    private Practitioner practitioner = null;
    private PractitionerRole role = null;
    private Organization organisation = null;
    
    void make(int b, ArrayList<String> rx) {        
        practitioner = new Practitioner();
        role = new PractitionerRole();
        organisation = new Organization();
        doPractitioner(b, rx);
        doOrg(b, rx);
        doRole(b, rx);
    }
    
    Practitioner getPractitioner() { return practitioner; }
    PractitionerRole getRole() { return role; }
    Organization getOrganisation() { return organisation; }
    
    private void doPractitioner(int b, ArrayList<String> rx) {
        practitioner.setId(UUID.randomUUID().toString().toLowerCase());
        HumanName h = new HumanName();
        h.setText(rx.get(b + EMUdefinitions.PERSONNAME));
        ArrayList<HumanName> ah = new ArrayList<>();
        ah.add(h);
        practitioner.setName(ah);
        Identifier identifier = new Identifier();
        identifier.setSystem("https://fhir.nhs.uk/Id/sds-user-id");
        identifier.setValue(rx.get(b + EMUdefinitions.SDSUSERID));
        ArrayList<Identifier> ai = new ArrayList<>();
        ai.add(identifier);
        practitioner.setIdentifier(ai);
    }
    
    private void doRole(int b, ArrayList<String> rx) {
        role.setId(UUID.randomUUID().toString().toLowerCase());
        Identifier identifier = new Identifier();
        identifier.setSystem("https://fhir.nhs.uk/Id/sds-role-profile-id");
        identifier.setValue(rx.get(b + EMUdefinitions.ROLEPROFILE));
        ArrayList<Identifier> ai = new ArrayList<>();
        ai.add(identifier);
        role.setIdentifier(ai);
        role.setPractitioner(new Reference(practitioner));
        role.setOrganization(new Reference(organisation));
        ContactPoint c = new ContactPoint();
        c.setSystem(ContactPoint.ContactPointSystem.PHONE);
        c.setUse(ContactPoint.ContactPointUse.WORK);
        c.setValue(rx.get(b + EMUdefinitions.ORGANISATIONTELECOM));
    }
    
    private void doOrg(int b, ArrayList<String> rx) {
        organisation.setId(UUID.randomUUID().toString().toLowerCase());
        Identifier identifier = new Identifier();
        identifier.setSystem("https://fhir.nhs.uk/Id/ods-organization-code");
        identifier.setValue(rx.get(b + EMUdefinitions.SDSORGANISATIONID));
        ArrayList<Identifier> ai = new ArrayList<>();
        ai.add(identifier);
        organisation.setIdentifier(ai);
        ContactPoint c = new ContactPoint();
        c.setSystem(ContactPoint.ContactPointSystem.PHONE);
        c.setUse(ContactPoint.ContactPointUse.WORK);
        c.setValue(rx.get(b + EMUdefinitions.ORGANISATIONTELECOM));
        
        // Note: type - EMU can't provide a FHIR OrganizationType
        
        CodeableConcept cc = new CodeableConcept();
        Coding code = new Coding();
        code.setSystem("urn:uuid:2.16.840.1.113883.2.1.3.2.4.17.94");
        code.setCode(rx.get(b + EMUdefinitions.ORGANISATIONTYPE));
        cc.addCoding(code);
        ArrayList<CodeableConcept> ac = new ArrayList<>();
        ac.add(cc);
        organisation.setType(ac);
        organisation.setName(rx.get(b + EMUdefinitions.ORGANISATIONNAME));
        
        // address
        
        Address addr = new Address();
        String l = rx.get(b + EMUdefinitions.ORGANISATIONADDRESSLINE1);
        if (l.length() > 0) {
            addr.addLine(l);
        }
        l = rx.get(b + EMUdefinitions.ORGANISATIONADDRESSLINE2);
        if (l.length() > 0) {
            addr.addLine(l);
        }
        l = rx.get(b + EMUdefinitions.ORGANISATIONADDRESSLINE3);
        if (l.length() > 0) {
            addr.addLine(l);
        }
        l = rx.get(b + EMUdefinitions.ORGANISATIONADDRESSLINE4);
        if (l.length() > 0) {
            addr.addLine(l);
        }
        l = rx.get(b + EMUdefinitions.ORGANISATIONADDRESSLINE5);
        if (l.length() > 0) {
            addr.addLine(l);
        }        
        addr.setPostalCode(rx.get(b + EMUdefinitions.ORGANISATIONPOSTCODE));
        ArrayList<Address> aa = new ArrayList<>();
        aa.add(addr);
        organisation.setAddress(aa);
        // part of (pct)
        Identifier pct = new Identifier();
        pct.setSystem("https://fhir.nhs.uk/Id/ods-organization-code");
        pct.setValue(rx.get(b + EMUdefinitions.PCTORGANISATIONSDSID));
        Reference pctref = new Reference();
        pctref.setIdentifier(pct);
        organisation.setPartOf(pctref);
    }
        
    boolean has(Practitioner p) {
        if (practitioner == null)
            return false;
        if (p == null)
            return false;
        return practitioner.equalsDeep(p);
    }
    
    boolean has(PractitionerRole r) {
        if (role == null)
            return false;
        if (r == null)
            return false;
        return role.equalsDeep(r);
    }
    
    boolean has(Organization o) {
        if (organisation == null)
            return false;
        if (o == null)
            return false;
        return organisation.equalsDeep(o);
    }
}
