# csv-benchmark
Project for benchmarking popular open source CSV parsers using [JMH].

  * [SimpleFlatMapper]
  * [univocity-parsers]
  * [sesseltjonna-csv]

Bugs, feature suggestions and help requests can be filed with the [issue-tracker].
## License
[Apache 2.0]

# Obtain
The project is based on [Gradle].

# Usage
Modify the build version to your current snapshot, and potentially change the include expression in [jmh-benchmark/build.gradle](jmh-benchmark/build.gradle) and/or the [CsvParam](jmh-benchmark/src/jmh/java/com/github/skjolber/csv/param/CsvParam.java) to scope your tests.  

Then run 

```
./gradlew clean jmhClasses jmh --info
```

If the JMH plugin seems to have trouble refreshing the project, restart the Gradle deamon before running:

```
./gradlew --stop && ./gradlew clean jmhClasses jmh --info
```

And also optionally refresh the dependencies using

```
./gradlew --stop && ./gradlew clean jmhClasses jmh --refresh-dependencies --info
```

Open the file `./jmh-benchmark/build/reports/jmh/index.html` to view a visualization of the results.

# Benchmarks

Inputs:
  * World cities
  * GTFS Trips
   
Modes:

  * plain or quoted
  * rows from 1 to 1 million
  * single-shot and sample time   

# Results

  * Single-shot [1](https://skjolber.github.io/csv-benchmark/docs/single-shot-1/index.html), [100](https://skjolber.github.io/csv-benchmark/docs/single-shot-1/index.html), [1000](https://skjolber.github.io/csv-benchmark/docs/single-shot-1/index.html), [100000](https://skjolber.github.io/csv-benchmark/docs/single-shot-1/index.html), [500000](https://skjolber.github.io/csv-benchmark/docs/single-shot-1/index.html), [1000000](https://skjolber.github.io/csv-benchmark/docs/single-shot-1/index.html)
  * Sample time: 

# History

 - 1.0.0: Initial version

[Apache 2.0]:          		http://www.apache.org/licenses/LICENSE-2.0.html
[issue-tracker]:       		https://github.com/skjolber/csv-benchmark/issues
[Gradle]:              		https://gradle.org/
[JMH]:							http://openjdk.java.net/projects/code-tools/jmh/
[visualization]:				https://skjolber.github.io/csv-benchmark/jmh/index.html
[univocity-parsers]: 			https://github.com/uniVocity/univocity-parsers
[SimpleFlatMapper]: 			http://simpleflatmapper.org/
[sesseltjonna-csv]:			https://github.com/skjolber/sesseltjonna-csv

