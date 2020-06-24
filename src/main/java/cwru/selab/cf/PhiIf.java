package cwru.selab.cf;

public class PhiIf<T> {
  boolean predicate;

  public PhiIf(boolean predicate) {
    this.predicate = predicate;
  }

  public boolean getPredVal() {
    return predicate;
  }

  public T merge(T truePredicateValue, T originalValue) {
    return predicate ? truePredicateValue : originalValue;
  }
}
