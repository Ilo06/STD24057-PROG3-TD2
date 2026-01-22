import java.time.Instant;

public class StockMovement {
    private int id;
    private StockValue value;
    private MovementTypeEnum movementType;
    private Instant CreationDatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StockValue getValue() {
        return value;
    }

    public void setValue(StockValue value) {
        this.value = value;
    }

    public MovementTypeEnum getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementTypeEnum movementType) {
        this.movementType = movementType;
    }

    public Instant getCreationDatetime() {
        return CreationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        CreationDatetime = creationDatetime;
    }

    public StockMovement(int id, StockValue value, MovementTypeEnum movementType, Instant creationDatetime) {
        this.id = id;
        this.value = value;
        this.movementType = movementType;
        CreationDatetime = creationDatetime;
    }
}
