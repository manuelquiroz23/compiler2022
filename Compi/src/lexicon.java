import java.io.RandomAccessFile;
import java.util.Stack;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class lexicon {
    Stack<String>  pilaAuxiliar = new Stack<String>();  
    Stack<varList> pilaVariables = new Stack<varList>();  
    Stack<Integer> pilaPostfijo = new Stack<Integer>();  
    Stack<Integer> pilaOperadores = new Stack<Integer>(); 
    Stack<polish>  ListaPolish = new Stack<polish>();  
    Stack<Integer> DupPost = new Stack<Integer>(); 

    public static node cabeza = null, p;
    int estado = 0, columnaumna, valorTM, linea = 1, character = 0;
    String lexeme = "";

    public static varList ruta = null, pVar;
    int tipo;
    String identificador;

    String token, DirSalto = "";
    String Salto = "";
    boolean EscribirSaltoDIRA = false;
    boolean EscribirSaltoDIRB = false;
    boolean EscribirSaltoDIRC = false;
    boolean EscribirSaltoDIRD = false;
    boolean AmbosSaltos   = false;
    boolean EscribirBRFA = false;
    boolean EscribirBRIB = false;
    boolean EscribirBRFC = false;
    boolean EscribirBRID = false;
    
    int brfA, briB, brfC, briD = 0;

    boolean ErrorSemantico = false;
    boolean ErrorEncontrado = false;
    boolean ValidarDeclaracion = false;

    String code = "/Users/manysystems/Desktop/Compi/codigo.txt";
    String rutaASM = "/Users/manysystems/Desktop/Compi/ensamblador";
    String Archivo = "compilador.asm";
    String contenidoASM = ";DIRECTIVAS de ensmablador por acuerdo se escribiran en MAYUSCULAS y en la primera columnaumnaa de la izquierda\n.MODEL SMALL\n.586\n.STACK  100h\n.DATA\n\tresult db 0\n\tProvisional db 0\n\tfirstDigit db 0\n\tsecondDigit db 0\n";

    int MatrizConf[][] = {
            //             l    d    +    -    *    /    <    >    =    .    ,    ;    :    "    '    (    )   eb  tab  ent  eof   nl   oc
            //          [00] [01] [02] [03] [04] [05] [06] [07] [08] [09] [10] [11] [12] [13] [14] [15] [16] [17] [18] [19] [20] [21] [22]
            /* q00 */ {   1,   2, 106, 107, 108,   7,  10,  11,  12, 116, 117, 118,   6,   5,   5, 119, 120,   0,   0,   0,   0,   0, 500 },
            /* q01 */ {   1,   1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 },
            /* q02 */ { 101,   2, 101, 101, 101, 101, 101, 101, 101,   3, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101 },
            /* q03 */ { 501,   4, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501, 501 },
            /* q04 */ { 102,   4, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102 },
            /* q05 */ {   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,   5,  5,  103, 103,   5,   5,   5,   5,   5,   5,   5,   5 },
            /* q06 */ { 105, 105, 105, 105, 105, 105, 105, 105, 104, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105 },
            /* q07 */ { 109, 109, 109, 109,   8, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109 },
            /* q08 */ {   8,   8,   8,   8,   9,   8,   8,   8,   8,   8,   8,   8,   8,   8,   8,   8,   8,   8,   8,   8, 503,   8,   8 },
            /* q09 */ { 503, 503, 503, 503, 503,   0, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503 },
            /* q10 */ { 110, 110, 110, 110, 110, 110, 110, 112, 111, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110 },
            /* q11 */ { 114, 114, 114, 114, 114, 114, 114, 114, 113, 114, 114, 114, 114, 114, 114, 114, 114, 114, 114, 114, 114, 114, 114 },
            /* q12 */ { 504, 504, 504, 504, 504, 504, 504, 504, 115, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504, 504 }
    };

    
    int[][] SRM = {
        {203, 204, 509, 509},
        {204, 204, 509, 509},
        {509, 509, 509, 509},
        {509, 509, 509, 509}
    };

    int[][] Division = {
        {204, 204, 509, 509},
        {204, 204, 509, 509},
        {509, 509, 509, 509},
        {509, 509, 509, 509}
    };

    int[][] OperadoresLogicos = {
        {205, 205, 509, 509},
        {205, 205, 509, 509},
        {509, 509, 509, 509},
        {509, 509, 509, 509}
    };
    
    int[][] EsIgualA = {
        {205, 205, 509, 509},
        {205, 205, 509, 509},
        {509, 509, 205, 509},
        {509, 509, 509, 205}
    };

    int[][] AsignacionDeTipos = {
        {203, 204, 509, 509},
        {204, 204, 509, 509},
        {509, 509, 202, 509},
        {509, 509, 509, 509}
    };



    String PalabrasReservadas[][] = {
            // 0 1
            /* [00] */ { "program", "200" },
            /* [01] */ { "var",     "201" },
            /* [02] */ { "string",  "202" },
            /* [03] */ { "integer", "203" },
            /* [04] */ { "real",    "204" },
            /* [05] */ { "boolean", "205" },
            /* [06] */ { "begin",   "206" },
            /* [07] */ { "end",     "207" },
            /* [08] */ { "read",    "208" },
            /* [09] */ { "write",   "209" },
            /* [10] */ { "if",      "210" },
            /* [11] */ { "then",    "211" },
            /* [12] */ { "else",    "212" },
            /* [13] */ { "while",   "213" },
            /* [14] */ { "do",      "214" },
            /* [15] */ { "or",      "215" },
            /* [16] */ { "and",     "216" },
            /* [17] */ { "not",     "217" },
            /* [18] */ { "true",    "218" },
            /* [19] */ { "false",   "219" },
            /* [19] */ { "endif",   "220" },
            /* [19] */ { "endwhile","221" }
    };

    String ListaDeErrores[][] = {
            // [00] [01]
            /* [00] */{ "ERROR EN SINTAXIS: EL CARACTER NO ES VALIDO", "500" },
            /* [01] */{ "ERROR EN SINTAXIS: SE ESPERA DIGITO", "501" },
            /* [02] */{ "ERROR EN SINTAXIS: SE ESPERA ' O '' ", "502" },
            /* [03] */{ "ERROR EN SINTAXIS: SE ESPERA CERRAR EL COMENTARIO", "503" },
            /* [03] */{ "ERROR EN SINTAXIS: SE ESPERA = ", "504" }
    };

    RandomAccessFile file = null;

    // Analizador Lexico
    public lexicon() {
        try {
            // open file [only read]
            file = new RandomAccessFile(code, "r");

            while (character != -1) { // read character one by one while -not end of file (eof)-
                character = file.read();

                // check if character is a letter, digit or special
                // after this assign a columnaumna
                if (Character.isLetter((char) character)) {
                    columnaumna = 0;
                } else if (Character.isDigit((char) character)) {
                    columnaumna = 1;
                } else {
                    switch ((char) character) {
                        case '+':
                            columnaumna = 2;
                            break;
                        case '-':
                            columnaumna = 3;
                            break;
                        case '*':
                            columnaumna = 4;
                            break;
                        case '/':
                            columnaumna = 5;
                            break;
                        case '<':
                            columnaumna = 6;
                            break;
                        case '>':
                            columnaumna = 7;
                            break;
                        case '=':
                            columnaumna = 8;
                            break;
                        case '.':
                            columnaumna = 9;
                            break;
                        case ',':
                            columnaumna = 10;
                            break;
                        case ';':
                            columnaumna = 11;
                            break;
                        case ':':
                            columnaumna = 12;
                            break;
                        case 39: // * simple quotation mark
                            columnaumna = 13;
                            break;
                        case 34: // * double quotation mark
                            columnaumna = 14;
                            break;
                        case '(':
                            columnaumna = 15;
                            break;
                        case ')':
                            columnaumna = 16;
                            break;
                        case ' ': // * blank space
                            columnaumna = 17;
                            break;
                        case 9: // * tabulation
                            columnaumna = 18;
                            break;
                        case 13: // * enter
                            columnaumna = 19;
                            break;
                        case 3: // * end of file
                            columnaumna = 20;
                            break;
                        case 10: // * new linea
                            columnaumna = 21;
                            linea = linea + 1;
                            break;
                        default: // * other character
                            columnaumna = 22;
                            break;
                    } // end switch
                } // end if

                // assign the MatrizConf valor
                valorTM = MatrizConf[estado][columnaumna];
                if (valorTM < 100) {
                    estado = valorTM;

                    if (estado == 0) {
                        lexeme = ""; // clear lexeme
                    } else {
                        lexeme += (char) character; // add new character to lexeme
                    }
                } else if (valorTM >= 100 && valorTM < 500) { // final estado
                    if (valorTM == 100) {
                        ValidarPalabrasReservadas();
                    }
                    // pointer -1, we arrive with other character
                    if (valorTM == 100 || valorTM == 101 || valorTM == 102 || valorTM == 105 || valorTM == 109
                            || valorTM == 110 || valorTM == 114 || valorTM >= 200) {
                        file.seek(file.getFilePointer() - 1);
                    } else {
                        lexeme += (char) character;
                    }
                    InsertarNodo();
                    estado = 0;
                    lexeme = ""; // clear lexeme
                } else { // Error estado
                    ImprimirError();
                    break;
                }
            }
            //<> printNodes();
            syntactic();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void ValidarPalabrasReservadas() {
        for (String[] PalabrasReservadas : PalabrasReservadas) { // send reserved words array
            if (lexeme.equals(PalabrasReservadas[0])) {
                valorTM = Integer.valueOf(PalabrasReservadas[1]); // assign valor
            }
        }
    }

    private void InsertarNodo() {
        node node = new node(lexeme, valorTM, linea);
        if (cabeza == null) {
            cabeza = node;
            p = cabeza;
        } else {
            p.next = node;
            p = node;
        }
    }


    // Analizador Sintactico
    private void syntactic() {
        p = cabeza;
        while (p != null) {
            if (p.token == 200) {
                p = p.next;
                if (p.token == 100) {
                    p = p.next;
                    if (p.token == 118) {
                        p = p.next;
                        seccion();
                        if (p.token == 116) { // .
                            Imprimirpila(pilaPostfijo, "POSTFIJO");
                            ConfirmarComp();
                            Imprimirpila(DupPost, "COPIA DE POSTFIJO");
                            ImprimirPolish();
                            p = p.next;
                        } else {
                            System.out.println("ERROR #508: SE ESPERA '.'");
                        }
                    } else {
                        System.out.println("ERROR #507: SE ESPERA ':'");
                    }
                } else {
                    System.out.println("ERROR #506: SE ESPERA IDENTIFICADOR");
                }
            } else {
                System.out.println("ERROR #505: SE ESPERA PALABRA RESERVADA");
            }
            break;
        }

        contenidoASM += ".EXIT ;DIRECTIVA donde terminará el ensamblador\nEND";   //!ENSAMBLADOR
        GenerarArchivo(contenidoASM, rutaASM, Archivo);
    }

    private void seccion() {
        DeclaracionDeVariables();
        estado();
    }
//VARIABLES
    private void DeclaracionDeVariables() {
        if (p.token == 201) { 
            p = p.next;
            DeclaracionVar();
            contenidoASM += ".code\n.STARTUP ;DIRECTIVA para el inicio de ensamblador\n"; 
            estado();
        }
    }

    private void DeclaracionVar() {

        if (p.token == 100) {
            LlenarpilaAux(pilaAuxiliar);
        }

        identificador();
        if (p.token == 105) { // :
            p = p.next;

            tipo();
            identificador = pilaAuxiliar.pop();
            PushNodoVariables();

            if (p.token == 118) { // ;
                p = p.next;
            } else {
                System.out.println("ERROR: SE ESPERA ';'");
            }
        } else {
            System.out.println("ERROR: SE ESPERA ':'");
        }
        if (p.token == 100) {
            DeclaracionVar();
        }
    }

    private void tipo() {
        // String | integer | real | boolean
        if (p.token == 202 || p.token == 203 || p.token == 204 || p.token == 205) {
            tipo = p.token;
            p = p.next;
        }
    }
//IDENTIFICAR VARIABLES
    private void identificador() {
        if (p.token == 100) {
            
            if (ValidarDeclaracion == false) {
                contenidoASM += "\t" + String.valueOf(p.lexeme) + " db 0\n"; 
            }
            
            
            if (ValidarDeclaracion) {
                identificador = p.lexeme;
                if (ValidarVariable(identificador) == false) {
                    System.out.println("ERROR #510: VARIABLE NO DECLARADA: " + identificador );
                    ErrorSemantico = true;
                }
            }
            p = p.next;
        }

        if (p.token == 101) {
            p = p.next;
        }
        if (p.token == 102) {
            p = p.next;
        }
        if (p.token == 103) {
            p = p.next;
        }
    }

    private void estado() {
        if (p.token == 206) {
            p = p.next;
            EstadoDe();
            if (p.token == 207) {
                p = p.next;
            }
        }
    }

    private void EstadoDe() {

        ValidarDeclaracion = true;

        if (p.token == 210 || p.token == 213) {
            EstEstructurado(); // if or while
        } else{
            EstSimple(); // assignament, read or write
            pilaOpVacia();
        }

        if (p.token == 118) {
            p = p.next;
        }
        if (p.token == 100 || p.token == 208 || p.token == 209 || p.token == 210 || p.token == 213) {
            EstadoDe();
        }
    }

    private void EstSimple() {
        if (p.token == 208) {
            LeerEstado();
        }
        if (p.token == 209) {
            EscribirEstado();
        }
        if (p.token == 100) {
            AsignacionEstado();
        }
    }

    private void AsignacionEstado() {
        EscribirSaltoDir();                
        InsertarPostfijo();                  
        identificador();
        if (p.token == 104) { // :
            InsertarPostfijo();              
            p = p.next;
            InsertarPostfijo();               
            identificador();
            
            Expresion();
        }
    }
    
    private void Expresion() {
        if (p.token == 106 || p.token == 107) {
            InsertarPostfijo();               
            p = p.next;
            InsertarPostfijo();               
            identificador();
        }
        
        if (p.token == 108 || p.token == 109) {
            InsertarPostfijo();               
            p = p.next;
            InsertarPostfijo();               
            identificador();
        }

        if (p.token == 110 || p.token == 111 || p.token == 112 || p.token == 113 || p.token == 114 || p.token == 115) {
            InsertarPostfijo();               
            p = p.next;
        }

        if (EsOperador()) {
            Expresion();
        }
    }
    
    private void EstEstructurado() {
        if (p.token == 210) {
            ifEstadoDe();
        } else {
            whileEstadoDe();
        }
    }
    
    private void ifEstadoDe() {
        if (p.token == 210) { // if
            p = p.next;

            EscribirSaltoDir();          

            InsertarPostfijo();               
            identificador();                   
            Expresion();
            InsertarPostfijo();               
            identificador();
            
            
            if (p.token == 211) { 
                pilaOpVacia();
                
                EscribirBRFA = true;    
                pushSalto("A");         

                p = p.next;
                EstadoDe();
                EscribirBRIB = true;    
                pushSalto("B");
                if (p.token == 212) { // else
                    EscribirSaltoDIRA = true;   //+ permitimos escribir la direccion de salto BRF-A#
                    p = p.next;
                    EstadoDe();
                    if (p.token == 220) {   //endif
                        p = p.next;
                    }
                }
                EscribirSaltoDIRB = true;//+ permitimos escribir la direccion de salto BRI-B#

                //if sin else
                if (p.token == 220) { // endif
                    AmbosSaltos = true;   //+ permitimos escribir la direccion de salto BRF-A, BRI-B
                    EscribirSaltoDir();
                    p = p.next;
                }

            }
        }
    }
    
    private void whileEstadoDe() {
        briD++;

        if (p.token == 213) { // while
            p = p.next;
    
            EscribirSaltoDIRD = true;

            EscribirSaltoDir();               //+ write BRI 
            InsertarPostfijo();                   
            identificador();
            Expresion();
            InsertarPostfijo();                   
            identificador();
            
            if (p.token == 214) { // do
                pilaOpVacia();
                
                EscribirBRFC = true;    //+ permitimos escribir la dirección de salto BRF-C
                pushSalto("C");  //<> insertamos nodo con la dirección de salto
                p = p.next;
                EstadoDe();

                if (p.token == 221) {   //endwhile

                    EscribirBRID = true;
                    pushSalto("D");

                    p = p.next;
                    EscribirSaltoDIRC = true;
                }
            }
        }
    }
    
    private void LeerEstado() {
        if (p.token == 208) { 

            EscribirSaltoDir();             
            InsertarPostfijo();                   
            p = p.next;
            //ESCRIBIR VARIABLE
            if (p.token == 119) { 
                p = p.next;
                do {
                    InsertarPostfijo();           
                    identificador();
                    contenidoASM += "\n\tmov ah, 0ah\n\tlea dx, " + identificador + "\n\tint 21h\n";
                    if (p.token == 117) { 
                        p = p.next;
                    }
                } while (p.token != 120); 
                if (p.token == 120) {
                    p = p.next;
                }
            }
        }
    }
    
    private void EscribirEstado() {
        if (p.token == 209) {

            EscribirSaltoDir();               
            InsertarPostfijo();                   

            p = p.next;
            if (p.token == 119) { 
                p = p.next;
                do {
                    InsertarPostfijo();           
                    identificador();

                    contenidoASM += "\tmov CL, " + identificador + "\n\tadd CL, 48\n\tmov Provisional, CL\n\tmov AH, 2h\n\tmov DL, Provisional\n\tint 21h\n\n\tmov ah, 02h\n\tmov dl, 0ah ;salto de lineaa\n\tint 21h\n\tmov ah, 02h\n\tmov dl, 0dh ;retorno de carro\n\tint 21h\n";
                    if (p.token == 117) { 
                        p = p.next;
                    }
                } while (p.token != 120); 
                if (p.token == 120) {
                    p = p.next;
                }
            }
        }
    }

    // Semanthic methods
    private void LlenarpilaAux(Stack pilaAuxiliar) {
        if (pilaAuxiliar.empty()) {
            pilaAuxiliar.push(p.lexeme);
        } else {
            pilaAuxiliar.push(p.lexeme);
        }
    }

    private void PushNodoVariables() {
        varList NuevoNodo = new varList(identificador, tipo);

        if (pilaVariables.size() < 1) {
            pilaVariables.addElement(NuevoNodo);
        } else {
            if (ValidarVariable(identificador)) {
                System.out.println("ERROR #509: VARIABLE DUPLICADA: " + NuevoNodo.identificador);
            } else {
                pilaVariables.addElement(NuevoNodo);
            }
        }
    }

    //variableStack -> pila que almacena el identificador y el tipo de variable
    private boolean ValidarVariable(String id) {
        for (int i = 0; i < pilaVariables.size(); i++) {
            if ((pilaVariables.get(i).identificador).equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    //@ POSTFIJO
    private void InsertarPostfijo(){

        if (EsOperado()) {
            tipo = TransformarATipo();

            if (TransformarATipo() == 0) {
                //integer
                if (p.token  == 101) {
                    tipo = 203;
                }
                //real
                if (p.token == 102) {
                    tipo = 204;
                }
                //string
                if (p.token == 103) {
                    tipo = 202;
                }
            }
            
            pilaPostfijo.addElement(tipo);
            
            //+ polish
            token = pilaPostfijo.peek().toString();
            pushPolish();
        }

        if (EsOperador()) {
            int PrioridadActual, UltimaPrioridad = 0;
            
            if (pilaOperadores.isEmpty()) {
                pilaOperadores.addElement(p.token);
                //<>
            }else{
                PrioridadActual = DarPrioridad(p.token);
                UltimaPrioridad = DarPrioridad(pilaOperadores.get(pilaOperadores.size()-1)); 
                
                if (PrioridadActual < UltimaPrioridad) {
                    pilaPostfijo.addElement(pilaOperadores.pop()); 
                    pilaOperadores.addElement(p.token);
                }
                
                if (PrioridadActual > UltimaPrioridad) {
                    pilaOperadores.addElement(p.token);
                }
                
                if (PrioridadActual == UltimaPrioridad) {
                    pilaPostfijo.addElement(pilaOperadores.pop());
                    pilaOperadores.addElement(p.token);
                    
                    //+polish
                    token = pilaPostfijo.peek().toString();
                    pushPolish();
                }
            }
        }
    }
    
    private int DarPrioridad(int token){
        int prioridad = 0;
        switch (token) {
            case 108: // *
                prioridad = 3;
                break;
            case 109: // /
                prioridad = 3;
                break;
            case 106: // '+
                prioridad = 2;
                break;
            case 107: // '-
                prioridad = 2;
                break;
            case 110: // <
                prioridad = 1;
                break;
            case 111: // <=
                prioridad = 1;
                break;
            case 113: // >=
                prioridad = 1;
                break;
            case 114: // >
                prioridad = 1;
                break;
            case 115: // ==
                prioridad = 1;
                break;
            case 112: // '<>
                prioridad = 1;
                break;
            case 104: // :=
                prioridad = 0;
                break;
            default:
                System.out.println("ERROR: OPERADOR NO PERMITIDO");
                break;
            }
        return prioridad;
    }
    
    private int TransformarATipo(){
        for (int i = 0; i < pilaVariables.size(); i++) {
            if ((pilaVariables.get(i).identificador).equals(p.lexeme)) {
                return pilaVariables.get(i).tipo;
            }
        }
        return 0;
    }
    
    private boolean EsOperador(){
        if (p.token == 106 || p.token == 107 || p.token == 108 || p.token == 109 || p.token == 110 || p.token == 111 || p.token == 115 || p.token == 113 || p.token == 114 || p.token == 112 ||  p.token == 104 || p.token == 208 || p.token == 209) {
            return true;
        }
        return false;
    }
    
    private boolean EsOperado(){
        if (p.token == 100 || p.token == 101 || p.token == 102 | p.token == 103) {
            return true;
        }
        return false;
    }
    
    private void pilaOpVacia(){
        if (!pilaOperadores.isEmpty()) {
            while (!pilaOperadores.isEmpty()) {
                pilaPostfijo.addElement(pilaOperadores.pop());
                //* polish
                token = pilaPostfijo.peek().toString();
                pushPolish();
            }
        }
    }
    
    private void pushPolish(){
        polish NodoPolish = new polish(token, DirSalto);
        
        ListaPolish.addElement(NodoPolish);
        DirSalto = "";
    }

    private void EscribirSaltoDir(){
        //if

        if (EscribirSaltoDIRA) {
            DirSalto = (" <- A" +  String.valueOf(brfA));
            EscribirSaltoDIRA = false;
        }

        if (EscribirSaltoDIRB) {
            DirSalto = (" <- B" + String.valueOf(briB));
            EscribirSaltoDIRB = false;
        }
        
        if (AmbosSaltos) {
            DirSalto = (" <- B" + String.valueOf(briB) + " - A" + String.valueOf(brfA));
            EscribirSaltoDIRB = false;
        }

        //while
        if (EscribirSaltoDIRC) {
            DirSalto = (" <- C" + String.valueOf(brfC));
            EscribirSaltoDIRC = false;
        }

        if (EscribirSaltoDIRD) {
            DirSalto = (" <- D" + String.valueOf(briD));
            EscribirSaltoDIRD = false;
        }
    }

    private void pushSalto(String txt){
        if (EscribirBRFA) {
            brfA++;
            Salto = ("BRF "+ txt + String.valueOf(brfA));
            EscribirBRFA = false;
        }

        if (EscribirBRIB) {
            briB++;
            Salto = ("BRI " + txt + String.valueOf(briB));
            EscribirBRIB = false;
        }
        if (EscribirBRFC) {
            brfC++;
            Salto = ("BRF " + txt + String.valueOf(brfC));
            EscribirBRFC = false;
        }
        if (EscribirBRID) {
            Salto = ("BRI " + txt + String.valueOf(briD));
            EscribirBRID = false;
        }

        polish NodoPolish = new polish(Salto);
        ListaPolish.addElement(NodoPolish);
    }



    //<> Screen prints
    private void ImprimirError() {
        if (character != -1 && valorTM >= 500) {
            for (String[] error : ListaDeErrores) {
                if (valorTM == Integer.valueOf(error[1])) {
                    System.out.println(error[0] + valorTM + " CARACTER: " + character + " EN LA LINEA: " + linea);
                }
            }
            ErrorEncontrado = true;
        }
    }
    
    private void printNodes() {
        System.out.println("LISTA DE NODOS: TOKEN - LINEA - LEXEMA");
        System.out.println("");
        p = cabeza;
        while (p != null) {
            System.out.println(p.token + " " + p.linea + " " + p.lexeme);
            p = p.next;
        }
    }
    
    private void Imprimirpila(Stack pila, String titulo){
        System.out.println("LISTA: " + titulo );
        for (int i = 0; i < pila.size(); i++) {
            System.out.println("|" + (i+1) + "| " + pila.get(i));
        }
    }

    private void ImprimirPolish(){
        System.out.println("NOTACION POLACA");
        for (int i = 0; i < ListaPolish.size(); i++) {
            if (ListaPolish.get(i).token == null) {
                System.out.println("-> " + ListaPolish.get(i).Salto);
            }else{
                System.out.println("->  " + ListaPolish.get(i).token + " " + ListaPolish.get(i).DirSalto);
            }
        }
    }  

    private void ConfirmarComp(){
        int PrimerOperando, SegundoOperando, columna, fila, valor, contador = 0;
        
        for (int i = 0; i < pilaPostfijo.size(); i++) {
            DupPost.push(pilaPostfijo.get(i));
            
            if (DupPost.elementAt(contador) == 106 || DupPost.elementAt(contador) == 107 || DupPost.elementAt(contador) == 108) {
                PrimerOperando = DupPost.elementAt(contador-2);   
                SegundoOperando = DupPost.elementAt(contador-1);

                columna = ObtColFila(PrimerOperando);
                fila = ObtColFila(SegundoOperando);

                // +
                if (DupPost.elementAt(contador) == 106) {
                    contenidoASM += "\tmov al, firstDigit\n\tadd al, secondDigit\n\tmov result, al\n\n\tmov ah, 09h\n\tlea dx, result\n\tint 21h\n\n\tmov al, result\n\taam\n\tmov bx, ax\n\tmov ah, 02h\n\tmov dl, bh\n\tadd dl, 30h\n\tint 21h\n\n\tmov ah, 02h\n\tmov dl, dl\n\tadd dl, 30h\n\tint 21h\n";
                }
                // -
                if (DupPost.elementAt(contador) == 107) {
                    contenidoASM += "\tmov al, firstDigit\n\tsub al, secondDigit\n\tmov result, al\n\n\tmov ah, 09h\n\tlea dx, result\n\tint 21h\n\n\tmov al, result\n\taam\n\tmov bx, ax\n\tmov ah, 02h\n\tmov dl, bh\n\tadd dl, 30h\n\tint 21h\n\n\tmov ah, 02h\n\tmov dl, dl\n\tadd dl, 30h\n\tint 21h\n";
                }
                // *
                if (DupPost.elementAt(contador) == 108) {
                    contenidoASM += "\tmov al, firstDigit\n\tmov bl, secondDigit\n\tmul bl\n\tmov result, al\n\n\tmov ah, 09h\n\tlea dx, result\n\tint 21h\n\n\tmov al, result\n\taam\n\tmov bx, ax\n\tmov ah, 02h\n\tmov dl, bh\n\tadd dl, 30h\n\tint 21h\n\n\tmov ah, 02h\n\tmov dl, dl\n\tadd dl, 30h\n\tint 21h\n";
                }
                valor = PosicionTipo(columna, fila, SRM);
                if (valor != 509) {
                    DupPost.pop();
                    DupPost.pop();
                    DupPost.pop();
                    DupPost.push(valor);
                    contador -= 2;
                }else{
                    System.out.println("ERROR #511: INCOMPATIBILIDAD DE TIPOS");
                }
            }

            // /
            if (DupPost.elementAt(contador) == 109) {
                PrimerOperando = DupPost.elementAt(contador-2);   
                SegundoOperando = DupPost.elementAt(contador-1);
                
                columna = ObtColFila(PrimerOperando);
                fila = ObtColFila(SegundoOperando);
                
                valor = PosicionTipo(columna, fila, Division);
                if (valor != 509) {
                    DupPost.pop();
                    DupPost.pop();
                    DupPost.pop();
                    DupPost.push(valor);
                    contador -= 2;
                }else{
                    System.out.println("ERROR #511: INCOMPATIBILIDAD DE TIPOS");
                }
            }

            //asignacion
            if (DupPost.elementAt(contador) == 104) {
                PrimerOperando = DupPost.elementAt(contador-2);   
                SegundoOperando = DupPost.elementAt(contador-1);
                
                columna = ObtColFila(PrimerOperando);
                fila = ObtColFila(SegundoOperando);
                
                valor = PosicionTipo(columna, fila, AsignacionDeTipos);
                if (valor != 509) {
                    System.out.println("TIPOS COMPATIBLES");
                }else{
                    System.out.println("ERROR #511: INCOMPATIBILIDAD DE TIPOS");
                }
            }

            //operadores logicos
            if (DupPost.elementAt(contador) == 110 || DupPost.elementAt(contador) == 113) {
                PrimerOperando = DupPost.elementAt(contador-2);   
                SegundoOperando = DupPost.elementAt(contador-1);

                columna = ObtColFila(PrimerOperando);
                fila = ObtColFila(SegundoOperando);

                valor = PosicionTipo(columna, fila, OperadoresLogicos);
                if (valor != 509) {
                    DupPost.pop();
                    DupPost.pop();
                    DupPost.pop();
                    DupPost.push(valor);
                    contador -= 2;
                }else{
                    System.out.println("ERROR #511: INCOMPATIBILIDAD DE TIPOS");
                }
            }
            contador++;
        }
    }

        private void GenerarArchivo(String content, String path, String child){
        try {            
            File archivo = new File(path, child);
            
            if (!archivo.exists()) {
                archivo.createNewFile();    
            }

            FileWriter fw = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int PosicionTipo(int columna, int fila, int[][] SistTipo){
        int position;
        position = SistTipo[columna][fila];
        return position;
    }

    private int ObtColFila(int columnafila){
        if (columnafila == 203) {
            columnafila = 0;
        }
        if (columnafila == 204) {
            columnafila = 1;
        }
        if (columnafila == 202) {
            columnafila = 2;
        }
        if (columnafila == 205) {
            columnafila = 3;
        }

        return columnafila;
    }


}