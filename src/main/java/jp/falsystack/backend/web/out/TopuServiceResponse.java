package jp.falsystack.backend.web.out;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TopuServiceResponse<T, E> {

    private final T data;
    private final E error;

    @Builder
    private TopuServiceResponse(T data, E error) {
        this.data = data;
        this.error = error;
    }

    public static <T, E> TopuServiceResponse<T, E> from(T data, E error) {
        return TopuServiceResponse.<T, E>builder()
                .data(data)
                .error(error)
                .build();
    }
}
