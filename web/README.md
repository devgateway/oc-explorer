# dg-toolkit web module

This module provides REST endpoints for the services needed, as well as basic security. It depends on the **persistence** module.

It also provides full authentication and security using Spring Security.
The module is packaged as a jar and can be deployed as a [fat jar](http://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html).

# Building

The web module is part of its larger dg-toolkit parent build, so you need to build on the parent first.

# Starting the app

Because it gets packaged as a fat jar, starting it is piece of cake:

`java -jar target/web-0.0.1-SNAPSHOT.jar`

This will start everything, including an embedded Tomcat Web server and all the services attached it.



# Endpoints

## Access to raw OCDS data

### A word about format for filters

There are 4 main types of filters, all of them are multivalue
- year - this is just the year. Example: 2014
- bidTypeId - this is the _dd_ of the Bid Type object, all entities are available here `/api/ocds/bidType/all`. Example: 10
- procuringEntityId - this is the _id_ of the procuring entity, all procuring entities are available here `/api/ocds/organization/procuringEntity/all`
- bidSelectionMethod - this is the bid selection method text, since OCDS does not allow an ID here we cannot create one, so we'll just use the text, all values are available here `/api/ocds/bidSelectionMethod/all`


### Display all OCDS data, paginated

`/api/ocds/release/all?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]&year=[year1]&year=[year2]...&pageNumber=[pageNo]&pageSize=[pageSize]`

Example: `/api/ocds/release/all?procuringEntityId=Z002131&pageNumber=0&pageSize=10`

* pageNumber is the page number, zero indexed. if you omit it, it defaults to page=0.
* pageSize is the page size (in number of Release elements) - if you omit it it defaults to 100. The maximum value for size is 1000.`

### Show OCVN Release by projectId

`/api/ocds/release/budgetProjectId/[projectId]`
Example: `/api/ocds/release/budgetProjectId/41067`

### Show OCVN Release by planning bid no

`/api/ocds/release/planningBidNo/[planningBidNo]`
Example: `/api/ocds/release/planningBidNo/20100300191`


## Visualization Endpoints

### Visualization 1 - Cost Effectiveness

#### Endpoint 1 - Cost Effectiveness Award Amount

`/api/costEffectivenessAwardAmount?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`


#### Endpoint 2 - Cost Effectiveness Tender Amount

`/api/costEffectivenessTenderAmount?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`

### Visualization 2 - Planned Locations

`/api/costEffectivenessTenderAmount?procuringEntityId=[proc1]&procuringEntityId=[proc2]`


### Visualization 3 - Bidding Period

`/api/tenderBidPeriodPercentiles?year=[year1]&year=[year2]...&bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`

### Visualization 4 - Funding by Bid Type


#### Endpoint 1 - Tender Price By OCDS Types

`/api/tenderPriceByOcdsTypeYear?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`

#### Endpoint 2 - Tender Price By Vietnam Types

`/api/tenderPriceByVnTypeYear?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`


### Visualization 5 - Counts for Tenders, Awards, Bid Plans Per Year

#### Endpoint 1 - Count of Bid Plans Per Year

`/api/countBidPlansByYear?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`

#### Endpoint 2 - Count of Bid Plans Per Year

`/api/countTendersByYear?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`

#### Endpoint 3 - Count of Awards Per Year

`/api/countAwardsByYear?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`

### Visualization 6 - Timeline Chart

#### Endpoint 1 - Average Tender Period

`/api/averageTenderPeriod?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`

#### Endpoint 2 - Average Award Period

`/api/averageAwardPeriod?bidTypeId=[bid1]&bidTypeId=[bid2]...&procuringEntityId=[proc1]&procuringEntityId=[proc2]....&bidSelectionMethod=[bidSel1]&bidSelectionMethod=[bidSel2]`
