public class node {
    String lexeme;
    int token;
    int linea;
    node next = null;

    node(String lexeme, int token, int linea){
        this.lexeme = lexeme;
        this.token = token;
        this.linea = linea;
    }
}
