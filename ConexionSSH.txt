import com.jcraft.jsch.*;
//TOMADO DE http://fuenteabierta.teubi.co/2013/02/estableciendo-conexiones-ssh-en-java-y.html
public class ConexionSSH {

  private JSch jsch;

  public ConexionSSH() {
    jsch = new JSch();
    // Es necesario capturar JSchException
    try {

      // NO realizar revision estricta de llaves
      JSch.setConfig("StrictHostKeyChecking", "no");

      // Creamos la nueva sesion SSH
      Session sesion = jsch.getSession("ANDII","host");

      // Establecemos la clave
      sesion.setPassword("ANDII117");
      
      // Conectamos la sesion
      sesion.connect();
      // Obtenemos un nuevo canal para enviar/recibir comandos
      // de consola
      ChannelShell consola = (ChannelShell) sesion.openChannel("shell");
      // Utilizamos la entrada y salida estándar del sistema
      // para recibir comandos y desplegar el resultado
      consola.setInputStream(System.in);
      consola.setOutputStream(System.out);
   // Conectamos nuestro canal
      consola.connect();

    } catch(JSchException e) {
      System.out.println("Error de JSCH. Mensaje: "+e.getMessage());
    }
  }

  public static void main(String args[]) {
    new ConexionSSH();
  }
}