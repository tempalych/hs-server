# hs server
Server for HS demo project

## Installation
1. Provide env-file with variables
```
DB_USERNAME
DB_DATABASE_NAME
DB_PASSWORD
DB_SERVER_NAME
LIQUIBASE_URL
```

2. Update database structure with Liquibase:
```
$ cd liquibase
$ source path/to/env-file
$ sh liquibase.sh
```

3. Run application

## Usage
Local run
```
$ lein run
```

Tests:
```
$ lein test
```
Compile sources:
```
$ lein uberjar
```
... and run:
```
$ java -jar hs-0.1.0-standalone.jar
```

Docker build image:
```
$ docker build . -t hs-server
```
Docker run image:
```
$ docker run --env-file /path/to/env -p 8080:8080 hs-server -i
```