# How to check if app is up
- in browser: go to localhost:8080/health/ping and you should see pong
- in Postman: GET localhost:8080/health/ping

# How to process a file
- POST localhost:8080/extract/file + provide multipart request parameter named file (in Postman it's attached in body > form-data)
- for different output formats - CSV, JSON, PLAIN you can append ?outputFormat= with the selected format 

# Note: 
CSV format does not normally allow for arrays (needed to display list of adults in each household),
so here "CSV format" is divided into blocks by households:
- occupantsNumber, address, city, state
- adult1FirstName, adult1lastName, adult1Age
- adult2FirstName, adult2lastName, adult2Age
- empty line before second household block
- occupantsNumber, address, city, state
- adult1FirstName, adult1lastName, adult1Age
- adult2FirstName, adult2lastName, adult2Age
- and so on

# To download result as a file:
- in Postman use "Save response to file" option
- in curl use -o flag followed by filename

# Test coverage: 96% of all lines of code
(integration and unit tests cover all business logic)