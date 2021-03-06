/* simple java logger
 * Copyright (C) 2012 Krzysztof Mędrzycki
 * 
 * This file is part of simple java logger library.
 *
 * simple java logger is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with simple java logger. If not, see <http://www.gnu.org/licenses/>.
*/

package aiwenar.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Logger
{
  private File              file;
  private FileWriter        stream;
  private int               level;
  private long              tid;
  private Ident             ident;
  private SimpleDateFormat  date;
  private HashMap           threads;
  
  public  boolean logDate;
  
  public Logger ( String fname )
  {
    file = new File ( fname );
    try
    {
      file.createNewFile ();
      stream = new FileWriter ( file );
    }
    catch ( IOException e )
    {
      System.err.println ( "! failded to create logging file" );
      System.err.println ( e.toString () );
    }
    date = new SimpleDateFormat ();
    date.applyPattern ( "[k:m:s]");
    logDate = false;
    tid = Thread.currentThread ().getId ();
    threads = new HashMap ();
    ident = new Ident ( level );
    threads.put ( tid, ident );
  }
  
  public void setDatePattern ( String s )
  {
    date.applyPattern ( s );
  }
  
  public void enter ( String classname, String funcname )
  {
    switchThread ();
    try
    {
      if ( logDate ) stream.write ( date.format ( new Date () ) + ' ' );
      stream.write ( new StringBuilder ().append ( ident ).toString () );
      stream.write ( "entered " + classname + "." + funcname + '\n' );
      stream.flush ();
    }
    catch ( IOException e ) {}
    catch ( NullPointerException e ) {}
    ident.increment ( 2 );
  }
  
  public void message ( String msg )
  {
    switchThread ();
    try
    {
      if ( logDate ) stream.write ( date.format ( new Date () ) + ' ' );
      stream.write ( new StringBuilder ().append ( ident ).toString () );
      string ( "| " + msg );
      stream.flush ();
    }
    catch ( IOException e ) {}
  }
  
  public void message ( ILoggable msg )
  {
    switchThread ();
    try
    {
      if ( logDate ) stream.write ( date.format ( new Date () ) + ' ' );
      stream.write ( new StringBuilder ().append ( ident ).toString () );
      string ( "| " + msg.message () );
      stream.flush ();
    }
    catch ( IOException e ) {}
  }
  
  public void message ( ILoggable msgs[] )
  {
    switchThread ();
    try
    {
      if ( logDate ) stream.write ( date.format ( new Date () ) + ' ' );
      stream.write ( new StringBuilder ().append ( ident ).toString () );
      for ( int i=0 ; i<msgs.length ; ++i )
      {
        string ( "| " + msgs[i].message () );
        stream.append ( '\n' );
      }
      stream.flush ();
    }
    catch ( IOException e ) {}
  }
  
  public void throwing ( Throwable t )
  {
    switchThread ();
    try
    {
      if ( logDate ) stream.write ( date.format ( new Date () ) + ' ' );
      stream.write ( new StringBuilder ().append ( ident ).toString () );
      stream.write ( "throwed " + t.toString () + '\n' );
      stream.flush ();
    }
    catch ( IOException e ) {}
  }
  
  public void leave ()
  {
    switchThread ();
    ident.decrement ( 2 );
    try
    {
      if ( logDate )
      {
        stream.write ( date.format ( new Date () ) + ' ' );
        stream.write ( new StringBuilder ().append ( ident ).toString () );
        stream.write ( "leaved" );
      }
    }
    catch ( IOException e ) {}
  }
  
  // private
  
  private void switchThread ()
  {
    long id = Thread.currentThread ().getId ();
    if ( id != tid )
    {
      Ident ident = (Ident) threads.get ( id );
      if ( ident == null )
      {
        ident = new Ident ( 0 );
        threads.put ( id, ident );
      }
      tid = id;
      
      try
      {
        string ( "<thread: " + id + ">" );
      }
      catch ( IOException e )
      {
        throwing ( e );
      }
    }
  }
  
  private void string ( String msg ) throws IOException
  {
    String msgs[] = msg.split ( "\n" );
    stream.write ( msgs[0] + '\n' );
    for ( int i=1 ; i<msgs.length ; ++i )
      stream.write ( ' '*level + msgs[i] + '\n' );
  }
  
  private class Ident implements CharSequence
  {
    private int level;
    
    public Ident ( int nlevel )
    {
      level = nlevel;
    }
    
    public void increment ( int i )
    {
      level += i;
    }
    
    public void decrement ( int i )
    {
      level -= i;
      if ( level < 0 ) level = 0;
    }
    
    @Override
    public int length ()
    {
      return level;
    }

    @Override
    public char charAt ( int i )
    {
      return ' ';
    }

    @Override
    public CharSequence subSequence ( int b, int e )
    {
      if ( e > level ) e -= e-level;
      return new Ident ( e-b );
    }
  }
}
