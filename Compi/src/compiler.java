import java.io.IOException;

public class compiler{
    node p;
    varList pVar;

    public static void main(String[] args) throws IOException{

        lexicon lexicon = new lexicon();

        if(!lexicon.ErrorEncontrado && !lexicon.ErrorSemantico){
            System.out.println("");
            System.out.println("---------------------------------");
            System.out.println("EL ANALIZADOR LEXICO FINALIZO");
            System.out.println("---------------------------------");
            syntactic syntactic = new syntactic();
            if (!syntactic.ErrorEncontrado) {
                System.out.println("EL ANALIZADOR SINTACTICO FINALIZO");
                System.out.println("---------------------------------");
                System.out.println("");
            }
        }else{
            System.out.println("\nLA COMPILACION CONTIENE ERRORES SEMANTICOS");
        }
    }
}