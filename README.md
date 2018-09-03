# csv-benchmark
Project for benchmarking popular open source CSV parsers using [JMH].

Bugs, feature suggestions and help requests can be filed with the [issue-tracker].
## License
[Apache 2.0]

# Obtain
The project is based on [Gradle].

# Downlaod resources

> wget http://www.maxmind.com/download/worldcities/worldcitiespop.txt.gz
> gunzip worldcitiespop.txt.gz

# Usage
Modify the build version to your current snapshot, then run 

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

## Benchmarks

  * Load GTFS feed (from ZIP archive) - single shot

# History

 - 1.0.0: Initial version

[Apache 2.0]:          			http://www.apache.org/licenses/LICENSE-2.0.html
[issue-tracker]:       			https://github.com/skjolber/otp-benchmark/issues
[Gradle]:              		 	https://gradle.org/
[JMH]:							http://openjdk.java.net/projects/code-tools/jmh/
[visualization]:				https://skjolber.github.io/otp-benchmark/jmh/index.html
[Open Trip Planner]:			https://github.com/opentripplanner/OpenTripPlanner

