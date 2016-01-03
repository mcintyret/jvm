# JVM
My crack at a JVM implementation in Java.

Functionality supported so far:

* Synchronization & multithreading
* File I/O (including zip files, reading from stdin etc)
* Garbage collection

Still to come:

* Socket I/O
* JMX
* More reflection methods

The JVM is written in Java 8 but runs Java 7 code.

This JVM has only been confirmed to work when run by the Oracle HotSpot JVM bundled with JDK 8 on OSX. It is unlikely
to work on any other platform!

## Demo

To try the demo and see the JVM in action, take the following steps:

1. make sure you have the full Java 7 SDK downloaded and installed
2. set the JAVA_7_HOME environment variable to point to the Home directory of your Java 7 install
3. clone the repo
4. run mvn clean install in the root directory. This will build the core jar (as well as confirming that the JVM will run on your platform)
5. cd demo
6. mvn assembly:single - this will build the demo jar
7. cd target
8. unzip jvm-demo.zip
9. cd jvm-demo/jvm-cp
10. javac Demo.java - compile the demo code
11. cd ..
12. ./javaJvm Demo

The provided Demo.java uses a Scanner to read from System.in and prints the reversed input. You can write your own demo code,
just replace 'Demo' with 'YourClass' in the instructions above.

In a separate window you may wish to run tail -f jvm.log to see what the JVM is up to.

To vary the output in the log file, change the log level in lib/logback.xml

| Level | Output |
|------|----------|
| WARN | See when GC is running |
| INFO | See method invocations |
| DEBUG | See individual opcode executions |


Please get in touch if you discover any bugs or have any issues running the demo!

