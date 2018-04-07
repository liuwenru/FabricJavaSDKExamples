package ijarvis.intelliq.FabricCA;

/**
 * 该类对应于业务系统中对应的一个实体类，实际在账本中要存储的东西
 * @author ijarvis
 *
 */



public class LedgerRecord {
	private String Key;
    private String Value;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public LedgerRecord(String key, String value) {
        Key = key;
        Value = value;
    }

    public String[] toStringArray() {
        return new String[] {this.getKey(),this.getValue()};
    }

    @Override
    public String toString() {
        return "LedgerRecord{" +
                "Key='" + Key + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }
}
