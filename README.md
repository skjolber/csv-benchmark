# csv-benchmark
Project for benchmarking high-performance open source CSV parsers using [JMH].

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
  * GTFS StopTimes
   
Modes:

  * plain or quoted
  * rows in intervals from 1 to 1 million
  * single-shot and sample time
  * databinding or raw String parsing

# Results
From using JDK 8 build 232.

### Single-shot
What is measured is the __cold start__, which is how long the parsing take the first time. As much as possible, this includes things like class-loading and inital configuration / creation from a Reader.

The results [1](https://skjolber.github.io/csv-benchmark/single-shot-1/index.html), [100](https://skjolber.github.io/csv-benchmark/single-shot-100/index.html), [1000](https://skjolber.github.io/csv-benchmark/single-shot-1000/index.html), [100000](https://skjolber.github.io/csv-benchmark/single-shot-100000/index.html), [500000](https://skjolber.github.io/csv-benchmark/single-shot-500000/index.html), [1000000](https://skjolber.github.io/csv-benchmark/single-shot-1000000/index.html) must be taken with a grain of salt. Most of these will differ quite a lot from run to run, but the current results give a ballpark picture of the performance.

The results show that the [SimpleFlatMapper] beats [univocity-parsers] when file size increases. However [sesseltjonna-csv] performs better than both. 

When parsing a lot of different CSV file formats, libraries using the same parser will probably have an advantage on the JIT side as there will be fewer files to compile and thus more of the overall time will be spent running JIT-optimized code paths.

### Sample time
What is measured here is the __warm start__, which is how long the parsing takes after an initial warmup (i.e. for repeated use). The warmup here referes to the Just-in-time-compilator to do its optimizations, which takes place after something like a second of back to back invocation of the code. Basically these optimizations usually mean inlining methods and compiling the bytecode to platform-specific native code.

Below [1000]((https://skjolber.github.io/csv-benchmark/sample-time-1-100-1000/index.html)) rows takes at most 1.1 milliseconds for all libraries, 1 million rows takes less than a second: [10000-1000000](https://skjolber.github.io/csv-benchmark/sample-time/index.html). 

[SimpleFlatMapper] beats [sesseltjonna-csv] in one of the benchmarks, otherwise is it quite consistenly [sesseltjonna-csv] fastest, [SimpleFlatMapper] second and [univocity-parsers] third.

### Bootstrap time

Just a note on using this library to minimize startup time - your milage will vary in relation to the above benchmark because you also have to take into account the time loading the dependency libraries - indeed this is true for all the CSV parser libraries. 

For sesseltjonna-csv, when using explicit `setter`, it loads the byte-buddy library to identify the setter method, whereas the default is to use reflection.

If however the CSV format is written in stone, it is possible to add the dynamically generated parser as regular class during build-time. 

So as with all optimization, make sure to measure the startup time before and after modifications.

# Links
Alternative benchmarks

  * [csv-parsers-comparison](https://github.com/uniVocity/csv-parsers-comparison)
  * [simpleflatmapper benchmarks](http://simpleflatmapper.org/12-csv-performance.html)
 
# History

 - 1.0.2: Added String-array benchmarks, made single-shot benchmarks do most of the classloading.
 - 1.0.1: Bumped versions, refreshed results.
 - 1.0.0: Initial version

[Apache 2.0]:                  http://www.apache.org/licenses/LICENSE-2.0.html
[issue-tracker]:               https://github.com/skjolber/csv-benchmark/issues
[Gradle]:                      https://gradle.org/
[JMH]:                         http://openjdk.java.net/projects/code-tools/jmh/
[visualization]:               https://skjolber.github.io/csv-benchmark/jmh/index.html
[univocity-parsers]:           https://github.com/uniVocity/univocity-parsers
[SimpleFlatMapper]:            http://simpleflatmapper.org/
[sesseltjonna-csv]:            https://github.com/skjolber/sesseltjonna-csv

