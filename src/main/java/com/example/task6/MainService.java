package com.example.task6;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

@Service
public class MainService {

    public List<User> generateUsers(ELocale locale, int count, long seed, double error) {
        Random random = new Random(seed);
        Faker faker = new Faker(new java.util.Locale(locale.toString()), random);
        List<User> users = new ArrayList<>();
        for (int i = 1; i < count + 1; i++) {
            User user = new User();
            user.setId(faker.number().numberBetween(1000000, 9999999));
            user.setName(faker.name().fullName());
            user.setAddress(faker.address().fullAddress());
            user.setPhone(faker.phoneNumber().phoneNumber());
            users.add(user);
        }
        if (error > 0) {
            makeMisprintUsers(users, error, locale, seed);
        }
        return users;
    }

    private void makeMisprintUsers(List<User> users, double error, ELocale locale, long seed) {
        Random random = new Random(seed);
        for (int i = 0; i < error; i++) {
            for (User user : users) {
                int randomProperty = random.nextInt(3);
                int misprintType = random.nextInt(3);
                switch (randomProperty) {
                    case 0 -> {
                        String name = user.getName();
                        String misprintName = makeMisprint(name, misprintType, locale, random, false);
                        user.setName(misprintName);
                    }
                    case 1 -> {
                        String address = user.getAddress();
                        String misprintAddress = makeMisprint(address, misprintType, locale, random, false);
                        user.setAddress(misprintAddress);
                    }
                    default -> {
                        String phone = user.getPhone();
                        String misprintPhone = makeMisprint(phone, misprintType, locale, random, true);
                        user.setPhone(misprintPhone);
                    }
                }
            }
        }
    }

    private String makeMisprint(String property, int misprintType, ELocale locale, Random random, boolean isPhone) {
        return switch (misprintType) {
            case 0 -> addChar(property, random, locale, isPhone);
            case 1 -> replaceChar(property, random);
            default -> deleteChar(property, random);
        };
    }

    private String addChar(String property, Random random, ELocale locale, boolean isPhone) {
        if (property.length() > 10) {
            return replaceChar(property, random);
        }
        StringBuilder str = new StringBuilder(property);
        int i = random.nextInt(property.length());
        char ch;
        String alphabet;
        if (isPhone) {
            int n = random.nextInt(10);
            return str.insert(i, n).toString();
        }
        switch (locale) {
            case ru -> {
                alphabet = Alphabet.CYRILLIC;
                int alphabetIndex = random.nextInt(alphabet.length());
                ch = alphabet.charAt(alphabetIndex);
            }
            case en -> {
                alphabet = Alphabet.LATIN;
                int alphabetIndex = random.nextInt(alphabet.length());
                ch = alphabet.charAt(alphabetIndex);
            }
            default -> {
                alphabet = Alphabet.POLISH;
                int alphabetIndex = random.nextInt(alphabet.length());
                ch = alphabet.charAt(alphabetIndex);
            }
        }
        return str.insert(i, ch).toString();
    }
    private String replaceChar(String property, Random random) {
        StringBuilder str = new StringBuilder(property);
        int i = random.nextInt(property.length());
        char ch = property.charAt(i);
        int adjacentIndex;
        if (i - 1 < 0) {
            adjacentIndex = i + 1;
        } else {
            adjacentIndex = i - 1;
        }
        return str.deleteCharAt(i).insert(adjacentIndex, ch).toString();
    }
    private String deleteChar(String property, Random random) {
        if (property.length() < 5) {
            return replaceChar(property, random);
        }
        StringBuilder str = new StringBuilder(property);
        int i = random.nextInt(property.length());
        return str.deleteCharAt(i).toString();
    }

}
