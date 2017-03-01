package org.devgateway.ocds.persistence.mongo.flags;

/**
 * Created by mpostelnicu on 3/1/17.
 * Stores flag
 */
public class FlagTypeCount {

    private FlagType flagType;
    private Integer flagCount;

    /**
     * creates a new instance of {@link FlagTypeCount} and sets the count to 1
     *
     * @param flagType
     * @return
     */
    public static FlagTypeCount newInstance(FlagType flagType) {
        FlagTypeCount ftc = new FlagTypeCount();
        ftc.setFlagCount(1);
        ftc.setFlagType(flagType);
        return ftc;
    }

    /**
     * Counts one more flag
     * returns self
     */
    public FlagTypeCount inc() {
        flagCount++;
        return this;
    }

    public FlagType getFlagType() {
        return flagType;
    }

    public void setFlagType(FlagType flagType) {
        this.flagType = flagType;
    }

    public Integer getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(Integer flagCount) {
        this.flagCount = flagCount;
    }
}
