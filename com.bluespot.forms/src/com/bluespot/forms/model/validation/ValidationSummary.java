package com.bluespot.forms.model.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationSummary<E> extends ValidationResult<E> {
    
    private final List<ValidationResult<E>> results;
    private List<ValidationResult<E>> successfulResults;
    private List<ValidationResult<E>> failedResults;
    
    protected ValidationSummary(E value, boolean successful, List<ValidationResult<E>> results) {
        super(value, successful);
        this.results = Collections.unmodifiableList(results);
        
    }

    public List<ValidationResult<E>> getResults() {
        return this.results;
    }
    
    protected void constructResults() {
        if(this.successfulResults != null)
            return;
        this.successfulResults = new ArrayList<ValidationResult<E>>();
        this.failedResults = new ArrayList<ValidationResult<E>>();
        for(ValidationResult<E> result : this.getResults()) {
            if(result.isSuccessful()) {
                this.successfulResults.add(result);
            } else {
                this.failedResults.add(result);
            }
        }
        this.successfulResults = Collections.unmodifiableList(this.successfulResults);
        this.failedResults = Collections.unmodifiableList(this.failedResults);
    }
    
    public List<ValidationResult<E>> getSuccessfulResults() {
        this.constructResults();
        return this.successfulResults;
    }
    
    public List<ValidationResult<E>> getFailedResults() {
        this.constructResults();
        return this.failedResults;
    }
}
