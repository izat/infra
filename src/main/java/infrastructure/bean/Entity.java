package infrastructure.bean;

import java.time.OffsetDateTime;

public abstract class Entity<PK> {
    private PK id;
    private OffsetDateTime createAt;
    private OffsetDateTime updateAt;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public OffsetDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(OffsetDateTime createAt) {
        this.createAt = createAt;
    }

    public OffsetDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(OffsetDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
