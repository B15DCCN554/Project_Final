package bean;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.sql.Timestamp;

@XmlRootElement
public class QrTerminalPo {
    private BigDecimal id;
    private String masterMerchant;
    private String masterMerchantName;
    private String merchantCode;
    private String merchantName;
    private String mccCode;
    private String mccName;
    private String terminalId;
    private String terminalName;
    private String tidMccCode;
    private String tidMccName;
    private String tidMccVnpayCode;
    private String tidMccVnpayName;
    private Timestamp createDate;
    private BigDecimal merchantId;

    public QrTerminalPo() {
    }

    public QrTerminalPo(BigDecimal id, String masterMerchant, String masterMerchantName,
                        String merchantCode, String merchantName, String mccCode,
                        String mccName, String terminalId, String terminalName,
                        String tidMccCode, String tidMccName, String tidMccVnpayCode,
                        String tidMccVnpayName, Timestamp createDate, BigDecimal merchantId){
        this.id = id;
        this.masterMerchant = masterMerchant;
        this.masterMerchantName = masterMerchantName;
        this.merchantCode = merchantCode;
        this.merchantName = merchantName;
        this.mccCode = mccCode;
        this.mccName = mccName;
        this.terminalId = terminalId;
        this.terminalName = terminalName;
        this.tidMccCode = tidMccCode;
        this.tidMccName = tidMccName;
        this.tidMccVnpayCode = tidMccVnpayCode;
        this.tidMccVnpayName = tidMccVnpayName;
        this.createDate = createDate;
        this.merchantId = merchantId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getMasterMerchant() {
        return masterMerchant;
    }

    public void setMasterMerchant(String masterMerchant) {
        this.masterMerchant = masterMerchant;
    }

    public String getMasterMerchantName() {
        return masterMerchantName;
    }

    public void setMasterMerchantName(String masterMerchantName) {
        this.masterMerchantName = masterMerchantName;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMccCode() {
        return mccCode;
    }

    public void setMccCode(String mccCode) {
        this.mccCode = mccCode;
    }

    public String getMccName() {
        return mccName;
    }

    public void setMccName(String mccName) {
        this.mccName = mccName;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTidMccCode() {
        return tidMccCode;
    }

    public void setTidMccCode(String tidMccCode) {
        this.tidMccCode = tidMccCode;
    }

    public String getTidMccName() {
        return tidMccName;
    }

    public void setTidMccName(String tidMccName) {
        this.tidMccName = tidMccName;
    }

    public String getTidMccVnpayCode() { return tidMccVnpayCode; }

    public void setTidMccVnpayCode(String tidMccVnpayCode) {
        this.tidMccVnpayCode = tidMccVnpayCode;
    }

    public String getTidMccVnpayName() {
        return tidMccVnpayName;
    }

    public void setTidMccVnpayName(String tidMccVnpayName) {
        this.tidMccVnpayName = tidMccVnpayName;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(BigDecimal merchantId) {
        this.merchantId = merchantId;
    }
}
