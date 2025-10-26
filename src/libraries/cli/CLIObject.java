package libraries.cli;

public abstract class CLIObject {

    /**
     * Show the object to the screen
     */
    public void show(){
        System.out.print(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
