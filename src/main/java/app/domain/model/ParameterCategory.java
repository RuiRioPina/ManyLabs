package app.domain.model;

import org.apache.commons.lang3.StringUtils;
//FROM ESOFT SLIDES
public class ParameterCategory {

    private String code;
	private String name;
	
    public ParameterCategory(String code, String name) {
        this.code = code;
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String toString() {
		return String.format("%s - %s", this.code, this.name);
	}
//    private void checkCodeRules(String code) {
//        if (StringUtils.isBlank(code))
//            throw new IllegalArgumentException("Code cannot be blank.");
//        if ( (code.length() < 4) || (code.length() > 8))
//            throw new IllegalArgumentException("Code must have 4 to 8 chars.");
//    }
}