package pl.mmakos.advent.utils;

public class Ref<T> {
  private T ref;

  public T get() {
    return ref;
  }

  public void set(T ref) {
    this.ref = ref;
  }
}
