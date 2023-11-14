public class Value {
    Core type;
    int intValue;
    int[] arrayValue;
    int rc;

    public Value(Core dataType, int intValue, int[] arrValue) {
        this.type = dataType;
        this.intValue = intValue;
        this.arrayValue = arrValue;
        this.rc = 0;
    }
}
