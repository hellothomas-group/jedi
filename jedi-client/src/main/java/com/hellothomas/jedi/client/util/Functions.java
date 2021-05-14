package com.hellothomas.jedi.client.util;

import com.google.common.base.Function;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface Functions {
    Function<String, Integer> TO_INT_FUNCTION = new Function<String, Integer>() {
        @Override
        public Integer apply(String input) {
            return Integer.parseInt(input);
        }
    };
    Function<String, Long> TO_LONG_FUNCTION = new Function<String, Long>() {
        @Override
        public Long apply(String input) {
            return Long.parseLong(input);
        }
    };
    Function<String, Short> TO_SHORT_FUNCTION = new Function<String, Short>() {
        @Override
        public Short apply(String input) {
            return Short.parseShort(input);
        }
    };
}
