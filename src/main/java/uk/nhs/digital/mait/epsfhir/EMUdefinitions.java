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

import java.text.SimpleDateFormat;

/**
 *
 * @author Damian Murphy
 */
public class EMUdefinitions {
    
    // Prescriptions
    public static final int ID = 0;
    public static final int URGENT = 1;
    public static final int PATIENTID = 2;
    public static final int PATIENTADDRESSLINE1 = 3;
    public static final int PATIENTADDRESSLINE2 = 4;
    public static final int PATIENTADDRESSLINE3 = 5;
    public static final int PATIENTADDRESSLINE4 = 6;
    public static final int PATIENTADDRESSLINE5 = 7;
    public static final int PATIENTADDRESSPOSTCODE = 8;
    public static final int PATIENTADDRESSPAFCODE = 9;
    public static final int PATIENTADDRESSTYPE = 10;
    public static final int PATIENTADDRESSUSEFROM = 11;
    public static final int PATIENTADDRESSUSETO = 12;
    public static final int PATIENTNAMETITLE = 13;
    public static final int PATIENTGIVENNAME1 = 14;
    public static final int PATIENTGIVENNAME2 = 15;
    public static final int PATIENTSURNAME = 16;
    public static final int PATIENTNAMESUFFIX = 17;
    public static final int PATIENTNAMETYPE = 18;
    public static final int PATIENTNAMEUSEFROM = 19;
    public static final int PATIENTNAMEUSETO = 20;
    public static final int PATIENTGENDER = 21;
    public static final int PATIENTBIRTHTIME = 22;
    public static final int PATIENTDECEASEDTIME = 23;
    public static final int PATIENTPRIMARYCAREPROVIDESDSID = 24;
    public static final int REPEATNUMBER = 25;
    public static final int MAXREPEATPRESCRIPTIONS = 26;
    public static final int MAXIMUMREPEATDISPENSES = 27;
    public static final int DAYSSUPPLYFROM = 28;
    public static final int DAYSSUPPLYTO = 29;
    public static final int EXPECTEDUSE = 30;
    public static final int TOKENISSUED = 31;
    public static final int PRESCRIPTIONTREATMENTTYPE = 32;
    public static final int PRESCRIPTIONTYPE = 33;
    public static final int TEMPORARYEXEMPTIONINFORMATION = 34;
    public static final int TEMPORARYEXEMPTIONFROM = 35;
    public static final int TEMPORARYEXEMPTIONTO = 36;
    public static final int REVIEWDATE = 37;
    public static final int ORIGINALPRESCRPTIONREFERENCE = 38;
    public static final int NOMINATEDPHARMACYID = 39;
    public static final int DISPENSINGSITEPREFERENCE = 40;
    public static final int AUTHORROLEPROFILE = 41;
    public static final int AUTHORJOBCODE = 42;
    public static final int AUTHORSDSUSERID = 43;
    public static final int AUTHORPERSONNAME = 44;
    public static final int AUTHORPERSONTELECOM = 45;
    public static final int AUTHORSDSORGANISATIONID = 46;
    public static final int AUTHORORGANISATIONNAME = 47;
    public static final int AUTHORORGANISATIONTYPE = 48;
    public static final int AUTHORORGANISATIONTELECOM = 49;
    public static final int AUTHORORGANISATIONADDRESSLINE1 = 50;
    public static final int AUTHORORGANISATIONADDRESSLINE2 = 51;
    public static final int AUTHORORGANISATIONADDRESSLINE3 = 52;
    public static final int AUTHORORGANISATIONADDRESSLINE4 = 53;
    public static final int AUTHORORGANISATIONADDRESSLINE5 = 54;
    public static final int AUTHORORGANISATIONPOSTCODE = 55;
    public static final int AUTHORPCTORGANISATIONSDSID = 56;
    public static final int AUTHORPARTICIPATIONTIME = 57;
    public static final int RESPONSIBLEPARTYROLEPROFILE = 58;
    public static final int RESPONSIBLEPARTYJOBCODE = 59;
    public static final int RESPONSIBLEPARTYSDSUSERID = 60;
    public static final int RESPONSIBLEPARTYPERSONNAME = 61;
    public static final int RESPONSIBLEPARTYPERSONTELECOM = 62;
    public static final int RESPONSIBLEPARTYSDSORGANISATIONID = 63;
    public static final int RESPONSIBLEPARTYORGANISATIONNAME = 64;
    public static final int RESPONSIBLEPARTYORGANISATIONTYPE = 65;
    public static final int RESPPARTYORGANISATIONTELECOM = 66;
    public static final int RESPPARTYORGANISATIONADDRESSLINE1 = 67;
    public static final int RESPPARTYORGANISATIONADDRESSLINE2 = 68;
    public static final int RESPPARTYORGANISATIONADDRESSLINE3 = 69;
    public static final int RESPPARTYORGANISATIONADDRESSLINE4 = 70;
    public static final int RESPPARTYORGANISATIONADDRESSLINE5 = 71;
    public static final int RESPPARTYORGANISATIONPOSTCODE = 72;
    public static final int RESPPARTYPCTORGANISATIONSDSID = 73;
    public static final int RESPONSIBLEPARTYPARTICIPATIONTIME = 74;
    public static final int LEGALAUTHENTICATORROLEPROFILE = 75;
    public static final int LEGALAUTHENTICATORJOBCODE = 76;
    public static final int LEGALAUTHENTICATORSDSUSERID = 77;
    public static final int LEGALAUTHENTICATORPERSONNAME = 78;
    public static final int LEGALAUTHENTICATORPERSONTELECOM = 79;
    public static final int LEGALAUTHENTICATORSDSORGANISATIONID = 80;
    public static final int LEGALAUTHENTICATORORGANISATIONNAME = 81;
    public static final int LEGALAUTHENTICATORORGANISATIONTYPE = 82;
    public static final int LEGALAUTHORGANISATIONTELECOM = 83;
    public static final int LEGALAUTHORGANISATIONADDRESSLINE1 = 84;
    public static final int LEGALAUTHORGANISATIONADDRESSLINE2 = 85;
    public static final int LEGALAUTHORGANISATIONADDRESSLINE3 = 86;
    public static final int LEGALAUTHORGANISATIONADDRESSLINE4 = 87;
    public static final int LEGALAUTHORGANISATIONADDRESSLINE5 = 88;
    public static final int LEGALAUTHORGANISATIONPOSTCODE = 89;
    public static final int LEGALAUTHPCTORGANISATIONSDSID = 90;
    public static final int LEGALAUTHENTICATORPARTICIPATIONTIME = 91;
    public static final int HANDLING = 92;
    public static final int PRESCRIPTIONCLINICALSTATEMENTID = 93;

    // Line items
    public static final int PRESCRIPTIONID = 0;
    public static final int SUBSTANCECODE = 1;
    public static final int DISPLAYNAME = 2;
    public static final int ORIGINALTEXT = 3;
    public static final int QUANTITYTEXT = 4;
    public static final int QUANTITYCODE = 5;
    public static final int QUANTITYCOUNT = 6;
    public static final int DOSAGEINTRUCTIONS = 7;
    public static final int ADDITIONALINSTRUCTIONS = 8;
    public static final int ORIGINALITEMREF = 9;
    public static final int PRESCRIBERENDORSEMENT = 10;
    public static final int INTENDEDMEDICATIONREF = 11;
    public static final int INTENDEDMEDICATIONMOOD = 12;
    public static final int DOSEQUANTITY = 13;
    public static final int RATEQUANTITY = 14;
    public static final int ITEMREPEATNUMBER = 15;
    public static final int MAXREPEATS = 16;
    public static final int LINEITEMID = 17;

    // Participant offsets
    public static final int ROLEPROFILE = 0;
    public static final int JOBCODE = 1;
    public static final int SDSUSERID = 2;
    public static final int PERSONNAME = 3;
    public static final int PERSONTELECOM = 4;
    public static final int SDSORGANISATIONID = 5;
    public static final int ORGANISATIONNAME = 6;
    public static final int ORGANISATIONTYPE = 7;
    public static final int ORGANISATIONTELECOM = 8;
    public static final int ORGANISATIONADDRESSLINE1 = 9;
    public static final int ORGANISATIONADDRESSLINE2 = 10;
    public static final int ORGANISATIONADDRESSLINE3 = 11;
    public static final int ORGANISATIONADDRESSLINE4 = 12;
    public static final int ORGANISATIONADDRESSLINE5 = 13;
    public static final int ORGANISATIONPOSTCODE = 14;
    public static final int PCTORGANISATIONSDSID = 15;
    public static final int PARTICIPATIONTIME = 16;

    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("YYYYMMdd");
    public static final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("YYYYMMddHHMMss");
    
}
