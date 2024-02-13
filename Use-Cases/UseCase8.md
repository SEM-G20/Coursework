# USE CASE: 8 Produce a report on the population of the world, a continent, a region, a country, a district and a city.


## CHARACTERISTIC INFORMATION

### Goal in Context

An organisation has asked for the population of the world, a continent, a region, a country, a district and a city to be made available to them.

### Scope

Organisation

### Level

Primary

### Preconditions

Database contains data on population of all regions, cities, disctrics, countrys, continents

### Success End Condition

Report is produced with the population of the world, a continent, a region, a country, a district and a city.

### Failed End Condition

No report produced

### Primary Actor

Organisation

### Trigger

Organisation requests population information from world database and specifies a target

## MAIN SUCCESS SCENARIO


1. Organisation requests population information from world database

2. Taking Name of continent/region/country, total population of the continent/region/country, total population of the continent/region/country living in cities (including a %), total population of the continent/region/country not living in cities (including a %) from world database

3. Information sorted

4. Report produced

## EXTENSIONS

Enters a value not inside the database, error message returned

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 1.0
