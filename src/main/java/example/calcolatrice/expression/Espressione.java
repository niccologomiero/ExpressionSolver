package example.calcolatrice.expression;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
/*La classe espressione prende la stringa e la risolve mediante
i metodi che tokenizzano ogni parte dell'espressione
 */
public class Espressione {
    private String inputExpr; //ciò che arriva
    private ArrayList tokensList;
    private ArrayList validTokensList;
    private ArrayList rpnExpression;

    public Espressione(String inputExpr){
        this.inputExpr = inputExpr;
        this.tokensList = new ArrayList<>();
        this.validTokensList = new ArrayList<>();
        this.rpnExpression= new ArrayList<>();
    }

    public Frazione getRisultato() {
        return risultato;
    }

    public ArrayList getRpnExpression() {
        return rpnExpression;
    }

    public ArrayList getValidTokensList() {
        return validTokensList;
    }

    public ArrayList getTokensList() {
        return tokensList;
    }

    public String getInputExpr() {
        return inputExpr;
    }

    private Frazione risultato;


    public static Frazione risolvi(String stringInput) throws ExpressionException, ArithmeticException{
        Espressione espressione = new Espressione(stringInput);
        //da inputExpr a tokenList
        espressione.scanner();
        //da tokenList a validTokenList
        espressione.parser();
        //da valid TokenList a RPNExpr
        espressione.toRPN();
        //da RPNExpr a risultato
        espressione.calcRPN();
        return espressione.getRisultato();
    }

    /**
     * Tokenizza ed esegue un controllo parziale
     * @throws ExpressionException
     */
    public void scanner() throws ExpressionException{
        long numeratore = 0;
        boolean inLetturaNumero = false;
        int contaParentesi = 0;
        for (char carattere : inputExpr.toCharArray()) {
            switch (carattere) {
                case '(', ')':
                    // se la parentesi è aperta e in lettura c'è un numero
                    if (inLetturaNumero)
                        //il numero letto deve esser chiuso N sarà solo N dato )/(
                        tokensList.add(new Frazione(numeratore, 1));
                    tokensList.add(Parentesi.getParentesi(carattere));
                    if (carattere == Parentesi.PARENTESI_APERTA.getSimbolo()) {
                        contaParentesi++;
                    } else {
                        // la chiusura ne comporta un azzeramento di fatto ad ogni parte che
                        // infatti se negativa significa più chiusure o aperture
                        contaParentesi--;
                        // ')' senza precedenti '('
                        if (contaParentesi < 0)
                            throw new ExpressionException("Espressione non valida",
                                    "Contiene parentesi non appropriatamente poste");
                    }


                    inLetturaNumero = false;

                    break;
                case '+', '-', '*', '/', '^':
                    if (inLetturaNumero) {
                        tokensList.add(new Frazione(numeratore, 1));
                        inLetturaNumero = false;
                    }
                    tokensList.add(Operatore.getOperatore(carattere));
                    break;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                    if (inLetturaNumero) {
                        numeratore = 10 * numeratore + Integer.parseInt(String.valueOf(carattere));
                    } else {
                        numeratore = Integer.parseInt(String.valueOf(carattere));
                        inLetturaNumero = true;
                    }
                    break;
                case ' ':
                    //spazi non considerati
                    break;

                default:
                    //lanciare exception
                    throw new ExpressionException(
                            "Errore", "Carattere non riconosciuto nell'espressione"
                    );
            }

        }
        // Fine del ciclo - se c'è ancora un numero in lettura, aggiungilo
        if (inLetturaNumero) {
            tokensList.add(new Frazione(numeratore, 1));
        }
        if (contaParentesi > 0) {
            throw new ExpressionException("Espressione non valida",
                    "Contiene parentesi non appropriatamente poste");
        }
        System.out.println(tokensList);
    }
/**
 * Esegue verifica dei token
 */
public void parser() throws ExpressionException {
    int stato = 0;
    for (Object token : tokensList) {
        //Analisi lettura dei token
        System.out.println("Token: " + token + " (tipo: " + token.getClass().getSimpleName() + ") - Stato: " + stato);

        switch (stato) {
            case 0:
                if (token instanceof Operatore) {
                    if (token == Operatore.getOperatore('*') || token == Operatore.getOperatore('/')) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                "Un'espressione non può iniziare con '*' oppure '/'"
                        );
                    }
                    validTokensList.add(token);
                    stato = 1;
                } else if (token instanceof Parentesi) {
                    if (token == Parentesi.getParentesi(')')) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                "Un'espressione non può iniziare con una parentesi chiusa"
                        );
                    } else {
                        validTokensList.add(token);
                        stato = 3;  // parentesi aperta
                    }
                } else if (token instanceof Frazione) {
                    validTokensList.add(token);
                    stato = 2;
                }
                break;

            case 1:  // dopo operatore
                if (token instanceof Operatore) {
                    throw new ExpressionException(
                            "Espressione non valida",
                            token + " non può seguire un altro operatore"
                    );
                } else if (token instanceof Parentesi) {
                    if (token == Parentesi.getParentesi(')')) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                token + " non può seguire un operatore"
                        );
                    } else {
                        validTokensList.add(token);
                        stato = 3;  // parentesi aperta
                    }
                } else if (token instanceof Frazione) {
                    validTokensList.add(token);
                    stato = 2;
                }
                break;

            case 2:  // dopo frazione
                if (token instanceof Frazione) {
                    throw new ExpressionException(
                            "Espressione non valida",
                            "Un numero non può seguire un altro numero senza operatore"
                    );
                } else if (token instanceof Operatore) {
                    validTokensList.add(token);
                    stato = 1;
                } else if (token instanceof Parentesi) {
                    if (token == Parentesi.getParentesi('(')) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                "Una parentesi aperta non può seguire un numero"
                        );
                    } else {
                        validTokensList.add(token);
                        stato = 4;  // parentesi chiusa
                    }
                }
                break;

            case 3:  // dopo parentesi APERTA
                if (token instanceof Frazione) {
                    validTokensList.add(token);
                    stato = 2;
                } else if (token instanceof Operatore) {
                    // operatore unario dopo (
                    if (token == Operatore.getOperatore('*') || token == Operatore.getOperatore('/')) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                "Dopo '(' non può esserci '*' o '/'"
                        );
                    }
                    validTokensList.add(token);
                    stato = 1;
                } else if (token instanceof Parentesi) {
                    if (token == Parentesi.getParentesi('(')) {
                        validTokensList.add(token);
                        // stato rimane 3
                    } else {
                        throw new ExpressionException(
                                "Espressione non valida",
                                "')' non può seguire direttamente '('"
                        );
                    }
                }
                break;

            case 4:  // dopo parentesi CHIUSA
                if (token instanceof Frazione) {
                    throw new ExpressionException(
                            "Espressione non valida",
                            "Un numero non può seguire una parentesi chiusa"
                    );
                } else if (token instanceof Operatore) {
                    validTokensList.add(token);
                    stato = 1;
                } else if (token instanceof Parentesi) {
                    if (token == Parentesi.getParentesi('(')) {
                        throw new ExpressionException(
                                "Espressione non valida",
                                "'(' non può seguire ')'"
                        );
                    } else {
                        validTokensList.add(token);
                        // stato rimane 4
                    }
                }
                break;
        }
    }
    System.out.println("Stato finale: " + stato);
    if (stato == 1) {
        throw new ExpressionException(
                "Espressione non valida",
                "Non può finire con un operatore"
        );
    }
}
/**
 * Algoritmo Shunting Yards
 * traduce espressione in stack Rpn
 */
public void toRPN(){
    Deque<Object> stackOperatori = new ArrayDeque<>();

    for (Object token : validTokensList) {

        if (token instanceof Frazione) {

            rpnExpression.add(token);

        } else if (token instanceof Operatore) {

            Operatore opCorrente = (Operatore) token;

            for (int i = stackOperatori.size(); i > 0; i--) {
                if (stackOperatori.isEmpty()) break;
                if (!(stackOperatori.peek() instanceof Operatore)) break;

                Operatore opStack = (Operatore) stackOperatori.peek();

                if (opStack.getPriority() > opCorrente.getPriority()
                        || (opStack.getPriority() == opCorrente.getPriority()
                        && opCorrente.isLeftAssociative())) {

                    rpnExpression.add(stackOperatori.pop());
                } else {
                    break;
                }
            }

            stackOperatori.push(opCorrente);

        } else if (token.equals(Parentesi.PARENTESI_APERTA)) {

            stackOperatori.push(token);

        } else if (token.equals(Parentesi.PARENTESI_CHIUSA)) {

            for (int i = stackOperatori.size(); i > 0; i-- ) {
                if (stackOperatori.isEmpty()) {
                    throw new IllegalStateException("Parentesi non bilanciate");
                }

                Object top = stackOperatori.peek();

                if (top.equals(Parentesi.PARENTESI_APERTA)) {
                    stackOperatori.pop();
                    break;
                }

                rpnExpression.add(stackOperatori.pop());
            }
        }
    }

    //svuoto lo stack operatori
    for(int i = stackOperatori.size(); i > 0; i--){
        rpnExpression.add(stackOperatori.pop());
    }
}
    /**
     * Valuta l'espressione RPN rpnExpression e scrive il risultato
     * nella variabile risultato
     */
    public void calcRPN() {
        Frazione operando1, operando2, risultatoParziale = null;
        Deque<Frazione> stackOperandi = new ArrayDeque<>();
        for (Object ob : rpnExpression) {
            if (ob instanceof Frazione) {
                stackOperandi.push((Frazione) ob);
            } else {
                //Si tratta di un operatore...
                //Tolgo l'elemento in cima allo stackOperandi e lo assegno a operando2
                operando2 = stackOperandi.pop();
                //Tolgo l'elemento in cima allo stackOperandi e lo assegno a operando1
                operando1 = stackOperandi.pop();
                //Eseguo l'operazione operando1 operatore operando2:
                try {
                    switch ((Operatore) ob){
                        case Operatore.ADD:
                            risultatoParziale = operando1.sum(operando2);
                            break;
                        case Operatore.SUB:
                            risultatoParziale = operando1.sub(operando2);
                            break;
                        case Operatore.MULT:
                            risultatoParziale = operando1.mult(operando2);
                            break;
                        case Operatore.DIV:
                            risultatoParziale = operando1.div(operando2);
                            break;
                        case Operatore.POW:
                            risultatoParziale = operando1.pow(operando2);
                            break;
                    }

                } catch (Exception ex) {
                    throw ex;
                }
                stackOperandi.push(risultatoParziale);
            }
        }
        this.risultato = stackOperandi.pop();
    }

}
