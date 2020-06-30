# EPSFHIR
## Builder for UK-Core MedicationRequest bundles from NHS Digital Test Data "EMU" files

When built the command-line expects the following arguments:

properties file (see below)

EMU prescriptions ("Export_JoinedTable_R2") file

EMU line items ("Exprt_LineItem_R2") file

Output directory (or - for stdout)

It will write a JSON or XML file containing the serialised bundle for each prescription in the input data, to
the given output directory, named after the short-form prescription id (eg "0EF1F6-B83002-00001X.json" or 
"0EF1F6-B83002-00001X.xml" as appropriate. When writing to standard output, data is separated by the short-form
prescription id on a line by itself.

"Static" configurations are held in the properties file:

uk.nhs.digital.mait.epsfhir.asid	ASID for MessageHeader.source  
uk.nhs.digital.mait.epsfhir.odscode	ODS code for MessageHeader.source  
uk.nhs.digital.mait.epsfhir.url	URL for MessageHeader.source  
uk.nhs.digital.mait.epsfhir.output	json|xml controlling output format  
uk.nhs.digital.mait.epsfhir.immediatefail	if true the program will fail immediately with a stack trace if something goes wrong,
otherwise it will write a message to System.err and attempt to carry on.

For example:

uk.nhs.digital.mait.epsfhir.asid	12456789012  
uk.nhs.digital.mait.epsfhir.odscode	DJM  
uk.nhs.digital.mait.epsfhir.url	https://mait.digital.nhs.uk/  
uk.nhs.digital.mait.epsfhir.output	json  
uk.nhs.digital.mait.epsfhir.immediatefail	true   
