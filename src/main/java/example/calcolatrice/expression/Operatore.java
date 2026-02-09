package example.calcolatrice.expression;
/*
Gestione priorità degli operatori, restituzione dei caaratteri associati
 */
public enum Operatore {
    ADD('+', 1, true), SUB('-', 1, true),
    MULT('*', 2, true), DIV('/', 2, true),
    POW('^', 3, true);
    private char simbolo;
    private int priority;
    private boolean leftAssociative;

    Operatore(char simbolo, int priority, boolean leftAssociative) {
        this.simbolo = simbolo;
        this.priority = priority;
        this.leftAssociative = leftAssociative;
    }

    public char getSimbolo() {
        return simbolo;
    }
    public static Operatore getOperatore(char simbolo){
        for(Operatore op: Operatore.values())
            if(op.getSimbolo() == simbolo)
                return op;
        return null;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isLeftAssociative() {
        return leftAssociative;
    }

    @Override
    public String toString() {
        return Character.toString(simbolo);
    }
}

