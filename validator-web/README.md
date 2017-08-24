# jOCDS - Open Contracting Data Standard Validator - Web API Component

## Presentation

The Web Component will package jOCDS as a web service executable. By invoking
it from the command line the web server will start up and wait for requests on port
8080.

## Swagger and the documented API

The Web server will automatically start a Swagger application which can be used
to browse the API and the endpoints using http://localhost:8080/swagger-ui.html

## Endpoints Explained

There are 3 main endpoint URLs that can be used for validation purposes.
Additional info can be found by using the Swagger link above and by clicking
on each endpoint. Each parameter is documented in the API.

All endpoints receive the same generic request object:

```json
OcdsValidatorRequest {

extensions (Array[string], optional): You can provide a set of OCDS extensions here to validate against.
All OCDS core extensions are supported, in offline mode, as well as any other OCDS extension given by URL ,

operation (string, optional): Provides the operation that needs to be performed.
The default is 'validate'.'show-supported-ocds' will list the supported ocds versions.
show-builtin-extensions will list the core OCDS extensions that are supported internally and in offline mode. ,

schemaType (string, optional): This is the schema type of the input JSON.
Currently supported values are 'release' and release-package ,

version (string, optional): This is the version of OCDS schema to validate against.
Leaving this empty will enable schema autodetection. This is helpful to test against another OCDS schema
besides the one specified in the incoming JSON.

}
```


In addition to that, the json or the URL to validate is given in in each particular case

### GET /validateFormInline

This is a GET request that can be used to validate OCDS JSON data using
x-www-form-urlencoded request. It is implemented for compatibility purposes and for testing purposes.
GET requests can be more easily used by non skilled editors, by directly building the URL of the request

The following shows a request that asks the server the supported OCDS version for validation purposes:

```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/validateFormInline?version=1.1.1&operation=show-supported-ocds&schemaType=release-package'

[
  {
    "level": "info",
    "message": "1.0.0"
  },
  {
    "level": "info",
    "message": "1.0.1"
  },
  {
    "level": "info",
    "message": "1.0.2"
  },
  {
    "level": "info",
    "message": "1.1.0"
  },
  {
    "level": "info",
    "message": "1.1.1"
  }
]

```


### POST /validateFormInline

This is the same as the GET but uses POST to send data. It is obviously more useful as it can be used to send
larger JSON files as POST. Unlike POST, GET requests are limited in terms of size because they have to be URL encoded.

### POST /validateJsonInline

This endpoint receives application/json encoded parameters.
The OCDS JSON is provided inline, inside the `node` property.
The request object is very similar to the generic one described above.

```json
OcdsValidatorNodeRequest {

extensions (Array[string], optional): You can provide a set of OCDS extensions here to validate against.
All OCDS core extensions are supported, in offline mode, as well as any other OCDS extension given by URL ,

node (JsonNode, optional): The json to validate against OCDS schema ,

operation (string, optional): Provides the operation that needs to be performed.
The default is 'validate'.'show-supported-ocds' will list the supported ocds versions.
show-builtin-extensions will list the core OCDS extensions that are supported internally and in offline mode. ,

schemaType (string, optional): This is the schema type of the input JSON.
Currently supported values are 'release' and release-package ,

version (string, optional): This is the version of OCDS schema to validate against.
Leaving this empty will enable schema autodetection.
This is helpful to test against another OCDS schema besides the one specified in the incoming JSON.

}
```


To save space, we will show now how a very simple OCDS json can be validated using this endpoint

```
curl -X POST --header 'Content-Type: application/json;charset=UTF-8' --header 'Accept: application/json' -d '{ \
   "operation": "validate", \
   "schemaType": "release", \
   "version": "1.1.1", \
   "node": {"ocid": "ocds-full-001", "id": "ocds-full-1234", "date": "2016-05-10T09:30:00Z", "tag": ["award"], "initiationType": "tender" } \
 }' 'http://localhost:8080/validateJsonInline'
"OK"
```

However if we introduce a typo and write `awardd` instead of `award`, we get:

```
curl -X POST --header 'Content-Type: application/json;charset=UTF-8' --header 'Accept: application/json' -d '{ \
   "operation": "validate", \
   "schemaType": "release", \
   "version": "1.1.1", \
   "node": {"ocid": "ocds-full-001", "id": "ocds-full-1234", "date": "2016-05-10T09:30:00Z", "tag": ["awardd"], "initiationType": "tender" } \
 }' 'http://localhost:8080/validateJsonInline'

 [
   {
     "level": "error",
     "schema": {
       "loadingURI": "#",
       "pointer": "/properties/tag/items"
     },
     "instance": {
       "pointer": "/tag/0"
     },
     "domain": "validation",
     "keyword": "enum",
     "message": "instance value (\"awardd\") not found in enum (possible values: [\"planning\",\"planningUpdate\",\"tender\",\"tenderAmendment\",\"tenderUpdate\",\"tenderCancellation\",\"award\",\"awardUpdate\",\"awardCancellation\",\"contract\",\"contractUpdate\",\"contractAmendment\",\"implementation\",\"implementationUpdate\",\"contractTermination\",\"compiled\"])",
     "value": "awardd",
     "enum": [
       "planning",
       "planningUpdate",
       "tender",
       "tenderAmendment",
       "tenderUpdate",
       "tenderCancellation",
       "award",
       "awardUpdate",
       "awardCancellation",
       "contract",
       "contractUpdate",
       "contractAmendment",
       "implementation",
       "implementationUpdate",
       "contractTermination",
       "compiled"
     ]
   }
 ]

```

Obviously much more complicated examples will work, this was just a simple example.

### POST /validateJsonUrl

This endpoint receives application/json encoded parameters.
The same generic parameters can be sent, however instead of `node` like for the previously described endpoint, here
we provide the OCDS Json as an URL. We will use an URL from the OCDS github repository as an example

```json
curl -X POST --header 'Content-Type: application/json;charset=UTF-8' --header 'Accept: application/json' -d '{"version": "1.1.0", "schemaType": "release-package", \
 "url": "https://raw.githubusercontent.com/open-contracting/sample-data/master/fictional-example/1.1/ocds-213czf-000-00001-02-tender.json"}' 'http://localhost:8080/validateJsonUrl'
"OK"
```