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

Providing api for logging.

#### creation

`Logger` providing one constructor, that take `String` path as argument.

    Logger log = new Logger ( "path/to/your.log" );

#### functions

    Logger ( String path )

Construct Logger and opens file under path for logging.

    void enter ( Strign classname, String funcname )

Logs function entrance and increment identation level.

    void leave ()

Logs function leave. Decrease identation level.

    void message ( String msg )`
    void message ( ILoggable msg )
    void message ( ILoggable msg [] )

Adds message to log. Each line beggining with '| '

    void throwing ( Throwable t )

Log throwing of `Throwable` t.

### interface ILoggable

Allow class to be logged.