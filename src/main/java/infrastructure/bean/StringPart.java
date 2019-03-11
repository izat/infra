package infrastructure.bean;

import java.util.Arrays;

public abstract class StringPart {

    private int expectedSeparatorCount;

    private StringPart(int expectedSeparatorCount) {
        this.expectedSeparatorCount = expectedSeparatorCount;
    }

    public static StringPart of(String part1, String part2) {
        return new Of2(part1, part2);
    }

    public static StringPart of(String part1, String part2, String part3) {
        return new Of3(part1, part2, part3);
    }

    public static StringPart of(String... parts) {
        switch (parts.length) {
            case 0:
                throw new IllegalArgumentException("Invalid part count, " +
                        "expect: at least 1, " +
                        "actual: " + parts.length);
            case 1:
                return new Of1(parts[0]);
            case 2:
                return new Of2(parts[0], parts[1]);
            case 3:
                return new Of3(parts[0], parts[1], parts[2]);
        }
        return new OfN(parts);
    }

    public StringPart withSeparator(Object separator) {
        return withSeparatorChecked(0, separator);
    }

    public StringPart withSeparator(int separatorIndex, Object separator) {
        if (separatorIndex < 0 || separatorIndex > expectedSeparatorCount) {
            throw new IllegalArgumentException("Invalid separatorIndex, " +
                    "expect: 0~" + expectedSeparatorCount + ", " +
                    "actual: " + separatorIndex);
        }
        return withSeparatorChecked(separatorIndex, separator);
    }

    protected abstract StringPart withSeparatorChecked(int separatorIndex, Object separator);

    public String join(Object... separators) {
        int length = separators.length;
        switch (length - expectedSeparatorCount) {
            case 0:
                return joinChecked(separators);
            case 1:
                return joinChecked(separators) + separators[length - 1];
            default:
                throw new IllegalArgumentException("Invalid separator count, " +
                        "expect: " + expectedSeparatorCount + "~" + (expectedSeparatorCount + 1) + ", " +
                        "actual: " + length);
        }
    }

    protected abstract String joinChecked(Object... separators);

    @Override
    public abstract String toString();

    private static class Of1 extends StringPart {
        private String part1;

        private Of1(String part1) {
            super(0);
            this.part1 = part1;
        }

        @Override
        protected StringPart withSeparatorChecked(int separatorIndex, Object separator) {
            return new Of1(part1 + separator);
        }

        @Override
        protected String joinChecked(Object... separators) {
            return part1;
        }

        @Override
        public String toString() {
            return "StringPart1[" + part1 + ']';
        }
    }

    private static class Of2 extends StringPart {
        private String part1, part2;

        private Of2(String part1, String part2) {
            super(1);
            this.part1 = part1;
            this.part2 = part2;
        }

        @Override
        protected StringPart withSeparatorChecked(int separatorIndex, Object separator) {
            switch (separatorIndex) {
                case 0:
                    return new Of1(part1 + separator + part2);
                default:
                    return new Of2(part1, part2 + separator);
            }
        }

        @Override
        protected String joinChecked(Object... separators) {
            return part1 + separators[0] + part2;
        }

        @Override
        public String toString() {
            return "StringPart2[" + part1 + ", " + part2 + ']';
        }
    }

    private static class Of3 extends StringPart {
        private String part1, part2, part3;

        private Of3(String part1, String part2, String part3) {
            super(2);
            this.part1 = part1;
            this.part2 = part2;
            this.part3 = part3;
        }

        @Override
        protected StringPart withSeparatorChecked(int separatorIndex, Object separator) {
            switch (separatorIndex) {
                case 0:
                    return new Of2(part1 + separator + part2, part3);
                case 1:
                    return new Of2(part1, part2 + separator + part3);
                default:
                    return new Of3(part1, part2, part3 + separator);
            }
        }

        @Override
        protected String joinChecked(Object... separators) {
            return part1 + separators[0] + part2 + separators[1] + part3;
        }

        @Override
        public String toString() {
            return "StringPart3[" + part1 + ", " + part2 + ", " + part3 + ']';
        }
    }

    private static class OfN extends StringPart {
        private String[] parts;

        private OfN(String[] parts) {
            super(parts.length - 1);
            this.parts = parts;
        }

        @Override
        protected StringPart withSeparatorChecked(int separatorIndex, Object separator) {
            int length = parts.length;
            if (length == 4) {
                switch (separatorIndex) {
                    case 0:
                        return new Of3(parts[0] + separator + parts[1], parts[2], parts[3]);
                    case 1:
                        return new Of3(parts[0], parts[1] + separator + parts[2], parts[3]);
                    case 2:
                        return new Of3(parts[0], parts[1], parts[2] + separator + parts[3]);
                }
            }

            String[] newParts;
            if (separatorIndex == length - 1) {
                newParts = new String[length];
                System.arraycopy(parts, 0, newParts, 0, separatorIndex);
                newParts[separatorIndex] = parts[separatorIndex] + separator;
            } else {
                newParts = new String[length - 1];
                System.arraycopy(parts, 0, newParts, 0, separatorIndex);
                newParts[separatorIndex] = parts[separatorIndex] + separator + parts[separatorIndex + 1];
                System.arraycopy(parts, separatorIndex + 2, newParts, separatorIndex + 1, length - separatorIndex - 2);
            }
            return new OfN(newParts);
        }

        @Override
        protected String joinChecked(Object... separators) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0, length = parts.length; i < length; i++) {
                builder.append(parts[i]);
                if (i != length - 1) {
                    builder.append(separators[i]);
                }
            }

            return builder.toString();
        }

        @Override
        public String toString() {
            return "StringPartN" + parts.length + "[" + Arrays.toString(parts) + ']';
        }
    }
}