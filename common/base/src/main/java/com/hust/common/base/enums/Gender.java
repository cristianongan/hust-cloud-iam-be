package com.hust.common.base.enums;

import com.hust.common.util.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    // male
    MALE("M"),
    
    // female
    FEMALE("F"),
    
    // other
    OTHER("O");
    
    final String gender;
    
    public static Gender getValueOf(String gender) {
        for (Gender g: values()) {
            if (Validator.equals(g.getGender(), gender)) {
                return g;
            }
        }
        
        return null;
    }
}
