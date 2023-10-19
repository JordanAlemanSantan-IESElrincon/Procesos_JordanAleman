import java.io.IOException;
import java.util.Arrays;

public class ProcesosJavaJordan {
    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Debe indicarse comando a ejecutar: ");
            System.exit(1);
        }

        // Este comando deja preparado la posible ejecución de un proceso que le
        // pasaremos por agumento.
        // Por ejemplo, podemo pasarlo por argumento algo como
        // ("C:\\Windows\\System32\\cmd.exe", "/c", "dir"). Así, cuando se ejecute más
        // adelante por medio de otro método lo que hará será:
        // - ejecutar un comando de terminal
        // - la "/c" es para indicarle que vamos a introducir con comando de terminal
        // - "dir" para indicarle que vamos a hacer un recorrido de todos nuestros
        // directorios y ficheros del directorio actual donde nos encontremos
        ProcessBuilder pb = new ProcessBuilder(args);

        // Está indicando que deseas que el proceso herede los flujos estándar (IO) de
        // la aplicación actual en la que se está ejecutando el programa Java. Esto
        // significa que los flujos de entrada (System.in), salida (System.out), y error
        // (System.err) del proceso que se va a lanzar serán los mismos que los flujos
        // de la aplicación Java actual. En otras palabras, cualquier salida producida
        // por el proceso lanzado se mostrará en la consola actual, y cualquier entrada
        // del usuario se tomará de la consola actual.
        pb.inheritIO();

        int codRet = 0;

        try {

            Process p = pb.start();
            System.out.println("Yo debería aparecer siempre primero");

            // Con `waitFor()`, mientras el proceso esté en ejecución el resto del código
            // quedará en espera hasta que finalice dicho proceso
            codRet = p.waitFor();
            System.out.println("¿En qué momento aparezco?");

            System.out.println("La ejecución de " + Arrays.toString(args)
                    + " devuelve " + codRet
                    + " " + (codRet == 0 ? "(ejecución correcta)" : "(ERROR)"));
        } catch (IOException e) {
            System.err.println("Error durante ejecución del proceso");
            System.err.println("Información detallada");
            System.err.println("---------------------");
            e.printStackTrace();
            System.err.println("----------------------");
            System.exit(2);
        } catch (InterruptedException e) {
            System.err.println("Proceso interrumpido");
            System.exit(3);
        }
    }
    // public static void main(String[] args) {
    // System.out.println("Hola");
    // ProcessBuilder pb = new ProcessBuilder("C:\\Windows\\System32\\cmd.exe",
    // "/c", "dir", "src\\Tema1Procesos"); // Ejemplo: listar archivos en un
    // directorio
    // pb.inheritIO(); // Redirigir los flujos de entrada y salida

    // try {
    // Process process = pb.start(); // Iniciar el proceso
    // int exitCode = process.waitFor(); // Esperar a que el proceso termine
    // System.out.println("El proceso ha terminado con código de salida: " +
    // exitCode);
    // } catch (IOException | InterruptedException e) {
    // System.out.println("No funciono");
    // e.printStackTrace();
    // }
    // }
}
