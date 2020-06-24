package cwru.selab.cf;

public class PhiWhile<T> {
  boolean predicate;
  boolean evaluatedOnce = false;

  public PhiWhile(boolean predicate) {
    this.predicate = predicate;
  }

  public T entry(T originalValue, T updatedValue) {
    if (evaluatedOnce) return updatedValue;
    else {
      evaluatedOnce = true;
      return originalValue;
    }
  }

  public T exit(T originalValue, T updatedValue) {
    return evaluatedOnce ? updatedValue : originalValue;
  }

  public void evalPred(boolean newPredicate) {
    predicate = newPredicate;
  }

  public boolean getPredVal() {
    return predicate;
  }
}
