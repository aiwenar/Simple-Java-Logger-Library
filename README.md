Simple Java Logging Library
===========================

Tiny and simple alternative for `java.util.logging`.

Usage
-----

### example

    Logger log = new Logger ( "file.log" );
    ...
    log.enter ( "MyClass", "myMethod" );
    log.message ( "hello log!" );
    log.leave ();

### Logger class

Provides api for logging.

#### creation

`Logger` contains one constructor that takes `String` path as argument.

    Logger log = new Logger ( "path/to/your.log" );

#### functions

    Logger ( String path )

Constructs Logger and opens file under path for logging.

    void enter ( Strign classname, String funcname )

Logs function entrance and increment identation level.

    void leave ()

Logs function leave. Decrease identation level.

    void message ( String msg )`
    void message ( ILoggable msg )
    void message ( ILoggable msg [] )

Adds message to log.

    void throwing ( Throwable t )

Logs throwing of `Throwable` t.

#### Logging with date

Logger support logging with date, to enable it set `Logger.logDate` to true.
You can change date format using `void setDatePattern ( String )` method, that takes
`SimpleDateFormat` pattern as argument.

### interface ILoggable

Allow class to be logged by implementing `String message ()` method.