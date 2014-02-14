titan-toy
=========


##Goal

To play with titan.

##Setup
- titan-all-0.4.2
- jdk7
- elasticsearch 0.90.10
- cassandra 2.0.3


##Tips
 - switch between jdk6 and jdk7 @macos:

> export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)
export JAVA_HOME=$(/usr/libexec/java_home -v 1.6)

 - When switch between cassandra 2.0.3 and 1.2.2
Do not forget to delete saved_caches_directory and data_file_directories
