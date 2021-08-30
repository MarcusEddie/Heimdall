/**
 * 
 */
package org.iman.Heimdallr.barrier;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.iman.Heimdallr.constants.enums.Rule;

/**
 * @author ey
 * @param <T>
 *
 */
public class CheckPoint<T> {

    private List<String> errs;

    public List<String> getErrs() {
        return errs;
    }

    public Boolean checkFailed() {
        return !CollectionUtils.sizeIsEmpty(errs);
    }

    private CheckPoint(Initializer<T> initializer) {
        errs = new ArrayList<String>(initializer.errMsgs);
    }

    public static class Initializer<T> {

        private T item;

        private String itemName;

        private List<String> errMsgs;

        public Initializer(T item, String itemName) {
            this.item = item;
            this.itemName = itemName;
            this.errMsgs = new ArrayList<String>();
        }

        public CheckPoint<T> create() {
            return new CheckPoint<T>(this);
        }

        public <E> Initializer<T> check(Rule rule, E o2) {
            String checkRs = check0(rule, o2);
            addErrMsg(checkRs, errMsgs);
            return this;
        }

        public Initializer<T> check(Rule rule) {
            String checkRs = check0(rule, null);
            addErrMsg(checkRs, errMsgs);
            return this;
        }

        private void addErrMsg(String msg, List<String> msgs) {
            if (StringUtils.isNotBlank(msg)) {
                msg = itemName + " " + msg;
                msgs.add(msg);
            }
        }

        private <E> String check0(Rule rule, E o2) {
            String checkRs = "";
            switch (rule) {
            case IS_NULL:
                checkRs = Ruler.isNull(item);
                break;
            case IS_NOT_NULL:
                checkRs = Ruler.isNotNull(item);
                break;
            case IS_EMPTY:
                checkRs = Ruler.isEmpty(item);
                break;
            case IS_NOT_EMPTY:
                checkRs = Ruler.isNotEmpty(item);
                break;
            case IS_BLANK:
                checkRs = Ruler.isBlank(item);
                break;
            case IS_NOT_BLANK:
                checkRs = Ruler.isNotBlank(item);
                break;
            case IS_DIGITAL:
                checkRs = Ruler.isDigits(item);
                break;
            case IS_POSITIVE:
                checkRs = Ruler.isPositive(item);
                break;
            case IS_NEGATIVE:
                checkRs = Ruler.isNegative(item);
                break;
            case EQUALS:
                checkRs = Ruler.equal(item, o2);
                break;
            case GREATER_THAN:
                checkRs = Ruler.gt(item, o2);
                break;
            case GREATER_THAN_OR_EQUAL:
                checkRs = Ruler.gte(item, o2);
                break;
            case LESS_THAN:
                checkRs = Ruler.lt(item, o2);
                break;
            case LESS_THAN_OR_EQUAL:
                checkRs = Ruler.lte(item, o2);
                break;
            default:
                break;
            }

            return checkRs;
        }

    }
}
