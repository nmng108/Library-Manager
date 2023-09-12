package org.nmng.library.manager.dto.response.common;

import lombok.Data;
import org.nmng.library.manager.dto.response.SuccessState;

//@Builder
//@AllArgsConstructor
@Data
public class CommonResponse {
    private SuccessState success = SuccessState.TRUE;
    private Object information; // provide supplement information, like warnings, deprecation, etc

    public CommonResponse(SuccessState successState) {
        this.success = successState;
    }

    /**
     * @param successState SuccessState: show main status of processing request
     * @param information  List<T> | single Object T
     */
    public CommonResponse(SuccessState successState, Object information) {
        this.success = successState;
        this.information = information;
    }
}
