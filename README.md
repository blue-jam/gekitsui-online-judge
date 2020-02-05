# gekitsui-online-judge

![build](https://github.com/blue-jam/gekitsui-online-judge/workflows/build/badge.svg)

Online Judge to challenge/hack wrong solutions. The Online Judge is working at https://gekitsui-online-judge.herokuapp.com

## How to run

### Prerequisites

You have to have the following software on your machine.

- PostgreSQL 10.10+
    - You have to create a `gekitsui` DB. You don't have to create any tables in the DB.
- RabbitMQ 3.6.10+
    - You have to create a queue with a name `gekitsui-queue` to run the judge worker.

You have to set the following properties on your machine:

- `spring.oauth2.client.registration.github.client-id` which is your client ID for GitHub
- `spring.oauth2.client.registration.github.client-secret` which is your client secret for GitHub
- `spring.datastore.username` which is a user name of your DB
- `spring.datastore.password` which is a password of your DB

Since this project uses the [Spring Boot Dev Tools](https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-devtools),
you can create `~/.config/spring-boot/spring-boot-devtools.yml` and set your properties in the file. Like:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: YOUR_CLIENT_ID
            client-secret: YOUR_CLIENT_SECRET
  datasource:
    username: YOUR_USER_NAME
    password: YOUR_PASSOWRD
```

### Web server

```shell script
gradle webapp:bootRun
```

### Judge worker

```shell script
gradle jusge:bootRun
```

## How to add a new problem

1. Add problem statement to [`webapp/src/main/resources/problem`](https://github.com/blue-jam/gekitsui-online-judge/tree/master/webapp/src/main/resources/problem)
1. Add a line to create/update a row of the problem to [`ProblemConfiguration.kt`](https://github.com/blue-jam/gekitsui-online-judge/blob/master/webapp/src/main/kotlin/bluejam/hobby/gekitsui/webapp/config/ProblemConfiguration.kt)
1. Add your wrong solution(s), correct solution, and testcase validator to [`judge` module](https://github.com/blue-jam/gekitsui-online-judge/tree/master/judge/src/main/kotlin/bluejam/hobby/gekitsui/judge/problem)
1. Register your test suite in [`TestSuiteMap.kt`](https://github.com/blue-jam/gekitsui-online-judge/blob/master/judge/src/main/kotlin/bluejam/hobby/gekitsui/judge/problem/TestSuiteMap.kt)
