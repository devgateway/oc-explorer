
package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * Value
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "amount",
        "currency"
})
public class Amount {

    /**
     * Amount
     * <p>
     * Amount as a number.
     */
    @JsonProperty("amount")
    @JsonPropertyDescription("Amount as a number.")
    @ExcelExport
    private BigDecimal amount;
    /**
     * Currency
     * <p>
     * The currency for each amount should always be specified using the uppercase 3-letter currency code from ISO4217.
     */
    @JsonProperty("currency")
    @JsonPropertyDescription("The currency for each amount should always be specified using the uppercase 3-letter "
            + "currency code from ISO4217.")
    @ExcelExport
    private Currency currency;


    /**
     * Amount
     * <p>
     * Amount as a number.
     */
    @JsonProperty("amount")
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Amount
     * <p>
     * Amount as a number.
     */
    @JsonProperty("amount")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Currency
     * <p>
     * The currency for each amount should always be specified using the uppercase 3-letter currency code from ISO4217.
     */
    @JsonProperty("currency")
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Currency
     * <p>
     * The currency for each amount should always be specified using the uppercase 3-letter currency code from ISO4217.
     */
    @JsonProperty("currency")
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("amount", amount)
                .append("currency", currency)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(amount).append(currency).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Amount)) {
            return false;
        }
        Amount rhs = ((Amount) other);
        return new EqualsBuilder().append(amount, rhs.amount)
                .append(currency, rhs.currency)
                .isEquals();
    }

    public enum Currency {

        ADP("ADP"),
        AED("AED"),
        AFA("AFA"),
        AFN("AFN"),
        ALK("ALK"),
        ALL("ALL"),
        AMD("AMD"),
        ANG("ANG"),
        AOA("AOA"),
        AOK("AOK"),
        AON("AON"),
        AOR("AOR"),
        ARA("ARA"),
        ARP("ARP"),
        ARS("ARS"),
        ARY("ARY"),
        ATS("ATS"),
        AUD("AUD"),
        AWG("AWG"),
        AYM("AYM"),
        AZM("AZM"),
        AZN("AZN"),
        BAD("BAD"),
        BAM("BAM"),
        BBD("BBD"),
        BDT("BDT"),
        BEC("BEC"),
        BEF("BEF"),
        BEL("BEL"),
        BGJ("BGJ"),
        BGK("BGK"),
        BGL("BGL"),
        BGN("BGN"),
        BHD("BHD"),
        BIF("BIF"),
        BMD("BMD"),
        BND("BND"),
        BOB("BOB"),
        BOP("BOP"),
        BOV("BOV"),
        BRB("BRB"),
        BRC("BRC"),
        BRE("BRE"),
        BRL("BRL"),
        BRN("BRN"),
        BRR("BRR"),
        BSD("BSD"),
        BTN("BTN"),
        BUK("BUK"),
        BWP("BWP"),
        BYB("BYB"),
        BYN("BYN"),
        BYR("BYR"),
        BZD("BZD"),
        CAD("CAD"),
        CDF("CDF"),
        CHC("CHC"),
        CHE("CHE"),
        CHF("CHF"),
        CHW("CHW"),
        CLF("CLF"),
        CLP("CLP"),
        CNY("CNY"),
        COP("COP"),
        COU("COU"),
        CRC("CRC"),
        CSD("CSD"),
        CSJ("CSJ"),
        CSK("CSK"),
        CUC("CUC"),
        CUP("CUP"),
        CVE("CVE"),
        CYP("CYP"),
        CZK("CZK"),
        DDM("DDM"),
        DEM("DEM"),
        DJF("DJF"),
        DKK("DKK"),
        DOP("DOP"),
        DZD("DZD"),
        ECS("ECS"),
        ECV("ECV"),
        EEK("EEK"),
        EGP("EGP"),
        ERN("ERN"),
        ESA("ESA"),
        ESB("ESB"),
        ESP("ESP"),
        ETB("ETB"),
        EUR("EUR"),
        FIM("FIM"),
        FJD("FJD"),
        FKP("FKP"),
        FRF("FRF"),
        GBP("GBP"),
        GEK("GEK"),
        GEL("GEL"),
        GHC("GHC"),
        GHP("GHP"),
        GHS("GHS"),
        GIP("GIP"),
        GMD("GMD"),
        GNE("GNE"),
        GNF("GNF"),
        GNS("GNS"),
        GQE("GQE"),
        GRD("GRD"),
        GTQ("GTQ"),
        GWE("GWE"),
        GWP("GWP"),
        GYD("GYD"),
        HKD("HKD"),
        HNL("HNL"),
        HRD("HRD"),
        HRK("HRK"),
        HTG("HTG"),
        HUF("HUF"),
        IDR("IDR"),
        IEP("IEP"),
        ILP("ILP"),
        ILR("ILR"),
        ILS("ILS"),
        INR("INR"),
        IQD("IQD"),
        IRR("IRR"),
        ISJ("ISJ"),
        ISK("ISK"),
        ITL("ITL"),
        JMD("JMD"),
        JOD("JOD"),
        JPY("JPY"),
        KES("KES"),
        KGS("KGS"),
        KHR("KHR"),
        KMF("KMF"),
        KPW("KPW"),
        KRW("KRW"),
        KWD("KWD"),
        KYD("KYD"),
        KZT("KZT"),
        LAJ("LAJ"),
        LAK("LAK"),
        LBP("LBP"),
        LKR("LKR"),
        LRD("LRD"),
        LSL("LSL"),
        LSM("LSM"),
        LTL("LTL"),
        LTT("LTT"),
        LUC("LUC"),
        LUF("LUF"),
        LUL("LUL"),
        LVL("LVL"),
        LVR("LVR"),
        LYD("LYD"),
        MAD("MAD"),
        MDL("MDL"),
        MGA("MGA"),
        MGF("MGF"),
        MKD("MKD"),
        MLF("MLF"),
        MMK("MMK"),
        MNT("MNT"),
        MOP("MOP"),
        MRO("MRO"),
        MTL("MTL"),
        MTP("MTP"),
        MUR("MUR"),
        MVQ("MVQ"),
        MVR("MVR"),
        MWK("MWK"),
        MXN("MXN"),
        MXP("MXP"),
        MXV("MXV"),
        MYR("MYR"),
        MZE("MZE"),
        MZM("MZM"),
        MZN("MZN"),
        NAD("NAD"),
        NGN("NGN"),
        NIC("NIC"),
        NIO("NIO"),
        NLG("NLG"),
        NOK("NOK"),
        NPR("NPR"),
        NZD("NZD"),
        OMR("OMR"),
        PAB("PAB"),
        PEH("PEH"),
        PEI("PEI"),
        PEN("PEN"),
        PES("PES"),
        PGK("PGK"),
        PHP("PHP"),
        PKR("PKR"),
        PLN("PLN"),
        PLZ("PLZ"),
        PTE("PTE"),
        PYG("PYG"),
        QAR("QAR"),
        RHD("RHD"),
        ROK("ROK"),
        ROL("ROL"),
        RON("RON"),
        RSD("RSD"),
        RUB("RUB"),
        RUR("RUR"),
        RWF("RWF"),
        SAR("SAR"),
        SBD("SBD"),
        SCR("SCR"),
        SDD("SDD"),
        SDG("SDG"),
        SDP("SDP"),
        SEK("SEK"),
        SGD("SGD"),
        SHP("SHP"),
        SIT("SIT"),
        SKK("SKK"),
        SLL("SLL"),
        SOS("SOS"),
        SRD("SRD"),
        SRG("SRG"),
        SSP("SSP"),
        STD("STD"),
        SUR("SUR"),
        SVC("SVC"),
        SYP("SYP"),
        SZL("SZL"),
        THB("THB"),
        TJR("TJR"),
        TJS("TJS"),
        TMM("TMM"),
        TMT("TMT"),
        TND("TND"),
        TOP("TOP"),
        TPE("TPE"),
        TRL("TRL"),
        TRY("TRY"),
        TTD("TTD"),
        TWD("TWD"),
        TZS("TZS"),
        UAH("UAH"),
        UAK("UAK"),
        UGS("UGS"),
        UGW("UGW"),
        UGX("UGX"),
        USD("USD"),
        USN("USN"),
        USS("USS"),
        UYI("UYI"),
        UYN("UYN"),
        UYP("UYP"),
        UYU("UYU"),
        UZS("UZS"),
        VEB("VEB"),
        VEF("VEF"),
        VNC("VNC"),
        VND("VND"),
        VUV("VUV"),
        WST("WST"),
        XAF("XAF"),
        XAG("XAG"),
        XAU("XAU"),
        XBA("XBA"),
        XBB("XBB"),
        XBC("XBC"),
        XBD("XBD"),
        XCD("XCD"),
        XDR("XDR"),
        XEU("XEU"),
        XFO("XFO"),
        XFU("XFU"),
        XOF("XOF"),
        XPD("XPD"),
        XPF("XPF"),
        XPT("XPT"),
        XRE("XRE"),
        XSU("XSU"),
        XTS("XTS"),
        XUA("XUA"),
        XXX("XXX"),
        YDD("YDD"),
        YER("YER"),
        YUD("YUD"),
        YUM("YUM"),
        YUN("YUN"),
        ZAL("ZAL"),
        ZAR("ZAR"),
        ZMK("ZMK"),
        ZMW("ZMW"),
        ZRN("ZRN"),
        ZRZ("ZRZ"),
        ZWC("ZWC"),
        ZWD("ZWD"),
        ZWL("ZWL"),
        ZWN("ZWN"),
        ZWR("ZWR");
        private final String value;
        private static final Map<String, Currency> CONSTANTS = new HashMap<String, Currency>();

        static {
            for (Currency c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Currency(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Currency fromValue(String value) {
            Currency constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
