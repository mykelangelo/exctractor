# How to check if app is up
- in browser: go to localhost:8080/health/ping and you should see pong
- in Postman: GET localhost:8080/health/ping

# How to process a file
- POST localhost:8080/extract/file + provide multipart request parameter named file (in Postman it's attached in body > form-data)