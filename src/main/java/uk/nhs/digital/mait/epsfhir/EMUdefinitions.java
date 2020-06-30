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

/**
 *
 * @author Damian Murphy
 */
 class EMUdefinitions {
    
    // Prescriptions
     static final int ID = 0;
     static final int URGENT = 1;
     static final int PATIENTID = 2;
     static final int PATIENTADDRESSLINE1 = 3;
     static final int PATIENTADDRESSLINE2 = 4;
     static final int PATIENTADDRESSLINE3 = 5;
     static final int PATIENTADDRESSLINE4 = 6;
     static final int PATIENTADDRESSLINE5 = 7;
     static final int PATIENTADDRESSPOSTCODE = 8;
     static final int PATIENTADDRESSPAFCODE = 9;
     static final int PATIENTADDRESSTYPE = 10;
     static final int PATIENTADDRESSUSEFROM = 11;
     static final int PATIENTADDRESSUSETO = 12;
     static final int PATIENTNAMETITLE = 13;
     static final int PATIENTGIVENNAME1 = 14;
     static final int PATIENTGIVENNAME2 = 15;
     static final int PATIENTSURNAME = 16;
     static final int PATIENTNAMESUFFIX = 17;
     static final int PATIENTNAMETYPE = 18;
     static final int PATIENTNAMEUSEFROM = 19;
     static final int PATIENTNAMEUSETO = 20;
     static final int PATIENTGENDER = 21;
     static final int PATIENTBIRTHTIME = 22;
     static final int PATIENTDECEASEDTIME = 23;
     static final int PATIENTPRIMARYCAREPROVIDESDSID = 24;
     static final int REPEATNUMBER = 25;
     static final int MAXREPEATPRESCRIPTIONS = 26;
     static final int MAXIMUMREPEATDISPENSES = 27;
     static final int DAYSSUPPLYFROM = 28;
     static final int DAYSSUPPLYTO = 29;
     static final int EXPECTEDUSE = 30;
     static final int TOKENISSUED = 31;
     static final int PRESCRIPTIONTREATMENTTYPE = 32;
     static final int PRESCRIPTIONTYPE = 33;
     static final int TEMPORARYEXEMPTIONINFORMATION = 34;
     static final int TEMPORARYEXEMPTIONFROM = 35;
     static final int TEMPORARYEXEMPTIONTO = 36;
     static final int REVIEWDATE = 37;
     static final int ORIGINALPRESCRPTIONREFERENCE = 38;
     static final int NOMINATEDPHARMACYID = 39;
     static final int DISPENSINGSITEPREFERENCE = 40;
     static final int AUTHORROLEPROFILE = 41;
     static final int AUTHORJOBCODE = 42;
     static final int AUTHORSDSUSERID = 43;
     static final int AUTHORPERSONNAME = 44;
     static final int AUTHORPERSONTELECOM = 45;
     static final int AUTHORSDSORGANISATIONID = 46;
     static final int AUTHORORGANISATIONNAME = 47;
     static final int AUTHORORGANISATIONTYPE = 48;
     static final int AUTHORORGANISATIONTELECOM = 49;
     static final int AUTHORORGANISATIONADDRESSLINE1 = 50;
     static final int AUTHORORGANISATIONADDRESSLINE2 = 51;
     static final int AUTHORORGANISATIONADDRESSLINE3 = 52;
     static final int AUTHORORGANISATIONADDRESSLINE4 = 53;
     static final int AUTHORORGANISATIONADDRESSLINE5 = 54;
     static final int AUTHORORGANISATIONPOSTCODE = 55;
     static final int AUTHORPCTORGANISATIONSDSID = 56;
     static final int AUTHORPARTICIPATIONTIME = 57;
     static final int RESPONSIBLEPARTYROLEPROFILE = 58;
     static final int RESPONSIBLEPARTYJOBCODE = 59;
     static final int RESPONSIBLEPARTYSDSUSERID = 60;
     static final int RESPONSIBLEPARTYPERSONNAME = 61;
     static final int RESPONSIBLEPARTYPERSONTELECOM = 62;
     static final int RESPONSIBLEPARTYSDSORGANISATIONID = 63;
     static final int RESPONSIBLEPARTYORGANISATIONNAME = 64;
     static final int RESPONSIBLEPARTYORGANISATIONTYPE = 65;
     static final int RESPPARTYORGANISATIONTELECOM = 66;
     static final int RESPPARTYORGANISATIONADDRESSLINE1 = 67;
     static final int RESPPARTYORGANISATIONADDRESSLINE2 = 68;
     static final int RESPPARTYORGANISATIONADDRESSLINE3 = 69;
     static final int RESPPARTYORGANISATIONADDRESSLINE4 = 70;
     static final int RESPPARTYORGANISATIONADDRESSLINE5 = 71;
     static final int RESPPARTYORGANISATIONPOSTCODE = 72;
     static final int RESPPARTYPCTORGANISATIONSDSID = 73;
     static final int RESPONSIBLEPARTYPARTICIPATIONTIME = 74;
     static final int LEGALAUTHENTICATORROLEPROFILE = 75;
     static final int LEGALAUTHENTICATORJOBCODE = 76;
     static final int LEGALAUTHENTICATORSDSUSERID = 77;
     static final int LEGALAUTHENTICATORPERSONNAME = 78;
     static final int LEGALAUTHENTICATORPERSONTELECOM = 79;
     static final int LEGALAUTHENTICATORSDSORGANISATIONID = 80;
     static final int LEGALAUTHENTICATORORGANISATIONNAME = 81;
     static final int LEGALAUTHENTICATORORGANISATIONTYPE = 82;
     static final int LEGALAUTHORGANISATIONTELECOM = 83;
     static final int LEGALAUTHORGANISATIONADDRESSLINE1 = 84;
     static final int LEGALAUTHORGANISATIONADDRESSLINE2 = 85;
     static final int LEGALAUTHORGANISATIONADDRESSLINE3 = 86;
     static final int LEGALAUTHORGANISATIONADDRESSLINE4 = 87;
     static final int LEGALAUTHORGANISATIONADDRESSLINE5 = 88;
     static final int LEGALAUTHORGANISATIONPOSTCODE = 89;
     static final int LEGALAUTHPCTORGANISATIONSDSID = 90;
     static final int LEGALAUTHENTICATORPARTICIPATIONTIME = 91;
     static final int HANDLING = 92;
     static final int PRESCRIPTIONCLINICALSTATEMENTID = 93;

    // Line items
     static final int PRESCRIPTIONID = 0;
     static final int SUBSTANCECODE = 1;
     static final int DISPLAYNAME = 2;
     static final int ORIGINALTEXT = 3;
     static final int QUANTITYTEXT = 4;
     static final int QUANTITYCODE = 5;
     static final int QUANTITYCOUNT = 6;
     static final int DOSAGEINTRUCTIONS = 7;
     static final int ADDITIONALINSTRUCTIONS = 8;
     static final int ORIGINALITEMREF = 9;
     static final int PRESCRIBERENDORSEMENT = 10;
     static final int INTENDEDMEDICATIONREF = 11;
     static final int INTENDEDMEDICATIONMOOD = 12;
     static final int DOSEQUANTITY = 13;
     static final int RATEQUANTITY = 14;
     static final int ITEMREPEATNUMBER = 15;
     static final int MAXREPEATS = 16;
     static final int LINEITEMID = 17;

    // Participant offsets
     static final int ROLEPROFILE = 0;
     static final int JOBCODE = 1;
     static final int SDSUSERID = 2;
     static final int PERSONNAME = 3;
     static final int PERSONTELECOM = 4;
     static final int SDSORGANISATIONID = 5;
     static final int ORGANISATIONNAME = 6;
     static final int ORGANISATIONTYPE = 7;
     static final int ORGANISATIONTELECOM = 8;
     static final int ORGANISATIONADDRESSLINE1 = 9;
     static final int ORGANISATIONADDRESSLINE2 = 10;
     static final int ORGANISATIONADDRESSLINE3 = 11;
     static final int ORGANISATIONADDRESSLINE4 = 12;
     static final int ORGANISATIONADDRESSLINE5 = 13;
     static final int ORGANISATIONPOSTCODE = 14;
     static final int PCTORGANISATIONSDSID = 15;
     static final int PARTICIPATIONTIME = 16;
    
}
