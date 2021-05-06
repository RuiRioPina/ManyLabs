package app.domain.store;

import java.util.ArrayList;
import java.util.List;
import app.domain.model.ParameterCategory;
import app.domain.model.TestType;

public class ParameterCategoryStore {

	private List<ParameterCategory> parameterCategories;
	
	public ParameterCategoryStore() {
		this.parameterCategories = new ArrayList<ParameterCategory>();
	}
	
	public ParameterCategory createParameterCategory(String code, String name) {
		return new ParameterCategory(code, name);
	}
	
	public List<ParameterCategory> getParameterCategories() {
		List<ParameterCategory> pc = new ArrayList<ParameterCategory>();
		pc.addAll(this.parameterCategories);
		return pc;
	}
	
	public void validateParameterCategory(ParameterCategory pc) throws IllegalArgumentException {
		checkCodeRules(pc.getCode());
		checkNameRules(pc.getName());
	}
	
	public void saveParameterCategory(ParameterCategory pc) throws IllegalArgumentException {
		validateParameterCategory(pc);
		addParameterCategory(pc);
	}
	
	private void addParameterCategory(ParameterCategory pc) {
		this.parameterCategories.add(pc);
	}
	
	private void checkCodeRules(String code) throws IllegalArgumentException {
        if (code.length() != 5) {
            throw new IllegalArgumentException("Code must have 5 chars.");
        }
        
        if (!code.matches("^[a-zA-Z0-9]*$")) {
        	throw new IllegalArgumentException("Code must be an alphanumeric.");
        }
        
        if (this.getParameterCategoryByCode(code) != null) {
        	throw new IllegalArgumentException("Code already exist.");
        }
    }
	
	private void checkNameRules(String name) throws IllegalArgumentException {
	    if ((name.length()) < 1 || name.length() > 15) {
	        throw new IllegalArgumentException("Name must have 1 to 15 chars.");
	    }
	}
	
	public ParameterCategory getParameterCategoryByCode(String parameterCategoryCode) {
		for(ParameterCategory pc : this.parameterCategories) {
			if(pc.getCode().equals(parameterCategoryCode)) {
				return pc;
			}
		}
		return null;
	}
}
