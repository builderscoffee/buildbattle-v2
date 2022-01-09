package eu.builderscoffee.expresso.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import eu.builderscoffee.expresso.utils.Tuple;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Configuration("cache")
public class CacheConfiguration {

    @Getter(lazy = true)
    private final List<Tuple<Object, Object>> pairList = new ArrayList<>();
}
