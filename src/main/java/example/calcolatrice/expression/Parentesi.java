package example.calcolatrice.expression;

public enum Parentesi {
    PARENTESI_APERTA('('), PARENTESI_CHIUSA(')');
    private char simbolo;

    Parentesi(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }

    @Override
    public String toString() {
        return Character.toString(simbolo);
    }

    public static Parentesi getParentesi(char simbolo){
        if(simbolo == '(')
            return PARENTESI_APERTA;
        else if(simbolo == ')')
            return PARENTESI_CHIUSA;
        return null;
    }
}

