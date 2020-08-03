package com.world.cinema.core.jdbc.fields;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ConditionalFieldDetails extends FieldDetails {

    private String sign;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ConditionalFieldDetails that = (ConditionalFieldDetails) o;
        return sign.equals(that.sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sign);
    }
}
