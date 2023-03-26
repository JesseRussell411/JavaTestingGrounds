public class Numbers {
    public static int ceilingDivision(int left, int right) {
        if (left % right == 0) {
            return left / right;
        } else {
            return left / right + 1;
        }
    }

    public static long ceilingDivision(long left, long right) {
        if (left % right == 0) {
            return left / right;
        } else {
            return left / right + 1;
        }
    }
}
