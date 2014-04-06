package atarigoserver;

public class Pair<A, B> {

    private A firstValue;
    private B secondValue;

    Pair() {
        firstValue = null;
        secondValue = null;
    }

    Pair(A a, B b) {
        firstValue = a;
        secondValue = b;
    }

    public A getFirstValue() {
        return firstValue;
    }

    public B getSecondValue() {
        return secondValue;
    }

    public void setFirstValue(A firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecondValue(B secondValue) {
        this.secondValue = secondValue;
    }
}
