package io.github.nosequel.hcf.data;

import java.util.List;

public interface Datable<T extends Data> {

    List<Class<T>> getRegisteredData();

}