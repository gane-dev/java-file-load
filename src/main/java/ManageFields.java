import java.sql.PreparedStatement;
import java.util.Map;

public class ManageFields {
    private Map<String,FieldType> fieldMap;
    public  int AddField(PreparedStatement insertStatement,String key,String val)
    {
        return  0;
    }
    public ManageFields(FileType p_fileType,TableType p_tableType)
    {
        fieldMap = CommonObjects.FieldMappings(p_tableType,p_fileType);
    }
}
