# USE CASE: 6 Produce a report on N capital cities in the world, continent, region, country and district where N is provided.


## CHARACTERISTIC INFORMATION

### Goal in Context

An organisation has asked for reports to be generated on the top N most populated capital cities in the world, continent, region, country and district where N is provided by the user.

### Scope

Organisation

### Level

Primary

### Preconditions

Database contains data on all cities population and capital cities are specified

### Success End Condition

Report is produced with the top N most populated capital cities in the world, continent, region, country and district

### Failed End Condition

No report produced

### Primary Actor

Organisation

### Trigger

Organisation requests capital city population information from world database

## MAIN SUCCESS SCENARIO


1. Organisation requests country population information from world database and specifies how many capital cities

2. Taking Name, Country, District and Population from database

3. Information sorted

4. Report produced

## EXTENSIONS

Enters a non integer value, error message returned

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 1.0
