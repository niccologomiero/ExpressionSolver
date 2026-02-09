package example.calcolatrice.expression;

/**
 * Classe che astrae una frazione algebrica numerica
 * La frazione è normalizzata nel seguente modo:
 * - numeratore e denominatore sono primi tra loro
 * - il denominatore è sempre positivo
 * - lo zero è rappresentato da numeratore = 0, denominatore = 1
 */
public class Frazione {
    long numeratore;
    long denominatore;

    public Frazione(long numeratore, long denominatore) throws ArithmeticException {
        if (denominatore == 0) {
            throw new ArithmeticException("Non definito razionale con denominatore = 0");
        }
        this.numeratore = numeratore;
        this.denominatore = denominatore;
        normalizza();
    }

    private void normalizza() {
        //se 0
        if (numeratore == 0) {
            denominatore = 1;
            return;
        }
        //segni
        if (denominatore < 0) {
            denominatore = denominatore * (-1);
            numeratore = numeratore * (-1);
        }
        //semplifico
        long mcd = massimoComunDenominatore(numeratore, denominatore);
        numeratore /= mcd;
        denominatore /= mcd;
    }

    private static long massimoComunDenominatore(long a, long b) {
        long a1 = Math.abs(a);
        long b1 = Math.abs(b);
        while (a1 != b1) {
            if (a1 > b1) {
                a1 -= b1;
            } else if (b1 > a1) {
                b1 -= a1;
            }
        }
        return a1;
    }

    @Override
    public String toString() {
        String s = "";
        if (numeratore == 0) {
            s += "0";
        } else if (denominatore == 1) {
            s += numeratore;
        } else {
            s += numeratore + "/" + denominatore;
        }
        return s;

    }

    /**
     * moltiplicazione tra frazioni
     *
     * @param f
     * @return il risultato della moltiplicazione
     */
    public Frazione mult(Frazione f) {
        return new Frazione(
                this.numeratore * f.numeratore,
                this.denominatore * f.denominatore);
    }

    private Frazione inverso() throws ArithmeticException {
        return new Frazione(this.denominatore, this.numeratore);
    }

    /**
     * divisione tra frazioni
     *
     * @param f
     * @return
     */
    public Frazione div(Frazione f) {
        return this.mult(f.inverso());
    }

    private Frazione opposto() {
        return new Frazione(numeratore * -1, denominatore);
    }

    private static long minimoComuneMultiplo(long a, long b) {
        long a1 = Math.abs(a);
        long b1 = Math.abs(b);
        return (a1 * b1) / massimoComunDenominatore(a1, b1);
    }

    /**
     * Somma di frazioni
     *
     * @param f
     * @return
     */
    public Frazione sum(Frazione f) {
        long mcm = minimoComuneMultiplo(denominatore, f.denominatore);
        long num = numeratore * (mcm / denominatore) + f.numeratore * (mcm / f.denominatore);
        return new Frazione(num, mcm);
    }

    private static Frazione opposto(Frazione f) {
        return new Frazione(-1 * f.numeratore, f.denominatore);
    }

    public Frazione sub(Frazione f) {
        return this.sum(opposto(f));
    }

    public Frazione pow(Frazione exp) {
        Frazione f = null;
        if (exp.numeratore == 0)
            if (this.numeratore == 0)
                throw new ArithmeticException("0^0 non definito");
            else
                f = new Frazione(1, 1);
        else if (exp.denominatore != 1) {
            throw new ArithmeticException("Non implementato calcolo di potenza con esponente non intero.");
        } else if (exp.numeratore > 0)
            f = new Frazione((long) Math.pow(this.numeratore, exp.numeratore),
                    (long) Math.pow(this.denominatore, exp.numeratore));
        else
            f = new Frazione((long) Math.pow(this.denominatore, -exp.numeratore),
                    (long) Math.pow(this.numeratore, -exp.numeratore));
        return f;
    }


}