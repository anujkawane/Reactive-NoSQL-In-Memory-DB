public class Array {

    public int start;
    public int end;

    public Array() {}

    public Array(int _start, int _end) {
        start = _start;
        end = _end;
    }

    @Override
    public String toString() {
        return "Array{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
