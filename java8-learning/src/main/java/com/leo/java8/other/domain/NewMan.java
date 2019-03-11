package com.leo.java8.other.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @author justZero
 * @since 2019/3/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMan {
    private Goddess goddess;

    public Optional<Goddess> getGoddess() {
        return Optional.ofNullable(this.goddess);
    }
}
