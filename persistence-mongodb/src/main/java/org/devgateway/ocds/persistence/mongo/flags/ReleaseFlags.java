/**
 *
 */
package org.devgateway.ocds.persistence.mongo.flags;

/**
 * @author mpostelnicu Represents the list of red flags at the Release level
 */
public class ReleaseFlags implements FlagsWrappable {

    // i038: Competitive tender w/ short bidding period
    private Flag i038;

    //i007 This awarded competitive tender only featured a single bid`
    private Flag i007;

    //i004: Sole source award above the threshold
    private Flag i004;

    public Flag getI004() {
        return i004;
    }

    public void setI004(Flag i004) {
        this.i004 = i004;
    }

    public Flag getI038() {
        return i038;
    }

    public void setI038(Flag i038) {
        this.i038 = i038;
    }

    public Flag getI007() {
        return i007;
    }

    public void setI007(Flag i007) {
        this.i007 = i007;
    }
}

