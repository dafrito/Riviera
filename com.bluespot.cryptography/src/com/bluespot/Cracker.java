package com.bluespot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.bluespot.solver.Frequencies;

public class Cracker {
    final static int LENGTH = 1;

    public static void main(String[] args) throws IOException {
        StringBuilder working = new StringBuilder();
        Map<CharSequence, Integer> frequencies = new HashMap<CharSequence, Integer>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("puzzles/email.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                working.append(line);
                working.append('\n');
                final int SEGMENTS = line.length() / LENGTH;
                for (int i = 0; i < SEGMENTS; i++) {
                    String code = line.substring(i * LENGTH, i * LENGTH + LENGTH);
                    Integer count = frequencies.get(code);
                    if (count == null) {
                        count = 0;
                    }
                    frequencies.put(code, count + 1);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        SortedSet<Entry<CharSequence, Integer>> counts = new TreeSet<Entry<CharSequence, Integer>>(
                new Comparator<Entry<CharSequence, Integer>>() {
            @Override
            public int compare(Entry<CharSequence, Integer> o1, Entry<CharSequence, Integer> o2) {
                int diff = o2.getValue() - o1.getValue();
                if (diff != 0) {
                    return diff;
                }
                return String.valueOf(o1.getKey()).compareTo(String.valueOf(o2.getKey()));
            }
        });
        counts.addAll(frequencies.entrySet());
        Map<CharSequence, String> conversions = new HashMap<CharSequence, String>();
        Iterator<Character> english = Frequencies.getEnglishFrequencies().iterator();
        for (Entry<CharSequence, Integer> e : counts) {
            StringBuilder sb = new StringBuilder(e.getKey() + ": ");
            for (int i = 0; i < e.getValue(); i++) {
                sb.append("#");
            }
            System.out.println(sb);
            if (english.hasNext()) {
                conversions.put(e.getKey(), String.valueOf(english.next()));
            }
        }
        StringBuilder converted = new StringBuilder();
        for (String line : working.toString().split("\n")) {
            final int SEGMENTS = line.length() / LENGTH;
            for (int i = 0; i < SEGMENTS; i++) {
                String code = line.substring(i * LENGTH, i * LENGTH + LENGTH);
                String plain = conversions.get(code);
                if (plain == null) {
                    plain = " ";
                }
                converted.append(plain.toLowerCase());
            }
            converted.append('\n');
        }
        System.out.println(converted);
    }
}
