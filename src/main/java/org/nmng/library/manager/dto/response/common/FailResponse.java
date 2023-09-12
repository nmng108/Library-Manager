package org.nmng.library.manager.dto.response.common;

import lombok.Getter;
import lombok.Setter;
import org.nmng.library.manager.dto.response.SuccessState;

@Getter
@Setter
public class FailResponse extends CommonResponse {
    private Object errors;

    /**
     * @param errors List<T> | single Object T
     */
    public FailResponse(Object errors) {
        super(SuccessState.FALSE);
        this.errors = errors;
    }
}
