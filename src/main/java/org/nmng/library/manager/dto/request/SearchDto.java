package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.nmng.library.manager.validator.AcceptedStrings;

import java.util.Objects;

@Data
public class SearchDto {
    public static final String[] OPERATORS = {"eq", "lt", "le", "gt", "ge", "like"};

    public static final String AND_SEARCH = "and";
    public static final String OR_SEARCH = "or";

    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;

    @AcceptedStrings(value = {AND_SEARCH, OR_SEARCH}, message = "\"mode\" is not an accepted value")
    private String mode;

    @Pattern(regexp = "[a-zA-Z]{1,10}", message = "Invalid sorted field names")
    private String sortBy;

    @AcceptedStrings(value = {ASCENDING, DESCENDING}, message = "\"order\" is not an accepted value")
    private String order;

    @Pattern(regexp = "[0-9]+(,)?([0-9]+)?", message = "Invalid page indicator")
    private String page;

    @Digits(integer = 2, fraction = 0)
    private Integer size;

    @AcceptedStrings({"true", "false"})
    private String count;

    public Integer getSize() {
        return Objects.isNull(this.page) ? null :
                Objects.isNull(this.size) ? DEFAULT_SIZE : this.size;
    }
}
