# Java Marking System

[![Tests](https://github.com/Anish25012/Java-marking-system/actions/workflows/tests.yml/badge.svg)](https://github.com/Anish25012/Java-marking-system/actions/workflows/tests.yml)

An automated marker for student Java coursework, built as a final year project at the University of Kent. It takes a student's ZIP, runs their code against the assignment's tests, and produces a mark out of 100, a PDF of feedback and a row in an Excel results sheet. Each submission takes under a minute.

## How it works

1. **Check and extract.** `compilation` finds the ZIP, rejects submissions in the wrong format, asks for the student's login and extracts the code.
2. **Test.** `projectTesting` runs JUnit tests against the extracted code. Each failed test deducts marks and records why.
3. **Report.** iText writes per-student PDF feedback, and Apache POI adds the marks to `studentMarks.xlsx`, creating it if it doesn't exist.

## What's in here

| Path | What it is |
| --- | --- |
| `src/main/java/compilation.java` | Checks and extracts a submission |
| `src/test/java/CompilationTest.java` | Tests for that step, run on every push |
| `harness/projectTesting.java` | The marking tests, plus PDF and Excel output. It's built for one assignment, a text adventure game, so it only compiles alongside that assignment's code. |
| `samples/homeSolution.zip` | A sample submission of that assignment |

## Run it

You'll need Java 21 and Maven.

```bash
mvn test
```

To mark a submission, put its ZIP in `comp6000 submission/src`, run `compilation` and enter the student's login, then run `projectTesting` alongside the extracted code.

No student data is kept in this repository.
