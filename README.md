# Cinema app
## Build app
In project root directory
```bash 
./gradlew build 
```
## Start application in docker:
In project root directory
```bash
docker-compose -up
```
## List of endpoints
Don't forget to replace variables in curly braces {{variable}} with your values

- **Link to swagger**

Detailed list of endpoints. Available after start of application.
```http request
http://localhost:8080/cinema/swagger-ui.html
```

- **Get all available cinema halls** 
```bash
curl --location --request GET '{{host}}:8080/cinema/hall'
```
- **Get all seats cinema halls** 
```bash
curl --location --request GET '{{host}}:8080/cinema/hall/{{hallId}}/seats'
``` 
- **Reserve seats by ids** 
```bash
curl --location --request POST '{{host}}:8080/cinema/seats' \
--header 'Content-Type: application/json' \
--data-raw '[
    {{id1}}, {{id2}}
]'
``` 
