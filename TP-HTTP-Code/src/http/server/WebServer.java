///A Simple Web Server (WebServer.java)

package http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Web server class
 *  @author Lucas Tissier, Louis Schibler
 * */
public class WebServer {

  /**
   * Start the server, read header and select the http method required
   *
   */
  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port 80");
    System.out.println("(press ctrl-c to exit)");
    try {
      s = new ServerSocket(3000);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        Socket remote = s.accept();
        System.out.println("Connection, sending data.");
        BufferedOutputStream out = new BufferedOutputStream(remote.getOutputStream());
        BufferedInputStream in = new BufferedInputStream(remote.getInputStream());

        // Read header which is ended with a blank line
        String header = new String();

        int bcur = '\0', bprec = '\0';
        boolean newline = false;
        while((bcur = in.read()) != -1 && !(newline && bprec == '\r' && bcur == '\n')) {
          if(bprec == '\r' && bcur == '\n') {
            newline = true;
          } else if(!(bprec == '\n' && bcur == '\r')) {
            newline = false;
          }
          bprec = bcur;
          header += (char) bcur;
        }

        System.out.println(header);

        if(!header.isEmpty()){
          header = header.replaceFirst("/","%20");
          String[] parameters = header.split(("%20"));
          String requestType = parameters[0].strip();
          String path = parameters[1];

          System.out.println("type : "+requestType);
          System.out.println("filepath : "+ path);

          if(requestType.equals("GET"))
            httpGET(out, path);
          else if(requestType.equals("POST"))
            httpPOST(in,out,path);
          else if(requestType.equals("PUT"))
            httpPUT(in,out,path);
          else if(requestType.equals("HEAD"))
            httpHEAD(out,path);
          else if(requestType.equals("DELETE"))
            httpDELETE(out,path);
        }


        out.flush();
        remote.close();
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }

  /**
   * Http GET method
   *
   * @param out Output stream (server response)
   * @param filename
   *            Command line parameters are not used.
   */
  public void httpGET(BufferedOutputStream out, String filename) {
    File file = new File(filename);
    try {
      if (!file.exists()) {
        file = new File("notfound.html");
        out.write(createHeader("404 ressource not found",file.getName(),file.length()).getBytes()); // the file does not exists
      } else {
        out.write(createHeader("200 OK", filename, file.length()).getBytes());
      }
      BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file));

      byte[] buffer = new byte[256];
      int nbRead;
      while((nbRead = fileIn.read(buffer)) != -1) {
        out.write(buffer, 0, nbRead);
      }
      out.flush();
      fileIn.close();

    } catch (IOException e) {
      e.printStackTrace();

      try{
        out.write(createHeader("500 Internal Server Error").getBytes());
        out.flush();
      } catch (Exception e2) {System.out.println("internal error");};
    }

  }

  /**
   * Http POST method for writing into a specified file
   *
   * @param in Input stream from request
   * @param out Output stream (server response)
   * @param filename Filepath of the file to modify
   */
  protected void httpPOST(BufferedInputStream in, BufferedOutputStream out, String filename) {
    System.out.println("POST " + filename);
    try {
      File resource = new File(filename);
      boolean existed = resource.exists();

      BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(resource, existed));

      byte[] buffer = new byte[256];
      while(in.available() > 0) {
        int nbRead = in.read(buffer);
        fileOut.write(buffer, 0, nbRead);
      }
      fileOut.flush();

      fileOut.close();

      if(existed) {
        out.write(createHeader("200 OK").getBytes());
      } else {
        out.write(createHeader("201 Created").getBytes());
      }
      out.flush();
    } catch (Exception e) {
      System.out.println("erreur envoi donnés");
      e.printStackTrace();
      try {
        out.write(createHeader("500 Internal Server Error").getBytes());
        out.flush();
      } catch (Exception e2) {System.out.println("internal error");};
    }
  }

  /**
   * Start the application.
   *
   * @param in Input stream from request
   * @param out Output stream (server response)
   * @param filename  Filepath of the file to add.
   */
  protected void httpPUT(BufferedInputStream in, BufferedOutputStream out, String filename){

    try{
      File newFile = new File(filename);
      boolean exist = newFile.exists();

      PrintWriter pw = new PrintWriter(newFile);
      pw.close();

      BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(newFile));

      byte[] buffer = new byte[256];
      while(in.available() >0) {
        int nbRead = in.read(buffer);
        fileOut.write(buffer,0,nbRead);
      }
      fileOut.flush();
      fileOut.close();

      if(!exist) {
        out.write(createHeader("200 Created").getBytes());
      } else {
        out.write(createHeader("204 Existed").getBytes());
      }

      out.flush();

    } catch (Exception e) {
      e.printStackTrace();
      try{
        out.write(createHeader("500 Server Internal Error").getBytes());
        out.flush();
      } catch(Exception e2){};
    }
  }

  /**
   * Start the application.
   *
   * @param out Output stream (server response)
   * @param filename Filepath of the file to modify
   */
  protected void httpHEAD(BufferedOutputStream out, String filename) {
    System.out.println("HEAD " + filename);
    try {
      File resource = new File(filename);
      if(resource.exists() && resource.isFile()) {
        out.write(createHeader("200 OK", filename, resource.length()).getBytes());
      } else {
        out.write(createHeader("404 Not Found").getBytes());
      }
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
      try {
        out.write(createHeader("500 Internal Server Error").getBytes());
        out.flush();
      } catch (Exception e2) {System.out.println("internal error");};
    }
  }

  /**
   * Start the application.
   *
   * @param out Output stream (server response)
   * @param filename Filepath of the file to delete
   */
  protected void httpDELETE(BufferedOutputStream out, String filename) {
    System.out.println("DELETE " + filename);
    try {
      File resource = new File(filename);
      boolean deleted = false;
      boolean existed = false;
      if((existed = resource.exists()) && resource.isFile()) {
        deleted = resource.delete();
      }

      if(deleted) {
        out.write(createHeader("204 No Content").getBytes());
      } else if (!existed) {
        out.write(createHeader("404 Not Found").getBytes());
      } else {
        out.write(createHeader("403 Forbidden").getBytes());
      }
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
      try {
        out.write(createHeader("500 Internal Server Error").getBytes());
        out.flush();
      } catch (Exception e2) {System.out.println("internal error");};
    }
  }

  /**
   * Create header for server response
   *
   * @param status status from server's response

   */
  protected String createHeader(String status) {
    String header = "HTTP/1.0 " + status + "\r\n";
    header += "Server: Bot\r\n";
    header += "\r\n";
    System.out.println("ANSWER HEADER :");
    System.out.println(header);
    return header;
  }

  /**
   * Create header for server response
   *
   * @param status status from server's response
   * @param filename Filepath to file
   * @param length File length
   */
  private String createHeader(String status, String filename, long length) {
    String header = "HTTP/1.0 " + status +"\r\n";
    if(filename.endsWith(".html")){
      header +="Content-Type: text/html\r\n";
    } else if(filename.endsWith(".png"))
      header +="Content-Type: image/png\r\n";
    else if(filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
      header +="Content-Type: image/jpeg\r\n";
    else if(filename.endsWith(".mp4"))
      header +="Content-Type: video/mp4\r\n";
    else if(filename.endsWith(".pdf"))
      header +="Content-Type: application/pdf\r\n";
    else if(filename.endsWith(".mp3"))
      header +="Content-Type: audio/mp3\r\n";
    else if(filename.endsWith(".css"))
      header +="Content-Type: text/css\r\n";

    header += "Content-Length: " + length +"\r\n";
    header += "Server: Bot\r\n";
    header +="\r\n";
    return header;
  }

  /**
   * Start the application.
   * 
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
