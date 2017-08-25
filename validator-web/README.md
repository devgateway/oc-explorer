# jOCDS - Open Contracting Data Standard Validator - Web API Component

## Presentation

The Web Component will package jOCDS as a web service executable. By invoking
it from the command line the web server will start up and wait for requests on port
8080.

## Swagger and the documented API

The Web server will automatically start a Swagger application which can be used
to browse the API and the endpoints using http://localhost:8080/swagger-ui.html

## Endpoints

There are 3 main endpoint URLs that can be used for validation purposes.
Additional info can be found by using the Swagger link above and by clicking
on each endpoint. Each parameter is documented in the API.

All endpoints receive the same generic request object:

```
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

### GET /api/ocds/validator/validateFormInline

This is a GET request that can be used to validate OCDS JSON data using
x-www-form-urlencoded request. It is implemented for compatibility purposes and for testing purposes.
GET requests can be more easily used by non skilled editors, by directly building the URL of the request

The following shows a request that asks the server the supported OCDS version for validation purposes:

```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/ocds/validator/validateFormInline?version=1.1.1&operation=show-supported-ocds&schemaType=release-package'

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


### POST /api/ocds/validator/validateFormInline

This is the same as the GET but uses POST to send data. It is obviously more useful as it can be used to send
larger JSON files as POST. Unlike POST, GET requests are limited in terms of size because they have to be URL encoded.

### POST /api/ocds/validator/validateJsonInline

This endpoint receives `application/json` encoded parameters.
The OCDS JSON is provided inline, inside the `node` property.
The request object is very similar to the generic one described above.

```
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
 }' 'http://localhost:8080/api/ocds/validator/validateJsonInline'
"OK"
```

However if we introduce a typo and write `awardd` instead of `award`, we get:

```
curl -X POST --header 'Content-Type: application/json;charset=UTF-8' --header 'Accept: application/json' -d '{ \
   "operation": "validate", \
   "schemaType": "release", \
   "version": "1.1.1", \
   "node": {"ocid": "ocds-full-001", "id": "ocds-full-1234", "date": "2016-05-10T09:30:00Z", "tag": ["awardd"], "initiationType": "tender" } \
 }' 'http://localhost:8080/api/ocds/validator/validateJsonInline'

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

### POST /api/ocds/validator/validateJsonUrl

This endpoint receives `application/json` encoded parameters.
The same generic parameters can be sent, however instead of `node` like for the previously described endpoint, here
we provide the OCDS Json as an URL. We will use an URL from the OCDS github repository as an example

```json
curl -X POST --header 'Content-Type: application/json;charset=UTF-8' --header 'Accept: application/json' -d '{"version": "1.1.0", "schemaType": "release-package", \
 "url": "https://raw.githubusercontent.com/open-contracting/sample-data/master/fictional-example/1.1/ocds-213czf-000-00001-02-tender.json"}' 'http://localhost:8080/api/ocds/validator/validateJsonUrl'
"OK"
```


## Validating release packages

jOCDS supports release as well as release-package schemas validation. When validating release packages,
the input has to specify `schemaType: 'release-package'`.

Example of input json request

```
{
	"schemaType": "release-package",
	"node": {
		"uri": "http://standard.open-contracting.org/examples/releases/ocds-213czf-000-00001-03-tenderAmendment.json",
		"publishedDate": "2010-03-20T09:45:00Z",
		"publisher": {
			"scheme": "GB-COH",
			"uid": "09506232",
			"name": "Open Data Services Co-operative Limited",
			"uri": "http://standard.open-contracting.org/examples/"
		},
		"license": "http://opendatacommons.org/licenses/pddl/1.0/",
		"publicationPolicy": "https://github.com/open-contracting/sample-data/",
		"version": "1.1",
		"releases": [
			{
				"ocid": "ocds-full-001",
				"id": "ocds-full-1234",
				"date": "2016-05-10T09:30:00Z",
				"tag": [
					"award"
				],
				"initiationType": "tender"
			}
		]
	}
}
```

Notice we omitted the `operation` property, as the default operation is `validate`.

We also ommited the `version` property, because the version is included in the `release-package` schema definition,
so it should be present in the json. Omitting `version` will use the version element from the `release-package`.

However we still can override the version by specifying our own `version` in the request. Using this can help test OCDS json
generated output from third party tools that are producing OCDS in a certain standard version, on a different standard version,
this could help see what costs will a migration to a new OCDS version cost and check for backward compliance.


## Validating Extensions

### Getting info about extensions

jOCDS has extensive extension support. Schemas are patched using jsonmerge patches and then are cached for later reuse.
This should leverage good throughput for validating large number of releases with similar extensions (a very common use case).

Second, jOCDS supports all core OCDS extensions in offline mode. To list all the extensions that are natively supported by
jOCDS, use the following JSON request:

```
{
	"schemaType": "release",
	"operation": "show-builtin-extensions"
}

```

This will produce an output similar to this:

```
[
	{
		"level": "info",
		"message": "ocds_additionalContactPoints_extension"
	},
	{
		"level": "info",
		"message": "ocds_bid_extension/v1.1"
	},
	{
		"level": "info",
		"message": "ocds_budget_breakdown_extension"
	},
	{
		"level": "info",
		"message": "ocds_budget_projects_extension"
	},
	{
		"level": "info",
		"message": "ocds_charges_extension"
	},
	{
		"level": "info",
		"message": "ocds_contract_suppliers_extension"
	},
	{
		"level": "info",
		"message": "ocds_documentation_extension"
	},
	{
		"level": "info",
		"message": "ocds_enquiry_extension/v1.1"
	},
	{
		"level": "info",
		"message": "ocds_extendsContractID_extension"
	},
	{
		"level": "info",
		"message": "ocds_location_extension/v1.1"
	},
	{
		"level": "info",
		"message": "ocds_lots_extension/v1.1"
	},
	{
		"level": "info",
		"message": "ocds_milestone_documents_extension/v1.1"
	},
	{
		"level": "info",
		"message": "ocds_participationFee_extension/v1.1"
	},
	{
		"level": "info",
		"message": "ocds_process_title_extension/v1.1"
	}
]
```

The `message` property consists of the name of the extension. This exactly corresponds with the extension names
provided by the core OCDS extensions. Some of them have an appended suffix that corresponds to the extension version (`/v1.1`).
This means that multiple extension versions are supported. Extension versioning currently has poor support in OCDS, see
[#515](https://github.com/open-contracting/standard/issues/515).

### Validating extensions using core offline extension feature

For example to validate the bids entities, we can use a request such as this:


```
{
	"schemaType": "release-package",
	"extensions": [
		"ocds_bid_extension/v1.1"
	],
	"node": {
		"uri": "http://standard.open-contracting.org/examples/releases/ocds-213czf-000-00001-03-tenderAmendment.json",
		"publishedDate": "2010-03-20T09:45:00Z",
		"publisher": {
			"scheme": "GB-COH",
			"uid": "09506232",
			"name": "Open Data Services Co-operative Limited",
			"uri": "http://standard.open-contracting.org/examples/"
		},
		"license": "http://opendatacommons.org/licenses/pddl/1.0/",
		"publicationPolicy": "https://github.com/open-contracting/sample-data/",
		"version": "1.1",
		"releases": [
			{
				"ocid": "ocds-full-001",
				"id": "ocds-full-1234",
				"date": "2016-05-10T09:30:00Z",
				"tag": [
					"award"
				],
				"bids": {
					"statistics": [],
					"details": [
						{
							"id": "1111",
							"status": "active",
							"tenderers": [],
							"value": {
								"amount": 46633.61,
								"currency": "UAH"
							},
							"documents": []
						}
					]
				},
						"initiationType": "tender"
			}
			]
		}
	}
```


### Validating extensions using the (standard) extension URL option

The advantage of using the core builtin extensions is they work offline, so one could validate OCDS json without internet access.
Obviously extensions work with the regular URL provided, as described by OCDS:


```
{
	"schemaType": "release-package",
	"extensions": [
        "https://raw.githubusercontent.com/open-contracting/ocds_bid_extension/v1.1/extension.json"
    ],
	"node": ...
}
```


### Checking vor extension compliance

jOCDS automatically checks if an extension is compliant with the version of OCDS json.
Extensions often have the `compatibility` property used, to specify what OCDS versions
are supported

```
{
  "name": "Bid statistics and details",
  "description": "Allowing bid statistics, and detailed bid information to be represented.",
  "compatibility": ">=1.1.0",
  "dependencies": []
}
```

Example, trying to use the 1.1 bids extension and enforcing 1.0.0 OCDS schema
will trigger an error:

```
{
	"schemaType": "release-package",
	"version": "1.0.0",
	"extensions": [
        "https://raw.githubusercontent.com/open-contracting/ocds_bid_extension/v1.1/extension.json"
    ],
	"node":
	....
}

{
	"timestamp": 1503585358302,
	"status": 500,
	"error": "Internal Server Error",
	"exception": "java.lang.RuntimeException",
	"message": "Cannot apply extension https://raw.githubusercontent.com/open-contracting/ocds_bid_extension/v1.1/extension.json due to version incompatibilities. Extension meta is {\"name\":\"Bid statistics and details\",\"description\":\"Allowing bid statistics, and detailed bid information to be represented.\",\"compatibility\":\">=1.1.0\",\"dependencies\":[]} requested schema version 1.0.0",
	"path": "/api/ocds/validator/validateJsonInline"
}
```

