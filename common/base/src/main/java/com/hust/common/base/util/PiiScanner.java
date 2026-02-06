package com.hust.common.base.util;

import com.hust.common.base.enums.LeakType;

import java.util.*;
import java.util.stream.Collectors;

public class PiiScanner {
    public static Set<String> phoneKeys = new HashSet<>();
    public static Set<String> emailKeys = new HashSet<>();
    public static Set<String> addressKeys = new HashSet<>();
    public static Set<String> bankAccountKeys = new HashSet<>();
    public static Set<String> passwordKeys = new HashSet<>();

    static {
        phoneKeys.addAll(List.of("phone", "mobile", "mobilephone", "sdt", "tel", "sodienthoai", "msisdn", "telephone", "mobilenumber"));
        emailKeys.addAll(List.of("email", "mail", "thudientu", "maildomain"));
        addressKeys.addAll(List.of("address","addr","diachi","street","ward","district","province","city","zipcode","postal","country"));
        bankAccountKeys.addAll(List.of("bank","bankaccount", "swift", "accountholder", "tknganhang", "taikhoannganhang", "creditcard", "debitcard", "bankcard", "atm", "atmcard"));
        passwordKeys.addAll(List.of("password", "pwd", "pass", "secret", "matkhau", "pin", "pincode"));
    }

    public static List<String> convertToEntityAttribute(Set<String> keys) {
        List<String> result = new ArrayList<>();

        keys = normalize(keys);

        if (keys.isEmpty()) return result;

        if (isContain(keys, phoneKeys)) {
            result.add(LeakType.PHONE.getValue());
        }

        if (isContain(keys, addressKeys)) {
            result.add(LeakType.ADDRESS.getValue());
        }

        if (isContain(keys, emailKeys)) {
            result.add(LeakType.EMAIL.getValue());
        }

        if (isContain(keys, bankAccountKeys)) {
            result.add(LeakType.BANK_ACCOUNT.getValue());
        }

        if (isContain(keys, passwordKeys)) {
            result.add(LeakType.PASSWORD.getValue());
        }

        return result;
    }

    public static boolean isContain(Set<String> setA, Set<String> setB) {
        Set<String> small = setA.size() <= setB.size() ? setA : setB;
        Set<String> large = small == setA ? setB : setA;
        boolean hasIntersection = false;
        for (String e : small) {
            if (large.contains(e)) { hasIntersection = true; break; }
        }
        return hasIntersection;
    }

    public static Set<String> normalize(Set<String> keys) {
        return keys.stream()
                .map(s -> s.toLowerCase().trim().replaceAll("[-_]+", "").trim())
                .collect(Collectors.toSet());
    }
}
