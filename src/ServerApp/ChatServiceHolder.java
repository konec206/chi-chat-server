package ServerApp;

/**
* ServerApp/ChatServiceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Server.idl
* jeudi 22 mars 2018 09 h 04 CET
*/

public final class ChatServiceHolder implements org.omg.CORBA.portable.Streamable
{
  public ServerApp.ChatService value = null;

  public ChatServiceHolder ()
  {
  }

  public ChatServiceHolder (ServerApp.ChatService initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ServerApp.ChatServiceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ServerApp.ChatServiceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ServerApp.ChatServiceHelper.type ();
  }

}