package ijarvis.intelliq;

/**
 * 账本数据结构,非常简单的一个数据Bean而已
 */
public class LedgerRecord {
    private String Perid;
    private String Legalname;
    private String Carddate;
    private String Cardaddr;
    private String Cardmonery;
    private String Companyaddr;

    public String getPerid() {
        return Perid;
    }

    public void setPerid(String perid) {
        Perid = perid;
    }

    public String getLegalname() {
        return Legalname;
    }

    public void setLegalname(String legalname) {
        Legalname = legalname;
    }

    public String getCarddate() {
        return Carddate;
    }

    public void setCarddate(String carddate) {
        Carddate = carddate;
    }

    public String getCardaddr() {
        return Cardaddr;
    }

    public void setCardaddr(String cardaddr) {
        Cardaddr = cardaddr;
    }

    public String getCardmonery() {
        return Cardmonery;
    }

    public void setCardmonery(String cardmonery) {
        Cardmonery = cardmonery;
    }

    public String getCompanyaddr() {
        return Companyaddr;
    }

    public void setCompanyaddr(String companyaddr) {
        Companyaddr = companyaddr;
    }

    public LedgerRecord(String perid, String legalname, String carddate, String cardaddr, String cardmonery, String companyaddr) {
        Perid = perid;
        Legalname = legalname;
        Carddate = carddate;
        Cardaddr = cardaddr;
        Cardmonery = cardmonery;
        Companyaddr = companyaddr;
    }

    public String[] toStringArray() {
        return new String[] {this.getPerid(),this.getLegalname(),this.getCarddate(),this.getCardaddr(),this.getCardmonery(),this.getCompanyaddr()};
    }

    @Override
    public String toString() {
        return "LedgerRecord{" +
                "Perid='" + Perid + '\'' +
                ", Legalname='" + Legalname + '\'' +
                ", Carddate='" + Carddate + '\'' +
                ", Cardaddr='" + Cardaddr + '\'' +
                ", Cardmonery='" + Cardmonery + '\'' +
                ", Companyaddr='" + Companyaddr + '\'' +
                '}';
    }
}
