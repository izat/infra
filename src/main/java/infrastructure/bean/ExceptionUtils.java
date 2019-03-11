package infrastructure.bean;

public class ExceptionUtils {

    private ExceptionUtils() {
    }

    @FunctionalInterface
    public interface BlockThrowsException {
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface BlockThrowsExceptionWithReturn<T> {
        T run() throws Exception;
    }

    public static void uncheck(BlockThrowsException block) {
        try {
            block.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T uncheck(BlockThrowsExceptionWithReturn<T> block) {
        try {
            return block.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}