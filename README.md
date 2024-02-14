# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]([https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSgM536HHCkARYGGABBECE5cAJsOAAjYBxQxp8zJgDmUCAFdsMAMRpgVAJ4wASik1IOYKMKQQ0RgO4ALJGBSZEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAel1lKAAdNABvLMoTAFsUABoYXA4ON2hpSpQS4CQEAF9MCnDYELC2SQ4oqBs7HygACiKoUoqqpVr6xubWgEpO1nYOGF6hEXEBwZhNFDAAVWzJ7Jm13bEJKW3QtSiyAFEAGVe4JJgpmZgAGY6Eq-bKYW77B6BXpdfqcKJoXQIBDraibR4wCH3GpREDDYQoc6US7FYBlSrVBZQaQ3OSQmoY54wACSADk3pYfn8ybNKXVqUsWggWaykvFQbRYeidnTsYc8SgCaJdGAPCTpjzaXs5Yz5FE2RyuVceZVgCqPEkIABrdAisUwM2q8Gyg7bGEbAZRR0W63oVFwrbQ0KwyIO82Wm1of2UN2hfzoMBRABMAAYU3l8t6I+gOuhpBptHoDIZoPxjjB3hBbK5DJ5vL540Fg6xQ3FEqkMsoas40BnueS5jV+dIOiGes20Z6YAgq0g0OqZhT5sO1lKBhisQcosczhd+ygtXdXb0mW9Pt8JRqyoDgZfndrj+7J-C72vOBuXVJcfifMrVQvNXvI8oSePURUNS9FzDVVs1cNl7W9ID6UDJ8Ay9cNfSjN8UInGgoHQmDMOjHpekbRMYFTdMCizTDczQfMtB0fQjG0FBbUrTQ9GYWsvB8PxkATDExyiaIBA+V4kleNJ0i7DgezyGjI2IoSPRfGdOJVcZFPQVdVMDUJNy-I4Tj-H1Iy0jDI0PZDdRecSL201wgQgEEpiQnUgz6TYCLMv1sNjPpQ0c5TPLI5M00wPMCyY4thmkCtRhgABxHkth4+t+ICZhUNbJKJOkzQeQUyy-JbccvKnZB7BSsoOAswirP9aUDM-HFjLAUzYPq3y0GsjzQLs88fkcm8XOgjx3MfXDvKOHl3lGJr1084TZrKeb7BC0iBIwcKqPyQq1tGOiGMLZjDGwXQoGwBAVDgH8VBqlR0r4siVO6ESEmSAqipMErewKA6UFZHlRzKt60JgBUCUe7rYMqQHgbKXTn30zFWsOHdOsw2HML648BvIezhr+0aQUQ-yZQfIyoZ8GGEcAwyGRPMCDVeTlVqBnk7XFQHJqhVCZsB5kBEW99lrK7ceWFza4228jKIzIWBGO6KiyMMwUGRCA3BgAApCA52SrnDHkBBQCtTLBJy-CYliU4O3SQHioa9AMzIuAIBnKB4al5WQumqcACsDbQGHHMqd3PegH2ymF5GAw-Km2sxv6ccaxnUdPInxrhyCuecsnzVF1GM+3E4Ycjr3Kn3GOUDjvmmYJ1n2f3bmOZgeJWXgOWPa9hucIql8leLgKVuHscArCiiIqixi1cMbRgAsRBFVgYBsGuwgnBcdxeIbOXwdbMTPkk6S1H9wf5RuvBxnj5q0aTq-V6JCY8ZA1QwOPiTXgdZEYAgAEr49KJ2Am1EA18oCmVvv3WymJxKSV-sKAB40R6U1AU-PAMM36Nw-lEL+CDgB-2QbzDOo89IIiRCiCm1sKHIhlt3LKu1Ir0UwEAA)https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSgM536HHCkARYGGABBECE5cAJsOAAjYBxQxp8zJgDmUCAFdsMAMRpgVAJ4wASik1IOYKMKQQ0RgO4ALJGBSZEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAel1lKAAdNABvLMoTAFsUABoYXA4ON2hpSpQS4CQEAF9MCnDYELC2SQ4oqBs7HygACiKoUoqqpVr6xubWgEpO1nYOGF6hEXEBwZhNFDAAVWzJ7Jm13bEJKW3QtSiyAFEAGVe4JJgpmZgAGY6Eq-bKYW77B6BXpdfqcKJoXQIBDraibR4wCH3GpREDDYQoc6US7FYBlSrVBZQaQ3OSQmoY54wACSADk3pYfn8ybNKXVqUsWggWaykvFQbRYeidnTsYc8SgCaJdGAPCTpjzaXs5Yz5FE2RyuVceZVgCqPEkIABrdAisUwM2q8Gyg7bGEbAZRR0W63oVFwrbQ0KwyIO82Wm1of2UN2hfzoMBRABMAAYU3l8t6I+gOuhpBptHoDIZoPxjjB3hBbK5DJ5vL540Fg6xQ3FEqkMsoas40BnueS5jV+dIOiGes20Z6YAgq0g0OqZhT5sO1lKBhisQcosczhd+ygtXdXb0mW9Pt8JRqyoDgZfndrj+7J-C72vOBuXVJcfifMrVQvNXvI8oSePURUNS9FzDVVs1cNl7W9ID6UDJ8Ay9cNfSjN8UInGgoHQmDMOjHpekbRMYFTdMCizTDczQfMtB0fQjG0FBbUrTQ9GYWsvB8PxkATDExyiaIBA+V4kleNJ0i7DgezyGjI2IoSPRfGdOJVcZFPQVdVMDUJNy-I4Tj-H1Iy0jDI0PZDdRecSL201wgQgEEpiQnUgz6TYCLMv1sNjPpQ0c5TPLI5M00wPMCyY4thmkCtRhgABxHkth4+t+ICZhUNbJKJOkzQeQUyy-JbccvKnZB7BSsoOAswirP9aUDM-HFjLAUzYPq3y0GsjzQLs88fkcm8XOgjx3MfXDvKOHl3lGJr1084TZrKeb7BC0iBIwcKqPyQq1tGOiGMLZjDGwXQoGwBAVDgH8VBqlR0r4siVO6ESEmSAqipMErewKA6UFZHlRzKt60JgBUCUe7rYMqQHgbKXTn30zFWsOHdOsw2HML648BvIezhr+0aQUQ-yZQfIyoZ8GGEcAwyGRPMCDVeTlVqBnk7XFQHJqhVCZsB5kBEW99lrK7ceWFza4228jKIzIWBGO6KiyMMwUGRCA3BgAApCA52SrnDHkBBQCtTLBJy-CYliU4O3SQHioa9AMzIuAIBnKB4al5WQumqcACsDbQGHHMqd3PegH2ymF5GAw-Km2sxv6ccaxnUdPInxrhyCuecsnzVF1GM+3E4Ycjr3Kn3GOUDjvmmYJ1n2f3bmOZgeJWXgOWPa9hucIql8leLgKVuHscArCiiIqixi1cMbRgAsRBFVgYBsGuwgnBcdxeIbOXwdbMTPkk6S1H9wf5RuvBxnj5q0aTq-V6JCY8ZA1QwOPiTXgdZEYAgAEr49KJ2Am1EA18oCmVvv3WymJxKSV-sKAB40R6U1AU-PAMM36Nw-lEL+CDgB-2QbzDOo89IIiRCiCm1sKHIhlt3LKu1Ir0UwEAA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
