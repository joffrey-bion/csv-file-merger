# CSV File Merger
[![Bintray](https://img.shields.io/bintray/v/joffrey-bion/applications/csv-file-merger.svg)](https://bintray.com/joffrey-bion/applications/csv-file-merger/_latestVersion)
[![Travis Build](https://img.shields.io/travis/joffrey-bion/csv-file-merger/master.svg)](https://travis-ci.org/joffrey-bion/csv-file-merger)
[![Dependency Status](https://www.versioneye.com/user/projects/56d2f55e157a6913c1e6c846/badge.svg)](https://www.versioneye.com/user/projects/56d2f55e157a6913c1e6c846)
[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/joffrey-bion/csv-file-merger/blob/master/LICENSE)

Merges together CSV files that have the same column headers.

## Download

You can find the executable Jar in the
[bintray repository](https://bintray.com/joffrey-bion/applications/csv-file-merger/_latestVersion).

## Usage

You can run the program in multiple ways, as it is a java program:

    java org.hildan.tools.csvmerger.CsvFileMerger [-o <dest>] <source1> [sources]*

Using the provided runnable JAR file:

    java -jar csv-file-merger-<version>.jar [-o <dest>] <source1> [sources]*

## License

Code released under [the MIT license](https://github.com/joffrey-bion/csv-file-merger/blob/master/LICENSE)