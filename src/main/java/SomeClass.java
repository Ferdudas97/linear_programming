public class SomeClass {
    synchronized  void dosom(){
        synchronized (SomeClass.class){}
    }

    static public void main(String []ar){
        new SomeClass().dosom();
    }
}
