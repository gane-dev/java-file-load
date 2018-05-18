public class FieldType {
    private int fieldKey;

    public FieldType(int fieldKey, String fieldName, String dataType, int fieldLength) {
        this.fieldKey = fieldKey;
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.fieldLength = fieldLength;
    }

    public int getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(int fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    private String dataType;

    public int getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    private int fieldLength;

}
