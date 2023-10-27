import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ProcesosJavaJordan {

    static void ejecutarComando(String[] comando) throws IOException, InterruptedException {
        // Este comando deja preparado la posible ejecución de un proceso que le
        // pasaremos por agumento.
        // Por ejemplo, podemo pasarlo por argumento algo como
        // ("C:\\Windows\\System32\\cmd.exe", "/c", "dir"). Así, cuando se ejecute más
        // adelante por medio de otro método lo que hará será:
        // - ejecutar un comando de terminal
        // - la "/c" es para indicarle que vamos a introducir con comando de terminal
        // - "dir" para indicarle que vamos a hacer un recorrido de todos nuestros
        // directorios y ficheros del directorio actual donde nos encontremos
        ProcessBuilder pb = new ProcessBuilder(comando);

        // Está indicando que deseas que el proceso herede los flujos estándar (IO) de
        // la aplicación actual en la que se está ejecutando el programa Java. Esto
        // significa que los flujos de entrada (System.in), salida (System.out), y error
        // (System.err) del proceso que se va a lanzar serán los mismos que los flujos
        // de la aplicación Java actual. En otras palabras, cualquier salida producida
        // por el proceso lanzado se mostrará en la consola actual, y cualquier entrada
        // del usuario se tomará de la consola actual.
        pb.inheritIO();

        // Código de comprobación
        int codVal = 0;

        Process proceso = pb.start();
        System.out.println("Yo debería aparecer siempre primero");
        // System.out.println(leerProceso(proceso));

        // Con `waitFor()`, mientras el proceso esté en ejecución el resto del código
        // quedará en espera hasta que finalice dicho proceso
        codVal = proceso.waitFor();
        System.out.println("¿En qué momento aparezco?");

        System.out.println("La ejecución de " + Arrays.toString(comando)
                + " devuelve " + codVal
                + " " + (codVal == 0 ? "(ejecución correcta)" : "(ERROR)"));

    }

    // Se le pasa por parámetro el comando y los argumentos y el método crea un
    // proceso que ejecuta dicho comando con sus argumentos. Cada x segundos
    // comprueba si el proceso ha finalizado y muestra el mensaje "Esperando..." en
    // cada comprobación.
    static void ejecutarComandoyComprueba(String[] comando) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.inheritIO();

        int tiempoEjecucion = 0;

        Process process = pb.start();
        boolean isDone = false;

        while (!isDone) {
            // process.waitFor(tiempoEjecucion, TimeUnit.SECONDS) devuelve un booleano
            // indicando si el proceso sigue en ejecución o ha finalizado.
            // Primer parámetro: Cantidad de tiempo `5`
            // Segundo parámetro: Unidad de tiempo `segundos`
            // Es decir, cada 5 segundos va a comprobar si el proceso sigue en ejecución o
            // ha finalizado. Devolverá un true si ha finalizado
            isDone = process.waitFor(5, TimeUnit.SECONDS);

            // Cuando el proceso ha finalizado le indico un break para que no se ejecute las
            // siguiente líneas de comandos, estas del System.out.println...
            if (isDone)
                break;

            // Si pusiéramos como ejemplo a ejecutar el proceso "mspaint.exe" para abrirme
            // un paint, por la terminal me aparecerá cada 5 segundos el mensajito indicado
            // en ese System.out.println hasta que finalice este proceso del paint (por
            // ejemplo, cerrándolo directamente)
            tiempoEjecucion += 5;
            System.out.println("Han pasado " + tiempoEjecucion + " segundos con el proceso en ejecución.");
        }

        process.waitFor();
    }

    // Se le pasa por parámetro el comando y los argumentos y el método crea un
    // proceso que ejecuta dicho comando con sus argumentos. Espera a que finalice
    // su ejecución durante 5 segundos. En caso de que no lo haga, destruye el
    // proceso hijo y finaliza su ejecución.se le pasa por parámetro el comando y
    // los argumentos y el método crea un proceso que ejecuta dicho comando con sus
    // argumentos. Espera a que finalice su ejecución durante 5 segundos. En caso de
    // que no lo haga, destruye el proceso hijo y finaliza su ejecución.
    static void ejecutarComandoyEspera(String[] comando) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.inheritIO();

        Process process = pb.start();

        // Se comprobará si el proceso aún no ha finalizado en 5 segundos. Si el proceso
        // sigue funcionando tras 5 segundos, lo destruimos e indicamos al usuario por
        // la terminal que el proceso ha sido destruido
        if (!process.waitFor(5, TimeUnit.SECONDS)) {
            process.destroy();
            System.out.println("Han pasado 5 segundos. Proceso destruido");
        } 
    }

    // Se le pasa por parámetro el comando y los argumentos, junto al directorio de
    // ejecución. El método debe ejecutar dicho comando en dicho directorio. La
    // salida debe demostrar que se ha realizado la ejecución en el directorio.
    static void ejecutarComandoyDirectorio(String[] comando) throws IOException, InterruptedException {
        File directorioEjecucion = new File(comando[comando.length - 1]);
        System.out.println(directorioEjecucion.getPath());

        String[] arrayComandos = Arrays.copyOfRange(comando, 0, (comando.length - 1));

        ProcessBuilder processBuilder = new ProcessBuilder(arrayComandos);

        processBuilder.directory(directorioEjecucion);
        processBuilder.inheritIO();

        Process proceso = processBuilder.start();

        // Esperar a que el proceso termine
        int estado = proceso.waitFor();

        if (estado == 0)
            System.out.println(
                    "Comando ejecutado exitosamente en el directorio: " + directorioEjecucion.getAbsolutePath());
        else
            System.out.println(
                    "Error al ejecutar el comando en el directorio: " + directorioEjecucion.getAbsolutePath());
    }

    // Este método debe ejecutar un proceso que busque mediante el comando "grep" (o
    // "find" en Windows) el texto pasado por parámetro en el primer fichero pasado
    // por parámetro (hay que redirigir la entrada estándar del proceso hijo a ese
    // fichero). La salida que genera el comando grep/find se debe redirigir al
    // segundo fichero pasado por parámetro.
    static void buscayGuarda(String[] comando) throws IOException, InterruptedException {
        // Comando debe de tener tres argumentos: [texto_a_buscar], [archivo_entrada], [archivo_salida]

        File archivoEntrada = new File(comando[1]);
        if (!archivoEntrada.exists() || !archivoEntrada.isFile()) {
            System.out.println("El archivo de entrada no existe: " + archivoEntrada.getAbsolutePath());
            System.exit(0);
        }

        // Archivo donde introduciremos el resultado de la búsqueda.
        File archivoSalida = new File(comando[2]);

        String[] arrayComandos = { "findstr", comando[0], comando[1] };

        ProcessBuilder processBuilder = new ProcessBuilder(arrayComandos);
        processBuilder.redirectOutput(archivoSalida);

        int estadoProceso;

        Process proceso = processBuilder.start();
        estadoProceso = proceso.waitFor();

        if (estadoProceso == 0)
            System.out.println("El proceso de ha ejecutado con éxito.");
        else
            System.err.println("Hubo un error con el proceso.");
    }

    static void help() {
        System.out.println("Guía de comandos: " +
                "\n1: ejecutarComando -> \t\t\t(Ejemplo: 1 cmd.exe /c dir)" +
                "\n2: ejecutarComandoyComprueba -> \t(Ejemplo: 2 mspaint.exe)" +
                "\n3: ejecutarComandoyEspera -> \t\t(Ejemplo: 3 mspaint.exe)" +
                "\n4: ejecutarComandoyDirectorio -> \t(Ejemplo: 4 cmd.exe /c dir \"C:\\\")" +
                "\n5: buscayGuarda -> \t\t\t(Ejemplo: 5 \"texto_a_buscar\" \"..ruta\\archivo_entrada.txt\" \"..ruta\\archivo_salida.txt\"");
    }

    private static String[] extraerRango(String[] comando) {
        return Arrays.copyOfRange(comando, 1, comando.length);
    }

    public static void main(String[] args) {
        if (args.length <= 1) {
            help();
            System.exit(1);
        }

        try {
            switch (args[0]) {
                case "1" -> ejecutarComando(extraerRango(args));
                case "2" -> ejecutarComandoyComprueba(extraerRango(args));
                case "3" -> ejecutarComandoyEspera(extraerRango(args));
                case "4" -> ejecutarComandoyDirectorio(extraerRango(args));
                case "5" -> buscayGuarda(extraerRango(args));
                default -> help();
            }

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
    // "C:\\Windows\\System32\\cmd.exe", "/c", "dir"
    // "C:\\Users\\Jordan\\Documents\\\\2_DAM\\\\PGV-ProgramacionServiciosProcesos(JuanCastro)\\\\ProyectoPGVGit\\\\Procesos_JordanAleman"

    // }
}
