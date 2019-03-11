package com.leo.java8.other.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易员
 * @author justZero
 * @since 2019/3/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trader {
    private String name;
    private String city;
}
