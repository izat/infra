package infrastructure.spring.security.authentication;

import java.io.Serializable;

public class SerializablePrincipalObject implements Serializable {

    private final String name;

    public SerializablePrincipalObject(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof SerializablePrincipalObject) {
            return name.equals(((SerializablePrincipalObject) o).getName());
        }
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}
