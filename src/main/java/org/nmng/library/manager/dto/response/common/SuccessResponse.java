package org.nmng.library.manager.dto.response.common;

import lombok.*;
import org.nmng.library.manager.dto.response.SuccessState;

@Getter
@Setter
public class SuccessResponse extends CommonResponse {
    private Object data;

    /**
     * @param data Object
     */
    public SuccessResponse(Object data) {
        super(SuccessState.TRUE);
        this.data = data;
    }
}
