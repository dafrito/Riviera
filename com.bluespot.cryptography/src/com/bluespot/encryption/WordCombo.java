package com.bluespot.encryption;

import java.util.Set;

class WordCombo {

    private final String word;
    private final Set<String> candidates;

    public WordCombo(final String word, final Set<String> candidates) {
        if (candidates == null) {
            throw new NullPointerException("candidates is null");
        }
        if (word == null) {
            throw new NullPointerException("word is null");
        }
        this.word = word;
        this.candidates = candidates;
    }

    public boolean isEmpty() {
        return this.getCandidates().isEmpty();
    }

    public String getWord() {
        return this.word;
    }

    public Set<String> getCandidates() {
        return this.candidates;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WordCombo)) {
            return false;
        }
        final WordCombo other = (WordCombo) obj;
        if (!this.getWord().equals(other.getWord())) {
            return false;
        }
        if (!this.getCandidates().equals(other.getCandidates())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getWord().hashCode();
        result = 31 * result + this.getCandidates().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Combo[word: %s, candidates: %d]", this.getWord(), this.getCandidates().size());
    }

}